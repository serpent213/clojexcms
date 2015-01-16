(ns clojexcms.server
  (:require [clojexcms.backend :refer [backend-page ring-ajax-get-or-ws-handshake
                                       ring-ajax-post start-chsk-router!]]
            [clojexcms.content-page :refer [content-page]]
            [clojexcms.dev :refer [is-dev? start-figwheel start-less]]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [resources]]
            [net.cgrand.reload :refer [auto-reload]]
            [ring.middleware.reload :as reload]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [environ.core :refer [env]]
            [org.httpkit.server :refer [run-server]]
            [taoensso.sente :as sente]))

(defroutes routes
  (resources "/")
  (resources "/react" {:root "react"})
  (GET "/admin/*" req (backend-page))

  ;; sente channel socket
  (GET  "/chsk" req (ring-ajax-get-or-ws-handshake req))
  (POST "/chsk" req (ring-ajax-post req))

  ;; frontend pages
  (GET "/" [] (content-page "welcome"))
  (GET "/about" [] (content-page "about")))

(def http-handler
  (if is-dev?
    (reload/wrap-reload (wrap-defaults #'routes site-defaults))
    (wrap-defaults routes site-defaults)))

(defn run-web-server [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (start-chsk-router!)
    (println (str "Starting web server on port " port "."))
    (run-server http-handler {:port port :join? false})))

(defn run-auto-reload [& [port]]
  (auto-reload *ns*)
  (start-figwheel)
  (start-less))

(defn run [& [port]]
  (when is-dev?
    (run-auto-reload))
  (run-web-server port))

(defn -main [& [port]]
  (run port))

(comment ; for manual evaluation
  (run))
