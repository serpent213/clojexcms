(ns clojexcms.content
  (:require [bootstrap-cljs :as bs :include-macros true]
            [clojexcms.tabpanel :refer (tabpanel)]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn edit-view [content owner]
  (om/component
   (dom/div nil
            (dom/textarea #js {:value (:body content) :cols 100 :rows 20})
            (bs/button-toolbar nil
                               (bs/button {:bsStyle "primary"
                                           :disabled false
                                           :onClick (fn [_] (js/alert "published"))}
                                          "Publish")))))

(defn content-view [content-all owner]
  (om/component
   (om/build tabpanel content-all
             {:state {:tabid :id
                      :tabtitle :title
                      :tabbody edit-view}})))
