package com.cr.pn.eventBus.baseInterface;

/**
 * Created by zy on 2018/8/21.
 */
public interface BaseEventBus<T extends Object> {

    /**
     * 注册.
     */
    public void register();

    /**
     * 取消注册.
     */
    public void unregister();

    /**
     * 发送数据.
     * @param o
     */
    public void postEvent(T o);

    /**
     * 发送粘性数据.
     * @param o
     */
    public void postStickyEvent(T o);

    /**
     * 判断是否已经注册.
     * @return
     */
    public boolean isRegister();

    /**
     * 返回Object.
     * @return
     */
    public Object getObject();

    /**
     * 删除粘性事件
     * @return
     */
    public void removeStickEventClass(Class<?> clazz);

}
