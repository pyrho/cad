(ns cst-buttons.skeleton
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]
            [cst-buttons.constants :refer :all]))

(def skeleton 
  (union
    ; y axis (short)
    (translate [short-skeleton-arm-x 0 0]
               (cube skeleton-width ltrac-width bottom-clearance))
    ; x axis (long)
    (cube ltrac-length skeleton-width bottom-clearance)))
