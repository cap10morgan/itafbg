(ns isthereafuckingbroncosgame.cli
  (:require [tick.core :as t]
            [isthereafuckingbroncosgame.fucking-broncos :as fb])
  (:gen-class))

(set! *warn-on-reflection* true)

(defn run [{:keys [date] :or {date (fb/date-in-denver (t/today))}}]
  (let [date (if (string? date)
               (fb/date-in-denver date)
               date)]
    ;; TODO: DRY up the messages w/ web ns
    ;(println "\nGame dates:" (pr-str (fb/game-dates)))
    (println "\nUsing date" (t/format (:iso-local-date t/predefined-formatters)
                                      date))
    (if (fb/is-it-fucking-football-season? date)
      (do
        (println "\n\nUnfortunately it is fucking football season.")
        (let [game (fb/is-there-a-fucking-broncos-game? date)]
          ;(println "Game:" (pr-str game))
          (if game
            (do
              (println "UGH there is a fucking Broncos game today.")
              (println "The fucking tee time or whatever is"
                       (str (t/format "h:mm a" (:start game)) "."))
              (println "Stay home or find some place without a fucking TV.")
              (println "Otherwise enjoy the fucking traffic, shouty morons,")
              (println "and large, annoying crowds."))
            (let [next-game (fb/when-is-the-next-fucking-game? date)]
              (println "PHEW no Broncos bullshit today!")
              (when next-game
                (println "Heads up tho, dawg. The next fucking game is on"
                         (t/format "EEEE, MMM d" (:start next-game))
                         "at" (str (t/format "h:mm a" (:start next-game)) ".")))))))
      (println "\n\nIt's not even American Tackle football season, dawg."))))

(defn -main [& args]
  (let [date (first args)]
    (run {:date date})))
