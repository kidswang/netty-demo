package com.waiwaiwai.demo.netty.prototcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 循环发出 10 条数据
        for (int i = 0; i < 5; i++) {
//            ctx.writeAndFlush(Unpooled.copiedBuffer("天气真好啊好啊" + i , CharsetUtil.UTF_8));

            String msg = "天气真好啊好啊" + i;
            byte[] content = msg.getBytes(StandardCharsets.UTF_8);
            int length = content.length;
            System.out.println("发送数据的长度是" + length);
            // 创建协议包对象
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(length);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
//        byte[] bytes = new byte[msg.readableBytes()];
//        msg.readBytes(bytes);
//        System.out.println(new String(bytes, CharsetUtil.UTF_8));
//        System.out.println("客户端接受到消息量 = " + (++this.count));

        int len = msg.getLen();
        byte[] content = msg.getContent();

        System.out.println("服务器传回的消息");
        System.out.println("长度 " + len);
        System.out.println("内容 " + new String(content, CharsetUtil.UTF_8));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
