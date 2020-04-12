package com.cr.pn.netWork.netty.base.exception;

public class NettyException extends Exception {

    public NettyException() {
        super();
    }

    public NettyException(String message) {
        super(message);
    }

    public NettyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NettyException(Throwable cause) {
        super(cause);
    }

    protected NettyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
