package com.waiwaiwai.demo.netty.simplehttp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestChannelInit extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        // 获取管道
        ChannelPipeline pipeline = ch.pipeline();

        // 添加一个netty提供的处理 http 请求的编码解码类
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());

        // 增加一个自定义的 handler 处理
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
    }
}
