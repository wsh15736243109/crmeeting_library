package com.cr.pn.websocket.config;

import com.cr.pn.websocket.callback.MyWebSocketListener;

/**
 * webSocket配置类
 */
public class WebSocketConfig {
    private String IP;//socket连接地址
    private int PORT;//socket连接端口
    private String APP_VERSION;//请求版本号
    private int PLATFORM;//客户端类别
    private String MODEL;//客户端设备型号
    private String personMeetingId;//个人会议id
    private MyWebSocketListener myWebSocketListener;
    private Builder build;

    public Builder getConfigs() {
        return build;
    }

    public void setBuild(Builder build) {
        this.build = build;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public void setAPP_VERSION(String APP_VERSION) {
        this.APP_VERSION = APP_VERSION;
    }

    public void setPLATFORM(int PLATFORM) {
        this.PLATFORM = PLATFORM;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public void setPersonMeetingId(String personMeetingId) {
        this.personMeetingId = personMeetingId;
    }

    public void setMyWebSocketListener(MyWebSocketListener myWebSocketListener) {
        this.myWebSocketListener = myWebSocketListener;
    }



    private WebSocketConfig(Builder builder) {
        this.setBuild(builder);
        this.setIP(builder.getIP());
        this.setPORT(builder.getPORT());
        this.setAPP_VERSION(builder.getAPP_VERSION());
        this.setPLATFORM(builder.getPLATFORM_TYPES());
        this.setMODEL(builder.getMODEL());
        this.setPersonMeetingId(builder.getPersonMeetingId());
    }

    public static final class Builder {
        private String IP;//socket连接地址
        private int PORT;//socket连接端口
        private String APP_VERSION;//请求版本号
        private int PLATFORM_TYPES;//客户端类别
        private String MODEL;//客户端设备型号
        private String personMeetingId;//个人会议id
        private MyWebSocketListener myWebSocketListener;

        public WebSocketConfig.Builder setMyWebSocketListener(MyWebSocketListener myWebSocketListener) {
            this.myWebSocketListener = myWebSocketListener;
            return this;
        }

        public MyWebSocketListener getMyWebSocketListener() {
            return myWebSocketListener;
        }

        public String getIP() {
            return IP;
        }

        public WebSocketConfig.Builder setIP(String IP) {
            this.IP = IP;
            return this;
        }

        public int getPORT() {
            return PORT;
        }

        public WebSocketConfig.Builder setPORT(int PORT) {
            this.PORT = PORT;
            return this;
        }

        public String getAPP_VERSION() {
            return APP_VERSION;
        }

        public WebSocketConfig.Builder setAPP_VERSION(String APP_VERSION) {
            this.APP_VERSION = APP_VERSION;
            return this;
        }

        public int getPLATFORM_TYPES() {
            return PLATFORM_TYPES;
        }

        public WebSocketConfig.Builder setPLATFORM_TYPES(int PLATFORM_TYPES) {
            this.PLATFORM_TYPES = PLATFORM_TYPES;
            return this;
        }

        public String getMODEL() {
            return MODEL;
        }

        public WebSocketConfig.Builder setMODEL(String MODEL) {
            this.MODEL = MODEL;
            return this;
        }

        public WebSocketConfig.Builder setPERSON_MEETING_ID(String personMeetingId) {
            this.personMeetingId = personMeetingId;
            return this;
        }

        public String getPersonMeetingId() {
            return personMeetingId;
        }

        public WebSocketConfig build() {
            return new WebSocketConfig(this);
        }
    }
}
