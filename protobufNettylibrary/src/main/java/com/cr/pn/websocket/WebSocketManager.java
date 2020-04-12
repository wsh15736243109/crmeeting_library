package com.cr.pn.websocket;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.cr.meetingpush.MessagePushEntity;
import com.cr.pn.protobuf.ProtobufResponseDataParse;
import com.cr.pn.protobuf.ProtobufSendParamsBuild;
import com.cr.pn.websocket.config.WebSocketConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketManager {

    private long sendTime = 0L;
    private static final long HEART_BEAT_RATE = 2 * 1000; // 每隔2秒发送一次心跳包，检测连接没有断开
    private Handler mHandler = new Handler();//心跳检测
    private WebSocket mSocket;
    private final String TAG = "WebSocketManager";
    private WebSocketConfig webSocketConfig;
    private WebSocketConfig.Builder webSocketConfigs;

    public void setWebSocketConfig(WebSocketConfig webSocketConfig) {
        this.webSocketConfig = webSocketConfig;
        webSocketConfigs = webSocketConfig.getConfigs();
        setExtraData(webSocketConfigs);
    }

    public static WebSocketManager instance;

    public static WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    /**
     * 连接socket
     */
    public void connect() {
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(3, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        //获取配置项
        webSocketConfigs = webSocketConfig.getConfigs();
        if (webSocketConfigs == null) {
            throw new RuntimeException("请先配置WebSocketConfig项");
        }
        if (webSocketConfigs.getMyWebSocketListener() == null) {
            throw new RuntimeException("请先设置数据回调监听");
        }
        if (webSocketConfigs.getIP().isEmpty() || webSocketConfigs.getPORT() == 0) {
            webSocketConfigs.getMyWebSocketListener().connnectFail("IP地址或端口号有误");
            return;
        }
        if (webSocketConfigs.getAPP_VERSION().isEmpty() || webSocketConfigs.getPersonMeetingId().isEmpty()) {
            webSocketConfigs.getMyWebSocketListener().connnectFail("APP版本号和个人会议ID不能为空");
            return;
        }
//        setExtraData(webSocketConfigs);
        //
        Request request = new Request.Builder().url("http://" + webSocketConfigs.getIP() + ":" + webSocketConfig.getConfigs().getPORT() + "/").build();
        //
        EchoWebSocketListener socketListener = new EchoWebSocketListener();
        //
        mOkHttpClient.newWebSocket(request, socketListener);
        //  mOkHttpClient.dispatcher().executorService().shutdown();
        //
        postHeart();
    }

    private void setExtraData(WebSocketConfig.Builder webSocketConfigs) {
        //设置额外参数
        ProtobufSendParamsBuild.getInstance().setExtra(webSocketConfigs.getAPP_VERSION(), webSocketConfigs.getMODEL(), webSocketConfigs.getPLATFORM_TYPES(), webSocketConfigs.getPersonMeetingId());
    }

    /**
     * 心跳检测
     */
    private void postHeart() {
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
    }

    /**
     * 创建房间
     *
     * @param personMeetingId
     * @param invitePeopleListJson
     */
    public void createChatRoom(String personMeetingId, String invitePeopleListJson, String headUrl, String nickname) {
        sendData(ProtobufSendParamsBuild.getInstance().buildCreateChatRoom(personMeetingId, invitePeopleListJson, headUrl, nickname));
    }

    /**
     * 结束聊天室
     */
    public void endChatRoom(String roomId) {
        sendData(ProtobufSendParamsBuild.getInstance().buildEndChatRoom());
    }

    /**
     * 接听电话
     *
     * @param roomId
     */
    public void receivingCalls(String roomId) {
        sendData(ProtobufSendParamsBuild.getInstance().buildReceivingCalls(roomId));
    }

    /**
     * 拒绝接听
     *
     * @param roomId
     */
    public void refuseToCall(String roomId) {
        sendData(ProtobufSendParamsBuild.getInstance().buildRefuseToCall(roomId));
    }

    /**
     * 发送心跳信息
     */
    public void sendHeart() {
        sendData(ProtobufSendParamsBuild.getInstance().buildHeartMessage());
    }


    private void changeUuid() {
        sendData(ProtobufSendParamsBuild.getInstance().buildChangeUuid());
    }

    // 发送心跳包
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                sendHeart();
                sendTime = System.currentTimeMillis();
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE); //每隔一定的时间，对长连接进行一次心跳检测
        }
    };

    private void sendData(MessagePushEntity.PushMessage pushMessage) {
        String data = pushMessage.toByteString().toStringUtf8();
//        com.google.protobuf.ByteString data = pushMessage.toByteString();
        Log.e(TAG, ">>>>>>>>send socket data:" + data);
        if (mSocket != null) {
            mSocket.send(data);
        } else {
            if (webSocketConfigs == null) {
                return;
            }
            webSocketConfigs.getMyWebSocketListener().sendFail("Socket connect fail");
        }
    }


    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            mSocket = webSocket;
            Log.e(TAG, "连接成功！" + response);    //连接成功后，发送登录信息
            webSocketConfigs.getMyWebSocketListener().conenctSuccess(response);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            ProtobufResponseDataParse.getInstance().parseData(bytes,webSocketConfigs.getMyWebSocketListener());
//            try {
//                MessagePushEntity.PushMessage pushMessage = MessagePushEntity.PushMessage.parseFrom(bytes.toByteArray());
//                if (pushMessage.getUserAccept().getCommandMessage().getDetailedType() == MessagePushEntity.PushMessage.UserAccept.CommandMessage.DetailedType.ConnectSucceed) {
//                    //首次连接成功  需要重新设置uuid
//                }
//            } catch (InvalidProtocolBufferException e) {
//                e.printStackTrace();
//            }
            webSocketConfigs.getMyWebSocketListener().resonponseData(bytes);
            Log.e(TAG, "receive bytes:" + bytes.utf8());
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.e(TAG, "服务器端发送来的信息：" + text);
            webSocketConfigs.getMyWebSocketListener().resonponseData(text);
            //具体可以根据自己实际情况断开连接，比如点击返回键页面关闭时，执行下边逻辑
            if (!TextUtils.isEmpty(text)) {


            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.e(TAG, "closed:" + reason);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Log.e(TAG, "closing:" + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.e(TAG, "failure:" + t.getMessage() + response);
        }
    }


    public void disconnect() {
        //心跳销毁
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        //关闭socket
        if (mSocket != null) {
            mSocket.close(1000, null);
            mSocket = null;
        }
    }

}


