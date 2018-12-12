(ns cljs-re-datatable.views.rv-table)


(defn person [i]
  {:name (str "name " i)
   :lastname (str "lastname " (+ 100 i))
   :age (rand-int 150)})

(defn table []
  (let [rows (map (comp clj->js person) (range 100))]
    [:>  js/ReactVirtualized.Table
     {:headerHeight    70
      :height          300
      :rowCount        (count rows)
      :rowGetter       (fn [o] (nth rows (:index o)))
      :rowHeight       50
      :width           450
      :headerClassName "headerColumn"
      :className       "Table"}
     [:> js/ReactVirtualized.Column
      {:label   "name"
       :dataKey (name :name)
       :className "bordered exampleColumn"
       :width   100}]
     [:> js/ReactVirtualized.Column
      {:label     "lastname"
       :dataKey   (name :lastname)
       :className "bordered exampleColumn"
       :width     100}]
     [:> js/ReactVirtualized.Column
      {:label     "age"
       :dataKey   (name :age)
       :className "bordered exampleColumn"
       :width     100}]]))

(defn main []
  [:div
   [:h2 "simple table"]
   [table]])