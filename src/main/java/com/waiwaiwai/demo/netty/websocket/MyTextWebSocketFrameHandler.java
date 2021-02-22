package com.waiwaiwai.demo.netty.websocket;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/22 15:49
 * @Description: TextWebSocketFrame 标书一个文本帧
 */
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> { // 规定了是以 TextWebSocketFrame 类型的数据传输,使用别的类型收不到
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println("f服务器拿到数据" + textWebSocketFrame.text());

        // 回复消息
        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("我收到消息了"));

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // id 表示一个唯一的值  一个是 longText 是唯一的
        // 一个是 shortText 这个有可能不是唯一的
        System.out.println("handlerAdded 被调用了" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用了" + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用了" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生了" + cause.getMessage());
        System.out.println("================================");
        ChannelFuture close = ctx.close();
        System.out.println(close.isSuccess());
    }
}
