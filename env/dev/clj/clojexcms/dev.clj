(ns clojexcms.dev
  (:require [environ.core :refer [env]]
            [net.cgrand.enlive-html :refer [set-attr prepend append html]]
            [leiningen.core.main :as lein]))

(def is-dev? (env :is-dev))

(def inject-devmode-html
  (comp
   (set-attr :class "is-dev")
   (prepend (html [:script {:type "text/javascript" :src "/js/backend/out/goog/base.js"}]))
   (append  (html [:script {:type "text/javascript"} "goog.require('clojexcms.dev')"]))
   (append  (html [:script {:type "text/javascript" :id "lt_ws"
                            :src "http://localhost:62439/socket.io/lighttable/ws.js"}]))))

(defn start-figwheel []
  (future
    (print "Starting figwheel.\n")
    (lein/-main ["figwheel"])))

(defn start-less []
  (future
    (println "Starting less.")
    (lein/-main ["less" "auto"])))
