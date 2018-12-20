(ns tools.viewtools
  (:require [reitit.frontend.easy :as rtfe]
            [reagent.core :as rc]))

(defn timing-wrapper [f attrs]
  (let [start-time (rc/atom nil)
        render-time (rc/atom nil)
        now #(.now js/Date)
        start #(reset! start-time (now))
        stop #(reset! render-time (- (now) @start-time))
        timed-f (with-meta f
                  {:component-will-mount start
                   :component-will-update start
                   :component-did-mount stop
                   :component-did-update stop})]
    (fn []
      [:div
       [:p [:em "render time: " @render-time "ms"]]
       [timed-f attrs]])))

(defn item [e]
  (cond
     (fn? e) [e]
     (vector? e) e
     (string? e) [:h2 e]))

(defn panel [name component]
  [:div

   [item name]
   [item component]])

;; navigation tools
(defn sep []
  [:span " | "])

(defn nav-item [i]
  (if (= :sep i)
    [sep]
    [:a {:href (rtfe/href (second i))} (first i)]))

(defn navigation [routes]
  (let [coll (->> routes (interpose :sep) (map-indexed vector))]
    [:div
     (for [[idx rt]  coll]
        ^{:key (str idx)} [nav-item rt])]))