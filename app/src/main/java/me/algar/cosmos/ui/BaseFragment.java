package me.algar.cosmos.ui;

import android.support.v4.app.Fragment;
import android.support.design.widget.Snackbar;

import rx.Subscription;
import timber.log.Timber;

abstract class BaseFragment extends Fragment {
    protected Subscription subscription;

    @Override
    public void onResume() {
        super.onResume();

        subscribe();
    }

    abstract void subscribe();

    @Override
    public void onPause() {
        super.onPause();

        if(subscription != null){
            subscription.unsubscribe();
        }
    }

    protected void showMessage(String message){
        Snackbar.make(getActivity().getWindow().getDecorView(),message,Snackbar.LENGTH_LONG);
    }

}
