(ns cljs-re-datatable.views.rv-multigrid
  (:require
     [reagent.core :as r]
     [cljs-re-datatable.views.rvutils :as rvu]
     [react-virtualized :refer [defaultCellRangeRenderer MultiGrid]]))
   

(defn ^:export xcell-renderer [{:keys [style key isVisible isScrolling] c :columnIndex r :rowIndex :as m}]
     (cond
       (not isVisible) nil
       (= [0 0] [c r])  nil
       (= [1 0] [c r])  nil

       :default (r/as-element [:div.Cell
                               {:key key
                                :style style}
                               (str "[" c ", " r "]")])))

(defn ^:export default-cellrenderer [m]
  (let [childs (defaultCellRangeRenderer m)]
    (.push childs (r/as-element [:div "hurray"]))
    childs))




(defn ^:export visible? [{:keys [visibleColumnIndices visibleRowIndices]} ri ci]
  (and
    (>= ci (:start visibleColumnIndices))
    (>= ri (:start visibleRowIndices))
    (<= ci (:stop visibleColumnIndices))
    (<= ri (:stop visibleRowIndices))))


(defn size-position-cell [pm index]
  (js-invoke  pm "getSizeAndPositionOfCell" index))

(defn cell-dimension [{:keys [rowSizeAndPositionManager
                              columnSizeAndPositionManager
                              horizontalOffsetAdjustment
                              verticalOffsetAdjustment]}
                      ri ci]
  (let [rd     (size-position-cell rowSizeAndPositionManager ri)
        cd     (size-position-cell columnSizeAndPositionManager ci)
        left   (+ (:offset cd) horizontalOffsetAdjustment)
        top    (+ (:offset rd) verticalOffsetAdjustment)
        height (:size rd)
        width  (:size cd)]
    {
     :height height
     :left   left
     :top    top
     :width  width}))


(defn dbg-cr-render [{:keys [columnStartIndex
                             columnStopIndex
                             rowStartIndex
                             rowStopIndex
                             visibleColumnIndices
                             visibleRowIndices
                             scrollLeft]}]
  {:csi [columnStartIndex columnStopIndex] :rsi [rowStartIndex rowStopIndex]
   :vc  (js->clj visibleColumnIndices) :vr (js->clj visibleRowIndices) :sl scrollLeft})


(defn xcellrange-renderer [{:keys                                    [
                                                                      cellRenderer,
                                                                      columnStartIndex,
                                                                      columnStopIndex,
                                                                      isScrolling,
                                                                      rowStartIndex,
                                                                      rowStopIndex,
                                                                      parent ] :as props}]

  (let [
        _visible? (partial visible? props)
        _cell-dim (partial cell-dimension props)
        _render   (fn [ri ci] (->
                                {:columnIndex ci
                                 :rowIndex    ri
                                 :isScrolling isScrolling
                                 :isVisible   true
                                 :parent      parent
                                 :key         (str ri "-" ci)
                                 :style       (_cell-dim ri ci)}

                                (clj->js)
                                cellRenderer))]
                                ;mg-renderer)))

    (->
      (for [ri (range rowStartIndex (inc rowStopIndex))
            ci (range columnStartIndex (inc columnStopIndex))]
        (when (_visible? ri ci) (_render ri ci)))
      clj->js)))


(defn multigrid [size]
  [:> MultiGrid
   {
    :fixedColumnCount  2,
    :fixedRowCount     1,
    :cellRenderer      xcell-renderer
    :cellRangeRenderer xcellrange-renderer
    :scrollToColumn    11
    :columnCount       30
    :rowCount          100
    :rowHeight         40
    :columnWidth       60
    :height            (:height size)
    :width             (:width size)}])

(defn main []
  (r/with-let [[size resize] (rvu/use-state {:width  0
                                             :height 0})]
    [:div.flex.flex-col.p-4.w-full.h-full
     [:h2.text-xl.font-semibold "multigrid"]
     [:div.flex-auto;
      [rvu/AutoSizr2 resize
       [multigrid @size]]]]))