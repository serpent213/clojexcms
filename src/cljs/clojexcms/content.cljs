(ns clojexcms.content
  (:require [clojexcms.tabpanel :refer (tabpanel)]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn edit-view [content owner]
  (om/component
   (dom/textarea #js {:value (:body content) :cols 100 :rows 20})))

(defn content-view [content-all owner]
  (om/component
   (om/build tabpanel content-all
             {:state {:tabid :id
                      :tabtitle :description
                      :tabbody edit-view}})))
