package com.waiwaiwai.demo.netty.onedemo.service.impl;

import cn.hutool.json.JSONUtil;
import com.waiwaiwai.demo.netty.onedemo.MessageRequest;
import com.waiwaiwai.demo.netty.onedemo.MessageResponse;
import com.waiwaiwai.demo.netty.onedemo.config.NettyConfig;
import com.waiwaiwai.demo.netty.onedemo.service.IPushService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class PushServiceImpl implements IPushService {

    @Override
    public void pushMsgToOne(MessageRequest request) {
        Channel channel = null;
        if (exist(request.getSenderId())) {
            channel = getChannel(request.getSenderId());
            MessageResponse response = new MessageResponse(request);
            response.setIsReceipt(1);
            response.setCode(200);
            channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(response)));
        }
    }

    @Override
    public void pushMsgToAll(String msg) {
        NettyConfig.getChannelGroup().writeAndFlush(new TextWebSocketFrame(msg));
    }


    /**
     * 根据用户id判断是否存在用户
     *
     * @param userId 用户id
     * @return Channel
     */
    public static boolean exist(String userId) {
        return NettyConfig.getUserChannelMap().containsKey(userId);
    }


    /**
     * 根据用户id获取对应的channel
     *
     * @param userId 用户id
     * @return Channel
     */
    public static Channel getChannel(String userId) {
        return NettyConfig.getUserChannelMap().get(userId);
    }

    /**
     * 用户绑定channel
     *
     * @param ctx ChannelHandlerContext
     * @param uid 用户id
     */
    public void bind(ChannelHandlerContext ctx, String uid) {
        NettyConfig.getUserChannelMap().put(uid, ctx.channel());
        // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户ID
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        ctx.channel().attr(key).setIfAbsent(uid);
    }
}

