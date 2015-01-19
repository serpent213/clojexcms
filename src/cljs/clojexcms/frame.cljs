(ns clojexcms.frame
  (:require [bootstrap-cljs :as bs :include-macros true]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defcomponent menu-entry [ui owner {:keys [id icon title]}]
  (render [_]
          (dom/li {:on-click (fn [e]
                               (om/update! ui :page id)
                               (.preventDefault e))}
                  (dom/a {:href "#"}
                         (dom/i {:class (str "fa fa-" icon)})
                         (str " " title)))))

(defcomponent navigation-menu [ui owner {:keys [menu-entries]}]
  (render [_]
          (dom/nav {:class "navbar navbar-inverse navbar-fixed-top" :role "navigation"}
                   (dom/div {:class "navbar-header"}
                            (dom/button {:type "button" :class "navbar-toggle"
                                         :data-toggle "collapse" :data-target ".navbar-ex1-collapse"}
                                        (dom/span {:class "sr-only"} "Toggle navigation")
                                        (repeatedly 3 #(dom/span {:class "icon-bar"})))
                            (dom/a {:class "navbar-brand" :href "#"} "ClojExCMS Admin"))
                   (dom/div {:class "collapse navbar-collapse navbar-ex1-collapse"}
                            (dom/ul {:class "nav navbar-nav side-nav"}
                                    (for [entry menu-entries]
                                      (om/build menu-entry ui {:opts entry})))))))

(defcomponent flash-messages [ui owner]
  (render [_]
          #_(dom/h1 "flash")))
