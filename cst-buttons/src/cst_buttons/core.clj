;;;; Issues {{{
; v0.1
; ====
; - [x] y arm is too high (too much towards the top of the trackball)
; - [x] side crosses are a bit too far apart, can't get them both to fit the holes
; - [x] bottom clearance is a tad too high
; - [x] the crosses could be a bit larger, the fit is quite loose
; - [ ] the side crosses are maybe a bit too deep 
;       This could be fixed by making the center hollow
; - [x] The top cross could be a bit deeper
;
; v0.2
; ====
; - [x] side cross still a bit too far apart, but really a bit.
; - [ ] refine fillet for upwards arm
; - [x] the side crosses are maybe a bit too deep 
;       This could be fixed by making the center hollow
; - [ ] upwards arm hits bezel on side of mouse
; v0.3
; ====
;;;; }}}

(ns cst-buttons.core
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]
            [cst-buttons.cross :refer :all]
            [cst-buttons.skeleton :refer :all]))


(def exoskeletonButtonHolder
  (union
    skeleton
    crosses))

(defn main []
  (->> exoskeletonButtonHolder
      (write-scad) 
      (str "include <utils.scad>; //translate([8.5, 11, 0]) ruler(100);\n")
      (spit "things/cst_proto.scad")))
(main)
