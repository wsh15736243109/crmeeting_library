package com.cr.pn.rxjava.httpRxjava;

import com.cr.pn.rxjava.apiObserver.ApiObserverProgress;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

/**
 * Created by zy on 2018/7/6.
 * Rxjava网络数据获取接口.
 */
public interface NetRxInterface {

    /**
     * 数据获取,订阅者和被订阅者的泛型默认相同.
     * @param <T>
     * @param observable
     */
    public <T> void DataAcquisition(Observable<T> observable, Observer<T> observe);

    /**
     * 数据获取,订阅者和被订阅者的泛型默认不相同，但转换时会默认相同.
     * @param observable
     * @param <T>
     */
    public <T,F> void DataAcquisitionFunction(Observable<T> observable, Observer<F> observer);

    /**
     * 数据获取,订阅者和被订阅者的泛型不同，并自定义转换.
     * @param observable
     * @param <T>
     */
    public <T,F> void DataAcquisitionFunction(Observable<T> observable, Observer<F> observer, Function<T, F> function);

    /**
     * 文件下载时Rxjava处理转换.
     * @param observable
     * @param <T>
     */
    public <T,F> void downFile(Observable<T> observable, ApiObserverProgress<F> observer, final String path);

}
