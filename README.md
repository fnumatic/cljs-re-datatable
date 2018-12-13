# cljs-re-datatable

A quick demo to show the use of [react-virtualized] (http://bvaughn.github.io/react-virtualized/)

There is an example for how to get row-span and col-span working.

![tweaked multigrid](public/img/table.png?raw=true)

* react-virtualized tables
* rowspan, colspan multigrid
* reitit frontend routing
* figwheel main
* garden styles

## Development Mode

### Start Cider from Emacs:

Put this in your Emacs config file:

```
(setq cider-cljs-lein-repl
	"(do (require 'figwheel-sidecar.repl-api)
         (figwheel-sidecar.repl-api/start-figwheel!)
         (figwheel-sidecar.repl-api/cljs-repl))")
```

Navigate to a clojurescript file and start a figwheel REPL with `cider-jack-in-clojurescript` or (`C-c M-J`)

### Compile css:

Garden styles are automatically injected into header.

### Compile and Run application:

```
lein fig:build
```

Figwheel will automatically push cljs changes to the browser.

