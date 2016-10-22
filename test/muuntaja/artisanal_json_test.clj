(ns muuntaja.artisanal-json-test
  (:require [muuntaja.artisanal-json :as artisanal]
            [clojure.test :refer [deftest is testing]]))

(deftest artisanal-test
  (testing "encoding JSON"
    (is (= "{\"hello\":\"world\"}" (artisanal/to-json {"hello" "world"}))))

  (testing "decoding JSON"
    (is (= {"hello" "world"} (artisanal/from-json "{\"hello\":\"world\"}")))))
