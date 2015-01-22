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
     :headers {"Content-Type" "application/edn"}
     :body (pr-str {:fullname "Administrator"})}))

(defn is-admin? [ring-req]
  (let [session (:session ring-req)
        uid     (:uid     session)]
    (if (nil? uid)
      (do
        (println "Access violation:" ring-req)
        false)
      true)))
