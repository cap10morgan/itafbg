(ns isthereafuckingbroncosgame.web.dynamic
  (:require
    [clojure.java.io :as io]
    [compojure.core :refer [defroutes GET ANY]]
    [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    [compojure.route :as route]
    [environ.core :refer [env]]
    [hiccup.page :refer [html5]]
    [isthereafuckingbroncosgame.fucking-broncos :as fb]
    [ring.adapter.jetty :as jetty]
    [tick.core :as t]
    [isthereafuckingbroncosgame.config :refer [config]])
  (:gen-class))

(set! *warn-on-reflection* true)

(def ^:const default-port 80)

(defn home-page [date]
  (let [date (or date (t/today))
        date (fb/date-in-denver date)]
    {:status  200
     :headers {"Content-Type" "text/html"}
     ;; TODO: DRY up the messages w/ cli ns
     :body
     (html5
       {}
       [:head
        [:title "Is there a fucking Broncos game today?"]
        [:meta {:name    "viewport"
                :content "width=device-width, initial-scale=1"}]
        [:script {:src "today.js"}]
        [:script "document.addEventListener(\"DOMContentLoaded\", setTodayElement);"]
        [:script "window.setInterval(setTodayElement, 3600000);"]]
       [:body
        [:p {:id "today-element"}]
        [:h1 "Is there a fucking Broncos game?"]
        (if (fb/is-it-fucking-football-season? date)
          (let [game (fb/is-there-a-fucking-broncos-game? date)]
            (if game
              [:div {:class "fuck"}
               [:p "UGH! Yes, there is a fucking Broncos game today."]
               [:p {:style "margin-top: 30pt"} "The fucking tee time or whatever is "
                (t/format "h:mm a" (:start game)) "."]
               [:p "They're playing the fucking " (:opponent game)
                " but who gives a shit?"]
               [:p (if (fb/home-game? game)
                     "It's a home game, so better stay at least 10 miles from Colfax & Federal."
                     "It's an away game, so probably even worse at bars and restaurants.")]
               [:p
                "Stay home or find some place without a fucking TV."
                [:br]
                "Otherwise enjoy the "
                (if (fb/home-game? game)
                  "fucking traffic "
                  "loud-ass televisions ")
                "and large, annoying crowds."]]
              (let [next-game (fb/when-is-the-next-fucking-game? date)]
                [:div {:class "phew"}
                 [:p "PHEW no fucking Broncos bullshit today. Enjoy Denver!"]
                 (when next-game
                   [:div {:style "margin-top: 30pt"}
                    [:p "Heads up tho, dogg. The next fucking game is on "
                     (t/format "EEEE, MMM d" (:start next-game))
                     " at " (t/format "h:mm a" (:start next-game)) "."]
                    [:p "They're playing the fucking " (:opponent next-game)
                     " but who gives a shit?"]
                    [:p (if (fb/home-game? next-game)
                          "It's a home game. Meaning \"You should stay home that day.\""
                          "It's an away game. Hopefully far away.")]])])))
          [:p "It's not even American Tackle Football Season, dawg!"])])}))

(defroutes app
  (GET "/health" []
    {:status  200
     :headers {}
     :body    ""})
  (GET "/" [d]
    (home-page d))
  (ANY "*" []
    (route/not-found "WTF you looking for?")))

(defn run [& [{:keys [port] :or {port (:default-port @config)}}]]
  (let [port (if (string? port) (Integer/parseInt port) port)
        site (-> app (wrap-defaults site-defaults))]
    (println "Starting web server on port" port)
    (jetty/run-jetty site {:port port :join? false})))

(defn generate-html [{:keys [path]}]
  (let [html      (:body (home-page nil))
        html-path (str path "/index.html")
        js-path   (str path "/today.js")]
    (io/make-parents html-path)
    (-> html-path io/file (spit html))
    (->> "js/today.js" io/resource slurp (spit js-path))))

(defn -main [{:keys [port generate]}]
  (if-let [p (or port (:port env))]
    (run {:port p})
    (if-let [gen-path generate]
      (generate-html {:path gen-path})
      (run))))
