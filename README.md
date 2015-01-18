# clojexcms – Clojure(Script) Example CMS

Developed for the [:clojureD conference](http://www.clojured.de) 2015, "Web Applications by
Example", Steffen Beyer.

*frontend* is the visitor-facing website, *backend* is the administration panel.

## Dependencies and building blocks

* [Chestnut](https://github.com/plexus/chestnut) – Leiningen template for ClJs development
  * Figwheel
  * http-kit
  * Om
* PostgreSQL
* [Flyway](http://flywaydb.org) – Database migrations

## Setup

* DB connection
* lein flyway...

## Development

Open a terminal and type `lein repl` to start a Clojure REPL
(interactive prompt).

In the REPL, type

```clojure
(run)
(browser-repl)
```

The call to `(run)` does two things, it starts the webserver at port
10555, and also the Figwheel server which takes care of live reloading
ClojureScript code and CSS. Give them some time to start.

Running `(browser-repl)` starts the Weasel REPL server, and drops you
into a ClojureScript REPL. Evaluating expressions here will only work
once you've loaded the page, so the browser can connect to Weasel.

When you see the line `Successfully compiled "resources/public/app.js"
in 21.36 seconds.`, you're ready to go. Browse to
`http://localhost:10555` and enjoy.

**Attention: It is not longer needed to run `lein figwheel`
  separately. This is now taken care of behind the scenes**

## Trying it out

If all is well you now have a browser window saying 'Hello Chestnut',
and a REPL prompt that looks like `cljs.user=>`.

Open `resources/public/css/style.css` and change some styling of the
H1 element. Notice how it's updated instantly in the browser.

Open `src/cljs/clojexcms/core.cljs`, and change `dom/h1` to
`dom/h2`. As soon as you save the file, your browser is updated.

In the REPL, type

```
(ns clojexcms.core)
(swap! app-state assoc :text "Interactivity FTW")
```

Notice again how the browser updates.

## Deploying to Heroku

This assumes you have a
[Heroku account](https://signup.heroku.com/dc), have installed the
[Heroku toolbelt](https://toolbelt.heroku.com/), and have done a
`heroku login` before.

``` sh
git init
git add -A
git commit
heroku create
git push heroku master:master
heroku open
```

## Running with Foreman

Heroku uses [Foreman](http://ddollar.github.io/foreman/) to run your
app, which uses the `Procfile` in your repository to figure out which
server command to run. Heroku also compiles and runs your code with a
Leiningen "production" profile, instead of "dev". To locally simulate
what Heroku does you can do:

``` sh
lein with-profile -dev,+production uberjar && foreman start
```

Now your app is running at
[http://localhost:5000](http://localhost:5000) in production mode.

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## Chestnut

Created with [Chestnut](http://plexus.github.io/chestnut/) 0.7.0-SNAPSHOT (ecadc3ce).

## Known issues/To do

* LT browser repl port (use internal browser?)
* LT plugins & behaviours & keybindings (publish?)
* LT paredit
* live CSS update/scrubber
* ask about ~1000 errors in Google group
* DRY DB config

## Live coding, possibilities and issues

* Chrome source view, sourcemap, debugger, breakpoints
* LT instarepl, watches, docs

* http://thinkrelevance.com/blog/2013/06/04/clojure-workflow-reloaded
* http://blog.michielborkent.nl/blog/2014/09/25/figwheel-keep-Om-turning/
* https://github.com/bhauman/lein-figwheel
* http://astashov.github.io/blog/2014/07/30/perfect-clojurescript-development-environment-with-vim/

## Further references

* CircleCI frontend
* Anna's talk
* Luminus
* Bret Victor
