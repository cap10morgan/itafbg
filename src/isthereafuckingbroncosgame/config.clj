(ns isthereafuckingbroncosgame.config
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as str]))

(defmethod aero/reader 'op
  [_opts _tag {:keys [account reference]}]
  (let [{:keys [exit out err]} (sh "op" (str "--account=" account) "read"
                                   reference)]
    (if (zero? exit)
      (str/trim-newline out)
      {::error err})))

(defn load-config
  []
  (-> "config.edn"
      io/resource
      aero/read-config))

(def config (delay (load-config)))
