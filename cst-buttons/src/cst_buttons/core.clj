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
(def cross-data {:width 6.4
                 :top {:height 5 :offset 29}
                 :side {:height 2.8 :offset 15}
                 :arm {:width 1.5}})
(def skeleton-upwards-length 32.54)
(def short-skeleton-arm-x (- (/ ltrac-length 2) skeleton-y-top-offset))
(def skeleton-data {:back {:height (+ bottom-clearance 3)
                           :angle 60
                           :angled-size '( 2 10 10)}
                    :front {:height (+ bottom-clearance 20)
                            :size '(2 10 10)}})

(def button-tower-data {:offset 16.5
                        :size [10 5 3 ]
                        :slope {:xyz [50 25 32]}
                        :tower {:xyz [10 5 32]}})
(def switch-data {:x 13.85
                  :y 13.85
                  :z 5.1})

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

#_((def back-clip
   " First iteration of the back bracket that would clip onto the sloped back
  of the trackball body"
   (let [skd skeleton-data
         clip-height (get-in skd [:back :height])
         clip-z-offset (+ (/ clip-height 2) 0.65)
         clip-x-offset (- (- ltrac-width) 1)
         upwards (->> (cube skeleton-width 2 (+ clip-z-offset (+ bottom-clearance clip-height)))
                      (rotate [0 0 (/ pi 2)])
                      (translate [clip-x-offset 0 clip-z-offset]))
         angle (get-in skd [:back :angle])
         clip (->> (cube 2 10 10)
                   (rotate [0 (deg->rad angle) 0])
                   (translate [ -85 0 7]))]
     (union upwards clip))))
(defn doPoly [l w h]
  (translate [(- (/ l 2)) (- (/ w 2)) 0](polyhedron [[0 0 0]
                                                     [l 0 0]
                                                     [l w 0]
                                                     [0 w 0] ; Bottom face
                                                     [0 0 h] ; Top points
                                                     [l 0 h]]
                                                    [[0 1 2 3]
                                                     [5 4 3 2]
                                                     [0 4 5 1]
                                                     [0 3 4]
                                                     [5 2 1]])))

(defn rotate-at-point [x y z point object]
  (->> object
       (translate point)
       (rotate [x y z])
       (translate (mapv #(- %) point))))

(def back-clip
  (let [skd skeleton-data
        clip-height (get-in skd [:back :height])
        ;; Place the upwards arm at the base of the bottom skeleton
        upwards-arm (translate [(- ltrac-width) 0 (- (/ clip-height 2) ltrac-z-mid)] (cube 2 skeleton-width clip-height))
        angle (get-in skd [:back :angle])
        angled-arm (->> (apply cube (get-in skd [:back :angled-size]))
                        ;; Translate the origin of the rotation at the base of the cube (by default it rotates around its center)
                        (translate [1 0 5])
                        (rotate [0 (deg->rad angle) 0])
                        ;; Compensate for the extra X axis that's spilling on the back clip after rotation
                        (translate [0 0 (/ (first (get-in skd [:back :angled-size])) 2)])
                        ;; Put in at the proper place
                        (translate [(- ltrac-width) 0 clip-height]))
        upwards-and-angled-fillet (->> (cylinder 1.3 10)
                                       (with-fn 50)
                                       (rotate [ 0 (/ pi 2) (/ pi 2) ])
                                       (translate [(- ltrac-width) 0 clip-height]))]

    (union angled-arm upwards-arm upwards-and-angled-fillet)))

(def front-clip
  (let [clip-height (get-in skeleton-data [:front :height])
        upwards (translate [ltrac-width 0 (- (/ clip-height 2) ltrac-z-mid)](cube 2 skeleton-width (get-in skeleton-data [:front :height])))]
    upwards))

(def side-tower
  "This is the tower which holds the buttons on the left side."
  (let [slope (apply doPoly (get-in button-tower-data [:slope :xyz]))
        translated-slope (translate [short-skeleton-arm-x
                                     (+ (/ ltrac-width 2) (/ ((get-in button-tower-data [:slope :xyz]) 1) 2))
                                     (- (/ bottom-clearance 2))]
                                    slope)
        bottom-support (->> (cube 50 10 bottom-clearance )
                            (rotate [ 0 0 (/ pi 4)])
                            (translate [-23 18 0]))
        top-support (->> bottom-support
                         (mirror [ 1 0 0])
                         (translate [-8 0 0]))
        hollower (union (->> (-#(cube (switch-data :x) 40 (switch-data :y)))
                             (translate [5 60 -24] )
                             (rotate [(+ (deg->rad 38.5)) 0 0]))
                        (->> (-#(cube (switch-data :x) 40 (switch-data :y)))
                             (translate [-15 60 -24] )
                             (rotate [(+ (deg->rad 38.5)) 0 0])))]
    (union (difference translated-slope hollower) bottom-support top-support)))

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

(def left-cross
  (let [side-offset (get-in cross-data [:side :offset])
        x short-skeleton-arm-x
        y (+ side-offset (- ltrac-y-mid))
        z 0
        isTopCross false]
    (place-cross x y z isTopCross)))

(def right-cross
  (let [side-offset (get-in cross-data [:side :offset])
        x short-skeleton-arm-x
        y (- ltrac-y-mid side-offset)
        z 0
        isTopCross false]
    (place-cross x y z isTopCross)))

(def crosses
  (union top-cross left-cross right-cross))

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
      (rotate [ 0 (deg->rad 70) 0])
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

