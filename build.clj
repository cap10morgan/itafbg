(ns build
  (:require [clojure.tools.build.api :as b]
            [clojure.string :as str]))

(def lib 'isthereafuckingbroncosgame.web.static)
(def class-dir "target/classes")
(def basis (b/create-basis {:project "deps.edn"}))
(def jar-file (format "target/%s.jar" (name lib)))

(defn clean [_]
  (b/delete {:path "target"})
  (b/delete {:path "public"}))

(defn uber [_]
  (clean nil)
  (b/copy-dir {:src-dirs   ["src"]
               :target-dir class-dir})
  (b/compile-clj {:basis     basis
                  :src-dirs  ["src"]
                  :class-dir class-dir})
  (b/uber {:class-dir class-dir
           :uber-file jar-file
           :basis     basis
           :main      'isthereafuckingbroncosgame.web.static}))

(defn html [_]
  (clean nil)
  (b/process {:command-args ["clojure" "-X:html"]}))

(defn dhtml [_]
  (clean nil)
  (b/process {:command-args ["clojure" "-X:dhtml"]}))

(defn ipfs-update [{:keys [remote quiet?]}]
  (html nil)
  (let [remote (name remote)
        {:keys [exit out err]} (b/process {:command-args
                                           ["ipfs" "add" "-Q" "public/index.html"]
                                           :out :capture
                                           :err :capture})
        _ (when-not (zero? exit)
            (when-not quiet? (println "Error adding file to IPFS:"
                                      out "\n" err))
            (System/exit exit))
        ipfs-hash (str/trim out)]
    (if quiet?
      (do (print ipfs-hash) (flush))
      (println "Added IPFS hash:" ipfs-hash))
    (b/process {:command-args ["ipfs" "pin" "add" ipfs-hash]
                :out (if quiet? :ignore :inherit)
                :err (if quiet? :ignore :inherit)})
    (when remote
      (b/process {:command-args ["ipfs" "pin" "remote" "add"
                                 (str "--service=" remote)
                                 (str "--name=isthereafuckingbroncosga.me")
                                 ipfs-hash]
                  :out (if quiet? :ignore :inherit)
                  :err (if quiet? :ignore :inherit)}))))

#_(defn cloudflare-dnslink-update [{:keys [ipfs-hash]}]
    (let [client  (cfd/api-client)
          zone-id (cfd/zone-name->id "isthereafuckingbroncosga.me")]
      (cfd/set-dnslink-record)))

(defn deploy [_]
  (ipfs-update :pinata))

