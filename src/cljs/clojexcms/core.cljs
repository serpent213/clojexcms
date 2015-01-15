(ns clojexcms.core
  (:require [cljs.core.async :as async :refer (<! >! put! chan)]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [taoensso.sente :as sente :refer (cb-success?)]))

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
  (.log js/console "Unhandled event: %s" event))

(defmethod event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (if (= ?data {:first-open? true})
    (.log js/console "Channel socket successfully established!")
    (.log js/console "Channel socket state change: %s" ?data)))

(defmethod event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (.log js/console "Push event from server: %s" ?data))

(defn main []
  (sente/start-chsk-router! ch-chsk event-msg-handler)
  (om/root
   (fn [app owner]
     (reify
       om/IRender
       (render [_]
               (dom/h1 nil (:text app)))))
   app-state
   {:target (. js/document (getElementById "app"))}))
