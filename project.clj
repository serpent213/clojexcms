(defproject clojexcms "0.1.0-SNAPSHOT"
  :description "Clojure(Script) Example CMS"
  :url "https://github.com/improper/clojexcms"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :source-paths ["src/clj" "src/sql"]

  :test-paths ["spec/clj"]

  :clean-targets ^{:protect false} [:target-path "resources/public/js"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665" :scope "provided"]

                 [compojure "1.3.1"]
                 [enlive "1.1.5"]
                 [environ "1.0.0"]
                 [http-kit "2.1.19"]
                 [markdown-clj "0.9.61" :exclusions [org.clojure/clojure]]
                 [om "0.8.0-rc1"]
                 [postgresql "9.3-1102.jdbc41"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.1.2"]
                 [yesql "0.4.0"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-environ "1.0.0"]
            [com.github.metaphor/lein-flyway "1.0"]
            [lein-less "1.7.2"]]

  :min-lein-version "2.5.0"

  :uberjar-name "clojexcms.jar"

  :cljsbuild {:builds {:app {:source-paths ["src/cljs"]
                             :compiler {:output-to     "resources/public/js/app.js"
                                        :output-dir    "resources/public/js/out"
                                        :source-map    "resources/public/js/out.js.map"
                                        :preamble      ["react/react.min.js"]
                                        :optimizations :none
                                        :pretty-print  true}}}}


  :less {:source-paths ["src/less"]
         :target-path "resources/public/css"}

  :profiles {:dev {:source-paths ["env/dev/clj"]

                   :dependencies [[figwheel "0.1.7-SNAPSHOT"]
                                  [leiningen "2.5.0"]]

                   :repl-options {:init-ns clojexcms.server}

                   :plugins [[lein-figwheel "0.1.7-SNAPSHOT"]]

                   :figwheel {:http-server-root "public"
                              :server-port 3449
                              :css-dirs ["resources/public/css"]}

                   :env {:is-dev true}

                   :cljsbuild {:builds
                               {:app
                                {:source-paths ["env/dev/cljs"]}}}}

             :uberjar {:source-paths ["env/prod/clj"]
                       :hooks [leiningen.cljsbuild leiningen.less]
                       :env {:production true}
                       :omit-source true
                       :aot :all
                       :cljsbuild {:builds {:app
                                            {:source-paths ["env/prod/cljs"]
                                             :compiler
                                             {:optimizations :advanced
                                              :pretty-print false}}}}}}

  ;; Flyway Database Migration configuration
  :flyway {
           ;; Database connection
           :driver "org.postgresql.Driver"
           :url "jdbc:postgresql://localhost/clojexcms"
           :user "dev"
           :password "dev"

           ;; Migration locations
           :locations ["migrations"]})
