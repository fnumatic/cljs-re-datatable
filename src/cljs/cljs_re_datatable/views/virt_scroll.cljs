(ns cljs-re-datatable.views.virt-scroll
  (:require
    [reagent.core :as r]
    [reagent.dom :as dom]
    [goog.functions]))
  


(defn scrollPos [el]
 (let [target (.-target el)]
   {:top (.-scrollTop target)
    :left (.-scrollLeft target)}))



(defn on-scroll [atm path value]
  (println value)
  (swap! atm assoc-in path value))

(def deb-on-scroll
  (goog.functions.debounce on-scroll 15))

(def throt-on-scroll
  (goog.functions.throttle on-scroll 100))

(defn master-scroll [attr cb pos path & childs]
    (r/create-class
      {
      ;  :component-did-mount
      ;   (fn [this]
      ;     (reagent.ratom/run!  (set! (.-scrollTop (r/dom-node this)) (:top (get-in @pos path)))))
       :reagent-render
        (fn []
          (println :rendermaster
           (into [:div
                  (merge-with merge
                              {:on-scroll (fn [ev] (do (cb (scrollPos ev))
                                                       (.preventDefault ev)
                                                       nil))
                               :style {:overflow :scroll
                                       :will-change :transform}}
                    
                    attr)]

                 childs)))}))

(defn slave-scroll [child scrollref]
    (r/create-class
      {:component-did-mount
             (fn [this]
               (reagent.ratom/run!  (set! (.-scrollTop (dom/dom-node this) ) @scrollref)))

       :reagent-render

            (fn []
              [child])}))


(defn defaultlist2 [pos path height meta]
  (r/with-let [numbers (range 1 100)
               parts (partition (inc (quot height 25)) 1 numbers)
               top-pos #(:top (get-in % path))
               winnum #(quot (top-pos %) 25)]
           (println (:name meta) (top-pos @pos)  (winnum @pos) (nth parts (winnum @pos)))

         [:div.col-sm-4
          {:style {:height 2000}}
          [:ul.list-unstyled
           {:style {
                    :position :absolute
                    :width "100%"
                    :left 0
                    :top (* (winnum @pos) 25)}}
           (for [n (nth parts (winnum @pos))]
             ^{:key n} [:li.Cell2 n])]]))

(defn row4 [n]
  [:tr
   (for [x (range 1 5)]
     [:td (* n x)])])

(defn row1 [n]
  [:tr
   [:td (* n 1)]])



(defn comblist []
  (r/with-let [
               top1 (r/atom {:li1 {:top 0 :left 0}})]

    [:div.container
     [:div.row (str (get-in @top1 [:li1]))]
     
     [master-scroll
      {:class-name :row
       :style      {:height 200}}
      #(throt-on-scroll top1 [:li1] %)
      top1 [:li1]
      [defaultlist2 top1 [:li1] 200 {:name :listAA}]]
     
     [master-scroll
      {:class-name :row
       :style      {:height 200}}
      #(throt-on-scroll top1 [:li1] %)
      top1 [:li1]
      [defaultlist2 top1 [:li1] 200 {:name :listBB}]]]))
     
     ;[:div.row2.mt-3.mb-1 (str (get-in [:li1] @top1))]]))
    