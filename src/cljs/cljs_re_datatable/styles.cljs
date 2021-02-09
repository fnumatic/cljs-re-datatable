(ns cljs-re-datatable.styles
  (:require
      [goog.dom :as gdom]
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
    (gdom/replaceNode new-node old-node)
    (gdom/appendChild (.-head document) new-node)))

(defn inject-inline-style [document id style]
  (let [old-style (gdom/getElement  id)
        new-style (gdom/createDom "style"
                                  (clj->js {:type "text/css"
                                            :id id})
                                  style)]

    (inject-node! old-style new-style document)))

(defn inject-inline-link [document id link]
  (let [old-link (gdom/getElement id)
        new-link (gdom/createDom "link"
                                 (clj->js {:id id
                                           :rel :stylesheet
                                           :href link}))]

    (inject-node! old-link new-link document)))


(defn inject-trace-styles [document]
  ;(inject-inline-link document "--bootstrap--"  "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css")
  (inject-inline-link document "--react-virtualized--"  "css/react-virtualized.inc.css")
  (inject-inline-link document "--tailwind--"  "app.css")
  (inject-inline-style document "--re-datatable--" (apply g/css styles)))

