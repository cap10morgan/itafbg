(ns isthereafuckingbroncosgame.fucking-broncos-test
  (:require [clojure.test :refer :all]
            [isthereafuckingbroncosgame.fucking-broncos :refer :all]
            [clj-time.core :as t]))

(deftest is-it-fucking-football-season?-test
  (testing "March 21 is not fucking football season"
    (is (not (is-it-fucking-football-season? (t/local-date 2021 03 21)))))
  (testing "October 5 is (sadly) fucking football season"
    (is (is-it-fucking-football-season? (t/local-date 2021 10 05)))))
