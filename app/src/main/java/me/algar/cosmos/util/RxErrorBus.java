package me.algar.cosmos.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;


/*
Simple event bus for handling error cases up from the API level to the UI level
 */
public class RxErrorBus {
    public static RxErrorBus sInstance = new RxErrorBus();

    private final Subject<Error, Error> errorSubject = new SerializedSubject<>(PublishSubject.create());

    public static RxErrorBus getInstance() {
        return sInstance;
    }

    public void errorHappened(Error o) {
        errorSubject.onNext(o);
    }

    public Observable<Error> observeErrors() {
        return errorSubject;
    }

    public static class Error {
        private final String message;
        private final Throwable throwable;

        public Error(String message, Throwable t){
            this.message = message;
            this.throwable = t;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public String getMessage() {
            return message;
        }
    }
}
