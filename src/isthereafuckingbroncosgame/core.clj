(ns isthereafuckingbroncosgame.core
  (:require [clj-http.client :as http]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]
            [clj-time.format :as tf])
  (:import (net.fortuna.ical4j.data CalendarBuilder)
           (java.io StringReader)
           (java.util Date))
  (:gen-class))

(def broncos-schedule-url
  "https://www.stanza.co/api/schedules/nfl-broncos/nfl-broncos.ics")

(def nfl-season
  {:start #inst "2017-09-07"
   :end   #inst "2018-02-04"})

(defn game-dates []
  (let [ical (-> broncos-schedule-url
                 http/get
                 :body)
        sr (StringReader. ical)
        builder (CalendarBuilder.)
        cal (.build builder sr)]
    (for [c (.getComponents cal)]
      {:start (.getDate (.getStartDate c))
       :end   (.getDate (.getEndDate c))})))

(defn is-it-fucking-football-season? [date]
  (let [jdate (tc/from-date date)
        after-start? (t/after? jdate (tc/from-date (:start nfl-season)))
        before-end? (t/before? jdate (tc/from-date (:end nfl-season)))]
    (and after-start? before-end?)))

(defn is-this-fucking-game-on-this-date? [date game]
  (let [jstart (tc/from-date (:start game))
        jdate (tc/from-date date)]
    (and (= (t/year jstart) (t/year jdate))
         (= (t/month jstart) (t/month jdate))
         (= (t/day jstart) (t/day jdate)))))

(defn is-there-a-fucking-broncos-game? [date]
  (boolean (some (partial is-this-fucking-game-on-this-date? date)
                 (game-dates))))

(defn -main [& args]
  (let [dates (game-dates)
        date-arg (first args)
        today (if date-arg
                (->> date-arg
                     (tf/parse (tf/formatter "yyyy-MM-dd"))
                     tc/to-date)
                (Date.))]
    (println "Using date" today)
    (doseq [d dates]
      (prn d))
    (if (is-it-fucking-football-season? today)
      (do
        (println "\n\nUnfortunately it is fucking football season.")
        (if (is-there-a-fucking-broncos-game? today)
          (println "UGH there is a fucking Broncos game today.")
          (println "PHEW no Broncos bullshit today!")))
      (println "\n\nIt's not even football season, dawg."))))
