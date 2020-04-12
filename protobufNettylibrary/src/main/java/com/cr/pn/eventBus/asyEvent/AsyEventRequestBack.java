package com.cr.pn.eventBus.asyEvent;

import com.cr.pn.eventBus.baseInterface.BaseReceiveEventBus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zy on 2018/8/21.
 * 异步EventBus消息发送.
 * 消息接收为BACKGROUND.
 */
public class AsyEventRequestBack<T extends Object> extends AsyEventRequestBase<T>{

    private BaseReceiveEventBus<T> baseReceiveEventBus;

    public AsyEventRequestBack(BaseReceiveEventBus<T> baseReceiveEventBus){
        super(baseReceiveEventBus);
        this.baseReceiveEventBus = baseReceiveEventBus;
    }

    /**
     * 接收数据.
     *
     * @param t
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    @Override
    public void onReceiveEvent(T t) {
        baseReceiveEventBus.onReceiveEvent(t);
    }

    /**
     * 接收粘性数据.
     *
     * @param t
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND,sticky = true)
    @Override
    public void onReceiveStickyEvent(T t) {
        baseReceiveEventBus.onReceiveStickyEvent(t);
    }

}
