(ns clojexcms.frame
  (:require [bootstrap-cljs :as bs :include-macros true]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defn menu-entry [icon text]
  (dom/li (dom/a {:href "#"}
                 (dom/i {:class (str "fa fa-" icon)})
                 " " text)))

(defn main-menu []
  (list
   (menu-entry "bullseye"   "Dashboard")
   (menu-entry "pencil"     "Content")
   (menu-entry "globe"      "You name it")))

(defcomponent navigation-menu [ui owner]
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
                                     (main-menu))))))

(defcomponent flash-messages [ui owner]
  (render [_]
          #_(dom/h1 "flash")))
