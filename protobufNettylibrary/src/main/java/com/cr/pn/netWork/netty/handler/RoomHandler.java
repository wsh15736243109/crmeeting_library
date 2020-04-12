package com.cr.pn.netWork.netty.handler;

import com.cr.pn.Utils.Json.JsonUtils;
import com.cr.pn.netWork.http.baseEntity.BaseEntity;
import com.cr.pn.netWork.netty.base.handler.inbound.AsyBaseChannelInBoundHandler;
import com.handler.BaseManagerHandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by zy on 2018/8/17.
 */

public class RoomHandler extends AsyBaseChannelInBoundHandler {

    private ChannelHandlerContext ctx;

    private BaseManagerHandler baseManagerHandler;

    private String name;

    public RoomHandler(BaseManagerHandler baseManagerHandler, String name){
        super();
        this.name = name;
        this.baseManagerHandler = baseManagerHandler;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        destroy();
        baseManagerHandler = null;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.ctx = ctx;
        synNettyEventRealize.setClass(BaseEntity.class);
        handlerMessage("crmeetingApp2<-->");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx,msg);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
        destroy();
        baseManagerHandler = null;
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        ctx.close();
        destroy();
        baseManagerHandler = null;
    }

    /**
     * handler之间传递数据.
     *
     * @param handlerMsg
     */
    @Override
    public void handlerMessage(Object handlerMsg) {
        if (ctx != null){
            if (ctx.channel().isWritable() && ctx.channel().isOpen()) {
                String msgStr = "";
                if (handlerMsg instanceof BaseEntity){
                    BaseEntity baseEntity = (BaseEntity) handlerMsg;
                    msgStr = JsonUtils.toJson(baseEntity);
                }else if (handlerMsg instanceof String){
                    msgStr = (String)handlerMsg;
                }
                ByteBuf msg = Unpooled.copiedBuffer(msgStr+"<-->", CharsetUtil.UTF_8);
                System.out.println("netty数据:"+msgStr);
                ctx.writeAndFlush(msg);
            }else {
                destroy();
                ctx.close();
                baseManagerHandler = null;
            }
        }
    }


    @Override
    public String backEntity(Object msg) {
        String str = (String)msg;
        System.out.println(str);
        return str;
    }
}
