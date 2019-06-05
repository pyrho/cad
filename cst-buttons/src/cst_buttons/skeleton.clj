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
  (->> (cube skeleton-width 2 skeleton-upwards-length)
       (translate [short-skeleton-arm-x (/ ltrac-width 2) (/ skeleton-upwards-length 2)])))

(def back-clip
  (let [skd skeleton-data
        clip-height (get-in skd [:back :height])
        clip-z-offset (+ (/ clip-height 2) 0.65)
        clip-x-offset (- (- ltrac-width) 1.5)
        upwards (->> (cube skeleton-width 2 clip-height)
                     (rotate [0 0 (/ pi 2)])
                     (translate [clip-x-offset 0 clip-z-offset]))
        angle (get-in skd [:back :angle])
        clip (->> (cube 2 10 10)
                  (rotate [0 (deg->rad angle) 0])
                  (translate [ -85 0 7]))]
    (union upwards clip)))

(def back-fillet
  (let [bottom (->> (difference (cylinder 1.8 10) (translate [-2.2, -2.7 1] (cube 5 5 20)))
                    (with-fn 50)
                    (rotate [0 (/ pi 2) (/ pi 2)])
                    (translate [(- (- ltrac-width) 0.75) 0 1]))
        top (->> (cylinder 1.3 10)
                 (with-fn 50)
                 (rotate [0 (/ pi 2) (/ pi 2)])
                 (translate [(- (- ltrac-width) 1.75) 0 4]))]
    (union top bottom)))
  

(def side-fillet
  (->> (cylinder 1.2 10)
       (with-fn 100)
       (rotate [0 (/ pi 2) 0])
       (translate [short-skeleton-arm-x (- (/ ltrac-width 2) 0.1) 0.5])))

(def skeleton 
  (union
    short-arm
    back-clip
    back-fillet
    long-arm))
    ; side-upwards
    ; side-fillet


