package com.cr.pn.baseMvpView.baseMvpPresenter;

import android.app.Service;
import android.os.Bundle;
import android.os.Message;

import com.cr.pn.baseMvpView.model.BaseModel;
import com.cr.pn.handler.NoLeakHandler;

/**
 * Created by zy on 2018/6/9.
 */
public class BasePresenterService<V extends Service,M extends BaseModel> extends BasePresenter<V,M>{

    public NoLeakHandler handler;

    public BasePresenterService(V mvpView, M mvpModel) {
        super(mvpView, mvpModel);
    }

    /**
     * 初始化handler.
     */
    public void initHandler(Service service){
        handler = new NoLeakHandler(service){
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                bundleReturn(bundle);
            }
        };
    }

    /**
     * 返回view层
     *
     * @return
     */
    @Override
    public V getView() {
        return super.getView();
    }

    /**
     * 销毁Presenter.
     */
    @Override
    public void destroy() {
        super.destroy();
        if (handler != null){
            handler = null;
        }
    }
}
