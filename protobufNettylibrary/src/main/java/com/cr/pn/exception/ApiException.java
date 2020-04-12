package com.cr.pn.exception;

/**
 * Created by zy on 2018/7/2.
 * Api错误处理
 */
public class ApiException extends Exception {

    private int code;

    public ApiException(String msg) {
        super(msg);
        this.code = -1;
    }

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}