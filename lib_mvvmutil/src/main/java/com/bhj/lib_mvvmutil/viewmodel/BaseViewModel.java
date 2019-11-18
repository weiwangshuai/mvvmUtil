package com.bhj.lib_mvvmutil.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class BaseViewModel extends AndroidViewModel implements LifecycleProvider<ActivityEvent>, LifecycleObserver {

    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    @NonNull
    @Override
    public Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @NonNull
    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        lifecycleSubject.onNext(ActivityEvent.CREATE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void onStart() {
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void onResume() {
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void onPause() {
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
    }

}
