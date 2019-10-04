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
    ;; TODO: DRY up the messages w/ web ns
    ;(println "\nGame dates:" (pr-str (fb/game-dates)))
    (println "\nUsing date" today)
    (if (fb/is-it-fucking-football-season? today)
      (do
        (println "\n\nUnfortunately it is fucking football season.")
        (let [game (fb/is-there-a-fucking-broncos-game? today)]
          ;(println "Game:" (pr-str game))
          (if game
            (do
              (println "UGH there is a fucking Broncos game today.")
              (println "The fucking tee time or whatever is"
                       (str (tf/unparse-local (tf/formatter "h:mm a")
                                              (:start game)) "."))
              (println "Stay home or find some place without a fucking TV.")
              (println "Otherwise enjoy the fucking traffic, shouty morons,")
              (println "and large, annoying crowds."))
            (println "PHEW no Broncos bullshit today!"))))
      (println "\n\nIt's not even American Tackle football season, dawg."))))