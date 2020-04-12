package com.cr.pn.protobuf;

import com.cr.meetingpush.MessagePushEntity;
import com.google.protobuf.Timestamp;

import java.util.ArrayList;

import static com.cr.meetingpush.MessagePushEntity.PushMessage.FeaturesMessage.MessageType.CreateChatRoom;
import static com.cr.meetingpush.MessagePushEntity.PushMessage.FeaturesMessage.MessageType.EndChatRoom;
import static com.cr.meetingpush.MessagePushEntity.PushMessage.FeaturesMessage.MessageType.ReceivingCalls;
import static com.cr.meetingpush.MessagePushEntity.PushMessage.FeaturesMessage.MessageType.RefuseToCall;
import static com.cr.pn.Utils.Json.JsonUtils.getAllInvitePeople;

public class ProtobufSendParamsBuild {

    /**
     * 公共参数
     */
    long commandTime = 0;//命令发送时间

    String appVersion = "";//app版本号
    String model = "";//终端型号信息
    int platform = 1;//终端类型   //Android = 1; Ios = 2; Web = 3; AndroidBox = 4;
    String uuid;//个人会议ID

    String messageId = "";//消息编码 //uuid+时间戳

    private static ProtobufSendParamsBuild instance;

    public static ProtobufSendParamsBuild getInstance() {
        if (instance == null) {
            instance = new ProtobufSendParamsBuild();
        }
        return instance;
    }

    public void setExtra(String appVersion, String model, int platform, String uuid) {
        this.appVersion = appVersion;
        this.model = model;
        this.platform = platform;
        this.uuid = uuid;
    }

    /**
     * 召开聊天室
     * @param roomId
     * @param invitePeopleListJson
     * @param headUrl
     * @param nickname
     * @return
     */
    public MessagePushEntity.PushMessage buildCreateChatRoom(String roomId, String invitePeopleListJson, String headUrl, String nickname) {

        MessagePushEntity.PushMessage.SenderInfo senderInfo = MessagePushEntity.PushMessage.SenderInfo.newBuilder()
                .setHeadUrl(headUrl)//自己的头像地址
                .setNickName(nickname)//自己的昵称信息
                .buildPartial();

        ArrayList<String> inviteArray = getAllInvitePeople(invitePeopleListJson);
        MessagePushEntity.PushMessage.FeaturesMessage.CreateChatRoomMessage createChatRoomMessage = MessagePushEntity.PushMessage.FeaturesMessage.CreateChatRoomMessage.newBuilder()
                .setSendInfo(senderInfo)
                .setChatRoomId(roomId)
                .addAllInviteUuids(inviteArray)//邀请人列表
                .build();

        MessagePushEntity.PushMessage.FeaturesMessage featuresMessage = MessagePushEntity.PushMessage.FeaturesMessage.newBuilder()
                .setCreateChatRoomMessage(createChatRoomMessage)
                .setMessageType(CreateChatRoom)
                .build();


        MessagePushEntity.PushMessage pushMessage = MessagePushEntity.PushMessage.newBuilder()
                .setUserSend(buildUserSend(MessagePushEntity.PushMessage.UserSend.UserSendType.Features_VALUE, featuresMessage))
                .build();
        return pushMessage;
    }

    public MessagePushEntity.PushMessage buildEndChatRoom() {
        MessagePushEntity.PushMessage.FeaturesMessage.EndChatRoomMessage endChatRoomMessage = MessagePushEntity.PushMessage.FeaturesMessage.EndChatRoomMessage.newBuilder()
                .buildPartial();
        MessagePushEntity.PushMessage.FeaturesMessage featuresMessage = MessagePushEntity.PushMessage.FeaturesMessage.newBuilder()
                .setEndChatRoomMessage(endChatRoomMessage)
                .setMessageType(EndChatRoom)
                .build();

        MessagePushEntity.PushMessage pushMessage = MessagePushEntity.PushMessage.newBuilder()
                .setUserSend(buildUserSend(MessagePushEntity.PushMessage.UserSend.UserSendType.Features_VALUE, featuresMessage))
                .build();

        return pushMessage;
    }

    /**
     * 接听电话Build
     *
     * @param roomId
     * @return
     */
    public MessagePushEntity.PushMessage buildReceivingCalls(String roomId) {
        MessagePushEntity.PushMessage.FeaturesMessage.CreateChatRoomMessage createChatRoomMessage = MessagePushEntity.PushMessage.FeaturesMessage.CreateChatRoomMessage.newBuilder()
                .setChatRoomId(roomId)
                .buildPartial();
        MessagePushEntity.PushMessage.FeaturesMessage featuresMessage = MessagePushEntity.PushMessage.FeaturesMessage.newBuilder()
                .setMessageType(ReceivingCalls)
                .setCreateChatRoomMessage(createChatRoomMessage)
                .build();
        MessagePushEntity.PushMessage pushMessage = MessagePushEntity.PushMessage.newBuilder()
                .setUserSend(buildUserSend(MessagePushEntity.PushMessage.UserSend.UserSendType.Features_VALUE, featuresMessage))
                .build();
        return pushMessage;
    }

