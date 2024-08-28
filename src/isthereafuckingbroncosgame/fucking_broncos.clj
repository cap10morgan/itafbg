(ns isthereafuckingbroncosgame.fucking-broncos
  (:require
   [clojure.string :as str]
   [clj-http.client :as http])
  (:import (java.io StringReader)
           (java.time LocalDate OffsetDateTime ZoneId ZonedDateTime)
           (java.time.format DateTimeFormatter)
           (java.util Optional)
           (net.fortuna.ical4j.data CalendarBuilder)
           (net.fortuna.ical4j.model.component VEvent)
           (net.fortuna.ical4j.model.property DateProperty Location Summary)))

(set! *warn-on-reflection* true)

(def broncos-schedule-url
  "https://www.denverbroncos.com/api/addToCalendar/ag/s?text=")

(def ^ZoneId denver-tz
  (ZoneId/of "America/Denver"))

(defn nfl-season [& [reference-date]]
  (let [^LocalDate ref-date (or reference-date (LocalDate/now))
        year-num            (-> ref-date .getYear)
        year                (if (> 3 (-> ref-date .getMonthValue))
                              (dec year-num)
                              year-num)]
    {:start (LocalDate/of ^int year 8 1)
     :end   (LocalDate/of ^int (inc year) 2 10)}))

(defn cal-date->local-date-time [^Optional maybe-date]
  (when (.isPresent maybe-date)
    (let [^DateProperty dp     (.get maybe-date)
          ^OffsetDateTime date (.getDate dp)]
      (.atZoneSameInstant date denver-tz))))

(defn format-date-time
  [fmt ^ZonedDateTime dt]
  (let [formatter (DateTimeFormatter/ofPattern fmt)]
    (.format dt formatter)))

(def cal-builder
  (delay
   (System/setProperty "net.fortuna.ical4j.timezone.cache.impl"
                       "net.fortuna.ical4j.util.MapTimeZoneCache")
   (CalendarBuilder.)))

(defn find-opponent
  [contest-summary]
  (let [away (first contest-summary)
        home (second contest-summary)]
    (if (= "Denver Broncos" home)
      away
      home)))

(defn game-dates []
  (let [ical                     (-> broncos-schedule-url
                                     http/get
                                     :body)
        ^StringReader sr         (StringReader. ical)
        ^CalendarBuilder builder @cal-builder
        cal                      (.build builder sr)]
    (for [c (.getComponents cal)]
      (let [maybe-location (.. ^VEvent c getLocation)
            location       (when (.isPresent maybe-location)
                             (let [^Location location (.get maybe-location)]
                               (.getValue location)))
            maybe-summary  (.. ^VEvent c getSummary)
            summary        (when (.isPresent maybe-summary)
                             (let [^Summary summary (.get maybe-summary)]
                               (.getValue summary)))
            opponent       (some-> ^Summary summary
                                   (str/split #" at ")
                                   find-opponent)]
        {:start    (cal-date->local-date-time (.getDateTimeStart ^VEvent c))
         :end      (cal-date->local-date-time (.getDateTimeEnd ^VEvent c))
         :location location
         :opponent opponent}))))

(defn is-it-fucking-football-season? [^LocalDate date]
  (let [season       (nfl-season date)
        after-start? (.isAfter date (:start season))
        before-end?  (.isBefore date (:end season))]
    (and after-start? before-end?)))

(defn is-this-fucking-game-on-this-date?
  [^LocalDate date {:keys [^ZonedDateTime start]}]
  (let [local-start (LocalDate/from start)]
    (.isEqual date local-start)))

(defn is-there-a-fucking-broncos-game? [date]
  (some #(when (is-this-fucking-game-on-this-date? date %) %)
        (game-dates)))

(defn home-game?
  [game]
  (str/includes? (:location game) "Denver"))

(defn when-is-the-next-fucking-game? [^LocalDate date]
  (let [max-days-to-search 100]
    (loop [d         date
           remaining max-days-to-search]
      (if-let [game (is-there-a-fucking-broncos-game? d)]
        game
        (when (and (is-it-fucking-football-season? d)
                   (< 0 remaining))
          (recur (.plusDays d 1)
                 (dec remaining)))))))
