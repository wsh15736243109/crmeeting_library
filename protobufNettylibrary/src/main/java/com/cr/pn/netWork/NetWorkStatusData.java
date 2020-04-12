package com.cr.pn.netWork;


import com.cr.pn.beanBase.BeanBase;

/**
 * Created by zy on 2018/8/17.
 */

public class NetWorkStatusData extends BeanBase {

    public static final int netSuccess = -1;

    /**
     * 网络状态:
     * -1:连接成功
     * 其余表示连接次数
     */
    private int status;

    /**
     * 用于某个mvp在tcp连接断开后单独请求重新连接tcp.
     * 适用于检测到有网络请求，但是tcp却断开了连接
     * 此时需要重新启动tcp的情况.
     */
    private String tagMsg;

    public int getStatus() {
        return status;
    }

    public NetWorkStatusData setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getTagMsg() {
        return tagMsg;
    }

    public NetWorkStatusData setTagMsg(String tagMsg) {
        this.tagMsg = tagMsg;
        return this;
    }
}
