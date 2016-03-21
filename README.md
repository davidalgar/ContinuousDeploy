# ContinuousDeploy
Android app that makes it easy to install the latest APK from your CI server

# CI Servers Supported
Jenkins
TeamCity (work in progress)

# Technologies Used
### Networking
Retrofit2 - network library, provides Observable network calls

### Architecture
RxJava / RxAndroid - reactive extensions for Android
RxRelay - like Subjects, but they do not terminate on onError/onComplete

### Data Storage
SqlBrite - provides Observable SQL queries, making it easier to keep the view updated with the data model
SqlDelight - auto-generation of model classes for SQLite from plain SQL

### Utilities
Butterknife - view binding
Timber - logging wrapper
