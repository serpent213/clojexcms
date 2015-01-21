(ns clojexcms.tabpanel
  "Tabbed panel for Bootstrap"
  (:require-macros [cljs.core.async.macros :refer (go go-loop)])
  (:require [cljs.core.async :as async :refer (<! >! put! chan)]
            [om.core :as om :include-macros true]
            [om-tools.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]))

(defcomponent tablist [tab owner]
  (render-state [_ {:keys [switch active index tabid tabtitle]}]
                (dom/li {:role "presentation"
                         :class (if active "active")
                         :on-click (fn [e]
                                     (put! switch index)
                                     (.preventDefault e))}
                        (let [id (if tabid (tabid tab) (str index))]
                          (dom/a {:href (str "#" id)
                                  :aria-controls id
                                  :role "tab"
                                  :data-toggle "tab"}
                                 (tabtitle tab))))))

(defcomponent tabcontent [tab owner]
  (render-state [_ {:keys [active index tabid tabbody]}]
                (let [id (if tabid (tabid tab) (str index))]
                  (dom/div {:role "tabpanel"
                            :class (str "tab-pane" (if active " active"))
                            :id id}
                           (om/build tabbody tab)))))

(defcomponent tabpanel [tabs owner]
  (init-state [_]
              {:switch (chan)
               :active-tab 0
               :tabtitle (fn [_] ":tabtitle")
               :tabbody (fn [_ _] (om/component (dom/span ":tabbody")))})
  (will-mount [_]
              (let [switch (om/get-state owner :switch)]
                (go-loop []
                         (let [index (<! switch)]
                           (om/set-state! owner :active-tab index))
                         (recur))))
  (render-state [_ {:keys [switch active-tab tabid tabtitle tabbody]}]
                (dom/div {:role "tabpanel"}
                         (dom/ul {:class "nav nav-tabs"
                                  :role "tablist"}
                                 (map-indexed (fn [i tab]
                                                (om/build tablist tab
                                                          {:state {:switch switch
                                                                   :active (= i active-tab)
                                                                   :index i
                                                                   :tabid tabid
                                                                   :tabtitle tabtitle}}))
                                              tabs))
                         (dom/div {:class "tab-content"}
                                  (map-indexed (fn [i tab]
                                                 (om/build tabcontent tab
                                                           {:state {:active (= i active-tab)
                                                                    :index i
                                                                    :tabid tabid
                                                                    :tabbody tabbody}}))
                                               tabs)))))
