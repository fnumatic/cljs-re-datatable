(ns ^:figwheel-hooks cljs-re-datatable.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [cljs-re-datatable.use-cases.core-cases :as ccases]
   [cljs-re-datatable.routes :as routes]
   [cljs-re-datatable.views.home :as home]
   [cljs-re-datatable.config :as config]
   [cljs-re-datatable.styles :as styl]))



(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (println "mount")
  (re-frame/clear-subscription-cache!)
  (styl/inject-trace-styles js/document)
  (reagent/render [home/main-panel]
                  (.getElementById js/document "app")))

(defn ^:after-load re-render []
  (mount-root))

(defn ^:export init []
  (println "init again..")
  (re-frame/dispatch-sync [::ccases/initialize-db])
  (routes/app-routes)
  (dev-setup)

  (mount-root))

(defonce init-block (init))
