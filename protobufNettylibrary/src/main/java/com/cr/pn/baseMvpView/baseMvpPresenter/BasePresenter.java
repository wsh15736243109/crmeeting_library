package com.cr.pn.baseMvpView.baseMvpPresenter;

import android.os.Bundle;

import com.cr.pn.baseMvpView.model.BaseModel;

/**
 * Created by zy on 2018/6/9.
 */
public class BasePresenter<V,M extends BaseModel> {

    private V mvpView;

    private M mvpModel;


    public BasePresenter(V mvpView,M mvpModel){
        this.mvpView = mvpView;
        this.mvpModel = mvpModel;
    }

    /**
     * 返回view层
     * @return
     */
    public V getView(){
        return mvpView;
    }

    /**
     * 返回model层.
     * @return
     */
    public M getModel(){
        return mvpModel;
    }

    /**
     * 线程Handler返回.
     * @param bundle
     */
    public void bundleReturn(Bundle bundle){

    }

    /**
     * model层返回数据.
     * @param data
     * @param target
     * 标记
     */
    public void modelBackString(int target,String data){

    }

    /**
     * 其余层返回数据.
     * @param t
     * @param target
     * 标记
     */
    public <T> void otherBackData(String target, T t){

    }

    /**
     * 判断model是否为空.
     * @return
     */
    public boolean modelNull(){
        return mvpModel == null;
    }

    /**
     * 判断view是否为空.
     * @return
     */
    public boolean viewNull(){
        return mvpView == null;
    }

    /**
     * 销毁BasePresenter.
     */
    public void destroy(){
        if (mvpView != null){
            mvpView = null;
        }
        if (mvpModel != null){
            mvpModel.destroy();
            mvpModel = null;
        }
    }

}
