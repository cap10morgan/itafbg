(ns isthereafuckingbroncosgame.fucking-broncos
  (:require [tick.core :as t]
            [clj-http.client :as http])
  (:import (java.io StringReader)
           (java.time Month Year)
           (net.fortuna.ical4j.data CalendarBuilder)
           (net.fortuna.ical4j.model.component VEvent)
           (net.fortuna.ical4j.model.property DateProperty)))

(set! *warn-on-reflection* true)

(def broncos-schedule-url
  "https://www.stanza.co/api/schedules/nfl-broncos/nfl-broncos.ics")

(defn date-in-denver
  ([date]
   (-> date
       t/date
       (t/at "12:00")
       (t/in "America/Denver")))
  ([year month day]
   (date-in-denver (t/new-date year month day))))

(defn nfl-season [& [reference-date]]
  (let [ref-date (or reference-date (t/today))
        year-num (-> ref-date ^Year t/year .getValue)
        year     (if (> 3 (-> ref-date ^Month t/month .getValue))
                   (dec year-num)
                   year-num)]
    {:start (date-in-denver year 8 1)
     :end   (date-in-denver (inc year) 2 10)}))

(defn cal-date->local-date-time [^DateProperty date]
  (-> date
      .getDate
      t/date-time
      (t/in "America/Denver")))

(def cal-builder
  (delay
    (System/setProperty "net.fortuna.ical4j.timezone.cache.impl"
                        "net.fortuna.ical4j.util.MapTimeZoneCache")
    (CalendarBuilder.)))

(defn game-dates []
  (let [ical                     (-> broncos-schedule-url
                                     http/get
                                     :body)
        ^StringReader sr         (StringReader. ical)
        ^CalendarBuilder builder @cal-builder
        cal                      (.build builder sr)]
    (for [c (.getComponents cal)]
      {:start (cal-date->local-date-time (.getStartDate ^VEvent c))
       :end   (cal-date->local-date-time (.getEndDate ^VEvent c))})))

(defn is-it-fucking-football-season? [date]
  (let [season       (nfl-season date)
        after-start? (t/> date (:start season))
        before-end?  (t/< date (:end season))]
    (and after-start? before-end?)))

(defn is-this-fucking-game-on-this-date? [date {:keys [start]}]
  (and (= (t/year start) (t/year date))
       (= (t/month start) (t/month date))
       (= (t/day-of-month start) (t/day-of-month date))))

(defn is-there-a-fucking-broncos-game? [date]
  (some #(when (is-this-fucking-game-on-this-date? date %) %)
        (game-dates)))

(defn when-is-the-next-fucking-game? [date]
  (let [max-days-to-search 100]
    (loop [d         date
           remaining max-days-to-search]
      (if-let [game (is-there-a-fucking-broncos-game? d)]
        game
        (when (and (is-it-fucking-football-season? d)
                   (< 0 remaining))
          (recur (t/>> d (t/new-duration 1 :days))
                 (dec remaining)))))))
