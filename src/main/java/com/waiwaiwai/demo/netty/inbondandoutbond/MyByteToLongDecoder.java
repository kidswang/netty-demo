package com.waiwaiwai.demo.netty.inbondandoutbond;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     *
     * @param ctx
     * @param in 入站的 buffer
     * @param out list 集合, 将解码后的数据放入 out 传给下个 handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // long 8个字节 一次读 8 个字节
        System.out.println("调用了 MyByteToLongDecoder");
//        byte[] contentByte = new byte[in.readableBytes()];
//        in.readBytes(contentByte);
//        System.out.println(new String(contentByte));
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
