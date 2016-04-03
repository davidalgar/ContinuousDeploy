package me.algar.cosmos.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.design.widget.Snackbar;

import icepick.Icepick;
import rx.Subscription;

abstract class BaseFragment extends Fragment {
    protected Subscription subscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

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
