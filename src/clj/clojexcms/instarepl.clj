;; (insta)repl code snippets

(require '[yesql.core :refer [defqueries]])

; Define a database connection spec. (This is standard clojure.java.jdbc.)
(def db-spec {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname "//localhost/clojexcms"
              :user "dev"
              :password "dev"})

(defqueries "frontend/content.sql")

(content-by-id db-spec "welcome")