    /**
     * 心跳Build
     *
     * @return
     */
    public MessagePushEntity.PushMessage buildHeartMessage() {
        MessagePushEntity.PushMessage pushMessage = MessagePushEntity.PushMessage.newBuilder()
                .setUserSend(buildUserSend(MessagePushEntity.PushMessage.UserSend.UserSendType.Heart_VALUE, null))
                .setCommandTime(buildTimestamp())
                .buildPartial();
        return pushMessage;
    }

    /**
     * 拒绝接听Build
     *
     * @param roomId
     * @return
     */
    public MessagePushEntity.PushMessage buildRefuseToCall(String roomId) {
        MessagePushEntity.PushMessage.FeaturesMessage.CreateChatRoomMessage createChatRoomMessage = MessagePushEntity.PushMessage.FeaturesMessage.CreateChatRoomMessage.newBuilder()
                .setChatRoomId(roomId)
                .buildPartial();

        MessagePushEntity.PushMessage.FeaturesMessage featuresMessage = MessagePushEntity.PushMessage.FeaturesMessage.newBuilder()
                .setMessageType(RefuseToCall)
                .setCreateChatRoomMessage(createChatRoomMessage)
                .build();
        MessagePushEntity.PushMessage pushMessage = MessagePushEntity.PushMessage.newBuilder()
                .setUserSend(buildUserSend(MessagePushEntity.PushMessage.UserSend.UserSendType.Features_VALUE, featuresMessage))
                .build();
        return pushMessage;
    }


    /**
     * 用于首次连接后设置个人会议ID 参数Build
     *
     * @return
     */
    public MessagePushEntity.PushMessage buildChangeUuid() {
        MessagePushEntity.PushMessage.CommonInfo commonInfo= MessagePushEntity.PushMessage.CommonInfo.newBuilder()
                .setUuid(uuid)
                .buildPartial();

        MessagePushEntity.PushMessage.UserSend.CommandSend commandSend = MessagePushEntity.PushMessage.UserSend.CommandSend.newBuilder()
                .setCommandType(MessagePushEntity.PushMessage.UserSend.CommandSend.CommandType.UuidSet)
                .buildPartial();

        MessagePushEntity.PushMessage pushMessage = MessagePushEntity.PushMessage.newBuilder()
                .setUserSend(buildUserSend(MessagePushEntity.PushMessage.UserSend.UserSendType.Command.getNumber(), null).toBuilder().setCommandSend(commandSend))
                .setCommonInfo(commonInfo)
                .setCommandTime(buildTimestamp())
                .buildPartial();
        return pushMessage;
    }

    private MessagePushEntity.PushMessage.UserSend buildUserSend(int userSendTypeValue,
                                                                 MessagePushEntity.PushMessage.FeaturesMessage featuresMessage) {
        MessagePushEntity.PushMessage.UserSend.Builder userSend = MessagePushEntity.PushMessage.UserSend.newBuilder();
        userSend.setUserSendTypeValue(userSendTypeValue);//消息类型 //功能消息  Features = 1; //指令消息 Command = 2; //心跳 Heart = 3;
        userSend.setUuid(uuid);//个人会议ID
        if (featuresMessage != null) {
            userSend.setFeaturesMessage(featuresMessage);//功能特征参数
        }
        userSend.setSendPlatform(buildSendPlatform(appVersion, model, platform));//客户端平台相关参数
        userSend.setMessageId(getMessageId());//消息ID
        return userSend.build();
    }

    /**
     * 平台参数Build
     *
     * @param appVersion
     * @param model
     * @param platformType
     * @return
     */
    private MessagePushEntity.PushMessage.UserSend.SendPlatform buildSendPlatform(String appVersion, String model, int platformType) {
        MessagePushEntity.PushMessage.UserSend.SendPlatform sendPlatform = MessagePushEntity.PushMessage.UserSend.SendPlatform.newBuilder()
                .setAppVersion(appVersion)
                .setModel(model)
                .setPlatformTypeValue(platformType)
                .buildPartial();
        return sendPlatform;
    }


    /**
     * 时间参数Build
     */
    private Timestamp buildTimestamp() {
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(commandTime)
                .build();
        return timestamp;
    }

    private long getCurrentTimeMills() {
        commandTime = System.currentTimeMillis();
        return commandTime;
    }

    private String getMessageId() {
        getCurrentTimeMills();
        messageId = uuid + commandTime;
        return messageId;
    }

}
