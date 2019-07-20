;;;;;;;;; Notes 
;;; #+BEGIN_SRC emacs-lisp
;;; 0.0.1
;; - [ ] connector hole 
;;; #+END_SRC
(ns iphone-dock
  (:require [scad-clj.model :refer :all]
            [scad-clj.scad :refer :all]))

;;;;;; Constants
(def iphone-corner-radius (/ 19 2))
(def cover-side-radius (/ 15 2))

(def dock-corner-radius (/ 20 2))

(def dock-size-offset
  "The fit is a bit too tight with the exact mesures, this allows for some space"
  0.5)

(def fn 100)

(def dock-z (+ dock-size-offset 7.5))
(def dock-x (+ dock-size-offset 67.16))
(def dock-y (+ dock-size-offset 48.4))

(def bottom-lip 1.5)
(def thickness
  "This defines the thickness of the shell that will surround the dock.
   It is used as an increment to the dock's size."
  2.5)

(def widen-connector-dim-by 5)
(def connector-dim {:x (+ 12 widen-connector-dim-by) :y (+ 7 widen-connector-dim-by)})
(def iphone-dim
  "Without the cover"
  {:x 67.25
   :y 138.17 ;this is not important, the guide does nott go all the way up
   :z 7.2})
(def iphone-dim-x-offset 8)
(def iphone-dim-offset 0)
(def iphone-dim-slim-cover
  "Without the cover"
  {:x (+ 70.25 iphone-dim-x-offset)
   :z (+ 9.2 iphone-dim-offset)})

;;;;; Shape generation

(defn create-dock-shape
  "This is the actual shape of the dock. It will be used to hollow out the
   outer shell."
  [x y z rs isRound?]

  (let [a-corner (if isRound?
                   (with-fn fn (sphere rs))
                   (extrude-linear {:center false :height z} (with-fn fn (circle rs))))
        front-y-trslt (- (/ y 2) rs)
        front-x-trslt (- rs (/ x 2))
        front-L-corner (translate [front-x-trslt (- front-y-trslt) 0]
                                  a-corner)
        front-R-corner (translate [(- front-x-trslt) (- front-y-trslt) 0]
                                  a-corner)

        back-y-trslt (- (/ y 2) rs)
        back-x-trslt (- rs (/ x 2))
        back-L-corner (translate [back-x-trslt back-y-trslt 0] a-corner)
        back-R-corner (translate [(- back-x-trslt) back-y-trslt 0] a-corner)]

    (hull front-L-corner
          front-R-corner
          back-R-corner
          back-L-corner)))

(defn create-iphone-shape
  ""
  [x y z rs]
  (create-dock-shape
   x
   y
   z
   rs
   true))

;;;;; Shapes
(def dock-shell
  (let [outer-dim {:x (+ thickness dock-x) :y (+ thickness dock-y) :z (+ thickness dock-z)}
        dock-inner (create-dock-shape dock-x dock-y dock-z dock-corner-radius false)
        dock-outer (create-dock-shape (outer-dim :x) (outer-dim :y) (outer-dim :z) dock-corner-radius false)

        ;; The y coordinates of the front edge
        y-edge (- (connector-dim :y) (/ dock-y 2) 3)

        ;; The distance between the edge and the start of the connector
        connector-y-edge-distance 3.8

        hllwr-y (+ y-edge connector-y-edge-distance)
        connector-hollower (translate [0 hllwr-y 0] ;3.8 from front edge inner
                                      (cube (connector-dim :x) (connector-dim :y) 30))
        back-hollower (translate [0 (/ dock-y 2) 0]
                                 (cube (- (+ 3 (outer-dim :x)) 0) 5 (+ 3 (outer-dim :z))))
        guide (let [x (- (iphone-dim-slim-cover :x) (/ iphone-corner-radius 2))
                    y (- 30 (/ iphone-corner-radius 2))
                    z (+ 10 (- (iphone-dim-slim-cover :z) (/ iphone-corner-radius 2)))

                    iphone (create-iphone-shape x y z iphone-corner-radius)

                    hllwr (translate [0 0 0] ;[(- (/ thickness 2)) (- (/ thickness 2)) (- (/ thickness 2))]
                                     (create-iphone-shape
                                      (- x thickness)
                                      (- y thickness)
                                      (- z thickness)
                                      (- iphone-corner-radius (/ thickness 2))))

                    shell (difference iphone hllwr)

        ;; Hollow out the combined part instead, this will allows to
        ;; incorporatethe angle.
         connector-hollower (translate [0 0 0]
                                       (-#(cube (connector-dim :x) 30 (connector-dim :y))))
                    cut (translate [0 20 0] (cube 120 30 80))
        ;whole (difference shell cut connector-hollower)
                    whole (difference shell cut)]
                (rotate [(/ pi 2) 0 0] whole))
        shell (difference
               (union (let [magicnr 20]
                        (->> guide
                             (translate [0 (- (/ dock-y 2) (/ (iphone-dim-slim-cover :x) 2)) magicnr])
                             (rotate [(- (/ pi 30)) 0 0])))
                      dock-outer)
               (-# dock-inner))]
    (difference shell
                ;; Hollow out the combined part instead, this will allows to
                ;; incorporatethe angle.
                connector-hollower
                back-hollower)))

;(difference
     ;; (translate [0 -20 33]
     ;;            (rotate [(/ pi 2) 0 0]
     ;;                    (difference iphone hllwr cut)))
     ;; connector-hollower)


(def ruler (translate [0 0 0] (cube (iphone-dim-slim-cover :x) dock-y dock-z)))
;;;;; Output
(def all
  (union
   ;; These are magic numbers..
   ;; (translate [0 4 20] (rotate [(- (/ pi 30)) 0 0] iphone7-guide))
   dock-shell
   ;; ruler
   ))


                                        ; OPen the back too dumb ass.

; The dimensions look waaay too big in cura..


(spit (str "resources/" "out.scad") (write-scad all))


;;;; Playground
  ;; (minkowski (create-dock-shape 70.2 30 9.2 cover-side-radius)
  ;;            (sphere 7)) ;with slim cover

;; (union
;;  (create-dock-shape dock-x dock-y dock-z dock-corner-radius)
;;  ruler)

;; (difference (rotate [(/ pi 2) 0 0]
;;                     (create-dock-shape
;;                      (iphone-dim-slim-cover :x)
;;                      (iphone-dim-slim-cover :z)
;;                      2
;;                      iphone-corner-radius
;;                      true))
;;             (translate [0 0 10]
;;                        (cube
;;                         (iphone-dim-slim-cover :x)
;;                         40
;;                         (iphone-dim-slim-cover :z))))
