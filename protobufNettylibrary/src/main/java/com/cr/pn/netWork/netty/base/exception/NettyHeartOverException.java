package com.cr.pn.netWork.netty.base.exception;

public class NettyHeartOverException extends NettyException {

    /**
     * 心跳超时.
     */
    public static final String HEART_TESTING_OVERTIME = "心跳超时";

    public NettyHeartOverException() {
        super(HEART_TESTING_OVERTIME);
    }
}
