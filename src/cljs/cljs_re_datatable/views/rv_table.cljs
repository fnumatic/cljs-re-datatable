(ns cljs-re-datatable.views.rv-table
  (:require
    [react-virtualized :refer [ Table Column ]]))


(defn person [i]
  {:name (str "name " i)
   :lastname (str "lastname " (+ 100 i))
   :age (rand-int 150)})

(defn table []
  (let [rows (map (comp clj->js person) (range 100))]
    [:>  Table
     {:headerHeight    70
      :height          300
      :rowCount        (count rows)
      :rowGetter       (fn [o] (nth rows (:index o)))
      :rowHeight       50
      :width           450
      :headerClassName "headerColumn"
      :className       "Table"}
     [:> Column
      {:label   "name"
       :dataKey (name :name)
       :className "bordered exampleColumn"
       :width   100}]
     [:> Column
      {:label     "lastname"
       :dataKey   (name :lastname)
       :className "bordered exampleColumn"
       :width     100}]
     [:> Column
      {:label     "age"
       :dataKey   (name :age)
       :className "bordered exampleColumn"
       :width     100}]]))

(defn main []
  [:div
   [:h2.text-xl.font-semibold "simple table"]
   [table]])