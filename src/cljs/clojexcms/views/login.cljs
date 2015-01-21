(ns clojexcms.views.login
  "User authentication"
  (:require [bootstrap-cljs :as bs :include-macros true]
            [clojexcms.server :as server]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defn login! [auth-state owner]
  (server/login!
   (om/get-state owner :user-id)
   (om/get-state owner :password))

  ;; no form submission
  false)

(def form-width "380px")

(defcomponent login-view [auth-state owner]
  (init-state [_]
              {:user-id ""
               :password ""})
  (render-state [_ {:keys [user-id password]}]
                (dom/div {:class "row"}
                         (dom/div {:class "col-lg-12 text-center v-center"
                                   :style {:padding-top "100px"}}
                                  (dom/h1 "Administrator Login")
                                  (dom/form {:class "col-lg-12"
                                             :on-submit #(login! auth-state owner)}
                                            (dom/div {:class "form-group"
                                                      :style {:width form-width
                                                              :margin-left "auto"
                                                              :margin-right "auto"}}
                                                     (dom/input {:type "text" :class "form-control input-lg"
                                                                 :placeholder "Email address"
                                                                 :value user-id
                                                                 :on-change
                                                                 #(om/set-state! owner :user-id (.. % -target -value))}))
                                            (dom/div {:class "form-group"
                                                      :style {:width form-width
                                                              :margin-left "auto"
                                                              :margin-right "auto"}}
                                                     (dom/input {:type "password" :class "form-control input-lg"
                                                                 :placeholder "Password"
                                                                 :value password
                                                                 :on-change
                                                                 #(om/set-state! owner :password (.. % -target -value))}))
                                            (dom/button {:type "submit" :class "btn btn-lg btn-primary"
                                                         :style {:width "150px"}}
                                                        "Login"))))))
