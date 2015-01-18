(ns clojexcms.content
  (:require [bootstrap-cljs :as bs :include-macros true]
            [clojexcms.tabpanel :refer (tabpanel)]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defcomponent edit-view [content owner]
  (render [_]
          (dom/div
           (dom/textarea {:value (:body content) :cols 100 :rows 20})
           (bs/button-toolbar
            (bs/button {:bs-style "primary"
                        :disabled false
                        :on-click (fn [_] (js/alert "published"))}
                       "Publish")))))

(defcomponent content-view [content-all owner]
  (render [_]
          (om/build tabpanel content-all
                    {:state {:tabid :id
                             :tabtitle :title
                             :tabbody edit-view}})))
