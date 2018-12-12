(ns cljs-re-datatable.routes
  (:require
    [re-frame.core :as rf]
    [reitit.frontend :as rtf]
    [reitit.frontend.easy :as rtfe]
    [reitit.coercion.schema :as rsc]
    [cljs-re-datatable.use-cases.core-cases :as ccases]
    [cljs-re-datatable.views.rv-table  :as rvtbl]
    [cljs-re-datatable.views.rv-multigrid  :as rvmg]
    [cljs-re-datatable.views.rv-multigrid-enh  :as rvmgenh]))

;;https://clojure.org/guides/weird_characters#__code_code_var_quote
(def routes
    (rtf/router
      ["/"
       [""
        {:name :routes/table
         :view #'rvtbl/main}]
       ["multigrid"
        {:name :routes/multigrid
          :view #'rvmg/main}]
       ["multigrid2"
        {:name :routes/multigrid2
          :view #'rvmgenh/main}]]


      {:data {:coercion rsc/coercion}}))



(defn app-routes []

  (rtfe/start! routes
               (fn [m] (rf/dispatch [::ccases/set-active-panel m]))
               {:use-fragment true}))