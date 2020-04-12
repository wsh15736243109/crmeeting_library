package com.cr.pn.netWork.netty.base.businessHandler;

/**
 * 业务线程具体实现父类，
 * 所有业务类都应使用该类实现相应业务逻辑.
 * @param
 */
public abstract class BusinessEvent implements Runnable{

    public abstract void Event();

    @Override
    public void run() {
        Event();
    }

}
