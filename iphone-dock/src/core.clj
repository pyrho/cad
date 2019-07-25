(ns iphone-dock
  (:require [scad-clj.model :refer :all]
            [scad-clj.scad :refer :all]))

;;;;;; Constants

;; It's actually a bit tighter than that (11), but it
;; won't matter if there's a bit more space.
(def cover-side-radius (/ 12 2))

(def dock-corner-radius (/ 20 2))

(def dock-size-offset
  "The fit is a bit too tight with the exact mesures, this allows for some space.
  0.5 is perfect."
  0.5)

(def fn 50)
(def fs 1)

(def dock-z (+ dock-size-offset 7.5))
(def dock-x (+ dock-size-offset 67.16))
(def dock-y (+ dock-size-offset 48.4))

(def iphone-nth-of-bottom 15)
; Home button data
(def home-button-distance-from-bottom 4.5)
(def home-button-radius (/ 12 2))

(def bottom-lip 1.5)

(def layer-height 0.2)
(def thickness (+ tolerance (* layer-height 7)))

(def widen-connector-dim-by 0.5)
(def connector-dim {:x (+ 12 widen-connector-dim-by)
                    :y (+ 7 widen-connector-dim-by)})
(def iphone-dim
  "Without the cover"
  {:x 67.25
   :y 138.17 ;this is not important, the guide does nott go all the way up
   :z 7.2})

(def iphone-corner-radius
  "The corner of the iphone with the cover."
  (/ 20 2))

(def iphone-shell-corner-radius
  "Make the corner of the inside of the shell where the iphone will be standing
  a bit broader so that it's easier to fit the phone in."
  (- iphone-corner-radius 4))

(def tolerance 0.5)

(def iphone-dim-slim-cover
  "With the cover"
  {:x (+ tolerance 70.25)
   :y (+ tolerance 138.40)
   :z (+ tolerance 9.2)})

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

(defn iphonev2 [extra]
  (let [corner-radius iphone-corner-radius
        ;; I have no idea why substracting half of the height works here.....
        r1            (+ extra (- corner-radius (/ (iphone-dim-slim-cover :z) 2)))

        ;; Nor do I know why r2 should be half of the height, but it works...
        r2            (+ extra (/ (iphone-dim-slim-cover :z) 2))
        corner        (call :torus r1 r2)
        bottom-left-y (- (+ (/ (iphone-dim-slim-cover :y) 2) 0))
        bottom-left-x (- (+ (/ (iphone-dim-slim-cover :x) 2) 0))
        torus-offset  (+ r1 r2)]
    (->> corner
         (translate [bottom-left-x bottom-left-y 0])
         (translate [torus-offset torus-offset 0])
         (call-module-with-block :reflect)
         (hull))))

;;;;; Shapes
(defn iphone-ref2 [ & [offset]]
  (cube (iphone-dim-slim-cover :x)
        (iphone-dim-slim-cover :y)
        (iphone-dim-slim-cover :z)))

(defn iphone-ref [ & [offset]]
  "THis is from the iphone case from github "
  (let [offset (or offset 0)
        dim0 (iphone-dim-slim-cover :y)
        dim1 (iphone-dim-slim-cover :x)
        dim2 (iphone-dim-slim-cover :z)
        corner-rad iphone-corner-radius
        r1 (+ (- corner-rad (/ dim2 2)) offset)
        r2 (+ (/ dim2 2) offset)]
    (->> (call :torus_true r1 r2)
         (translate [(- (/ dim0 2) corner-rad) (- (/ dim1 2) corner-rad) 0])
         (call-module-with-block :reflect)
         (hull))))

(defmacro three-x-vec [x] [x x x])

(def hollow-iphone-shape
  (let [thicker-scale-factor      1.1
        raise-up                  #(rotate [(/ pi 2) 0 0] %)
        iphone-shape              (translate [0 0 (/ (iphone-dim-slim-cover :y) 2)] (raise-up (iphonev2 tolerance)))
        block-z                   (+ home-button-distance-from-bottom home-button-radius)
        connector-hollower        (-#(cube (+ (connector-dim :x) 0) (+ 0 (connector-dim :y)) 30))
        block                     (translate [0 0 (/ block-z 2)]
                                             (cube (+ thickness (iphone-dim-slim-cover :x))
                                                   (+ thickness (iphone-dim-slim-cover :z))
                                                   block-z
                                        ;(+ thickness (iphone-dim-slim-cover :y))
                                                   ))
        home-button-mask          (let [h      (+ thickness (iphone-dim-slim-cover :z))
                                        z-zero (+ home-button-distance-from-bottom home-button-radius)
                                        x-zero (- (/ (iphone-dim-slim-cover :y) 2) (+ cover-side-radius) (/ home-button-radius 2))]
                                    (translate [0 (- h 1) z-zero]
                                               (rotate [(/ pi 2) 0 0]
                                                       (-# (cylinder home-button-radius
                                                                     h)))))
        button-hollower           (-# (cylinder home-button-radius 10))]

    (difference
     (difference block iphone-shape)
     home-button-mask
     connector-hollower
     ;iphone-mask
     )
    ))

(def iphone-shape-mask
  (translate [0 0 (/ (* (+ 1 thicker-scale-factor) (iphone-dim-slim-cover :y)) 2)]
             (-# iphone-thicker-shape2)))

(def only-bottom-of-iphone-shape
  (difference
   (intersection
    hollow-iphone-shape
    (let [b (/ (iphone-dim-slim-cover :y) iphone-nth-of-bottom)
          a (- (/ (iphone-dim-slim-cover :y) 2) (/ b 2))]
      (-#(translate [0 (- a) 0]
                    (cube (+ (iphone-dim-slim-cover :x) 1)
                          b
                          (+ (iphone-dim-slim-cover :z) 10))))))
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

;(def iphone-ref (cube (iphone-dim-slim-cover :x) (iphone-dim-slim-cover :y) (iphone-dim-slim-cover :z)))
(def ruler (translate [0 0 0] (cube (iphone-dim-slim-cover :x) dock-y dock-z)))
;;;;; Output

(def all
  (union
   ;(translate [0 0 -30] (rotate [0 0 (/ pi 2)] (iphone-ref 5)))
   ;; (translate [0 0 -10] (-%(iphone-ref2 0)))
   ;(translate [00  0 -10] only-bottom-of-iphone-shape)
   ;; (iphonev2 tolerance)
   ;; (translate [0 0 10] (scale [1.05 1.05 1.05] (iphonev2 tolerance)))
   ;; ;; (translate [0 0 10] (iphonev2 (+ thickness tolerance)))
   hollow-iphone-shape
   ;; ;; ruler
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
