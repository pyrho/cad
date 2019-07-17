(ns hand-base.core
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.model :refer :all]
            [scad-clj.scad :refer :all]))

;;; Constants
(def base-radius 40)
(def insert-height 20)
(def hand-ellipse {:x 25 :y 25.9})

;;; Pieces
(def base
  "The base used to stabilize the hand."
  (extrude-linear {:height 3 :center false} (with-fn 100 (circle base-radius))))

(def insert
  "A piece that will go inside the hand to hold it in place.
  It's not a perfect circle so it needs to be resized like an ellipse."
  (let [outer (with-fn 100 (resize [(hand-ellipse :x) (hand-ellipse :y) 1] (circle 1)))
        inner (scale [0.8 0.8 0.8] outer)]
    (extrude-linear {:height insert-height :center false} (difference outer inner))))

(def insert-hollow
  "This object is used to hollow out the center of the base"
  (let [outer (with-fn 100 (resize [25 25.9 1] (circle 1)))
        inner (scale [0.8 0.8 0.8] outer)]
    (extrude-linear {:height insert-height } inner)))


(def all
  (difference (union base insert) insert-hollow))

(defn spit-scad [name obj]
  (spit (str "resources/" name ".scad") (write-scad obj)))

(spit-scad "out" all)
