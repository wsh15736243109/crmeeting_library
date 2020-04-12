package com.cr.pn.baseMvpView.dispatch;

import com.cr.pn.baseMvpView.model.BaseModel;

/**
 * Created by zy on 2018/6/9.
 */

public class MvpDispatch <M extends BaseModel>{

    public volatile static MvpDispatch mvpDispatch;

    private MvpDispatch(){
    }

    @Deprecated
    public static MvpDispatch getInstance(){
        if(mvpDispatch == null){
            synchronized (MvpDispatch.class){
                if (mvpDispatch == null){
                    mvpDispatch = new MvpDispatch();
                }
            }
        }
        return mvpDispatch;
    }

}
