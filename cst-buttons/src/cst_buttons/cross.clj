(ns cst-buttons.cross
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]
            [cst-buttons.constants :refer :all]))

(defn create-cross [isTopCross]
  (let [c cross-data
        create-arm (#(cube 
                      (c :width) 
                      (get-in c [:arm :width])
                      (get-in c [(if isTopCross :top :side) :height])))]
    ; elevate the cross so that it sits above the skeleton
    (translate 
      [0 0 bottom-clearance]
      ; first branch of the cross
      (union
        create-arm
        (rotate [0 0 (/ pi 2)] create-arm)))))

(defn place-cross [x y z isTopCross]
  (translate [x y z] (create-cross isTopCross)))

(def top-cross 
  (let [top-offset (get-in cross-data [:top :offset])]
    (place-cross 
      (- ltrac-x-mid top-offset) 0 0.3 true)))

; A little hole to accomodate the plastic thing coming out of the side cross.
; Probably a PCB mount inside the mouse
(defn side-cross-holes [x y z]
  (->> (sphere 3)
       (with-fn 100)
       (translate [x y (+ z 4)])))

(def left-cross 
  (let [side-offset (get-in cross-data [:side :offset])
        x short-skeleton-arm-x
        y (+ side-offset (- ltrac-y-mid))
        z 0
        isTopCross false]
    (difference
      (place-cross x y z isTopCross)
      (side-cross-holes x y z))))

(def right-cross 
  (let [side-offset (get-in cross-data [:side :offset])
        x short-skeleton-arm-x
        y (- ltrac-y-mid side-offset)
        z 0
        isTopCross false]
    (difference
      (place-cross x y z isTopCross)
      (side-cross-holes x y z))))

(def crosses 
  (union top-cross left-cross right-cross))
