(ns metabase.query-processor-test.string-extracts-test
  (:require [metabase.query-processor-test :refer :all]
            [metabase.test.data :as data]
            [metabase.test.data.datasets :as datasets]))

(defn- test-string-extract
  [expr]
  (->> {:expressions {"test" expr}
        :fields      [[:expression "test"]]
        :limit       1}
       (data/run-mbql-query venues)
       rows
       ffirst))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  "ed Medicine"
  (test-string-extract [:trim [:field-id (data/id :venues :name)] "R"]))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  "Red Medicin"
  (test-string-extract [:trim [:field-id (data/id :venues :name)] "e"]))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  "foo"
  (test-string-extract [:trim " foo "]))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  "foo "
  (test-string-extract [:ltrim " foo "]))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  " foo"
  (test-string-extract [:rtrim " foo "]))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  "RED MEDICINE"
  (test-string-extract [:upper [:field-id (data/id :venues :name)]]))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  "red medicine"
  (test-string-extract [:lower [:field-id (data/id :venues :name)]]))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  "Red"
  (test-string-extract [:substring [:field-id (data/id :venues :name)] 1 3]))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  "Blue Medicine"
  (test-string-extract [:replace [:field-id (data/id :venues :name)] "Red" "Blue"]))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  1
  (test-string-extract [:coalesce 1 2]))

(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions :regex)
  "R"
  (test-string-extract [:regex-match-first [:field-id (data/id :venues :name)] "[A-Z]+"]))

;; test nesting
(datasets/expect-with-drivers (non-timeseries-drivers-with-feature :expressions)
  "ED"
  (test-string-extract [:upper [:rtrim [:substring [:trim [:field-id (data/id :venues :name)] "R"] 1 3]]]))
