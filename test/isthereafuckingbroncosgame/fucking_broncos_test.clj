(ns isthereafuckingbroncosgame.fucking-broncos-test
  (:require [clojure.test :refer :all]
            [isthereafuckingbroncosgame.fucking-broncos :refer :all])
  (:import (java.time LocalDate)))

(deftest is-it-fucking-football-season?-test
  (testing "March 21 is not fucking football season"
    (is (not (is-it-fucking-football-season? (LocalDate/of 2021 03 21)))))
  (testing "October 5 is (sadly) fucking football season"
    (is (is-it-fucking-football-season? (LocalDate/of 2021 10 05)))))

;; This doesn't work once the next season's schedule goes up
;; b/c the source I'm getting the schedule iCal feeds from doesn't
;; seem to archive old ones. :(
#_(deftest is-there-a-fucking-broncos-game?-test
    (testing "There sure as fuck was a Broncos game on Jan. 3, 2021"
      (is (is-there-a-fucking-broncos-game? (LocalDate/of 2021 1 3)))))
