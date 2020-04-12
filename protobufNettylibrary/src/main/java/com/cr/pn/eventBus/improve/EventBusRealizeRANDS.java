package com.cr.pn.eventBus.improve;

import com.cr.pn.beanBase.BeanBase;
import com.cr.pn.eventBus.baseInterface.BaseReceiveEventBus;

import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zy on 2018/8/23.
 * 包含优化的接收和发送类.
 */
public class EventBusRealizeRANDS<T extends BeanBase>{

    private EventBusRealizeBase<T> receiveBus;

    public EventBusRealizeRANDS(ThreadMode mode, BaseReceiveEventBus<T> receive){
        switch (mode){
            case MAIN:
                receiveBus = new EventBusRealizeMain<T>(receive);
                break;
            case POSTING:
                receiveBus = new EventBusRealizePosting<T>(receive);
                break;
            case ASYNC:
                receiveBus = new EventBusRealizeAsync<T>(receive);
                break;
            case BACKGROUND:
                receiveBus = new EventBusRealizeBack<T>(receive);
                break;
        }
    }

    /**
     * 接触接收消息的类型.
     * @param clazz
     */
    public void setClass(Class<?>... clazz){
        receiveBus.setClazz(clazz);
    }

    /**
     * 接触接收消息的类型.
     * @param clazz
     */
    public void removeClazz(Class<?>... clazz){
        receiveBus.removeClazz(clazz);
    }

    /**
     * 发送数据.
     *
     * @param o
     */
    public void postEvent(T o) {
        receiveBus.postEvent(o);
    }

    /**
     * 发送粘性数据.
     *
     * @param o
     */
    public void postStickyEvent(T o) {
        receiveBus.postStickyEvent(o);
    }

    public void unregister(){
        receiveBus.unregister();
    }

    public void removeStickEventClass(Class<?> clazz){
        receiveBus.removeStickEventClass(clazz);
    }


}
