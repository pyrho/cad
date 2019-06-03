(ns cst-buttons.utils
  (:refer-clojure :exclude [use import])
  (:require [scad-clj.model :refer :all]))

(defn drawscale [length step width]
  (for [n (range 0 (- length 1) step)]
    (if (= (mod n (* 2 step)) 0)
      (->> (cube step width 1
            (translate [n 0 0])
            (color [1 1 1])))))) 

; Draws a ruler of specified length.
; Origin of the ruler is at 0, and it is laid out along the X axis.
; Call with: ruler(length);
(defn ruler [ length]
  (difference
    (union
      (for [n (range 1 4)]
        (if (> length (Math/pow 10 (- n 1)))
          (translate [0 (* -1 (Math/pow 2 (+ 1 n))) 0]
                    (drawscale length (Math/pow 10 (- n 1)) (Math/pow 2 n))))))
    (->> (cube 1000 38 3)
         (translate [length -38 -1])
         (color [0 0 0]))))
; (ruler 100)
