(ns cljs-re-datatable.views.rvutils
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [cljsjs.react-virtualized]))

(defn AutoSizr [sizer-fn]
  [:> js/ReactVirtualized.AutoSizer
   (fn [props]
       ;(println :autosizr props)
       (sizer-fn props)
       (r/as-element [:<>]))])

(defn use-state [value]
  (let [r (r/atom value)]
    [r #(reset! r %)]))

