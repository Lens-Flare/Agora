(ns keygen.test.core
  (:use [keygen.core])
  (:use [clojure.test]))

(deftest gen-test
  (-main 512 "id"))

(deftest de-ser-test
  (deserialize "id"))