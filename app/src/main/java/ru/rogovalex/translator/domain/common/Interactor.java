package ru.rogovalex.translator.domain.common;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 18:18
 */
public abstract class Interactor<ResultType, ParameterType> {

    private Disposable mSubscription;
    private final Scheduler mJobScheduler;
    private final Scheduler mUiScheduler;

    public Interactor(Scheduler jobScheduler, Scheduler uiScheduler) {
        mJobScheduler = jobScheduler;
        mUiScheduler = uiScheduler;
    }

    protected abstract Observable<ResultType> buildObservable(ParameterType parameter);

    public Disposable execute(ParameterType parameter, Consumer<ResultType> onNext) {
        return execute(parameter, onNext, Functions.ERROR_CONSUMER);
    }

    public Disposable execute(ParameterType parameter, Consumer<ResultType> onNext,
                              Consumer<? super Throwable> onError) {
        return execute(parameter, onNext, onError, Functions.EMPTY_ACTION);
    }

    public Disposable execute(ParameterType parameter, Consumer<ResultType> onNext,
                              Consumer<? super Throwable> onError, Action onComplete) {
        mSubscription = buildObservable(parameter)
                .subscribeOn(mJobScheduler)
                .observeOn(mUiScheduler)
                .subscribe(onNext, onError, onComplete);
        return mSubscription;
    }

    public void cancel() {
        if (isRunning()) {
            mSubscription.dispose();
        }
    }

    public boolean isRunning() {
        return mSubscription != null && !mSubscription.isDisposed();
    }
}
