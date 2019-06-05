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
