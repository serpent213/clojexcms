(ns clojexcms.state
  "Application state")

(defonce app-state (atom {:site
                          {:name "ClojExCMS"}

                          :auth
                          {:logged-in? false
                           :uid        nil
                           :fullname   nil}

                          :ui
                          {:page :dashboard}

                          :content
                          #_{:example {:id                  "example"
                                       :description         "Example for documentation"
                                       :body                "foo"
                                       :more-db-columns     23
                                       ; the following are client-only
                                       :dirty?              false
                                       :changed-on-server?  false}}
                          {}}))
