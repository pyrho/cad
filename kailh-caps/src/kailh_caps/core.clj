(ns kailh-caps.core
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.model :refer :all]
            [scad-clj.scad :refer :all]))

(def cap-thickness
  "The minnimum width of each wall element of the keycap"
  1.5)

(def prong-dimensions
  "The x y z dimensions of one prong that go into the stem"
  [1.2 2.9 3])

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
  [unit name]
  `(do
     (def ~(symbol (str "cap-" name "u-x-top"))
       ~(* unit cap-1u-x-top))
     (def ~(symbol (str "cap-" name "u-x-bottom"))
       ~(* unit cap-1u-x-bottom))
     (def ~(symbol (str "cap-" name "u-y-top"))
       ~(* unit cap-1u-y-bottom))
     (def ~(symbol (str "cap-" name "u-y-bottom"))
       ~(* unit cap-1u-y-bottom))))

(def-cap-size 2 "2")
(def-cap-size 1.5 "1point5")
(def-cap-size 1.25 "1point25")

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
  "Creates a keycap with a DSA-ish profile.
  It takes a dictionnary as argument the following keys:
  - `top`: a dictionary with two keys: `x` & `y` describing the size in mm of the top keycap
  - `bottom`: same as for top, but for the bottom
  - `depth`: The depth of the well created on the top of the keycap, the value shouldn't exceed 3, otherwise the well would be too deep

  "
  [& {:keys [top bottom x-angle y-angle depth height] :or {x-angle 0 y-angle 0}}]
  (let [;; A magic number to adjust the prong length from the top of the well, i'm lazy to figure out how to compute this rn :/
        z-offset-magic-nr -1.3 ;; 0.5 works well for a well depth of 1.5

        sliver            0.0001
        make-rounded-cube (fn [x y z radius] (minkowski
                                              (cube (- x (* 2 radius)) (- y (* 2 radius)) (- z sliver))
                                              (cylinder radius sliver)))
        get-dish-radus    (fn [width depth] (/ (+ (* width width) (* 4 depth depth)) (* 8 depth)))
        bottom-plate      (with-fs 0.1 (make-rounded-cube (bottom :x) (bottom :y) 0.01 0.5))
        top-plate         (with-fs 0.1 (->> (make-rounded-cube (top :x) (top :y) 0.01 3)
                                            (rotate [(deg->rad x-angle) (deg->rad y-angle) 0])
                                            (translate [0 0 height])))
        dish-maker-sphere (->> (with-fn 100 (sphere (get-dish-radus (* (top :x) (Math/sqrt 2)) depth)))
                               (scale [1 2.1 1])
                               (translate [0 0 (- (get-dish-radus (* (top :x) (Math/sqrt 2)) depth) depth)])
                               (rotate [(deg->rad x-angle) (deg->rad y-angle) 0])
                               (translate [0 0 height]))
        cap               (difference (hull top-plate bottom-plate) dish-maker-sphere)
        bottom-plate-inner (with-fs 0.1 (->> (make-rounded-cube (- (bottom :x) cap-thickness) (- (bottom :y) cap-thickness) 0.01 0.5)
                                             (translate [0 0 -0.001])))
        top-plate-inner (with-fs 0.1
                          (->> (make-rounded-cube (- (top :x) cap-thickness) (- (top :y) cap-thickness) 0.01 3)
                               (translate [0 0 (- height cap-thickness depth)])
                               (rotate [(deg->rad x-angle) (deg->rad y-angle) 0])))
        cap-inner (-# (hull top-plate-inner bottom-plate-inner))
        left-prong (let [top-plate (->> (cube (get prong-dimensions 0) (get prong-dimensions 1) 0.01)
                                        (translate [0 0 5]))
                         bottom-plate (->> (cube (get prong-dimensions 0) (get prong-dimensions 1) 0.01)
                                           (translate [0 0 0])
                                           ;; (translate [0 0 -3])
                                           ;; ( translate [0 0 (- (- (- (get prong-dimensions 2) z-offset-magic-nr)) (/ height 2))])
                                           (translate [0 0 (- (get prong-dimensions 2))]))]
                     (color [0 1 0] (hull top-plate bottom-plate)))
        test (+ 1 3)
        right-prong (let [x-offset distance-between-prong-centers
                          top-plate (->> (cube (get prong-dimensions 0) (get prong-dimensions 1) 0.01)
                                         (translate [x-offset 0 5]))
                          bottom-plate (->> (cube (get prong-dimensions 0) (get prong-dimensions 1) 0.01)
                                            (translate [x-offset 0 0])
                                           ;; (translate [0 0 -3])
                                            (translate [0 0 (- (get prong-dimensions 2))]))]
                      (color [0 1 0] (hull top-plate bottom-plate)))
        prongs (difference (translate [(- (/ distance-between-prong-centers 2)) 0 0] (union left-prong right-prong)) dish-maker-sphere)]
    (union prongs (difference cap cap-inner))))

(defn spit-scad [name obj]
  (spit (str "resources/" name ".scad") (write-scad obj)))

(def thumb-2u-hi (make-dsa :top {:x cap-1u-x-top :y cap-2u-y-top}
                           :bottom {:x cap-1u-x-bottom :y cap-2u-y-bottom}
                           :y-angle 15
                           :depth 2
                           :height (+ 2 cap-z)))

(def thumb-2u-lo (make-dsa :top {:x cap-1u-x-top :y cap-2u-y-top}
                           :bottom {:x cap-1u-x-bottom :y cap-2u-y-bottom}
                           :y-angle 10
                           :height cap-z
                           :depth 2))

(def thumb-1u-hi (make-dsa :top {:x cap-1u-x-top :y cap-1u-y-top}
                           :bottom {:x cap-1u-x-bottom :y cap-1u-y-bottom}
                           :y-angle 15
                           :depth 2
                           :height (+ 2 cap-z)))

(def thumb-1point5u (make-dsa :top {:x cap-1point5u-x-top :y cap-1u-y-top}
                              :bottom {:x cap-1point5u-x-bottom :y cap-1u-y-bottom}
                              :y-angle 0
                              :x-angle 5
                              :depth 1.5
                              :height cap-z))

(def outter-1point5u (make-dsa :top {:x cap-1point5u-x-top :y cap-1u-y-top}
                               :bottom {:x cap-1point5u-x-bottom :y cap-1u-y-bottom}
                               :y-angle -5
                               :x-angle 0
                               :depth 1.5
                               :height (+ 1 cap-z)))
(def oneU (make-dsa :top {:x cap-1u-x-top :y cap-1u-y-top}
                               :bottom {:x cap-1u-x-bottom :y cap-1u-y-bottom}
                               :y-angle 0
                               :x-angle 0
                               :depth 1.5
                               :height cap-z))

(def thumb-1point25u (make-dsa :top {:x cap-1point25u-x-top :y cap-1u-y-top}
                               :bottom {:x cap-1point25u-x-bottom :y cap-1u-y-bottom}
                               :y-angle 0
                               :x-angle 5
                               :depth 1.5
                               :height cap-z))


;; (spit-scad "out" (union (translate [-25 0 0] oneU) outter-1point5u))
(spit-scad "1point25u" thumb-1point25u)
;; (spit-scad "2u-thumb-1" thumb-2u-1)

;; (spit-scad "2u-thumb-2" thumb-2u-2)
;;;;;;;;;;;;;;;;;
;; Experiments ;;
;;;;;;;;;;;;;;;;;
;; cap-2u-x-top
;; cap-2u-x-bottom
;; cap-2u-y-top
;; cap-2u-y-bottom
