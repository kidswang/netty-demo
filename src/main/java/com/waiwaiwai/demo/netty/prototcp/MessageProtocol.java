package com.waiwaiwai.demo.netty.prototcp;

import lombok.Data;

// 协议包
@Data
public class MessageProtocol {

    private int len; //关键
    private byte[] content;
}
