package com.cr.pn.netWork.netty.base.businessHandler;

import com.cr.pn.Utils.runable.StandardThreadExecutor;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 业务线程实现
 * 每个连接都应创建一个BusinessHandler类
 * 用于实现业务代码
 * 如果该类不能满足具体需求，可自行根据该类的父类进行相应扩展.
 * @param
 */
public class BusinessHandler extends BusinessEvent{

    /**
     * 保存需要实行的业务线程命令.
     */
    private Queue<BusinessEvent> linkedList;

    private StandardThreadExecutor standardThreadExecutor;

    private boolean linkedListExecute = true;

    private Object object = new Object();

    public BusinessHandler(int coreThread, int maxThreads){
        linkedList = new LinkedBlockingQueue<>();
        standardThreadExecutor = new StandardThreadExecutor(coreThread,maxThreads);
        standardThreadExecutor.execute(this);
    }

    /**
     * 加入任务列队执行.
     * 列队里面所有任务公用一个线程.
     * 不耗费时间的任务可放入列队线程
     */
    public boolean handMsg(BusinessEvent t){
        synchronized (object){
            boolean success = false;
            try {
                success = linkedList.add(t);
            }catch (IllegalStateException e){
                success = false;
            }finally {
                object.notify();
                return success;
            }
        }
    }

    /**
     * 直接执行，不加入列队.
     */
    public boolean execute(BusinessEvent event){
        return standardThreadExecutor.executeRunnable(event);
    }

    @Override
    public void Event() {
        while(linkedListExecute){
            synchronized (object){
                if (linkedList.isEmpty()){
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                BusinessEvent businessEvent = linkedList.poll();
                businessEvent.Event();
            }
        }
    }

    /**
     * 销毁改业务操作.
     */
    public void destroy(){
        linkedListExecute  = false;
        standardThreadExecutor.shutdown();
        standardThreadExecutor = null;
        if (!linkedList.isEmpty()){
            linkedList.remove();
        }
        linkedList = null;
    }

}
