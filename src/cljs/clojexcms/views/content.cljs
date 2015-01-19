(ns clojexcms.views.content
  (:require [bootstrap-cljs :as bs :include-macros true]
            [clojexcms.tabpanel :refer (tabpanel)]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(def chsk-send! (atom #()))

(defn update-content! [content owner]
  (@chsk-send! [:content/update! {:id (:id content) :body (:body content)}]
               5000
               (fn [cb-reply]
                 (when (= cb-reply :content/update-success)
                   (om/set-state! owner :dirty? false)))))

(defn handle-change [e content owner]
  (om/set-state! owner :dirty? true)
  (om/update! content :body (.. e -target -value)))

(defcomponent edit-view [content owner]
  (init-state [_]
              {:dirty? false})
  (render-state [_ {:keys [dirty?]}]
                (dom/div
                 (dom/textarea {:value (:body content)
                                :on-change #(handle-change % content owner)
                                :cols 100 :rows 20})
                 (bs/button-toolbar
                  (bs/button {:bs-style "primary"
                              :disabled (not dirty?)
                              :on-click #(update-content! content owner)}
                             "Publish")))))

(defcomponent content-view [content-all owner]
  (render [_]
          (dom/div
           (dom/h1 "Content")
           (om/build tabpanel content-all
                     {:state {:tabid :id
                              :tabtitle :title
                              :tabbody edit-view}}))))
