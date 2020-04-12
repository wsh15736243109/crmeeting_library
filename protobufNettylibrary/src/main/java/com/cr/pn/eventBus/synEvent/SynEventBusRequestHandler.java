package com.cr.pn.eventBus.synEvent;

import com.cr.pn.Utils.reflect.ClassGetTypeToken;
import com.cr.pn.beanBase.BeanBase;
import com.cr.pn.rxjava.apiObserver.RxObserver;

import java.lang.reflect.Type;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zy on 2018/8/21.
 */

public abstract class SynEventBusRequestHandler<T extends BeanBase,F extends BeanBase> {

    private SynEventBusRequest synNettyRequest;

    private T t;

    private Type type;

    private boolean isCancel = false;

    public  SynEventBusRequestHandler(String key,T t){
        synNettyRequest = new SynEventBusRequest();
        type = ClassGetTypeToken.getSuperclassTypeParameter(getClass(),1);
        SynEventKeyManager.getInstance().registerKey(key,type);
        this.t = t;
    }

    public abstract RxObserver<F> backRxObserver(SynEventBusRequestHandler synEventBusRequestHandler);

    public SynEventBusRequestHandler start(){
        Observable.create(new ObservableOnSubscribe<F>() {

            @Override
            public void subscribe(ObservableEmitter<F> emitter) throws Exception {
                F t2 = (F) synNettyRequest.sendAndGet(t);
                if (t2 == t && !isCancel){
                    throw new TimeoutException();
                }else{
                    emitter.onNext(t2);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(backRxObserver(this));
        return this;
    }

    /**
     * 取消本次连接
     */
    public void cancel(){
        isCancel = true;
        synNettyRequest.cancel();
    }

    /**
     * 设置超时时间.
     * @param time
     * @return
     */
    public SynEventBusRequestHandler setOutTime(long time, SynEventBusRequest.OverTime overTime){
        synNettyRequest.setOutTime(time,overTime);
        return this;
    }

    /**
     * 设置超时时间.
     * @param time
     * @return
     */
    public SynEventBusRequestHandler setOutTime(long time){
        return setOutTime(time,null);
    }

}
