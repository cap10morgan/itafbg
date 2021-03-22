(ns isthereafuckingbroncosgame.fucking-broncos-test
  (:require [clojure.test :refer :all]
            [isthereafuckingbroncosgame.fucking-broncos :refer :all]
            [clj-time.core :as t]))

(deftest is-it-fucking-football-season?-test
  (testing "March 21 is not fucking football season"
    (is (not (is-it-fucking-football-season? (t/local-date 2021 03 21)))))
  (testing "October 5 is (sadly) fucking football season"
    (is (is-it-fucking-football-season? (t/local-date 2021 10 05)))))

(deftest is-there-a-fucking-broncos-game?-test
  (testing "There sure as fuck was a Broncos game on Jan. 3, 2021"
    (is (is-there-a-fucking-broncos-game? (t/local-date 2021 1 3)))))
