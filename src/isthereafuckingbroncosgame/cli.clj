(ns isthereafuckingbroncosgame.cli
  (:require
   [isthereafuckingbroncosgame.fucking-broncos :as fb]
   [cheshire.core :as json])
  (:gen-class)
  (:import (java.time LocalDate)))

(set! *warn-on-reflection* true)

(defn run [{:keys [date] :or {date (LocalDate/now)}}]
  (let [mode    (some-> "ITAFBG_MODE" System/getenv keyword)
        fmt     (some-> "ITAFBG_FMT" System/getenv keyword)
        date    (if (string? date)
                  (LocalDate/parse date)
                  date)
        denver  (if (= :masto mode)
                  "#Denver"
                  "Denver")
        broncos (if (= :masto mode)
                  ["#Broncos" "#DenverBroncos"]
                  ["Broncos" "Denver Broncos"])
        output (with-out-str
                 ;; TODO: DRY up the messages w/ web ns
                 ;(println "\nGame dates:" (pr-str (fb/game-dates)))
                 (println "Today is" (str (t/format "EEEE, MMMM d, y" date) "."))
                 (println "\nIs there a fucking" (second broncos) "game?")
                 (if (fb/is-it-fucking-football-season? date)
                   (let [game (fb/is-there-a-fucking-broncos-game? date)]
                     ;(println "Game:" (pr-str game))
                     (if game
                       (do
                         (println "UGH there is a fucking" (second broncos) "game today.")
                         (println "The fucking tee time or whatever is"
                                  (str (t/format "h:mm a" (:start game)) "."))
                         (println "They're playing the fucking" (:opponent game)
                                  "but who gives a shit?")
                         (if (fb/home-game? game)
                           (println "It's a home game, so better stay at least 10 miles from Colfax & Federal.")
                           (println "It's an away game, so probably even worse at bars and restaurants."))
                         (println "Stay home or find some place without a fucking TV.")
                         (println (str "Otherwise enjoy the ")
                                  (if (fb/home-game? game)
                                    "fucking traffic"
                                    "loud-ass televisions"))
                         (print "and large, annoying crowds.")
                         (when (= :mast mode)
                           (println denver)
                           (println)))
                       (let [next-game (fb/when-is-the-next-fucking-game? date)]
                         (println "PHEW no fucking" (first broncos) "bullshit today. Enjoy"
                                  (str denver "!"))
                         (when next-game
                           (println "Heads up tho, dawg. The next fucking game is on"
                                    (t/format "EEEE, MMM d" (:start next-game))
                                    "at" (str (t/format "h:mm a" (:start next-game)) "."))
                           (println "They're playing the fucking" (:opponent next-game)
                                    "but who gives a shit?")
                           (println (if (fb/home-game? next-game)
                                      "It's a home game. Meaning \"You should stay home that day.\""
                                      "It's an away game. Hopefully far away."))))))
                   (println "\n\nIt's not even American Tackle football season, dawg."))
                 (when (= :masto mode)
                   (println "\nhttps://isthereafuckingbroncosga.me/")))]
    (println (case fmt
               :json (json/encode (case mode
                                    :masto {:status output}
                                    output))
               output))))

(defn -main [& args]
  (let [date (first args)]
    (run {:date date})))
