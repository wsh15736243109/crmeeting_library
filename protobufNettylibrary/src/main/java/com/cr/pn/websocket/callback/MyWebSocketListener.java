package com.cr.pn.websocket.callback;

public interface MyWebSocketListener {
    void conenctSuccess(Object data);
    void connnectFail(String msg);
    void resonponseData(Object data);
    void sendFail(String msg);
}
