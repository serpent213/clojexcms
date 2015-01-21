(ns clojexcms.views.dashboard
  "Could well become a dashboard..."
  (:require [bootstrap-cljs :as bs :include-macros true]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defcomponent dashboard-view [app owner]
  (render [_]
          (dom/h1 "Dashboard "
                  (dom/small "Statistics and more"))))
