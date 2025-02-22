(ns asami.memory-index-test
  #?(:clj
     (:require [asami.graph :refer [graph-add resolve-pattern count-pattern]]
               [asami.index :refer [empty-graph]]
               [schema.test :as st :refer [deftest]]
               [clojure.test :as t :refer [testing is run-tests]])
     :cljs
     (:require [asami.graph :refer [graph-add resolve-pattern count-pattern]]
               [asami.index :refer [empty-graph]]
               [schema.test :as st :refer-macros [deftest]]
               [clojure.test :as t :refer-macros [testing is run-tests]])))

(t/use-fixtures :once st/validate-schemas)

(def data
  [[:a :p1 :x]
   [:a :p1 :y]
   [:a :p2 :z]
   [:a :p3 :x]
   [:b :p1 :x]
   [:b :p2 :x]
   [:b :p3 :z]
   [:c :p4 :t]])

(defn assert-data [g d]
  (reduce (fn [g [s p o]] (graph-add g s p o 1)) g d))

(defn unordered-resolve
  [g pattern]
  (into #{} (resolve-pattern g pattern)))

(deftest test-load
  (let [g (assert-data empty-graph data)
        r1 (unordered-resolve g '[:a ?a ?b])
        r2 (unordered-resolve g '[?a :p2 ?b])
        r3 (unordered-resolve g '[:a :p1 ?a])
        r4 (unordered-resolve g '[?a :p2 :z])
        r5 (unordered-resolve g '[:a ?a :x])
        r6 (unordered-resolve g '[:a :p4 ?a])
        r6' (unordered-resolve g '[:a :p3 ?a])
        r7 (unordered-resolve g '[:a :p1 :x])
        r8 (unordered-resolve g '[:a :p1 :b])
        r9 (unordered-resolve g '[?a ?b ?c])]
    (is (= #{[:p1 :x]
             [:p1 :y]
             [:p2 :z]
             [:p3 :x]} r1))
    (is (= #{[:a :z]
             [:b :x]} r2))
    (is (= #{[:x]
             [:y]} r3))
    (is (= #{[:a]} r4))
    (is (= #{[:p1]
             [:p3]} r5))
    (is (empty? r6))
    (is (= #{[:x]} r6'))
    (is (= #{[]} r7))
    (is (empty? r8))
    (is (= (into #{} data) r9))))

(deftest test-count
  (let [g (assert-data empty-graph data)
        r1 (count-pattern g '[:a ?a ?b])
        r2 (count-pattern g '[?a :p2 ?b])
        r3 (count-pattern g '[:a :p1 ?a])
        r4 (count-pattern g '[?a :p2 :z])
        r5 (count-pattern g '[:a ?a :x])
        r6 (count-pattern g '[:a :p4 ?a])
        r7 (count-pattern g '[:a :p1 :x])
        r8 (count-pattern g '[:a :p1 :b])
        r9 (count-pattern g '[?a ?b ?c])]
    (is (= 4 r1))
    (is (= 2 r2))
    (is (= 2 r3))
    (is (= 1 r4))
    (is (= 2 r5))
    (is (zero? r6))
    (is (= 1 r7))
    (is (zero? r8))
    (is (= 8 r9))))

#?(:cljs (run-tests))

