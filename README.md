# cljs-re-datatable

A quick demo to show the use of [react-virtualized](http://bvaughn.github.io/react-virtualized/)

There is an example for how to get row-span and col-span working.

![tweaked multigrid](resources/public/img/table.png?raw=true)


Demo site: [explore via workspaces](http://fnumatic.github.io/cljs-re-datatable/)

* react-virtualized tables
* rowspan, colspan multigrid
* reitit frontend routing
* [garden styles](https://github.com/noprompt/garden)
* [tailwind css](https://github.com/tailwindlabs/tailwindcss)


## Autosizer

Use of react virtualized Autosizer. Render props are not idiomatic in cljs land.

[reagent hooks](https://gitlab.com/boogie666/reagent-hooks)

```clojure
(defn AutoSizr [sizer-fn]
  [:> js/ReactVirtualized.AutoSizer
   (fn [props]
       (sizer-fn props)
       (r/as-element [:<>]))])

(defn use-state [value]
  (let [r (r/atom value)]
    [r #(reset! r %)]))
```

```clojure
;;avoid render props
(r/with-let [[size resize] (rvu/use-state {:width 0 :height 0})]
  [:div
    [rvu/AutoSizr resize]
    [multigrid @size]]])
```


### Compile css:

Garden styles are automatically injected into header.

```
npm run-script tw
```

### Compile and Run application:

```
./run watch
```


