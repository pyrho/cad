(ns kailh-caps.core
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.model :refer :all]
            [scad-clj.scad :refer :all]))

(def cap-thickness
  "The minnimum width of each wall element of the keycap"
  1.5)

(def prong-dimensions
  "The x y z dimensions of one prong that go into the stem"
  [1.2 3 3])

(def distance-between-prong-centers
  "The x distance between the middle of each prongs"
  5.70)

(def cap-1u-x-bottom
  "The x length of the base of the keycap"
  17.5)
(def cap-1u-x-top
  "The x length of the top of the keycap"
  14.3)

(def cap-1u-y-bottom
  "The y length at bottom"
  16.6)

(def cap-1u-y-top 13)

(def cap-z 3.5)

(defmacro def-cap-size
  "Create all the bindings for cap sizes for a given Unit"
  [unit]
  `(do
     (def ~(symbol (str "cap-" unit "u-x-top"))
       ~(* unit cap-1u-x-top))
     (def ~(symbol (str "cap-" unit "u-x-bottom"))
       ~(* unit cap-1u-x-bottom))
     (def ~(symbol (str "cap-" unit "u-y-top"))
       ~(* unit cap-1u-y-bottom))
     (def ~(symbol (str "cap-" unit "u-y-bottom"))
       ~(* unit cap-1u-y-bottom))))

(def-cap-size 2)
(def-cap-size 1.5)

(defn thingi-1
  []
  (translate [-10 0 0] (rotate [(-(/ pi 2)) 0 0] (import "/Users/damien/Downloads/Low_Profile_Spherical_Keycap_v2.stl"))))

(defn thingi-2
  []
  (import "/Users/damien/Downloads/KailhCap2u_v0.stl"))

(def prong
  "Create a single prong"
  (apply cube prong-dimensions))

(def place-prongs
  "Create the two cap prongs positioned relatively to one another and center then on the plane"
  (let [left prong
        right (translate [distance-between-prong-centers 0 0] prong)
        mid-x (/ distance-between-prong-centers 2)
        mid-z (/ (get prong-dimensions 2) 2)]
    (translate [(- mid-x)
                0
                (+ (-(/ (get prong-dimensions 2) 2)) (- cap-z cap-thickness))]
               (union left right))))

(def make-1u
  "Make a 1u keycap.
  A hull of the bottom and top parts is made.
  Then a smaller hull is made, offseted by `cap-thickness`, the difference is taken to make the cap.
  "
  (let [hull-plate-z 0.01
        outer        (let [bottom (cube cap-1u-x-bottom cap-1u-y-bottom hull-plate-z)
                           top    (translate [0 0 cap-z] (cube cap-1u-x-top cap-1u-y-top hull-plate-z))]
                       (hull bottom top))
        inner        (let [bottom        (cube (- cap-1u-x-bottom cap-thickness) (- cap-1u-y-bottom cap-thickness) hull-plate-z)
                           bottom-placed (translate [0 0 -1] bottom)
                           top           (cube (- cap-1u-x-top cap-thickness) (- cap-1u-y-top cap-thickness) hull-plate-z)
                           top-placed    (translate [0 0 (- cap-z cap-thickness)] top)]
                       (hull bottom-placed top-placed))]
    (difference outer inner)))

(def spit-scad
  (spit "resources/out.scad" (s/write-scad (union
                                            make-1u
                                            place-prongs
                                            ))))

spit-scad

;;;;;;;;;;;;;;;;;
;; Experiments ;;
;;;;;;;;;;;;;;;;;
;; cap-2u-x-top
;; cap-2u-x-bottom
;; cap-2u-y-top
;; cap-2u-y-bottom
