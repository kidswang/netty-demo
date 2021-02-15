package com.waiwaiwai.demo.netty.nettysimple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 自定义个 handler 需要继承 netty 已经定义好的某个 handlerAdapter
 * 这样我们定义好的 handler 才是一个合法的 handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * read 数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 用户自定义普通任务
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(2L);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello kehuduan", CharsetUtil.UTF_8));
        });

        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5L);
                System.out.println(Thread.currentThread().getName() + LocalDateTime.now());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello kehuduan2", CharsetUtil.UTF_8));
        });

        // 自定义定时任务
        ctx.channel().eventLoop().schedule(() -> {
            System.out.println(Thread.currentThread().getName() + LocalDateTime.now());
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello kehuduan2", CharsetUtil.UTF_8));
        }, 5, TimeUnit.SECONDS);


        System.out.println("go on");
//        System.out.println("ctx 的值是 " + ctx);
//
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();

        // 将 msg 转成一个 byteBuf
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端发送的消息是 " + buf.toString());
//        System.out.println("客户端的地址是 " + channel.remoteAddress());
//        super.channelRead(ctx, msg);
    }

    /**
     * 数据读取完毕后做的事
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 向客户端发送个你好
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello 客户端", CharsetUtil.UTF_8));
        System.out.println("on on on on");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
