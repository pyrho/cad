(ns cst-buttons.skeleton
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]
            [cst-buttons.constants :refer :all]))

(def long-arm
  (cube ltrac-length skeleton-width bottom-clearance))

(def short-arm
    (translate [short-skeleton-arm-x 0 0]
               (cube skeleton-width ltrac-width bottom-clearance)))

(def side-upwards
  (->>
    (cube skeleton-width 2 skeleton-upwards-length)
    (translate [short-skeleton-arm-x (/ ltrac-width 2) (/ skeleton-upwards-length 2)])))

(def fillet
  (->> (cylinder 0.8 10)
       (with-fn 100)
       (rotate [0 (/ pi 2) 0])
       (translate [short-skeleton-arm-x (+ 0.3 (/ ltrac-width 2)) 0.02])))

(def skeleton 
  (union
    short-arm
    side-upwards
    fillet
    long-arm))

