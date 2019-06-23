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

;; (def cap-1u-y-bottom
;;   "The y length at bottom"
;;   16.6)

;; (def cap-1u-y-top 13)
(def cap-1u-y-bottom
  "The y length at bottom"
  cap-1u-x-bottom)

(def cap-1u-y-top cap-1u-x-top)

(def cap-z
  "The original caps are `3.5`"
  4.5)

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
  (translate [-10 0 0] (rotate [(- (/ pi 2)) 0 0] (import "/Users/damien/Downloads/Low_Profile_Spherical_Keycap_v2.stl"))))

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
                (- (+ (- (/ (get prong-dimensions 2) 2)) (- cap-z cap-thickness)) 2)]
               (union left right))))

(defn make-dsa
  ""
  [& {:keys [top bottom x-angle y-angle depth] :or {x-angle 0 y-angle 0}}]
  (let [sliver            0.0001
        make-rounded-cube (fn [x y z radius] (minkowski
                                              (cube (- x (* 2 radius)) (- y (* 2 radius)) (- z sliver))
                                              (cylinder radius sliver)))
        get-dish-radus    (fn [width depth] (/ (+ (* width width) (* 4 depth depth)) (* 8 depth)))
        bottom-plate      (with-fs 0.1 (make-rounded-cube (bottom :x) (bottom :y) 0.01 0.5))
        top-plate         (with-fs 0.1 (->> (make-rounded-cube (top :x) (top :y) 0.01 3)
                                            (rotate [(deg->rad x-angle) (deg->rad y-angle) 0])
                                            (translate [0 0 cap-z])))
        dish-maker-y      (->> (with-fn 100 (cylinder (get-dish-radus (top :x) depth) 60))
                               (rotate [(/ pi 2) 0 0])
                               (translate [0 0 (- (get-dish-radus (top :x) depth) depth)])
                               (rotate [(deg->rad x-angle) (deg->rad y-angle) 0])
                               (translate [0 0 cap-z]))
        dish-maker-x      (->> (with-fn 100 (cylinder (get-dish-radus (top :x) depth) 60))
                               (rotate [(/ pi 2) 0 (/ pi 2)])
                               (translate [0 0 (- (get-dish-radus (top :x) depth) depth)])
                               (rotate [(deg->rad x-angle) (deg->rad y-angle) 0])
                               (translate [0 0 cap-z]))
        dish-maker-sphere (->> (with-fn 100 (sphere (get-dish-radus (* (top :x) (Math/sqrt 2)) depth)))
                               (scale [1 2.1 1])
                               (translate [0 0 (- (get-dish-radus (* (top :x) (Math/sqrt 2)) depth) depth)])
                               (rotate [(deg->rad x-angle) (deg->rad y-angle) 0])
                               (translate [0 0 cap-z]))
        cap               (difference (hull top-plate bottom-plate) dish-maker-sphere)
        bottom-plate-inner (with-fs 0.1 (->> (make-rounded-cube (- (bottom :x) cap-thickness) (- (bottom :y) cap-thickness) 0.01 0.5)
                                             (translate [0 0 -1])))
        top-plate-inner (with-fs 0.1
                          (->> (make-rounded-cube (- (top :x) cap-thickness) (- (top :y) cap-thickness) 0.01 3)
                               (translate [0 0 (- cap-z cap-thickness)])
                               (rotate [(deg->rad x-angle) (deg->rad y-angle) 0])))
        cap-inner (difference (hull top-plate-inner bottom-plate-inner) (translate [0 0 (- cap-thickness)] dish-maker-sphere))
        left-prong (let [x-offset 0
                         top-plate (->> (cube (get prong-dimensions 0) (get prong-dimensions 1) 0.01)
                                                        (translate [x-offset 0 5]))
                         bottom-plate (->> (cube (get prong-dimensions 0) (get prong-dimensions 1) 0.01)
                                           (translate [x-offset 0 cap-z])
                                           (translate [0 0 -3])
                                           (translate [0 0 (- (get prong-dimensions 2))]))]
                     (color [0 1 0] (hull top-plate bottom-plate)))
        right-prong (let [x-offset distance-between-prong-centers
                         top-plate (->> (cube (get prong-dimensions 0) (get prong-dimensions 1) 0.01)
                                        (translate [x-offset 0 5]))
                         bottom-plate (->> (cube (get prong-dimensions 0) (get prong-dimensions 1) 0.01)
                                           (translate [x-offset 0 cap-z])
                                           (translate [0 0 -3])
                                           (translate [0 0 (- (get prong-dimensions 2))]))]
                      (color [0 1 0] (hull top-plate bottom-plate)))
        prongs (difference (translate [(- (/ distance-between-prong-centers 2)) 0 0] (union left-prong right-prong))dish-maker-sphere )]
    (union prongs (difference cap cap-inner))))

(def spit-scad
  (spit "resources/out.scad" (write-scad (union
                                          (make-dsa :top {:x cap-1u-x-top :y cap-2u-y-top}
                                                    :bottom {:x cap-1u-x-bottom :y cap-2u-y-bottom}
                                                    :y-angle 10
                                                    :depth 1.5)
                                          ;; place-prongs
                                          ))))

spit-scad

;;;;;;;;;;;;;;;;;
;; Experiments ;;
;;;;;;;;;;;;;;;;;
;; cap-2u-x-top
;; cap-2u-x-bottom
;; cap-2u-y-top
;; cap-2u-y-bottom
