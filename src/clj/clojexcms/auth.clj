(ns clojexcms.auth
  "Admin user authentication"
  (:require [clojexcms.database :refer [db]]
            [crypto.password.bcrypt :as password]
            [yesql.core :refer [defqueries]]))

(defqueries "backend/auth.sql")

(defn login!
  [ring-request]
  (let [{:keys [session params]} ring-request
        {:keys [user-id password]} params
        admin (first (admin-account db user-id))]
    (println "Login request:" params)
    (if (and admin (password/check password (:password admin)))
      {:status 200
       :session (assoc session :uid user-id)
       :headers {"Content-Type" "application/edn"}
       :body (pr-str {:fullname "Administrator"})}
      (do
        (println "Login failure:" ring-request)
        {:status 403}))))

(defn is-admin? [ring-req]
  (let [session (:session ring-req)
        uid     (:uid     session)]
    (if (nil? uid)
      (do
        (println "Access violation:" ring-req)
        false)
      true)))

(comment ; for manual evaluation
  (password/encrypt "admin"))
