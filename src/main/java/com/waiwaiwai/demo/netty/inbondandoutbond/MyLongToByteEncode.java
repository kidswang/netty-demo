package com.waiwaiwai.demo.netty.inbondandoutbond;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyLongToByteEncode extends MessageToByteEncoder<Long> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("调用了 MyLongToByteEncode ");
        System.out.println("msg = " + msg);
        out.writeLong(msg);
    }

}
