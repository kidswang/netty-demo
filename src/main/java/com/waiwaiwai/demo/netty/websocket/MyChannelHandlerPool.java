package com.waiwaiwai.demo.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

public class MyChannelHandlerPool {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<>();

    public static ChannelGroup getChannelGroup() {

        return channelGroup;
    }

    public static ConcurrentHashMap<String, Channel> getMap() {
        return map;
    }
}
