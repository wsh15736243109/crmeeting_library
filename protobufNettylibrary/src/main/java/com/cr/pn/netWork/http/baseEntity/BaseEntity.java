package com.cr.pn.netWork.http.baseEntity;

import com.cr.pn.beanBase.BeanBase;

/**
 * Created by zy on 2018/7/2.
 */
public class BaseEntity<T> extends BeanBase {

    /**
     * 返回标识.
     */
    private int status_code;

    /**
     * 错误信息.
     */
    private String error_msg;

    /**
     * 返回信息.
     */
    private String msg;

    /**
     * 返回页数.
     */
    private int page = 0;

    /**
     * 每页多少行.
     */
    private int cum = 10;

    /**
     * 转换的实体类
     */
    private T data;

    private String tagMsg;

    public String getTagMsg() {
        return tagMsg;
    }

    public void setTagMsg(String tagMsg) {
        this.tagMsg = tagMsg;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCum() {
        return cum;
    }

    public void setCum(int cum) {
        this.cum = cum;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

}
