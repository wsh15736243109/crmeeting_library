package com.cr.pn.eventBus.improve;

import com.cr.pn.beanBase.BeanBase;
import com.cr.pn.eventBus.asyEvent.AsyEventRequestAsync;
import com.cr.pn.eventBus.baseInterface.BaseReceiveEventBus;

/**
 * Created by zy on 2018/8/22.
 * EventBus消息扩展，优化消息发送方会接收到自己发送的消息这一问题.
 * 解决发送消息后，粘性和非粘性会同时接收到消息,
 * 将这一机制改为，非粘性方法可以收到粘性和非粘性消息
 * 但粘性消息只能收到粘性消息.
 * 消息接收为新线程.
 */
public class EventBusRealizeAsync<T extends BeanBase> extends EventBusRealizeBase<T> {

    private AsyEventRequestAsync<T> asyEventRequest;

    public EventBusRealizeAsync(BaseReceiveEventBus<T> receiveEventBus){
        this(receiveEventBus,false);
    }

    /**
     *
     * @param receiveEventBus
     * 设置消息接收类
     * @param isSendMy
     * 发送方是否能够接收到消息
     */
    public EventBusRealizeAsync(BaseReceiveEventBus<T> receiveEventBus, boolean isSendMy){
        super(receiveEventBus,isSendMy);
        asyEventRequest = new AsyEventRequestAsync<>(this);
        register();
    }

    /**
     * 接收数据.
     *@param t
     */
    @Override
    public void onReceiveEvent(T t) {
        if (t.getClass() == PostEvent.class){
            Object t1 =((PostEvent)t).getT();
            if (IsSendMsg(t1)){
                receiveEventBus.onReceiveEvent((T) t1);
            }
        }
        if (t.getClass() == PostStickyEvent.class){
            Object t1 =((PostEvent)t).getT();
            if (IsSendMsg(t1)){
                receiveEventBus.onReceiveEvent((T) t1);
            }
        }
    }

    /**
     * 接收粘性数据.
     *
     * @param t
     */
    @Override
    public void onReceiveStickyEvent(T t) {
        if (t.getClass() == PostStickyEvent.class){
            Object t1 =((PostEvent)t).getT();
            if (IsSendMsg(t1)){
                receiveEventBus.onReceiveStickyEvent((T) t1);
            }
        }
    }

    /**
     * 发送数据.
     *
     * @param o
     */
    @Override
    public void postEvent(T o) {
        if (isSendMy){
            asyEventRequest.postEvent((T) new PostEvent().setT(o));
            return;
        }
        t = o;
        asyEventRequest.postEvent((T) new PostEvent().setT(t));
    }

    /**
     * 发送粘性数据.
     *
     * @param o
     */
    @Override
    public void postStickyEvent(T o) {
        if (isSendMy){
            asyEventRequest.onReceiveStickyEvent((T) new PostStickyEvent().setT(o));
            return;
        }
        t = o;
        asyEventRequest.onReceiveStickyEvent((T) new PostStickyEvent().setT(t));
    }


    /**
     * 注册.
     */
    @Override
    public void register() {
        asyEventRequest.register();
    }

    /**
     * 取消注册.
     */
    @Override
    public void unregister() {
        asyEventRequest.unregister();
    }

    /**
     * 判断是否已经注册.
     *
     * @return
     */
    @Override
    public boolean isRegister() {
        return asyEventRequest.isRegister();
    }

    /**
     * 返回Object.
     *
     * @return
     */
    @Override
    public Object getObject() {
        return asyEventRequest.getObject();
    }

    /**
     * 删除粘性事件
     *
     * @return
     */
    @Override
    public void removeStickEventClass(Class<?> clazz) {
        asyEventRequest.removeStickEventClass(clazz);
    }
}
