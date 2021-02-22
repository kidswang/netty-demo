package com.waiwaiwai.demo.netty.nettyheart;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 处理心跳
 */
public class NettyHeartHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            IdleState state = idleStateEvent.state();
            String eventType = "";
            switch (state) {
                case READER_IDLE:
                    eventType = "读空闲";
                    ctx.disconnect();
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    ctx.disconnect();
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    ctx.disconnect();
                    break;
                default:
                    eventType = "还活着";
            }

            System.out.println(ctx.channel().remoteAddress() + "---超时时间---" + eventType);

        }


    }
}
