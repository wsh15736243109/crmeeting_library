package com.cr.pn.eventBus.baseEvent;

import com.cr.pn.eventBus.baseInterface.BaseEventBus;
import com.cr.pn.eventBus.baseInterface.BaseReceiveEventBus;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zy on 2018/8/21.
 * EventBus操作基类.
 */

public abstract class BaseEventBusRealize<T extends Object> implements BaseEventBus<T>,BaseReceiveEventBus<T> {

    /**
     * 注册.
     */
    @Override
    public void register() {
        if (!isRegister()){
            EventBus.getDefault().register(getObject());
        }
    }

    /**
     * 取消注册.
     */
    @Override
    public void unregister() {
        if(isRegister()){
            EventBus.getDefault().unregister(getObject());
        }
    }

    /**
     * 删除粘性事件
     *
     * @return
     */
    @Override
    public void removeStickEventClass(Class<?> clazz) {
        EventBus.getDefault().removeStickyEvent(clazz);
    }

    /**
     * 发送数据.
     *
     * @param t
     */
    @Override
    public void postEvent(T t) {
        EventBus.getDefault().post(t);
    }

    /**
     * 发送粘性数据.
     *
     * @param t
     */
    @Override
    public void postStickyEvent(T t) {
        EventBus.getDefault().postSticky(t);
    }

    /**
     * 判断是否已经注册.
     *
     * @return
     */
    @Override
    public boolean isRegister() {
        return EventBus.getDefault().isRegistered(getObject());
    }

}
