package com.cr.pn.rxjava.apiObserver;

import com.cr.pn.UIUtils.AlertDialog.AlertDialogWait;
import com.cr.pn.activityManager.ActivityManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zy on 2018/6/9.
 */

public abstract class RxObserver<T> implements Observer<T>{

    public Disposable disposable;

    public T t;

    /**
     * 是否开启等带框，默认开启.
     */
    public boolean isOpenWait = true;

    private AlertDialogWait alertDialogWait;

    public RxObserver(){
        this(false);
    }

    public RxObserver(boolean isOpenWait){
        this.isOpenWait = isOpenWait;
        if (isOpenWait){
            if(ActivityManager.getInstance().currentActivity()!=null){
                alertDialogWait = new AlertDialogWait(ActivityManager.getInstance().currentActivity());
                alertDialogWait.setDismissListener(new AlertDialogWait.dismissListener() {
                    @Override
                    public void dismiss() {
                        alertDialogWait = null;
                        alertDialogDismiss();
                    }
                });
            }
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (isOpenWait&&alertDialogWait!=null){
            alertDialogWait.showWait("");
        }
        disposable = d;
    }

    @Override
    public void onComplete() {
        try {
            complete(t);
        }catch (Exception e){
            e.printStackTrace();
        }
        destroy();
    }

    public void alertDialogDismiss(){}

    public void cancel(){
        destroy();
        cancelCompete();
    }

    /**
     * 只要链式调用中抛出了异常都会走这个回调
     */
    @Override
    public void onError(Throwable e) {
        destroy();
    }

    @Override
    public void onNext(T t) {
        this.t = t;
        try {
            next(t);
        }catch (Exception e) {
            onError(e);
        }
    }

    /**
     * 任务结束取消订阅.
     */
    public void destroy(){
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        if (isOpenWait&&alertDialogWait!=null){
            try {
                alertDialogWait.dismiss();
            }catch (Exception e){

            }
        }
    }

    /**
     * 返回错误信息.
     * @param e
     * @param msg
     * @return
     */
    public abstract void Error(Throwable e,String msg);

    /**
     * 访问完成
     */
    public abstract void complete(T t);

    public void next(T t) throws Exception{}

    /**
     * 任务取消完成
     */
    public void cancelCompete(){}

}
