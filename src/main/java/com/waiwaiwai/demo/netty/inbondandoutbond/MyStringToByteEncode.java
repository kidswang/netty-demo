package com.waiwaiwai.demo.netty.inbondandoutbond;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

public class MyStringToByteEncode extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        System.out.println("调用了 MyStringToByteEncode");
        System.out.println("msg = " + msg);
        out.writeBytes(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    }
}
