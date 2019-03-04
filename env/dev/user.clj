(ns user
  (:require [figwheel.main.api :as ra]))

(defn start [] (ra/start  "dev"))

(defn startws [] (ra/start  "ws"))

(defn stopp [] (ra/stop-all))

(defn cljs [] (ra/cljs-repl "dev"))