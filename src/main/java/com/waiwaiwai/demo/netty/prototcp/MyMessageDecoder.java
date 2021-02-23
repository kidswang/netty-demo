package com.waiwaiwai.demo.netty.prototcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * ReplayingDecoder 在接收到所需长度的字节后再调用 decode 方法, 而不是一遍一遍的手动检查流中字节长度
 * // 还可以使用 DelimiterBasedFrameDecoder 自定义数据的分隔符的形式解决粘包、拆包问题。
 */
public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder 被调用");

        // 需要将得到的二进制字节码转换成 messageProtocol 数据包(对象)
        int len = in.readInt();
        byte[] content = new byte[len];
        in.readBytes(content);

        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(len);
        messageProtocol.setContent(content);
        out.add(messageProtocol);
    }
}
