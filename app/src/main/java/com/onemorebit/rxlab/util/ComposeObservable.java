package com.onemorebit.rxlab.util;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Euro on 2/24/16 AD.
 */
public class ComposeObservable {

    public static @SuppressWarnings("unchecked") <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io());
    }
}
