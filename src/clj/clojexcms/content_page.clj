(ns clojexcms.content-page
  (:require [clojexcms.database :refer [db]]
            [clojure.java.io :as io]
            [markdown.core :refer [md-to-html-string]]
            [net.cgrand.enlive-html :refer [deftemplate content]]
            [yesql.core :refer [defqueries]]))

(defqueries "frontend/content.sql")

(deftemplate content-page
  (io/resource "frontend.html") [id]
  [:div#content] (content (md-to-html-string (:body (first (content-by-id db "welcome"))))))
