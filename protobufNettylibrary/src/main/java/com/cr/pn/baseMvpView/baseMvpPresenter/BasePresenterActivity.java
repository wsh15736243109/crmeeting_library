package com.cr.pn.baseMvpView.baseMvpPresenter;

import android.app.Activity;

import com.cr.pn.baseMvpView.model.BaseModel;
import com.cr.pn.handler.NoLeakHandler;

/**
 * Created by zy on 2018/6/9.
 */
public class BasePresenterActivity<V extends Activity,M extends BaseModel> extends BasePresenter<V,M>{

    public NoLeakHandler handler;

    public BasePresenterActivity(V mvpView, M mvpModel) {
        super(mvpView, mvpModel);
    }

    /**
     * 初始化handler.
     */
    public void initHandler(Activity activity){
//        handler = new NoLeakHandler(activity){
//            @Override
//            public void handleMessage(Message msg) {
//                Bundle bundle = msg.getData();
//                bundleReturn(bundle);
//            }
//        };
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
