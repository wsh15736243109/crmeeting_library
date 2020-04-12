package com.cr.pn.eventBus.baseInterface;

/**
 * Created by zy on 2018/8/21.
 * 消息接收器.
 */
public interface BaseReceiveEventBus<T extends Object> {

    /**
     * 接收数据.
     * @param t
     */
    public void onReceiveEvent(T t);

    /**
     * 接收粘性数据.
     * @param t
     */
    public void onReceiveStickyEvent(T t);

}
