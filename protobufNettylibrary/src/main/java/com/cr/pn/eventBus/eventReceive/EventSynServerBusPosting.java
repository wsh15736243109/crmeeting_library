package com.cr.pn.eventBus.eventReceive;

import com.cr.pn.Utils.Json.JsonUtils;
import com.cr.pn.beanBase.BeanBase;
import com.cr.pn.eventBus.baseInterface.BaseReceiveEventBus;
import com.cr.pn.eventBus.improve.EventBusRealizeRANDS;
import com.cr.pn.eventBus.synEvent.SynEventKeyManager;
import com.cr.pn.netWork.http.baseEntity.BaseEntity;

import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zy on 2018/8/23.
 * 消息发送器。当一个业务需要为多个类返回消息，并且消息需要有key值对应时，可以使用此类.
 */
public class EventSynServerBusPosting<T extends BeanBase> implements BaseReceiveEventBus<T> {

    private EventBusRealizeRANDS<T> eventBusRealizeBase;

    private BaseReceiveEventBus<T> receiveEventBus;

    public EventSynServerBusPosting(BaseReceiveEventBus<T> receiveEventBus){
        this.receiveEventBus = receiveEventBus;
        eventBusRealizeBase = new EventBusRealizeRANDS<T>(ThreadMode.BACKGROUND,this);
    }

    public void setClass(Class<?>... classes){
        eventBusRealizeBase.setClass(classes);
    }

    /**
     * netty返回数据保存数据，进行数据分发.
     * @param msg
     */
    public void setWriteMsg(String msg){
        SynEventKeyManager.TypeAndKey typeAndKey = SynEventKeyManager.getInstance().whileType(msg);
        if (typeAndKey != null){
            SynEventKeyManager.getInstance().removeEvent(typeAndKey.getKey());
            T t = JsonUtils.toObjectType(msg,typeAndKey.getType());
            eventBusRealizeBase.postEvent((T) typeAndKey.setObject(t));
        }else {
            //如果未找到key,则获取长存消息里面的key值
            typeAndKey = SynEventKeyManager.getInstance().whileTypeForever(msg);
            if (typeAndKey != null){
                SynEventKeyManager.getInstance().removeEvent(typeAndKey.getKey());
                T t = JsonUtils.toObjectType(msg,typeAndKey.getType());
                eventBusRealizeBase.postEvent((T) t);
            }else {
                //若长存消息里面没有，则直接转换为BaseEntity
                BaseEntity<String> baseEntity = new BaseEntity<>();
                baseEntity.setData(msg);
                eventBusRealizeBase.postEvent((T) baseEntity);
            }
        }
    }

    /**
     * 接收数据.
     */
    @Override
    public void onReceiveEvent(T t) {
        if (t != null){
            receiveEventBus.onReceiveEvent(t);
        }
    }

    /**
     * 接收粘性数据.
     */
    @Override
    public void onReceiveStickyEvent(T t) {
        if (t != null){
            receiveEventBus.onReceiveStickyEvent(t);
        }
    }

    public void unregister(){
        eventBusRealizeBase.unregister();
    }

    public void removeStickEventClass(Class<?> clazz){
        eventBusRealizeBase.removeStickEventClass(clazz);
    }
}
