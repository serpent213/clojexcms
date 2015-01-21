(ns clojexcms.views.content
  "Render a tabbed editor for content elements"
  (:require [bootstrap-cljs :as bs :include-macros true]
            [clojexcms.tabpanel :refer (tabpanel)]
            [clojexcms.server :as server]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defn update-content! [content owner]
  (server/update-content! content
                          (fn []
                            (om/update! content :dirty? false)
                            (om/update! content :changed-on-server? false))))

(defn handle-change [e content owner]
  (om/update! content :dirty? true)
  (om/update! content :body (.. e -target -value)))

(defcomponent edit-view [content owner]
  (render [_]
          (dom/form {:role "form"}
                    (dom/textarea {:class "form-control"
                                   :value (:body content)
                                   :on-change #(handle-change % content owner)
                                   :style {:width "100%"
                                           :height "60ex"}})
                    (if (:changed-on-server? content)
                      (dom/div {:class "alert alert-warning"
                                :role "alert"
                                :style {:margin-top "8px"}}
                               (dom/strong "Warning: ")
                               (str "Content changed server-side by someone else. If you "
                                    "publish now, these changes will be overwritten!")))
                    (bs/button-toolbar {:class "pull-right" :style {:margin-top "8px"}}
                                       (bs/button {:bs-style "warning"
                                                   :disabled (not (:dirty? content))
                                                   :on-click #(update-content! content owner)}
                                                  "Publish")))))

(defcomponent content-view [content-all owner]
  (render [_]
          (dom/div
           (dom/h1 "Content snippets")
           (om/build tabpanel (sort-by :position (vals content-all))
                     {:state {:tabid :id
                              :tabtitle :title
                              :tabbody edit-view}}))))
