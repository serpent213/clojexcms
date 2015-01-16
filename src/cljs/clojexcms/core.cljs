(ns clojexcms.core
  ; (:require-macros [cljs.core.async.macros :refer (go go-loop)])
  (:require ; [cljs.core.async :as async :refer (<! >! put! chan)]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [taoensso.sente :as sente :refer (cb-success?)]))

(enable-console-print!)

(defonce app-state (atom {:text "Hello Chestnut!"}))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/chsk" {:type :auto})]
  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state))  ; Watchable, read-only atom

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
                  (println ":content/get-all reply:" cb-reply)
                  (when (not= cb-reply :chsk/timeout)
                    (swap! app-state assoc :content cb-reply)
                    (println "new app-state:" @app-state))))))

#_(defmethod event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (println "Push event from server:" ?data))

(defn main []
  (sente/start-chsk-router! ch-chsk event-msg-handler)
  (om/root
   (fn [app owner]
     (reify
       om/IRender
       (render [_]
               (dom/div nil
                        (dom/h1 nil (:text app))
                        (dom/textarea #js {:value (get-in app [:content :welcome :body])
                                           :cols 100 :rows 20} nil)))))
   app-state
   {:target (. js/document (getElementById "app"))}))
