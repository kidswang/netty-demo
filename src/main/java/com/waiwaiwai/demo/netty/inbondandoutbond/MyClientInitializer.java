package com.waiwaiwai.demo.netty.inbondandoutbond;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyStringToByteEncode());
        pipeline.addLast(new MyLongToByteEncode());
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyClientHandler());
    }
}
