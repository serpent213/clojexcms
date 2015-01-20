(ns clojexcms.state)

(defonce app-state (atom {:site
                          {:name "ClojExCMS"}

                          :ui
                          {:page :dashboard}

                          :content
                          []}))
