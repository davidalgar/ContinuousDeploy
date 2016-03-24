# ContinuousDeploy
Android app that makes it easy to install the latest APK from your CI server

# CI Servers Supported
* Jenkins
* TeamCity (work in progress)

# Technologies Used
### Networking
* [Retrofit2](https://github.com/square/retrofit) - network library, provides Observable network calls

### Architecture
* [RxJava](https://github.com/ReactiveX/RxJava) / [RxAndroid](https://github.com/ReactiveX/RxAndroid) - Reactive extensions for Android
* [RxRelay](https://github.com/JakeWharton/RxRelay) - like Subjects, but they do not terminate on onError/onComplete

### Data Storage
* [SQLBrite](https://github.com/square/sqlbrite) - provides Observable SQL queries, making it easier to keep the view updated with the data model
* [SQLDelight](https://github.com/square/sqldelight) - auto-generation of model classes for SQLite from plain SQL

### Utilities
* [Butterknife](http://jakewharton.github.io/butterknife/) - view binding
* [Timber](https://github.com/JakeWharton/timber) - logging wrapper
