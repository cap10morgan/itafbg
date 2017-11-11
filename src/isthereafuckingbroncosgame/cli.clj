(ns isthereafuckingbroncosgame.cli
  (:require [clj-time.coerce :as tc]
            [clj-time.format :as tf]
            [isthereafuckingbroncosgame.fucking-broncos :as fb])
  (:import (java.util Date))
  (:gen-class))

(defn -main [& args]
  (let [date-arg (first args)
        today (if date-arg
                (->> date-arg
                     (tf/parse (tf/formatter "yyyy-MM-dd"))
                     tc/to-local-date)
                (tc/to-local-date (Date.)))]
    #_(println "\nGame dates:" (pr-str (game-dates)))
    (println "\nUsing date" today)
    (if (fb/is-it-fucking-football-season? today)
      (do
        (println "\n\nUnfortunately it is fucking football season.")
        (if (fb/is-there-a-fucking-broncos-game? today)
          (println "UGH there is a fucking Broncos game today.")
          (println "PHEW no Broncos bullshit today!")))
      (println "\n\nIt's not even football season, dawg."))))