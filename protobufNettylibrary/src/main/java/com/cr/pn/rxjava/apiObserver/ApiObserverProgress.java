package com.cr.pn.rxjava.apiObserver;


import androidx.collection.SimpleArrayMap;

import com.cr.pn.netWork.http.progressListener.ProgressEvent;
import com.cr.pn.netWork.http.progressListener.ProgressListener;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by zy on 2018/7/6.
 * 网络RxjavaHttp订阅类，包含Progress.
 */

public abstract class ApiObserverProgress<T> extends ApiObserver<T> implements ProgressListener{

    public ApiObserverProgress(){
        this(true);
    }

    public ApiObserverProgress(boolean isOpenWait) {
        super(isOpenWait);
    }

    public ApiObserverProgress(SimpleArrayMap<String, String> map) {
        this(map,true);
    }

    public ApiObserverProgress(SimpleArrayMap<String, String> map, boolean isOpenWait) {
        super(map,isOpenWait);
    }

    @Override
    public void startHttp(long contentLength) {
        handleMsg(new ProgressEvent(-2,contentLength,false,""));
    }

    @Override
    public void onProgress(long bytesRead, long contentLength, boolean done) {
        ProgressEvent progressEvent = new ProgressEvent(bytesRead,contentLength,done,"");
        handleMsg(progressEvent);
    }

    private void handleMsg(final ProgressEvent progressEvent){
        Observable.create(new ObservableOnSubscribe<ProgressEvent>() {
            @Override
            public void subscribe(ObservableEmitter<ProgressEvent> emitter) throws Exception {
                emitter.onNext(progressEvent);
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<ProgressEvent>(false) {
                    @Override
                    public void Error(Throwable e, String msg) {

                    }

                    @Override
                    public void complete(ProgressEvent progressEvent) {
                        if (progressEvent.getBytesRead() == -2){
                            start(progressEvent.getContentLength());
                        }
                        if (!progress(progressEvent.getBytesRead(),progressEvent.getContentLength(),progressEvent.isDone())){
                            if (!ApiObserverProgress.this.disposable.isDisposed()){
                                ApiObserverProgress.this.disposable.dispose();
                                ApiObserverProgress.this.cancel();
                            }
                        }
                    }
                });
    }

    @Override
    public void onComplete() {
        super.onComplete();
    }

    /**
     * 开始访问
     * @param contentLength
     * 本次http访问获取的数据大小
     */
    public abstract void start(long contentLength);

    /**
     * 访问进度.
     * @param bytesRead
     * @param contentLength
     * @param done
     * @return
     * 是否继续下载，可用于断点续传等功能
     */
    public abstract boolean progress(long bytesRead, long contentLength, boolean done);

}
