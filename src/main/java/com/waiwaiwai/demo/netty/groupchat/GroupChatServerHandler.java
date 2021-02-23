package com.waiwaiwai.demo.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup defaultChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 上线 发送上线提示给其他客户端
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        defaultChannelGroup.writeAndFlush("[客户端] " + channel.remoteAddress() + " 加入群聊");
        defaultChannelGroup.add(channel);
    }

    /**
     * 客户端处于上线状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("客户端" + channel.remoteAddress() + "zai xian shang ");
    }

    /**
     * 客户端下线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("客户端" + channel.remoteAddress() + " 下线了");
    }

    /**
     * 断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 自动从 defaultChannelGroup 中删除
        Channel channel = ctx.channel();
        System.out.println("客户端" + channel.remoteAddress() + " 离开了");
        System.out.println(defaultChannelGroup.size());
    }

    /**
     * 异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 关闭通道
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 读取消息并转发给其他客户端
        Channel channel = ctx.channel();
        defaultChannelGroup.forEach(x -> {
            if (x != channel) {
                x.writeAndFlush("[客户端]" + channel.remoteAddress() + "发送了消息" + msg);
            }
        });
    }
}
