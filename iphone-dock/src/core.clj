(ns iphone-dock
  (:require [scad-clj.model :refer :all]
            [scad-clj.scad :refer :all]))

;;;;;; Constants
(def iphone-corner-radius (/ 19 2))

;; It's actually a bit tighter than that (11), but it
;; won't matter if there's a bit more space.
(def cover-side-radius (/ 14 2))

(def dock-corner-radius (/ 20 2))

(def dock-size-offset
  "The fit is a bit too tight with the exact mesures, this allows for some space.
  0.5 is perfect."
  0.5)

(def fn 50)
(def fs 0.1)

(def dock-z (+ dock-size-offset 7.5))
(def dock-x (+ dock-size-offset 67.16))
(def dock-y (+ dock-size-offset 48.4))

(def iphone-nth-of-bottom 20)
; Home button data
(def home-button-distance-from-bottom 4.5)
(def home-button-radius (/ 12 2))

(def bottom-lip 1.5)

(def layer-height 0.2)
(def thickness (* layer-height 7))

(def widen-connector-dim-by 5)
(def connector-dim {:x (+ 12 widen-connector-dim-by)
                    :y (+ 7 widen-connector-dim-by)})
(def iphone-dim
  "Without the cover"
  {:x 67.25
   :y 138.17 ;this is not important, the guide does nott go all the way up
   :z 7.2})
(def iphone-dim-slim-cover
  "Without the cover"
  {:x 70.25
   :y 138.17
   :z 9.2})

;;;;; Shape generation

(defn create-dock-shape
  "This is the actual shape of the dock. It will be used to hollow out the
   outer shell."
  [x y z rs]

  (let [a-corner (extrude-linear {:center false :height z} (with-fn fn (circle rs)))
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
  "Note: we don't need to z size because the radius of the side de-facto sets
  the height of the iphone."
  [extra-thickness]
  (let [r2                    (+ extra-thickness cover-side-radius)
        r1                    iphone-corner-radius
        ix                    (+ extra-thickness (iphone-dim-slim-cover :x))
        iy                    (+ extra-thickness (iphone-dim-slim-cover :y))
        a-corner              (call :torus r1 r2)
        get-torus-coord-for   #(- (/ % 2) (+ r1 r2))
        torus-top-coord       (get-torus-coord-for ix)
        torus-right-coord     (get-torus-coord-for iy)
        torus-top-right-coord [torus-top-coord torus-right-coord 0]]
    (->> a-corner
         ;; Place it on the top right corner (relative to top view)
         (translate torus-top-right-coord)
         ;; Copy it along the x/y axis to create the 4 cornes of the iPhone
         (call-module-with-block :reflect)
         ;; Join all 4 corners to create the shape
         (hull))))
;;;;; Shapes
(def iphone-shape (create-iphone-shape 0))
(def iphone-thicker-shape (create-iphone-shape thickness))
(def hollow-iphone-shape (difference iphone-thicker-shape iphone-shape))
(def only-bottom-of-iphone-shape
  (difference
   (intersection
    hollow-iphone-shape
    (let [b (/ (iphone-dim-slim-cover :y) iphone-nth-of-bottom)
          a (- (/ (iphone-dim-slim-cover :y) 2) (/ b 2))]
      (-#(translate [0 (- a) 0]
                    (cube (+ (iphone-dim-slim-cover :x) 10)
                          b
                          (+ (iphone-dim-slim-cover :z) 100))))))
   (let [a (- (/ (iphone-dim-slim-cover :y) 2) (+ cover-side-radius) (/ home-button-radius 2))]
     (-# (translate
          [0 (- a) 10]
          (cylinder home-button-radius 20))))))

(def dock-shell
  (let [outer-dim  {:x (+ thickness dock-x) :y (+ thickness dock-y) :z (+ thickness dock-z)}
        dock-inner (create-dock-shape dock-x dock-y dock-z dock-corner-radius)
        dock-outer (create-dock-shape (outer-dim :x) (outer-dim :y) (outer-dim :z) dock-corner-radius)

        ;; The distance between the edge and the start of the connector
        connector-y-edge-distance 4

        a (+ (- (/ dock-y 2) (/ (connector-dim :y) 2) (+ thickness connector-y-edge-distance)))
        connector-hollower (translate [0  a 0] ;3.8 from front edge inner
                                                    (-#(cube (+ (connector-dim :x) 0) (+ 0 (connector-dim :y)) 30)))
        back-hollower      (translate [0 (/ dock-y 2) 0]
                                      (cube (- (+ 3 (outer-dim :x)) 0) 5 (+ 3 (outer-dim :z))))
        shell              (difference
                            (union (let [magicnr 15]
                                     (->> only-bottom-of-iphone-shape
                                          (translate [0 (- (/ dock-y 2) (/ (iphone-dim-slim-cover :x) 2) 6) magicnr])
                                          (rotate [(- (/ pi 25)) 0 0])))
                                   dock-outer)
                            (-# dock-inner))]
    (difference shell
                ;; Hollow out the combined part instead, this will allows to
                ;; incorporatethe angle.
                connector-hollower
                back-hollower)))

(def iphone-ref (cube (iphone-dim-slim-cover :x) (iphone-dim-slim-cover :y) (iphone-dim-slim-cover :z)))
(def ruler (translate [0 0 0] (cube (iphone-dim-slim-cover :x) dock-y dock-z)))
;;;;; Output
(def all
  (union
   dock-shell
   ;; ruler
   ))


                                        ; OPen the back too dumb ass.

; The dimensions look waaay too big in cura..


(spit
 (str "resources/" "out.scad")
 (write-scad [(include "BH-Lib/all.scad")
              all
              (fn! fn)
              ]))


;;;; Playground
