package com.cr.pn.UIUtils.userOperation.PressTFinish;

import com.cr.pn.UIUtils.ToastUtil.ToastUtil;
import com.cr.pn.application.BaseApplication;

/**
 * Created by zy on 2018/7/31.
 * 按下两次返回退出
 */
public class PreTFinish {

    private long interval = 0;

    private long firstTime = -1;

    private String msg = "再按一次退出";

    public PreTFinish(long interval){
        this.interval = interval;
    }

    private boolean setFirstTime(long firstTime){
        this.firstTime = firstTime;
        ToastUtil.showToast(BaseApplication.sApplicationContext,msg);
        return false;
    }

    private boolean compareTime(long secondTime){
        if (secondTime-firstTime<interval){
            return true;
        }else {
            return setFirstTime(secondTime);
        }
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean pressDown(long time){
        return compareTime(time);
    }

}
