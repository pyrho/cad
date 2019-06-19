(ns cst-buttons.core
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]))

(def ltrac-width 88)
(def ltrac-length 177)
(def ltrac-x-mid (/ ltrac-length 2))
(def ltrac-y-mid (/ ltrac-width 2))
(def bottom-clearance 1.5)
(def ltrac-z-mid (/ bottom-clearance 2))
(def skeleton-width 10)
(def skeleton-y-top-offset 92.55)
(def cross-data {:width 6.2
                 :top {:height 5 :offset 29}
                 :side {:height 2 :offset 9.6}
                 :arm {:width 1.5}})
(def skeleton-upwards-length 32.54)
(def short-skeleton-arm-x (- (/ ltrac-length 2) skeleton-y-top-offset))
(def skeleton-data {:back {:height (+ bottom-clearance 3)
                           :width 2
                           :angle 60
                           :angled-size '(2 10 10)}
                    :front {:height (+ bottom-clearance 20)
                            :width 2
                            :size '(2 10 10)}})
(def inner-distance-between-side-cross 60.80)
(def switch-around-offset 8)
(def switch-data {:x 14
                  :y 14
                  :z 5.1
                  :with-cap {:x 17.72 :y 16.63}})
(def switch-x-padding (- (get-in switch-data [:with-cap :x]) (switch-data :x)))
(def switch-y-padding (- (get-in switch-data [:with-cap :y]) (switch-data :y)))

(def button-tower-data {:offset 30
                        :slope {:xyz [(+ switch-around-offset (* 2 (get-in switch-data [:with-cap :x]))) (+ switch-around-offset (* 2 (switch-data :y))) 30]}})
;;;;;;;;;;;;;;;;;;;;;
;; Skeleton
;;;;;;;;;;;;;;;;;;;;;;
(def long-arm
  " The long arm of the skeleton on the X axis"
  (cube ltrac-length skeleton-width bottom-clearance))

(def short-arm
  " The short arm of the skeleton, on the Y axis"
  (translate [short-skeleton-arm-x 0 0]
             (cube skeleton-width ltrac-width bottom-clearance)))

(def side-upwards
  " The side bracket to hold the skeleton in place.
    This idea has been dismissed for now."
  (->> (cube skeleton-width 2 skeleton-upwards-length)
       (translate [short-skeleton-arm-x (/ ltrac-width 2) (/ skeleton-upwards-length 2)])))

(def back-clip
  (let [skd                       skeleton-data
        clip-height               (get-in skd [:back :height])
        clip-width                (get-in skd [:back :width])
        mid-x                     (/ ltrac-length 2)
        x-translate               (- (- (/ clip-width 2)) mid-x)
        ;; Place the upwards arm at the base of the bottom skeleton
        upwards-arm               (translate [x-translate 0 (- (/ clip-height 2) ltrac-z-mid)]
                                             (cube clip-width skeleton-width clip-height))
        angle                     (get-in skd [:back :angle])
        angled-arm                (->> (apply cube (get-in skd [:back :angled-size]))
                                       ;; Translate the origin of the rotation at the base of the cube (by default it rotates around its center)
                                       (translate [1 0 5])
                                       (rotate [0 (deg->rad angle) 0])
                                       ;; Compensate for the extra X axis that's spilling on the back clip after rotation
                                       (translate [0 0 (/ (first (get-in skd [:back :angled-size])) 2)])
                                       ;; Put in at the proper place
                                       (translate [x-translate 0 clip-height]))
        upwards-and-angled-fillet (->> (cylinder 1.3 10)
                                       (with-fn 50)
                                       (rotate [0 (/ pi 2) (/ pi 2)])
                                       (translate [x-translate 0 clip-height]))]

    (union angled-arm upwards-arm upwards-and-angled-fillet)))

(def front-clip
  (let [clip-height (get-in skeleton-data [:front :height])
        clip-width (get-in skeleton-data [:front :width])
        mid-x (/ ltrac-length 2)
        upwards (translate [(+ mid-x (/ clip-width 2)) 0 (- (/ clip-height 2) ltrac-z-mid)]
                           (cube 2 skeleton-width (get-in skeleton-data [:front :height])))]
    upwards))

(def side-tower
  "This is the tower which holds the buttons on the left side."
  (let [[x y z] (get-in button-tower-data [:slope :xyz])
        angled  (let [angled-face            (->> (apply cube (get-in button-tower-data [:slope :xyz])) ; First create the angled face
                                                  (rotate [(- (/ pi 3)) 0 0]))
                      switch-hole-high       (->> (cube (switch-data :x) (switch-data :y) 70)
                                                  (rotate [(- (/ pi 3)) 0 0])
                                                  (translate [(- (- switch-x-padding (- (/ x 2) (/ (switch-data :x) 2))))
                                                              (- (- (- (/ y 2) (/ (switch-data :y) 2)) switch-y-padding))
                                                              0]))
                      switch-hole-low        (->> (cube (switch-data :x) (switch-data :y) 70)
                                                  (rotate [(- (/ pi 3)) 0 0])
                                                  (translate [(- switch-x-padding (- (/ x 2) (/ (switch-data :x) 2)))
                                                              (- (- (/ y 2) (/ (switch-data :y) 2)) switch-y-padding)
                                                              0]))
                      cable                  (->> (-# (cylinder 4 50))
                                                  (mirror [1 0 1])
                                                  (translate [14 4 5]))
                      bottom-slicer          (-# (->> (cube (+ 1 x) (+ 10 y) z)
                                                      (translate [0 0 (- z)])
                                                      (translate [0 0 7.5])))
                      right-slicer           (-# (->> (cube (+ 1 x) (+ 10 y) (+ 11 z))
                                                      (translate [0 (- (- (/ y 2)) 1) 10])))
                      angled-face-with-holes (difference angled-face switch-hole-low switch-hole-high bottom-slicer right-slicer cable)
                      placed                 (translate [(button-tower-data :offset) 40 (+ 5.3 bottom-clearance)]
                                                        angled-face-with-holes)]
                  placed)
        holder  (->> (cube skeleton-width (/ ltrac-width 2) bottom-clearance)
                     (translate [(+ (button-tower-data :offset) short-skeleton-arm-x 15) (/ ltrac-width 4) 0]))]
    (union angled holder)))

(def skeleton
  (union
   short-arm
   back-clip
   front-clip
   long-arm))
;;;;;;;;;;;;;;
; Design log

;;;;;;; Crosses
(defn create-cross [isTopCross]
  (let [c          cross-data
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

(def side-crosses
  "Place the side-crosses in the middle, and move them appart"
  (let [left-cross (place-cross short-skeleton-arm-x (- (/ inner-distance-between-side-cross 2)) 0 false)
        right-cross (place-cross short-skeleton-arm-x (/ inner-distance-between-side-cross 2) 0 false)]
    (union left-cross right-cross)))

(def crosses
  (union top-cross side-crosses))

;;;;;;;;;;;;;;;;;;;
; Desing logs

; #Holes for side crosses
; A bad idea, because it makes the crosses too flimsy.

(def exoskeletonButtonHolder
  (union
   skeleton
   crosses
   side-tower))

(defn main []
  (->> exoskeletonButtonHolder
       (write-scad)
       (str "include <utils.scad>; //translate([8.5, 11, 0]) ruler(100);\n")
       (spit "things/cst_proto2.scad")))

(main)

;;;; Experiments
;;; Rotate & translate
#_((->> (cube 2 10 10)
        (translate [0  0 (/ 10 2)])
        (rotate [0 (deg->rad 70) 0])
        (translate [0  0 (- (/ 10 2))])
      ;; (rotate-at-point 0 (deg->rad 70) 0 [10 0 10])
        (write-scad)
        (spit "things/cst_proto.scad")))
;; Button tower

;; (->> (doPoly 10 5 3)
;;      (write-scad)
;;      (spit "things/cst_proto.scad"))
;;; polyhedron(
;;points=[[0,0,0], [l,0,0], [l,w,0], [0,w,0], [0,w,h], [l,w,h]],
;;faces=[[0,1,2,3],[5,4,3,2],[0,4,5,1],[0,3,4],[5,2,1]]
;;);


; Tut from https://hackaday.com/2018/02/13/openscad-tieing-it-together-with-hull/
;; (def points [[0 0 0] [30 0 0] [0 30 0] [30 30 0]])

;; (defn cylinders [points diameter thickness]
;;   (map #(translate % (-#(cylinder (/ diameter 2) thickness))) points))

;; (defn plate [points diameter thickness hole-diameter]
;;   (difference
;;    (hull (cylinders points diameter thickness))
;;    (cylinders points hole-diameter (+ 1 thickness))))

;; (defn bar [length width thickness hole-diameter]
;;   (plate [ [0 0 0] [length 0 0]] width thickness hole-diameter))

; Attempt at a curve
(def curvebase
  (translate [2 -2 0] (rotate [0 pi 0] (import "/home/pyrho/Downloads/CST_2-Switch_Housing.stl"))))
(def reference-points [

             [0 0 0] ;origin

                       [-145 0 0] ;back

             [-98 20 0] ;mid-back
             [-48 29 0] ;mid-front
             [0 19 0] ;front

             ])

(def x (loop [res []
              lpoints reference-points]
         (if (vector? lpoints)
           (recur (conj (first lpoints) [1 1 1]) (rest lpoints) )
           res)))
x

(rest reference-points)
(conj (first [[0 0 0]]) [1 1 1])

(defn place-points [points]
  (color [1 0 0 1] (map #(translate % (cylinder 1 10)) points)))


(def all
  ;; (union curvebase (place-points reference-points)))
  (union curvebase (place-points reference-points)))

(->> all
     (write-scad)
     (spit "things/cst_proto.scad"))


(vector? (rest [1]))
