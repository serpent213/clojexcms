(ns clojexcms.server
  "Server calls and events"
  (:require [cljs.core.async :as async :refer (<! >! put! chan)]
            [clojexcms.state :refer (app-state)]
            [cljs.reader :as reader]
            [om.core :as om :include-macros true]
            [taoensso.sente :as sente :refer (cb-success?)]))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/chsk" {:type :auto})]
  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state))  ; Watchable, read-only atom

(defn seed-app-state! []
  (chsk-send! [:content/get-all] 5000
              (fn [cb-reply]
                #_(println ":content/get-all reply:" cb-reply)
                (when (cb-success? cb-reply)
                  (swap! app-state assoc :content cb-reply)))))

(defmulti event-msg-handler :id) ; Dispatch on event-id

(defmethod event-msg-handler :default ; Fallback
  [{:as ev-msg :keys [event]}]
  (println "Unhandled event:" event))

(defmethod event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (println "Channel socket state change:" ?data)
  (when (and (not= (:uid ?data) ::sente/nil-uid)
             (empty? (:content @app-state)))
    (seed-app-state!)))

(defmulti push-msg-handler :id)

(defmethod push-msg-handler :content/update!
  [{:as ev-msg :keys [?data]}]
  (let [id (keyword (:id ?data))]
    (if (get-in @app-state [:content id :dirty?])
      (swap! app-state assoc-in [:content id :changed-on-server?] true)
      (swap! app-state assoc-in [:content id :body] (:body ?data)))))

(defmethod event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  #_(println "Push event from server:" ?data)
  (push-msg-handler {:id (first ?data) :?data (second ?data)}))

(defn update-content! [content on-success & [?on-error]]
  (chsk-send! [:content/update! {:id (:id content) :body (:body content)}]
              5000
              (fn [cb-reply]
                (if (= cb-reply :content/update-success)
                  (on-success)
                  (if ?on-error (?on-error))))))

(defn start-router! []
  (sente/start-chsk-router! ch-chsk event-msg-handler))

(defn login! [user-id password]
  (sente/ajax-call "/login"
                   {:method :post
                    :params {:user-id               user-id
                             :password              password
                             "__anti-forgery-token" (:csrf-token @chsk-state)}}
                   (fn [ajax-resp]
                     (println "Ajax login response:" ajax-resp)
                     (when (= (:?status ajax-resp) 200)
                       (let [{:keys [fullname]} (:?content ajax-resp)]
                         (swap! app-state assoc :auth {:logged-in? true
                                                       :uid        user-id
                                                       :fullname   fullname})))))
  (sente/chsk-reconnect! chsk))
