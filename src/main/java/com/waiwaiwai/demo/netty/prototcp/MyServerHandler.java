package com.waiwaiwai.demo.netty.prototcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("长度是 " + msg.getLen());
        System.out.println("内容是 " + new String(msg.getContent(), CharsetUtil.UTF_8));
        System.out.println("服务器端接受到消息量 = " + (++this.count));


        // 服务器会送数据给客户端
//        ctx.writeAndFlush(Unpooled.copiedBuffer(UUID.randomUUID().toString() + "\n", CharsetUtil.UTF_8));
        String responseMsg = UUID.randomUUID().toString();
        int length = responseMsg.getBytes(StandardCharsets.UTF_8).length;

        // 构建一个协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(responseMsg.getBytes(StandardCharsets.UTF_8));
        messageProtocol.setLen(length);
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
