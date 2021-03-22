(defproject isthereafuckingbroncosgame "0.1.0-SNAPSHOT"
  :description "Is there a fucking Broncos game?"
  :url "https://github.com/cap10morgan/itafbg"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.mnode.ical4j/ical4j "3.0.22"]
                 [clj-http "3.12.1"]
                 [clj-time "0.15.2"]
                 [compojure "1.6.2"]
                 [ring/ring-jetty-adapter "1.9.2"]
                 [environ "1.2.0"]
                 [hiccup "1.0.5"]]
  :min-lein-version "2.7.1"
  :plugins [[environ/environ.lein "0.3.1"]]
  :hooks [environ.leiningen.hooks]
  :main ^:skip-aot isthereafuckingbroncosgame.cli
  :aliases {"web" ["run" "-m" "isthereafuckingbroncosgame.web"]}
  :uberjar-name "isthereafuckingbroncosgame-standalone.jar"
  :profiles {:uberjar {:aot :all}})
