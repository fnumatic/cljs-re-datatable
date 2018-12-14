(ns cljs-re-datatable.views.rv-multigrid-enh
  (:require
     [reagent.core :as r]
     [cljsjs.react-virtualized]))


(defn size-position-cell [pm index]
  (js-invoke  pm "getSizeAndPositionOfCell" index))

(def matrix
  {:rowCount 200
   :columnCount 30})

(defn kfn [ri ci]
  (str ri "-" ci))

(defn indexed-by [kfn v]
  (into {} (map (juxt kfn identity) v)))


(def data
  (->
    (for [
          ri (range  0 (inc (:rowCount matrix)))
          ci (range  0 (inc (:columnCount matrix)))]
      {:content (kfn ri ci)
       :pos [ri ci]})
    (->> (indexed-by :pos))
    (dissoc [0 0] [0 1] [0 3] [0 4] [1 3] [1 4] [2 2] [2 3] [2 4] [1 6])
    (update [0 2] assoc :col-span 3)
    (update [1 2] assoc :col-span 3 :row-span 2)
    (update [1 5] assoc :col-span 2)))
;    (update [1 0] assoc :col-span 2)))


(defn has-span? [c] (or (:row-span c) (:col-span c)))

(defn cell-at [pos]
  (-> data (get pos)))

(defn cell-content [c]
  (:content c))

(defn fullgrid-position [{:keys [fixedRowCount fixedColumnCount className]} ri ci]
  ;which grid is rendering -> from relative grid position to fullgrid position
  (case (keyword className)
    :bottomRightGrid [(+ ri fixedRowCount) (+ ci fixedColumnCount)]
    :topRightGrid    [ri (+ ci fixedColumnCount)]
    :bottomLeftGrid  [(+ ri fixedRowCount) ci]
                     [ri ci]))

(defn calc-span [row-span col-span ri ci]
  ;calculate every possible position for row/col span
    (doall
      (for [r (range 0 (or row-span 1))
            c (range 0 (or col-span 1))]
        [(+ ri r) (+ ci c)])))

(defn visible-in-span? [cells rows cols]
  (some #(and (>= (first %)  (:start rows))
              (>= (second %) (:start cols)))
        cells))

(defn visible? [{:keys [visibleColumnIndices visibleRowIndices parent]} ri ci cellfn]
  (let [pos        (fullgrid-position (:props parent) ri ci)
        cell       (cellfn pos)
        virt-cells #(calc-span (:row-span cell) (:col-span cell) ri ci)]

    (or
      (and
        (>= ci (:start visibleColumnIndices))
        (>= ri (:start visibleRowIndices))
        (<= ci (:stop visibleColumnIndices))
        (<= ri (:stop visibleRowIndices)))
      (and
        (boolean (has-span? cell))
        (boolean (visible-in-span? (virt-cells) visibleRowIndices visibleColumnIndices))))))

(defn sizefn [posman offs]
  (:size (size-position-cell posman offs)))

(defn offsetfn [posman offs]
  (:offset (size-position-cell posman offs)))

(defn span-size [posman offs span]
  ( if span
    (->>
      (+ offs (or span 1))
      (range offs)
      (map (partial sizefn posman))
      (apply +))
    (sizefn posman offs)))


(defn cell-dimension [{:keys [rowSizeAndPositionManager
                              columnSizeAndPositionManager
                              horizontalOffsetAdjustment
                              verticalOffsetAdjustment
                              parent]}
                      ri ci cellfn]

  (let [pos  (fullgrid-position (:props parent) ri ci)
        cell (cellfn pos)]

    {
     :left   (+ (offsetfn columnSizeAndPositionManager ci) horizontalOffsetAdjustment)
     :top    (+ (offsetfn rowSizeAndPositionManager ri) verticalOffsetAdjustment)
     :height (span-size rowSizeAndPositionManager ri (:row-span cell))
     :width  (span-size columnSizeAndPositionManager ci (:col-span cell))}))


(defn xcell-renderer [{:keys [style key isVisible columnIndex rowIndex]}]
  (let [cell (cell-at [rowIndex columnIndex])]
    (cond
      (nil? cell) nil
      (not isVisible) nil

      :default (r/as-element [:div.Cell
                              {:key key
                               :style style}
                              (cell-content cell)]))))


(defn xcellrange-renderer [{:keys                   [
                                                     cellRenderer,
                                                     columnStopIndex,
                                                     isScrolling,
                                                     rowStopIndex,
                                                     parent] :as props}]

  (let [
        ;_me (-> parent :props :className keyword)
        _render   (fn [ri ci] (->
                                {:columnIndex ci
                                 :rowIndex    ri
                                 :isScrolling isScrolling
                                 :isVisible   true
                                 :parent      parent
                                 :key         (str ri "-" ci)
                                 :style       (cell-dimension props ri ci cell-at)}

                                (clj->js)
                                cellRenderer))]


       (->
         (for [ri (range 0 (inc rowStopIndex))
               ci (range 0 (inc columnStopIndex))]
           (when (visible? props ri ci cell-at)
                 (_render ri ci)))
         clj->js)))




(defn multigrid []
  [:> js/ReactVirtualized.MultiGrid
   {
    :fixedColumnCount         2,
    :fixedRowCount            1,
    :cellRenderer             xcell-renderer
    :cellRangeRenderer        xcellrange-renderer
    ;:scrollToColumn    8
    :columnCount              (:columnCount matrix)
    :rowCount                 (:rowCount matrix)
    :rowHeight                40
    :columnWidth              70
    :height                   300
    :width                    650
    ; needed for dispatch in cellrange-renderer
    ; want to know wich grid is calling
    :classNameBottomRightGrid :bottomRightGrid
    :classNameTopLeftGrid     :topLeftGrid
    :classNameTopRightGrid    :topRightGrid
    :classNameBottomLeftGrid  :bottomLeftGrid}])

(defn main []
  [:div
     [:h2 "enhanced multigrid with spanned rows cols"]
     [multigrid]])

(comment
  (defn dbg-cr-render [{:keys [columnStartIndex
                               columnStopIndex
                               rowStartIndex
                               rowStopIndex
                               visibleColumnIndices
                               visibleRowIndices
                               scrollLeft]}]
    {:csi [columnStartIndex columnStopIndex] :rsi [rowStartIndex rowStopIndex]
     :vc  (js->clj visibleColumnIndices) :vr (js->clj visibleRowIndices) :sl scrollLeft})
  ;;dbg visible?
  (when has-span?
              (println
                (-> parent :props :className keyword)
                [ri ci] pos :| [(:start visibleRowIndices) (:start visibleColumnIndices)]
                :| (virt-cells)
                :virtual
                (visible-in-span? (virt-cells)  visibleRowIndices visibleColumnIndices)
                :raw
                (and
                        (>= ci (:start visibleColumnIndices))
                        (>= ri (:start visibleRowIndices))
                        (<= ci (:stop visibleColumnIndices))
                        (<= ri (:stop visibleRowIndices))))))