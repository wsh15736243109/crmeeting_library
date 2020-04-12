package com.cr.pn.baseMvpView.model.realizationModel;

import com.cr.pn.baseMvpView.baseMvpPresenter.BasePresenter;
import com.cr.pn.baseMvpView.model.BaseModel;
import com.cr.pn.netWork.http.HttpServiceHelper.HttpServiceHelper;
import com.cr.pn.netWork.http.retrofitInit.RetrofitInit;

/**
 * Created by zy on 2018/6/15.
 */

public class RealizationModel<P extends BasePresenter> implements BaseModel{

    private P basePresenterWeakReference;

    /**
     * 设置BasePresenter.
     * @param p
     */
    public void setMyBasePresenter(P p){
        basePresenterWeakReference = p;
    }

    /**
     * 返回一个Http接口操作实例.
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> HttpServiceHelper<T> getHttpInterface(Class<T> clazz){
       return RetrofitInit.getInstance().createService(clazz);
    }

    /**
     * 返回BasePresenter实例.
     * @return
     */
    public P getBasePresenter() {
        return basePresenterWeakReference;
    }

    @Override
    public void destroy() {
        basePresenterWeakReference = null;
    }
}
