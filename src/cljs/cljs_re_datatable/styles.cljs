(ns cljs-re-datatable.styles
  (:require

      [garden.core :as g]))


(def styles
  [
   [:.Table
    {:width "100%"
     :margin-top "15px"}]
   [:.headerRow :.evenRow :.oddRow
    {:border-bottom "1px solid #e0e0e0"}]
   [:.oddRow
    {:background-color "#fafafa"}]
   [:.headerColumn
    {:text-transform :none}]
   [:.exampleColumn
    {:white-space :nowrap
     :overflow :hidden
     :text-overflow :ellipsis}]
   [:.checkboxLabel
    {:margin-left ".5rem"}]
   [:.checkboxLabel:first-of-type
    {:margin-left "0"}]
   [:.noRows
    {:position :absolute
     :top 0
     :bottom 0
     :left 0
     :right 0
     :display :flex
     :align-items :center
     :justify-content :center
     :font-size "1em"
     :color "#bdbdbd"}]

   [:.Cell
    {:display :flex
     :position :absolute
     :border "1px solid grey"
     :border-bottom "1px solid #eee"
     :border-right "1px solid #eee"
     :align-items :center
     :justify-content :center}]
   [:.Cell2 :.bordered
    {
     :border-bottom "1px solid #eee"
     :border-right "1px solid #eee"
     :align-items :center
     :justify-content :center}]])


(defn inject-node! [old-node new-node document]
  (if old-node
    (-> old-node
        (.-parentNode)
        (.replaceChild new-node old-node))
    (let []
      (.appendChild (.-head document) new-node)
      new-node)))

(defn inject-inline-style [document id style]
  (let [old-style (.getElementById document id)
        new-style (.createElement document "style")
        attr      #(.setAttribute new-style %1 %2)]
    (attr "id" id)
    (attr "type" "text/css")
    (-> new-style
        (.-innerHTML)
        (set! style))
    (inject-node! old-style new-style document)))

(defn inject-inline-link [document id link]
  (let [old-link (.getElementById document id)
        new-link (.createElement document "link")
        attr     #(.setAttribute new-link %1 %2)]
    (attr  "id" id)
    (attr  "rel" "stylesheet")
    (attr  "href" link)

    (inject-node! old-link new-link document)))


(defn inject-trace-styles [document]
  (inject-inline-link document "--bootstrap--"  "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css")
  (inject-inline-link document "--react-virtualized--"  "css/react-virtualized.inc.css")
  (inject-inline-style document "--re-datatable--" (apply g/css styles)))

