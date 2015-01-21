(ns clojexcms.state
  "Application state")

(defonce app-state (atom {:site
                          {:name "ClojExCMS"}

                          :ui
                          {:page :dashboard}

                          :content
                          ;; {:example {:id "example"
                          ;;            :description "Example for documentation"
                          ;;            :body "foo"
                          ;;            :more-db-columns 23}}
                          {}}))
