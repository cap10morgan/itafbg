(ns isthereafuckingbroncosgame.fucking-broncos
  (:require [clj-time.core :as t]
            [clj-time.coerce :as tc]
            [clj-http.client :as http])
  (:import (java.io StringReader)
           (net.fortuna.ical4j.data CalendarBuilder)))

(def broncos-schedule-url
  "https://www.stanza.co/api/schedules/nfl-broncos/nfl-broncos.ics")

(defn nfl-season [& [reference-date]]
  (let [ref-date (or reference-date (t/today))
        year (if (> 3 (t/month ref-date))
               (dec (t/year ref-date))
               (t/year ref-date))]
    {:start (t/local-date year 8 1)
     :end   (t/local-date (inc year) 2 10)}))

(defn cal-date->local-date-time [date]
  (-> date
      .getDate
      tc/to-date-time
      (t/to-time-zone (t/time-zone-for-id "America/Denver"))
      tc/to-local-date-time))

(def cal-builder
  (delay
   (System/setProperty "net.fortuna.ical4j.timezone.cache.impl"
                       "net.fortuna.ical4j.util.MapTimeZoneCache")
   (CalendarBuilder.)))

(defn game-dates []
  (let [ical (-> broncos-schedule-url
                 http/get
                 :body)
        sr (StringReader. ical)
        builder @cal-builder
        cal (.build builder sr)]
    (for [c (.getComponents cal)]
      {:start (cal-date->local-date-time (.getStartDate c))
       :end   (cal-date->local-date-time (.getEndDate c))})))

(defn is-it-fucking-football-season? [date]
  (let [after-start? (t/after? date (:start (nfl-season)))
        before-end? (t/before? date (:end (nfl-season)))]
    (and after-start? before-end?)))

(defn is-this-fucking-game-on-this-date? [date {:keys [start]}]
  (and (= (t/year start) (t/year date))
       (= (t/month start) (t/month date))
       (= (t/day start) (t/day date))))

(defn is-there-a-fucking-broncos-game? [date]
  (some #(when (is-this-fucking-game-on-this-date? date %) %)
        (game-dates)))
