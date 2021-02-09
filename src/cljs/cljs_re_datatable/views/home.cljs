(ns cljs-re-datatable.views.home
  (:require
    [goog.object :as go]
    [re-frame.core :as re-frame]
    [react-virtualized]
    [cljs-re-datatable.use-cases.core-cases :as ccases]
    [tools.viewtools :as vt]))


(extend-type object
  ILookup
  (-lookup
   ([o k]
    (go/get o (name k)))
   ([o k not-found]
    (go/get o (name k) not-found))))


(def toolbar-items
  [
   ["#" :routes/table]
   ["multigrid" :routes/multigrid]
   ["multigrid2" :routes/multigrid2]
   ["virtscroll" :routes/virtscroll]])

;; main

(defn show-panel [route]
  (when-let [route-data (:data route)]
    (let [view (:view route-data)]
      [:<>
       [view]])))
       ;[:pre (with-out-str (cljs.pprint/pprint route))]])))

(defn main-panel []
  (let [active-route (re-frame/subscribe [::ccases/active-panel])]
    [:div.flex.flex-col.h-screen
     [vt/navigation toolbar-items]
     [show-panel @active-route]]))
