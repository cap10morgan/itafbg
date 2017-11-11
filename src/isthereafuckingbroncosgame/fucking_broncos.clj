(ns isthereafuckingbroncosgame.fucking-broncos
  (:require [clj-time.core :as t]
            [clj-time.coerce :as tc]
            [clj-http.client :as http])
  (:import (java.io StringReader)
           (net.fortuna.ical4j.data CalendarBuilder)))

(def broncos-schedule-url
  "https://www.stanza.co/api/schedules/nfl-broncos/nfl-broncos.ics")

(def nfl-season
  {:start (t/local-date 2017 9 7)
   :end   (t/local-date 2018 2 4)})

(defn cal-date->local-date-time [date]
  (-> date
      .getDate
      tc/to-date-time
      (t/to-time-zone (t/time-zone-for-id "America/Denver"))
      tc/to-local-date-time))

(defn game-dates []
  (let [ical (-> broncos-schedule-url
                 http/get
                 :body)
        sr (StringReader. ical)
        builder (CalendarBuilder.)
        cal (.build builder sr)]
    (for [c (.getComponents cal)]
      {:start (cal-date->local-date-time (.getStartDate c))
       :end   (cal-date->local-date-time (.getEndDate c))})))

(defn is-it-fucking-football-season? [date]
  (let [after-start? (t/after? date (:start nfl-season))
        before-end? (t/before? date (:end nfl-season))]
    (and after-start? before-end?)))

(defn is-this-fucking-game-on-this-date? [date {:keys [start]}]
  (and (= (t/year start) (t/year date))
       (= (t/month start) (t/month date))
       (= (t/day start) (t/day date))))

(defn is-there-a-fucking-broncos-game? [date]
  (boolean (some (partial is-this-fucking-game-on-this-date? date)
                 (game-dates))))