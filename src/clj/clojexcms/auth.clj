(ns clojexcms.auth
  "Admin user authentication"
  (:require [clojexcms.database :refer [db]]
            [yesql.core :refer [defqueries]]))

(defqueries "backend/auth.sql")

(defn login!
  [ring-request]
  (let [{:keys [session params]} ring-request
        {:keys [user-id password]} params]
    (println "Login request:" params)
    {:status 200
     :session (assoc session :uid user-id)
     :body {:name "Administrator"}}))
