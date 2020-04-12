package com.cr.pn.rxjava.defaultTransformer;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zy on 2018/7/2.
 */

public class DefaultFunctionTransformer<T,F> implements ObservableTransformer<T, F> {

    private Function<T,F> function;

    public DefaultFunctionTransformer(Function function){
        this.function = function;
    }

    @Override
    public ObservableSource<F> apply(Observable<T> upstream) {
        Observable<T> tObservable = upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io());
        if (function != null){
            return tObservable.map(function)
                    .observeOn(AndroidSchedulers.mainThread());
        }else {
            return tObservable.map(new Function<T, F>() {
                @Override
                public F apply(T t) throws Exception {
                    return (F) t;
                }
            })
            .observeOn(AndroidSchedulers.mainThread());
        }
    }

}
