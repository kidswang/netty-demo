package com.waiwaiwai.demo.netty.onedemo;

import lombok.Data;

@Data
public class MessageResponse {

    /**
     * 管道类型
     */
    private String channel;

    /**
     * 消息id
     */
    private String msgId;

    /**
     * 发送者Id
     */
    private String senderId;

    /**
     * 接受者id
     */
    private String receiverId;

    /**
     * 发送者对象
     */
    private Object sender;

    /**
     * 接受者对象
     */
    private Object receiver;

    /**
     * 响应消息
     */
    private String msgEntity;

    /**
     * 消息时间
     */
    private String dateTime;

    /**
     * 200成功
     */
    private Integer code;

    /**
     * 0不是回执，1是回执
     */
    private Integer isReceipt;

    public MessageResponse(){}

    public MessageResponse(MessageRequest messageRequest){
        this.isReceipt = 1;
        this.code = 500;
        this.channel = messageRequest.getChannel();
        this.senderId = messageRequest.getSenderId();
        this.receiverId = messageRequest.getReceiverId();
        this.msgEntity = messageRequest.getMsgEntity();
    }
}
