(ns cljs-re-datatable.views.rvutils
  (:require 
            [reagent.core :as r]
            [react-virtualized :refer [ AutoSizer]]))


(defn AutoSizr2 [sizer-fn cmp]
  [:> AutoSizer
   (fn [props]
     (sizer-fn props)
     (r/as-element cmp))])

(defn AutoSizr [sizer-fn]
  [:> AutoSizer
   (fn [props]
       (sizer-fn props)
       (r/as-element [:<>]))])

(defn merge-size [size-atom size-value]
  (swap! size-atom merge
         (js->clj  size-value :keywordize-keys true)))

(defn use-state [value]
  (let [r (r/atom value)]
    [r #(merge-size r %)]))

