(defproject isthereafuckingbroncosgame "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-beta2"]
                 [org.mnode.ical4j/ical4j "1.0.2"]
                 [clj-http "3.7.0"]
                 [clj-time "0.14.0"]]
  :main ^:skip-aot isthereafuckingbroncosgame.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
