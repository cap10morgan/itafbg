(ns isthereafuckingbroncosgame.web
  (:require [compojure.core :refer [defroutes GET ANY]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [hiccup.page :refer [html5]]
            [isthereafuckingbroncosgame.fucking-broncos :as fb]
            [ring.adapter.jetty :as jetty]
            [tick.core :as t]))

(set! *warn-on-reflection* true)

(defn home-page [date]
  (let [date (or date (t/today))
        date (fb/date-in-denver date)]
    {:status  200
     :headers {"Content-Type" "text/html"}
     ;; TODO: DRY up the messages w/ cli ns
     :body    (html5 {}
                     [:head
                      [:title "Is there a fucking Broncos game today?"]
                      [:meta {:name    "viewport"
                              :content "width=device-width, initial-scale=1"}]]
                     [:body
                      [:p "Today is " (t/format "EEEE, MMMM d, y" date)]
                      [:h1 "Is there a fucking Broncos game?"]
                      (if (fb/is-it-fucking-football-season? date)
                        (let [game (fb/is-there-a-fucking-broncos-game? date)]
                          (if game
                            [:div {:class "fuck"}
                             [:p "UGH! Yes, there is a fucking Broncos game today."]
                             [:p "The fucking tee time or whatever is "
                              (t/format "h:mm a" (:start game)) "."]
                             [:p
                              "Stay home or find some place without a fucking TV."
                              [:br]
                              "Otherwise enjoy the fucking traffic, shouty morons, "
                              "and large, annoying crowds."]]
                            (let [next-game (fb/when-is-the-next-fucking-game? date)]
                              [:div {:class "phew"}
                               [:p "PHEW no fucking Broncos bullshit today. Enjoy Denver!"]
                               (when next-game
                                 [:p "Heads up tho, dawg. The next fucking game is on "
                                  (t/format "EEEE, MMM d" (:start next-game))
                                  " at " (t/format "h:mm a" (:start next-game)) "."])])))
                        [:p "It's not even American Tackle Football Season, dawg!"])
                      [:h1
                       [:a
                        {:href "https://denverfan.com/2096930/the-oddsmakers-seem-to-think-aaron-rodgers-is-coming-to-the-broncos/"}
                        "FUCK AARON RODGERS!!!!!"]]])}))

(defroutes app
  (GET "/" [d]
    (home-page d))
  (ANY "*" []
    (route/not-found "WTF you looking for?")))

(defn run [{:keys [port] :or {port 8080}}]
  (let [port (if (string? port) (Integer/parseInt port) port)
        site (-> app (wrap-defaults site-defaults))]
    (println "Starting web server on port" port)
    (jetty/run-jetty site {:port port :join? false})))

(defn -main [& [port]]
  (run {:port (or port (:port env))}))
