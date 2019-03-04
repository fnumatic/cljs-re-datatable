(ns workspaces.cards
  (:require
    [nubank.workspaces.core :as ws]
    [nubank.workspaces.model :as wsm]
    [nubank.workspaces.card-types.react :as ct.react]
    [reagent.core :as rc]
    [cljs-re-datatable.views.rv-table :as table]
    [cljs-re-datatable.views.rv-multigrid :as mg]
    [cljs-re-datatable.views.rv-multigrid-enh :as mge]))



(ws/defcard simpletable
   (ct.react/react-card
     (rc/as-element [table/main])))

(ws/defcard multigrid
   {::wsm/align {:flex "1 1 auto"}}
   (ct.react/react-card
     (rc/as-element [mg/main])))

(ws/defcard multigrid-enhanced
   (ct.react/react-card
     (rc/as-element [mge/main])))
