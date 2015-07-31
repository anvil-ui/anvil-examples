# Anvil samples

[Anvil][1] is a tiny reactive UI library for Android. Inspired by [React][2]
and [Mithril][3], it brings declarative data binding, unidirectional data flow
and componentization and other things that would make your code look cleaner
and easier to maintain.

This repository contains small examples of how Anvil can be used.

## Example projects

* [Hello][4] - simple static layout with a classical text message
	- how to start Anvil project
	- how to write layouts without XML
* [Counter][5] - simple click counter
	- how to bind variables to views
	- how to bind event listeners to views
	- how easy is to to keep UI in sync with data (automatic rendering)
* [Login form][6] - two input fields, push button and some logic behind them
	- how to use text watcher bindings
	- how to use Java 8 method references as event bindings
* [Item picker][7] - animated item picker component with next/prev buttons
	- how to use animations
	- how to use states
	- how to use config() to get access to the real View object
	- how to use Java8 lambdas in Anvil
* [Currency exchange app][8] - fetches latest currency rates from the backend, calculates converted values as you type.
	- how to separate model logic from the view logic
	- how to separate view styling from view hierarchy
	- how to bind adapters
	- how to get two-directional data binding for text input
* [Countdone clone][9] (current Anvil example) - pomodoro-like app: define how long the task should take and see if you finish it in time
	- how to use backstack having just one activity
	- how to save component state
	- how to use custom fonts and icon fonts
* [Todo app][11] - classical MVC example: add tasks, check tasks, remove checked tasks
	- how to use list adapters
	- how the same app would look with [Java 7][10], [Java 8][11] and [Kotlin][12]

[1]: https://github.com/zserge/anvil/
[2]: http://facebook.github.io/react/
[3]: http://mithril.js.org/
[4]: https://github.com/zserge/anvil-examples/hello
[5]: https://github.com/zserge/anvil-examples/counter
[6]: https://github.com/zserge/anvil-examples/login
[7]: https://github.com/zserge/anvil-examples/anim-picker
[8]: https://github.com/zserge/anvil-examples/currency
[9]: https://github.com/zserge/anvil-examples/countdone
[10]: https://github.com/zserge/anvil-examples/todo
[11]: https://github.com/zserge/anvil-examples/todo-java8
[12]: https://github.com/zserge/anvil-examples/todo-kotlin
