(ns cst-buttons.constants)

(def ltrac-width 88)
(def ltrac-length 177)
(def ltrac-x-mid (/ ltrac-length 2))
(def ltrac-y-mid (/ ltrac-width 2))
(def bottom-clearance 1.5)
(def skeleton-width 10)
(def skeleton-y-top-offset 92.55)
(def cross-data {:width 6.5
                 :top {:height 5 :offset 29}
                 :side {:height 3.5 :offset 9.8}
                 :arm {:width 1.7}})
(def skeleton-upwards-length 32.54)
(def short-skeleton-arm-x (- (/ ltrac-length 2) skeleton-y-top-offset))
(def skeleton-data {:back {:height 3 :angle 60}})
