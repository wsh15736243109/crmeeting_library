package com.cr.pn.interfaceClass.application;

public interface OnMeetingPushInterface {
    void connectSuccess();

    void disConnect(String reason);

    void connectFail(String msg);

    void responseData(int code, Object msg);

    void sendSuccess();

    void sendFail(String msg);
}
