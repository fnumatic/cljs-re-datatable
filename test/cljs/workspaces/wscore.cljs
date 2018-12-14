(ns workspaces.wscore
  (:require
    [goog.object :as go]
    [cljs-re-datatable.styles :as styl]
    [nubank.workspaces.core :as ws]
    [workspaces.cards]))


(extend-type object
  ILookup
  (-lookup
   ([o k]
    (go/get o (name k)))
   ([o k not-found]
    (go/get o (name k) not-found))))

(defn ^:after-load re-render []
  (do
    (styl/inject-trace-styles js/document)
    (ws/after-load)))



(defn ^:export init []
  (do
    (styl/inject-trace-styles js/document)
    (ws/mount)))

(defonce init-block (init))