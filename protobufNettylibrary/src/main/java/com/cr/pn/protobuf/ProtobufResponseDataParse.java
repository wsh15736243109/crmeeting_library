package com.cr.pn.protobuf;

import com.cr.meetingpush.MessagePushEntity;
import com.cr.meetingpush.MessagePushEntity.PushMessage.UserAccept.UserAcceptType;
import com.cr.pn.websocket.callback.MyWebSocketListener;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;

import java.util.ArrayList;

import okio.ByteString;

import static com.cr.meetingpush.MessagePushEntity.PushMessage.FeaturesMessage.MessageType.CreateChatRoom;
import static com.cr.meetingpush.MessagePushEntity.PushMessage.FeaturesMessage.MessageType.EndChatRoom;
import static com.cr.meetingpush.MessagePushEntity.PushMessage.FeaturesMessage.MessageType.ReceivingCalls;
import static com.cr.meetingpush.MessagePushEntity.PushMessage.FeaturesMessage.MessageType.RefuseToCall;
import static com.cr.meetingpush.MessagePushEntity.PushMessage.UserAccept.UserAcceptType.Alter_VALUE;
import static com.cr.pn.Utils.Json.JsonUtils.getAllInvitePeople;

public class ProtobufResponseDataParse {

    private static ProtobufResponseDataParse instance;

    public static ProtobufResponseDataParse getInstance() {
        if (instance == null) {
            instance = new ProtobufResponseDataParse();
        }
        return instance;
    }


    public void parseData(ByteString bytes, MyWebSocketListener myWebSocketListener) {
        try {
            MessagePushEntity.PushMessage pushMessage = MessagePushEntity.PushMessage.parseFrom(bytes.toByteArray());
            if (pushMessage != null) {
                if (pushMessage.getUserAccept() != null) {
                    switch (pushMessage.getUserAccept().getUserAcceptTypeValue()) {
                        case UserAcceptType.Features_VALUE://功能消息
                            if (pushMessage.getUserAccept().getFeaturesMessage() != null) {
                                MessagePushEntity.PushMessage.FeaturesMessage featuresMessage = pushMessage.getUserAccept().getFeaturesMessage();
                                switch (featuresMessage.getMessageType()) {
                                    case CreateChatRoom://创建房间
                                        //
                                        break;
                                    case EndChatRoom://结束聊天室
                                        break;
                                    case ReceivingCalls://接听电话
                                        //拿到房间号 拿到发送邀请的人的昵称、头像等信息
                                        if (featuresMessage.getCreateChatRoomMessage() != null) {
                                            featuresMessage.getCreateChatRoomMessage().getChatRoomId();
                                        }
                                        break;
                                    case RefuseToCall://拒绝接听电话
                                        break;
                                    case InTheCallInvite://在房间中邀请好友
                                        break;
                                    case DetermineCreateChatRoom://通知发送人可以召开房间了
                                        break;
                                }
                            }
                            break;
                        case UserAcceptType.Alter_VALUE://Alter消息
                            break;
                        case UserAcceptType.Popup_VALUE://确认弹框
                            break;
                        case UserAcceptType.SelectPopup_VALUE://选择框
                            break;
                        case UserAcceptType.MessagePush_VALUE://消息推送（可能电话来了）
                            break;
                        case UserAcceptType.Command_VALUE://内部指令, 一般系统或者管理员发出，界面不会有任何反馈
                            break;
                    }
                }
            }

        } catch (InvalidProtocolBufferException e) {
            myWebSocketListener.resonponseData("错误的数据类型");
        }
    }
}
