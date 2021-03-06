(ns isthereafuckingbroncosgame.web
  (:require [clj-time.coerce :as tc]
            [clj-time.format :as tf]
            [compojure.core :refer [defroutes GET ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [hiccup.page :refer [html5]]
            [isthereafuckingbroncosgame.fucking-broncos :as fb]
            [ring.adapter.jetty :as jetty]
            [clj-time.format :as tf])
  (:import (java.util Date)))

(defn home-page [date]
  (let [today (tc/to-local-date (if date
                                  (tf/parse (tf/formatter "yyyy-MM-dd") date)
                                  (Date.)))]
    {:status 200
     :headers {"Content-Type" "text/html"}
     ;; TODO: DRY up the messages w/ cli ns
     :body (html5 {}
                  [:head
                   [:title "Is there a fucking Broncos game today?"]
                   [:meta {:name "viewport"
                           :content "width=device-width, initial-scale=1"}]]
                  [:body
                   [:p "Today is " (tf/unparse-local-date
                                    (tf/formatter "EEEE, MMMM d, y") today)]
                   [:h1 "Is there a fucking Broncos game?"]
                   (if (fb/is-it-fucking-football-season? today)
                     (let [game (fb/is-there-a-fucking-broncos-game? today)]
                       (if game
                         [:div {:class "fuck"}
                          [:p "UGH! Yes, there is a fucking Broncos game today."]
                          [:p "The fucking tee time or whatever is "
                           (tf/unparse-local (tf/formatter "h:mm a")
                                             (:start game)) "."]
                          [:p
                           "Stay home or find some place without a fucking TV."
                           [:br]
                           "Otherwise enjoy the fucking traffic, shouty morons, "
                           "and large, annoying crowds."]]
                         [:p "PHEW no Broncos bullshit today. Enjoy Denver!"]))
                     [:p "It's not even American Tackle Football Season, dawg!"])])}))

(defroutes app
  (GET "/" [d]
       (home-page d))
  (ANY "*" []
       (route/not-found "WTF you looking for?")))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 8080))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))