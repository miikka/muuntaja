(ns muuntaja.artisanal-json
  "A strawman implementation of JSON encoding and decoding.

  Encoding uses string-building, decoding uses Jackson's streaming API. These
  may or may not yield good performance."
  (:import (com.fasterxml.jackson.core JsonFactory)
           (muuntaja.artisanal JsonEncoder JsonDecoder)))

(defn to-json [x]
  (.toString (doto (JsonEncoder.) (.toJson x))))

(def ^JsonFactory factory (JsonFactory.))

(defn from-json [x]
  (with-open [jp (.createJsonParser factory ^String x)]
    (.nextToken jp)
    (JsonDecoder/fromJson jp)))
