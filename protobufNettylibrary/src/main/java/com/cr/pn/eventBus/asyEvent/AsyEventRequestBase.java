package com.cr.pn.eventBus.asyEvent;

import com.cr.pn.eventBus.baseEvent.BaseEventBusRealize;
import com.cr.pn.eventBus.baseInterface.BaseReceiveEventBus;

/**
 * Created by zy on 2018/8/21.
 * 异步EventBus消息发送.
 * 消息接收为主线程.
 */
public abstract class AsyEventRequestBase<T extends Object> extends BaseEventBusRealize<T> {

    public BaseReceiveEventBus<T> baseReceiveEventBus;

    public AsyEventRequestBase(BaseReceiveEventBus<T> baseReceiveEventBus){
        this.baseReceiveEventBus = baseReceiveEventBus;
    }

    /**
     * 返回Object.
     *
     * @return
     */
    @Override
    public Object getObject() {
        return this;
    }

    /**
     * 删除粘性事件
     *
     * @param clazz
     * @return
     */
    @Override
    public void removeStickEventClass(Class<?> clazz) {
        super.removeStickEventClass(clazz);
    }
}
