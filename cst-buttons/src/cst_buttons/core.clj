;;;; Issues {{{
; V1
; ==
; - [ ] y arm is too high (too much towards the top of the trackball)
; - [ ] bottom clearance is a tad too high
; - [ ] the crosses could be a bit longer, the fit is quite loose
; - [ ] the side crosses are maybe a bit too deep 
;       This could be fixed by making the center hollow
; - [ ] The top cross could be a bit deeper
;;;; }}}

(ns cst-buttons.core
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.scad :refer :all]
            [scad-clj.model :refer :all]))

;;; Constants
(def ltrac-width 88)
(def ltrac-length 177)
(def ltrac-x-mid (/ ltrac-length 2))
(def ltrac-y-mid (/ ltrac-width 2))

(def arrow-width 6.5)
(def arrow-hole-depth 2.5) ; Depth on the trackball
(def arrow-height arrow-hole-depth) ; this can't be longer than the hole depth
(def arrow-arm-width 1.7)
(def arrow-top-offset 29)
(def arrow-right-offset 9.6)
(def arrow-mid (/ arrow-width 2))

(def bottom-clearance 2)
(def skeleton-width 10)

(def top-cross)
  
(def side-cross
  (translate [0 0 bottom-clearance]
    (union
      (cube arrow-width arrow-arm-width arrow-height)
      (->> (cube arrow-width arrow-arm-width arrow-height)
           (rotate (/ Math/PI 2) [0 0 1])))))

(defn all-crosses []
  (translate [(- ltrac-x-mid arrow-top-offset) 0 0] side-cross)
  (translate [0 (- ltrac-y-mid arrow-right-offset) 0] side-cross)
  (translate [0 (+ arrow-right-offset (- ltrac-y-mid) ) 0] side-cross))


(defn below-support []
  (def skel (union 
              (cube skeleton-width ltrac-width bottom-clearance)
              (cube ltrac-length skeleton-width bottom-clearance)
              (all-crosses)
              (translate [(- ltrac-x-mid arrow-top-offset) 0 0] side-cross)
              (translate [0 (- ltrac-y-mid arrow-right-offset) 0] side-cross)
              (translate [0 (+ arrow-right-offset (- ltrac-y-mid) ) 0] side-cross)))
  skel)



(->> (below-support)
     (with-fn 100)
     (write-scad) 
     (str "include <utils.scad>; //translate([8.5, 11, 0]) ruler(100);\n")
     (spit "things/cst_proto.scad"))
