package com.cr.pn.eventBus.synEvent;

import com.cr.pn.beanBase.BeanBase;
import com.cr.pn.eventBus.baseInterface.BaseReceiveEventBus;
import com.cr.pn.eventBus.improve.EventBusRealizeRANDS;
import com.cr.pn.rxjava.apiObserver.RxObserver;

import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by zy on 2018/8/21.
 * 同步EventBus消息发送和接收.
 */

public class SynEventBusRequest<T extends BeanBase> implements BaseReceiveEventBus<T> {

    private T t = null;

    private BackData backData;

    private RxObserver outTime;

    private EventBusRealizeRANDS<T> eventBusRealizePosting;

    /**
     * 设置超时时间.
     */
    private long outTimeMilli = 5000;

    /**
     * 是否取消.
     */
    private boolean isCancel = false;

    /**
     * 连接超时回调.
     */
    private OverTime overTime;

    public SynEventBusRequest(){
        eventBusRealizePosting = new EventBusRealizeRANDS<>(ThreadMode.BACKGROUND,this);
        eventBusRealizePosting.setClass(SynEventKeyManager.TypeAndKey.class);
        backData = new BackData() {

            @Override
            public <T extends BeanBase> T backData() {
                while(!isCancel){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                eventBusRealizePosting.unregister();
                return (T) t;
            }
        };
        outTime = new RxObserver<Long>(false) {

            @Override
            public void Error(Throwable e, String msg) {

            }

            @Override
            public void complete(Long integer) {
                if (overTime != null){
                    overTime.overTime();
                }else {
                    timeOverCancel();
                }
            }

            @Override
            public void next(Long integer) throws Exception {

            }
        };
    }

    /**
     * 发送接收数据.
     * @return
     */
    public T sendAndGet(T t){
        startCountDown();
        this.t = t;
        eventBusRealizePosting.postEvent(t);
        this.t = backData.backData();
        return this.t;
    }

    /**
     * 取消本次连接
     */
    public void cancel(){
        if (!isCancel){
            isCancel = true;
            unregister();
        }
    }

    public void timeOverCancel(){
        isCancel = true;
        unregister();
    }

    public void unregister(){
        eventBusRealizePosting.unregister();
    }

    /**
     * 设置超时时间.
     * @param time
     * @return
     */
    public SynEventBusRequest setOutTime(long time, OverTime overTime){
        this.outTimeMilli = time;
        this.overTime = overTime;
        return this;
    }

    private void startCountDown(){
        Observable.interval(0, 1, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long increaseTime) throws Exception {
                        return outTimeMilli - increaseTime;
                    }
                })
                .take(outTimeMilli)
                .subscribe(outTime);
    }

    /**
     * 接收数据.
     *
     * @param t
     */
    @Override
    public void onReceiveEvent(T t) {
        if (t instanceof SynEventKeyManager.TypeAndKey){
            SynEventKeyManager.TypeAndKey typeAndKey = (SynEventKeyManager.TypeAndKey) t;
            this.t = (T) typeAndKey.getObject();
            outTime.disposable.dispose();
            isCancel = true;
        }

    }

    /**
     * 接收粘性数据.
     * 同步消息不考虑接收粘性消息，
     * 避免当前消息和粘性消息冲突.
     * @param t
     */
    @Override
    public void onReceiveStickyEvent(T t) {

    }

    public interface BackData{
        public <T extends BeanBase> T backData();
    }

    public interface OverTime{
        public void overTime();
    }
}
