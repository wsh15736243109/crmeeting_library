package com.cr.pn.netWork.netty.base.handler.inbound;

import com.cr.pn.netWork.netty.base.businessHandler.BusinessEvent;
import com.cr.pn.netWork.netty.base.businessHandler.BusinessHandler;
import com.cr.pn.netWork.netty.synDataBack.nettySynInterface.NetMsgKeyForValue;
import com.cr.pn.netWork.netty.synDataBack.nettySynInterface.SynNettyEventRealize;
import com.handler.inbound.BaseChannelInBoundHandler;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zy on 2018/8/21.
 * 支持Asy数据handler
 * 请注意在close的时候调用destroy
 */
public abstract class AsyBaseChannelInBoundHandler extends BaseChannelInBoundHandler implements NetMsgKeyForValue{

    public SynNettyEventRealize synNettyEventRealize;

    private BusinessHandler businessHandler;

    public int coreThread;
    public int maxThreads;

    public AsyBaseChannelInBoundHandler(){
        this(2,5);
    }

    public AsyBaseChannelInBoundHandler(int coreThread, int maxThreads){
        this.coreThread = coreThread;
        this.maxThreads = maxThreads;
    }

    public void initAsy(){
        synNettyEventRealize = new SynNettyEventRealize(this);
        businessHandler = new BusinessHandler(coreThread,maxThreads);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        initAsy();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, final Object msg) throws Exception {
        final Object msgEvent = msg;
        businessHandler.execute(new BusinessEvent() {
            @Override
            public void Event() {
                //解析消息.
                String str = backEntity(msgEvent);
                //保存消息，准备分发.
                synNettyEventRealize.setWriteMsg(str);
            }
        });

    }

    /**
     * 销毁订阅.
     */
    public void destroy(){
        if (synNettyEventRealize != null){
            synNettyEventRealize.unregister();
        }
    }

}
