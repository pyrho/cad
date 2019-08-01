(ns macbook-kb-cover
  (:require [scad-clj.model :refer :all]
            [scad-clj.scad :refer :all]))

;;;;;; Constants
(def scad-fn 10)
(def tolerance 0.5)

(def u-y-d
  "Cap y-size. Shared by all caps"
  16.6)

(def gap-d
  "Gap between each each for both axes"
  { :x 2 :y 1.9 })

(def left-d
  "Size of the left half of the cover"
  { :x 148 :y 107.2 })

(def right-d
  "Size of the right of the cover"
  { :x (left-d :x) :y 107.2 })

(def cap-height 0.65)

;; Shapes
(def keyboard
  (let [merge-vectors (comp vec flatten conj) 

        ;; Returns x/y sizes for a given cap type (eg. 1u, 2u)
        get-cap-dim (fn [name axis]
                       ((case name
                          ;; Basic key
                          :u1 {:x 17 :y u-y-d}

                          ;; Cmd/Lshift
                          :u1point25 { :x 21.76 :y u-y-d }

                          ;; shitty enter bottom
                          :u0point25 {:x 12.3 :y (+ (gap-d :y) (* u-y-d 2))}

                          ;; Capslock
                          :u1point75 { :x 31.2 :y u-y-d }

                          ;; Backspace/tab
                          :u1point5 { :x 26.5 :y u-y-d }

                          :space { :x 93.08 :y u-y-d }

                          ;; Rshift
                          :u2 { :x 40.79 :y u-y-d }) axis))

        ;; Defines the rows of keys by their dimensions, first item is the bottom row
        kb-map [(merge-vectors [] (take 3 (repeat :u1)) :u1point25 :space :u1point25 (take 4 (repeat :u1)))
                (merge-vectors [] :u1point25 (take 11 (repeat :u1)) :u2)
                ;; Includes bottom of Enter (ISO) key
                (merge-vectors [] :u1point75 (take 12 (repeat :u1)) :u0point25)
                ;; Includes top of Enter (ISO) key
                (merge-vectors [] :u1point5 (take 13 (repeat :u1)))
                (merge-vectors [] (take 13 (repeat :u1)) :u1point5)]

        ;; Places all the key for a row given the data
        ;; and the y offset at which the row should be positioned.
        place-row (fn [row-data y-offset]
                    (loop [row_     row-data
                           x-offset 0  ;; Accumulator to offset the placement of the next key
                           row-caps [] ;; Accumulator to return all the keys in a row 
                           ]
                      (if (empty? row_)
                        row-caps ;; Exit loop if there is no more key to process
                        (let [cap-type   (first row_)
                              cap-x      (get-cap-dim cap-type :x)
                              cap-y      (get-cap-dim cap-type :y)
                              placed-key (translate [x-offset y-offset 0]
                                                    (cube cap-x cap-y cap-height :center nil))]
                          (recur (rest row_)
                                 (+ cap-x (gap-d :x) x-offset) ;; Increase the offset by the size of the current cap the gap
                                 (conj row-caps placed-key))))))]

    (loop [kb-map_  kb-map
           y-offset 0
           all-keys []]
      (if (empty? kb-map_)
        all-keys ;; Exit loop if there are no more rows to process
        (recur (rest kb-map_)
               (+ (gap-d :y) u-y-d y-offset)
               (conj all-keys (place-row (first kb-map_) y-offset)))))))

(def all
  keyboard)

(spit
 (str "resources/" "out.scad")
 (write-scad [;(include "BH-Lib/all.scad")
              all
              (fn! scad-fn)
              ]))


;;;; Playground
(comment (let [mapp [[:u1 :u1point25] [:u1 :u1]]]
   (for [row mapp]
     (for [cap row]
       (cube (get-cap-dim cap :x) (get-cap-dim cap :y) 0)))))
(comment (let [mapp [[:u1 :u1point25] [:u1 :u1]]]
   (map (fn [i e1] (map-indexed (fn [j e2] (cube 0 0 0)) e1)) mapp)))

(comment (let [kb-map [[1 2]
               [3 4]]]
   (loop [kb-map_ kb-map
          y-offset 0
          all-keys []]
     (let [row (first kb-map_)]
       (if (nil? row)
         all-keys
         (let [tmp (loop [row_ row
                          x-offset 0
                          row-caps []]
                     (let [cap (first row_)]
                       (if (nil? cap)
                         row-caps
                         (recur (rest row_)
                                (+ 1 x-offset)
                                (conj row-caps :cube)))))]
           (recur (rest kb-map_)
                  (+ 1 y-offset)
                  (conj all-keys tmp))))))))
