package com.cr.pn.netWork.netty;

import com.cr.pn.netWork.NetWorkStatusData;
import com.cr.pn.netWork.netty.handler.RoomHandler;
import com.handler.BaseManagerHandler;
import com.handler.heart.HeartWriteHandler;
import com.manager.client.ClientRunnable;
import com.manager.client.NettyClientDefaultInit;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * Created by zy on 2018/8/17.
 */
public class ClientNettyForCall extends NettyClientDefaultInit {

    /**
     * 重新连接次数.
     */
    private int reConnectNum = 0;

    /**
     * tcp连接状态.
     */
    private TcpStatus tcpStatus;

    /**
     * 用于某个mvp在断开连接之后单独进行tcp连接.
     */
    private String tagMsg;

    private ChannelPipeline channelPipeline;

    private boolean isReConnect = true;

    public Channel getNettyChannel() {
        return nettyChannel;
    }

    public void setNettyChannel(Channel nettyChannel) {
        this.nettyChannel = nettyChannel;
    }

    private Channel nettyChannel;

    public ClientNettyForCall() {
    }

    public void initClient(String tagMsg, TcpStatus tcpStatus) {
        this.tagMsg = tagMsg;
        this.tcpStatus = tcpStatus;
        isReConnect = true;
    }

    public String getTagMsg() {
        return tagMsg;
    }


    @Override
    public BaseManagerHandler initBaseManagerHandler() {
        return new BaseManagerHandler() {
            @Override
            protected void initChannel(Channel ch, BaseManagerHandler baseManagerHandler) throws Exception {
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(100000, Unpooled.copiedBuffer("<-->", CharsetUtil.UTF_8)));
                //创建数据的编码模式
                ch.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
                ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                ch.pipeline().addLast(new HeartWriteHandler(25, "crmeetingApp<-->"));
                ch.pipeline().addLast(new RoomHandler(baseManagerHandler, "AppInit"));
                channelPipeline = ch.pipeline();
            }
        };
    }

    /**
     * tcp连接失败.
     */
    private void tcpFailed(int frequency) {
        if (tcpStatus != null) {
            tcpStatus.tcpStatus(frequency);
        }
    }

    /**
     * tcp连接成功.
     */
    private void tcpSuccess(int frequency) {
        if (tcpStatus != null) {
            tcpStatus.tcpStatus(frequency);
        }
    }

    @Override
    public void throwable(Throwable throwable, ClientRunnable clientRunnable, ChannelFuture future) {
        reConnectNum++;
        tcpFailed(reConnectNum);
        if (isReConnect) {
            try {
                clientRunnable.logFormat("netty连接失败，4秒后重新连接...");
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                clientRunnable.logFormat("netty重新连接尝试...");
                clientRunnable.start();
            }
        }
    }

    @Override
    public void startSuccess(ChannelFuture future, ClientRunnable clientRunnable) {
        reConnectNum = 0;
        try {
            nettyChannel = future.sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tcpSuccess(NetWorkStatusData.netSuccess);
    }

    @Override
    public String ip() {
        return "222.180.221.241";
    }

    @Override
    public int port() {
        return 8088;
    }

    public interface TcpStatus {
        public boolean tcpStatus(int frequency);
    }

    /**
     * 断开连接.
     */
    public void closeConnect() {
        if (channelPipeline != null) {
            channelPipeline.close();
            isReConnect = false;
        }
    }

}
