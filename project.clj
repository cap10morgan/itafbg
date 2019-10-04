(defproject isthereafuckingbroncosgame "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.mnode.ical4j/ical4j "3.0.11"]
                 [clj-http "3.10.0"]
                 [clj-time "0.15.2"]
                 [compojure "1.6.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [environ "1.1.0"]
                 [hiccup "1.0.5"]]
  :min-lein-version "2.7.1"
  :plugins [[environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :main ^:skip-aot isthereafuckingbroncosgame.cli
  :aliases {"web" ["run" "-m" "isthereafuckingbroncosgame.web"]}
  :uberjar-name "isthereafuckingbroncosgame-standalone.jar"
  :profiles {:uberjar {:aot :all}})
