(ns clojexcms.tabpanel
  (:require-macros [cljs.core.async.macros :refer (go go-loop)])
  (:require [cljs.core.async :as async :refer (<! >! put! chan)]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn tablist [tab owner]
  (reify
    om/IRenderState
    (render-state [_ {:keys [switch active index tabid tabtitle]}]
                  (dom/li #js {:role "presentation" :className (if active "active")
                               :onClick (fn [e]
                                          (put! switch index)
                                          (.preventDefault e))}
                          (let [id (if tabid (tabid tab) (str index))]
                            (dom/a #js {:href (str "#" id) :aria-controls id
                                        :role "tab" :data-toggle "tab"}
                                   (tabtitle tab)))))))

(defn tabcontent [tab owner]
  (reify
    om/IRenderState
    (render-state [_ {:keys [active index tabid tabbody]}]
                  (let [id (if tabid (tabid tab) (str index))]
                    (dom/div #js {:role "tabpanel" :className (str "tab-pane" (if active " active"))
                                  :id id}
                             (om/build tabbody tab))))))

(defn tabpanel [tabs owner]
  (reify
    om/IInitState
    (init-state [_]
                {:switch (chan)
                 :active-tab 0
                 :tabtitle (fn [_] ":tabtitle")
                 :tabbody (fn [_ _] (om/component (dom/span nil ":tabbody")))})
    om/IWillMount
    (will-mount [_]
                (let [switch (om/get-state owner :switch)]
                  (go-loop []
                           (let [index (<! switch)]
                             (om/set-state! owner :active-tab index))
                           (recur))))
    om/IRenderState
    (render-state [_ {:keys [switch active-tab tabid tabtitle tabbody]}]
                  (dom/div #js {:role "tabpanel"}
                           (apply dom/ul #js {:className "nav nav-tabs" :role "tablist"}
                                  (map-indexed (fn [i tab]
                                                 (om/build tablist tab
                                                           {:state {:switch switch
                                                                    :active (= i active-tab)
                                                                    :index i
                                                                    :tabid tabid
                                                                    :tabtitle tabtitle}}))
                                               (vals tabs)))
                           (apply dom/div #js {:className "tab-content"}
                                  (map-indexed (fn [i tab]
                                                 (om/build tabcontent tab
                                                           {:state {:active (= i active-tab)
                                                                    :index i
                                                                    :tabid tabid
                                                                    :tabbody tabbody}}))
                                               (vals tabs)))))))
