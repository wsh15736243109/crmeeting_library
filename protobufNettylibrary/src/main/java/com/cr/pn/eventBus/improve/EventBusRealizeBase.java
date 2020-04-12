package com.cr.pn.eventBus.improve;

import com.cr.pn.beanBase.BeanBase;
import com.cr.pn.eventBus.baseInterface.BaseEventBus;
import com.cr.pn.eventBus.baseInterface.BaseReceiveEventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2018/8/22.
 * EventBus消息扩展基类，优化消息发送方会接收到自己发送的消息这一问题.
 * 解决发送消息后，粘性和非粘性会同时接收到消息,
 * 将这一机制改为，非粘性方法可以收到粘性和非粘性消息
 * 但粘性消息只能收到粘性消息.
 * 需要注意的是：EventBusRealizeBase只能接收到EventBusRealizeBase发送的消息.
 */
public abstract class EventBusRealizeBase<T extends BeanBase> implements BaseReceiveEventBus<T> ,BaseEventBus<T>{

    public T t;

    private List<Class<?>> clazz =new ArrayList<>();;

    /**
     * 设置消息接收类.
     */
    public BaseReceiveEventBus<T> receiveEventBus;
    
    /**
     * 发送方是否能够接收到消息.
     */
    public boolean isSendMy = false;

    public EventBusRealizeBase(BaseReceiveEventBus<T> receiveEventBus){
        this(receiveEventBus,false);
    }

    /**
     * 设置接收消息的类型,避免类型转换错误.
     * @param clazz
     */
    public void setClazz(Class<?>... clazz) {
        if (clazz != null){
            int size = clazz.length;
            for (int i = 0;i<size;i++){
                this.clazz.add(clazz[i]);
            }
        }
    }

    /**
     * 移除接收消息的类型.
     * @param clazz
     */
    public void removeClazz(Class<?>... clazz){
        if (clazz != null){
            int size = clazz.length;
            for (int i = 0;i<size;i++){
                if (this.clazz.contains(clazz[i])){
                    this.clazz.remove(clazz[i]);
                }
            }
        }
    }

    /**
     *
     * @param receiveEventBus
     * 设置消息接收类
     * @param isSendMy
     * 发送方是否能够接收到消息
     */
    public EventBusRealizeBase(BaseReceiveEventBus<T> receiveEventBus, boolean isSendMy){
        this.receiveEventBus = receiveEventBus;
        this.isSendMy = isSendMy;
    }

    /**
     * 判断当前接收到的事件是否该分发出去。
     * @param t
     * @return
     */
    public boolean IsSendMsg(Object t){

        //进行类型判断，不符合自定义类的消息不进行发送.
        if (clazz != null&&clazz.size()>0){
            if (!clazz.contains(t.getClass())){
                return false;
            }
        }
        if (receiveEventBus!=null){
            if (this.t != t){
                return true;
            }
        }
        return false;
    }

    public class PostEvent extends BeanBase{

        private T t;

        public T getT() {
            return t;
        }

        public PostEvent setT(T t) {
            this.t = t;
            return this;
        }
    }

    public class PostStickyEvent extends BeanBase{

        private T t;

        public T getT() {
            return t;
        }

        public PostStickyEvent setT(T t) {
            this.t = t;
            return this;
        }
    }
}
