package com.waiwaiwai.demo.netty.onedemo.service;

import com.waiwaiwai.demo.netty.onedemo.MessageRequest;

/**
 */
public interface IPushService {
    /**
     * 推送给指定用户
     *
     * @param userId
     * @param msg
     */
    void pushMsgToOne(MessageRequest request);

    /**
     * 推送给所有用户
     *
     * @param msg
     */
    void pushMsgToAll(String msg);
}

