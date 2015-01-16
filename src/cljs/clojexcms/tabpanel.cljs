(ns clojexcms.tabpanel
  (:require-macros [cljs.core.async.macros :refer (go go-loop)])
  (:require [cljs.core.async :as async :refer (<! >! put! chan)]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(defn tablist [tab owner]
  (reify
    om/IRenderState
    (render-state [_ {:keys [switch active index]}]
                  (dom/li #js {:role "presentation" :className (if active "active")
                               :onClick (fn [e]
                                          (put! switch index)
                                          (.preventDefault e))}
                          (dom/a #js {:href (str "#" (:id tab)) :aria-controls (:id tab)
                                      :role "tab" :data-toggle "tab"}
                                 (:description tab))))))

(defn tabcontent [tab owner]
  (reify
    om/IRenderState
    (render-state [_ {:keys [active]}]
                  (dom/div #js {:role "tabpanel" :className (str "tab-pane" (if active " active"))
                                :id (:id tab)}
                           (dom/textarea #js {:value (:body tab) :cols 100 :rows 20})))))

(defn tabpanel [tabs owner]
  (reify
    om/IInitState
    (init-state [_]
                {:switch (chan)
                 :active-tab 0})
    om/IWillMount
    (will-mount [_]
                (let [switch (om/get-state owner :switch)]
                  (go-loop []
                           (let [index (<! switch)]
                             (println "switch to" index)
                             (om/set-state! owner :active-tab index))
                           (recur))))
    om/IRenderState
    (render-state [_ {:keys [switch active-tab]}]
                  (dom/div #js {:role "tabpanel"}
                           (apply dom/ul #js {:className "nav nav-tabs" :role "tablist"}
                                  (map-indexed (fn [i tab]
                                                 (om/build tablist tab
                                                           {:state {:switch switch
                                                                    :active (= i active-tab)
                                                                    :index i}}))
                                               (vals tabs)))
                           (apply dom/div #js {:className "tab-content"}
                                  (map-indexed (fn [i tab]
                                                 (om/build tabcontent tab
                                                           {:state {:active (= i active-tab)}}))
                                               (vals tabs)))))))
