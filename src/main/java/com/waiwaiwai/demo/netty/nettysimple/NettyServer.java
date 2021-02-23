package com.waiwaiwai.demo.netty.nettysimple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty 服务器端
 */
public class NettyServer {

    public void serverStart() {
        // 创建两个线程组
        // bossGroup 负责处理链接请求    workGroup 负责处理读写请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置队列得到连接的个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置状态为保持连接
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 初始化管道
                        @Override
                        protected void initChannel(SocketChannel ch) { // 添加处理器
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            // 绑定一个端口并且同步 生成一个 ChannelFuture 对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            cf.addListener(future -> {
               if (future.isSuccess()) {
                   System.out.println("done");
               } else {
                   System.out.println("none");
               }
            });



            // 对通道的关闭事件进行监听
            cf.channel().closeFuture().sync();
//            cf.isDone()
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyServer().serverStart();
    }

}
