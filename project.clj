(defproject cljs-re-datatable "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.439"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.6"]
                 [metosin/reitit "0.2.7"]
                 [metosin/reitit-schema "0.2.7"]
                 [metosin/reitit-frontend "0.2.7"]
                 [garden "1.3.6"]
                 [cljsjs/react-virtualized "9.18.5-1"]]

  :plugins [[lein-cljsbuild "1.1.7"]]

  :min-lein-version "2.8.1"

  :source-paths ["src/clj" "src/cljs" "test/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]


  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [day8.re-frame/re-frame-10x "0.3.6"]
                   [com.bhauman/figwheel-main "0.1.9"]
                   [com.bhauman/rebel-readline-cljs "0.1.4"]
                   [cider/piggieback "0.3.10"]]
    :resource-paths ["target"]
    :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}}}



  :cljsbuild
  {:builds
   [

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            cljs-re-datatable.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}]}

  :aliases {"fig"       ["trampoline" "run" "-m" "figwheel.main"]
            "fig:build" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:min"   ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]

            "ancient-all" ["update-in" ":plugins" "conj" "[lein-ancient \"0.6.15\"]"
                                    "--" "ancient" "upgrade" ":interactive" ":all" ":check-clojure" ":no-tests"]
            "ancient" ["update-in" ":plugins" "conj" "[lein-ancient \"0.6.15\"]" "--" "ancient"]})




