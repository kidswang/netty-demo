package com.waiwaiwai.demo.netty.onedemo;

import lombok.Data;

@Data
public class MessageRequest {

    /**
     * 多个handle 通过编写路由handler 通过该属性判断是哪一个handler执行
     * */
    private String channel;

    /**
     * 消息时间
     */
    protected String dateTime;

    /**
     * 发送者id
     */
    private String senderId;

    /**
     * 消息接收方id
     */
    private String receiverId;

    /**
     * 发送方头像
     */
    private String sendPortrait;

    /**
     * 接收方头像
     */
    private String receiverPortrait;

    /**
     * 消息接收方昵称
     */
    private String nickName;

    /**
     * 发送者昵称
     */
    private String senderName;

    /**
     * 发送时间
     * */
    private String sendTime;

    /**
     * 消息id，用于撤回
     */
    private String msgId;

    /**
     * 消息实体json字符串
     */
    private String msgEntity;

}


