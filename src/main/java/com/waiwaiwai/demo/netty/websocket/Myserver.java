package com.waiwaiwai.demo.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/22 15:37
 * @Description: netty 长连接
 */
public class Myserver {

    public static void main(String[] args) {
        // 创建两个线程组
        // bossGroup 负责处理链接请求    workGroup 负责处理读写请求
        EventLoopGroup bossGroup = null;
        EventLoopGroup workGroup = null;
        try {
            bossGroup = new NioEventLoopGroup();
            workGroup = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            // 添加 http 协议的编码解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 以块的方式写
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 添加聚合 因为 http 在传输数据过大是是分段发起请求的
                            // 这个处理器可以将多段数据聚合
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            // 对应 websocket 它的数据是以帧的方式传输的
                            // websocketframe
                            // WebSocketServerProtocolHandler 的核心功能是将 http 协议升级为 websocket 协议 保持长连接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            // 自定义一个 Handler 处理业务
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });
            ChannelFuture cf = bootstrap.bind(8080).sync();
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (bossGroup != null)
                bossGroup.shutdownGracefully();

            if (workGroup != null)
                workGroup.shutdownGracefully();
        }
    }
}
