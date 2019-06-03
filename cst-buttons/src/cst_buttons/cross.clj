(ns cst-buttons.cross
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]
            [cst-buttons.constants :refer :all]))

(defn create-cross [isTopCross]
  (let [c cross-data
        create-arm #((cube 
                      (c :width) 
                      (get-in c [:arm :width])
                      (get-in c [(if isTopCross :top :side) :height])))]
    ; elevate the cross so that it sits above the skeleton
    (translate 
      [0 0 bottom-clearance]
      ; first branch of the cross
      (union
        (create-arm)
        (rotate 0 0 90 (create-arm))))))

(defn place-cross [x y z isTopCross]
  (translate [x y z] (create-cross isTopCross)))

(def top-cross 
  (let [cross-top-offset 29]
    (place-cross 
      (- ltrac-x-mid cross-top-offset) 0 0 true)))

(def left-cross 
  (place-cross 
    short-skeleton-arm-x (+ (cross :side-offset) (- ltrac-y-mid) ) 0 false))

(def right-cross 
  (place-cross 
    short-skeleton-arm-x (- ltrac-y-mid (cross :side-offset)) 0 false))

(def crosses 
  (union top-cross left-cross right-cross))
