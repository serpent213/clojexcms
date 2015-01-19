(ns clojexcms.core
  ; (:require-macros [cljs.core.async.macros :refer (go go-loop)])
  (:require [cljs.core.async :as async :refer (<! >! put! chan)]
            [clojexcms.content :refer (content-view)]
            [clojexcms.frame :refer (navigation-menu flash-messages)]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent defcomponentmethod]]
            [taoensso.sente :as sente :refer (cb-success?)]))

(enable-console-print!)

(defonce app-state (atom {:site {:name "ClojExCMS"}
                          :ui {:page :dashboard}
                          :content []}))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/chsk" {:type :auto})]
  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state))  ; Watchable, read-only atom

(reset! clojexcms.content/chsk-send! chsk-send!)

(defmulti event-msg-handler :id) ; Dispatch on event-id

(defmethod event-msg-handler :default ; Fallback
  [{:as ev-msg :keys [event]}]
  (println "Unhandled event:" event))

(defmethod event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (println "Channel socket state change:" ?data)
  (when (:first-open? ?data)
    (chsk-send! [:content/get-all] 5000
                (fn [cb-reply]
                  #_(println ":content/get-all reply:" cb-reply)
                  (when (not= cb-reply :chsk/timeout)
                    (swap! app-state assoc :content (vec cb-reply)))))))

(do ; server push events

  (defn positions
    [pred coll]
    (keep-indexed (fn [idx x]
                    (when (pred x)
                      idx))
                  coll))

  (defmulti push-msg-handler :id)

  (defmethod push-msg-handler :content/update!
    [{:as ev-msg :keys [?data]}]
    (let [index (first (positions #(= (:id ?data) (:id %)) (:content @app-state)))]
      (swap! app-state assoc-in [:content index :body] (:body ?data))))

  (defmethod event-msg-handler :chsk/recv
    [{:as ev-msg :keys [?data]}]
    #_(println "Push event from server:" ?data)
    (push-msg-handler {:id (first ?data) :?data (second ?data)})))

(defcomponent page [app owner]
  (render [_]
          (case (get-in app [:ui :page])
            :dashboard  (om/build content-view (:content app))
            :content    (om/build content-view (:content app))
            :empty      (om/build content-view (:content app)))))

(defn main []
  (sente/start-chsk-router! ch-chsk event-msg-handler)
  (om/root
   (fn [app owner]
     (reify
       om/IInitState
       (init-state [_]
                   {:navigation (chan)})
       om/IRender
       (render [_]
               (dom/div
                (om/build navigation-menu (:ui app))
                (dom/div {:id "page-wrapper"}
                         (om/build flash-messages (:ui app))
                         (om/build page app))))))
   app-state
   {:target (. js/document (getElementById "wrapper"))}))
