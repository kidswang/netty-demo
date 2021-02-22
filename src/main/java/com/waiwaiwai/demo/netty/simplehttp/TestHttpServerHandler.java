package com.waiwaiwai.demo.netty.simplehttp;

import com.sun.jndi.toolkit.url.Uri;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 自定义处理器
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpRequest) {
//            System.out.println("msg 的类型是 " + msg.getClass());
//            System.out.println("客户端地址 " + ctx.channel().remoteAddress());
            System.out.println("pipeline的hashcode " + ctx.pipeline().hashCode() + " TestHttpServerHandler d的hashcode " + this.hashCode());
            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if ("favicon.ico".equals(uri.getPath())) {
                System.out.println("zhe shi yi ge fei fa qing qiu");
                return;
            }

            // 回复给浏览器的信息
            ByteBuf content = Unpooled.copiedBuffer("hello 浏览器", CharsetUtil.UTF_8);

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
//            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            ctx.writeAndFlush(response);
        }
    }
}
