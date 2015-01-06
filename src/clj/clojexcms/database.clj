(ns clojexcms.database)

; Define a database connection spec. (This is standard clojure.java.jdbc.)
(defonce db {:classname "org.postgresql.Driver"
             :subprotocol "postgresql"
             :subname "//localhost/clojexcms"
             :user "dev"
             :password "dev"})
