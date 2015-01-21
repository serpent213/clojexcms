(ns clojexcms.core
  "Client startup and page dispatcher"
  (:require [clojexcms.frame :refer (navigation-menu flash-messages)]
            [clojexcms.server :as server]
            [clojexcms.state :refer (app-state)]
            [clojexcms.views.content :refer (content-view)]
            [clojexcms.views.dashboard :refer (dashboard-view)]
            [clojexcms.views.login :refer (login-view)]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(enable-console-print!)

(def menu-entries
  [{:id :dashboard :icon "bullseye" :title "Dashboard"}
   {:id :content   :icon "pencil"   :title "Content"}
   {:id :empty     :icon "globe"    :title "You name it"}])

(defcomponent page [app owner]
  (render [_]
          (if (not (get-in app [:auth :logged-in?]))
            (om/build login-view (:auth app))
            (case (get-in app [:ui :page])
              :dashboard  (om/build dashboard-view app)
              :content    (om/build content-view (:content app))
              :empty      (dom/h1 (dom/small "This page intentionally left blank."))))))

(defn main []
  (server/start-router!)
  (om/root
   (fn [app owner]
     (om/component
      (dom/div
       (om/build navigation-menu
                 {:site (:site app)
                  :auth (:auth app)
                  :ui (:ui app)}
                 {:opts {:menu-entries menu-entries}})
       (dom/div {:id "page-wrapper"}
                (om/build flash-messages (:ui app))
                (om/build page app)))))
   app-state
   {:target (. js/document (getElementById "wrapper"))}))
