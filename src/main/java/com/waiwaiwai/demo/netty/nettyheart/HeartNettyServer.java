package com.waiwaiwai.demo.netty.nettyheart;

import com.waiwaiwai.demo.netty.nettysimple.NettyServer;
import com.waiwaiwai.demo.netty.nettysimple.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class HeartNettyServer {


    public void serverStart() {
        // 创建两个线程组
        // bossGroup 负责处理链接请求    workGroup 负责处理读写请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
//                    .option(ChannelOption.SO_BACKLOG, 128) // 设置队列得到连接的个数
//                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置状态为保持连接
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 初始化管道
                        @Override
                        protected void initChannel(SocketChannel ch) { // 添加处理器
//                            ch.pipeline().addLast(new NettyServerHandler());
                            ChannelPipeline pipeline = ch.pipeline();
                            // idle触发后,就会传递给管道的下一个 Handler 去处理 通过条用下一个 Handler 的 userEventTrigger 在该方法中处理空闲
                            pipeline.addLast(new IdleStateHandler(0, 20, 50, TimeUnit.SECONDS));
                            // 添加心跳检测处理
                            pipeline.addLast(new NettyHeartHandler());
                            // 添加重连

                        }
                    });

            // 绑定一个端口并且同步 生成一个 ChannelFuture 对象
            ChannelFuture cf = bootstrap.bind(6668).sync();
            // 对通道的关闭事件进行监听
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HeartNettyServer().serverStart();
    }


}
