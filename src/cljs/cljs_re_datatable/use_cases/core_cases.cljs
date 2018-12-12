(ns cljs-re-datatable.use-cases.core-cases
  (:require
    [re-frame.core :as re-frame]
    [tools.reframetools :refer [sdb gdb]]
    [cljs-re-datatable.db :as db]))



(re-frame/reg-sub ::name (gdb [:name]))
(re-frame/reg-sub ::active-panel (gdb [:active-panel]))
(re-frame/reg-sub ::re-pressed-example  (gdb [:re-pressed-example]))

(re-frame/reg-event-db ::initialize-db (constantly db/default-db))
(re-frame/reg-event-db ::set-active-panel [re-frame/debug] (sdb [:active-panel]))


