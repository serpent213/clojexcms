(ns clojexcms.content-page
  "Frontend page rendered from DB"
  (:require [clojexcms.database :refer [db]]
            [clojure.java.io :as io]
            [markdown.core :refer [md-to-html-string]]
            [net.cgrand.enlive-html :refer [deftemplate html-content]]
            [yesql.core :refer [defqueries]]))

(defqueries "frontend/content.sql")

(deftemplate content-page
  (io/resource "frontend.html") [id]
  [:div#content] (html-content (md-to-html-string (:body (first (content-by-id db id))))))
