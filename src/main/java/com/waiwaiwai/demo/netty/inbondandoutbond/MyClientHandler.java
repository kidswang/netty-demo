package com.waiwaiwai.demo.netty.inbondandoutbond;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器的ip=" + ctx.channel().remoteAddress());
        if (msg instanceof Long) {
            System.out.println(" wo shi long");
        }
        System.out.println("收到服务器消息=" + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        //ctx.writeAndFlush(Unpooled.copiedBuffer(""))
//        ctx.writeAndFlush(12345678912345L); //发送的是一个long
//         ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
//        ByteBuf byteBuf = Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8);
        ctx.writeAndFlush("abcdabcdabcdabcd");
    }
}
