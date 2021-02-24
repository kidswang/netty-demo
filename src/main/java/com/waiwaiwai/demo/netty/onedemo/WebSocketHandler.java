package com.waiwaiwai.demo.netty.onedemo;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.waiwaiwai.demo.netty.onedemo.config.NettyConfig;
import io.micrometer.core.instrument.util.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description: @Sharable 注解用来说明ChannelHandler是否可以在多个channel直接共享使用
 */
@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    private WebSocketServerHandshaker handshaker;

    /**
     * webSocket路径
     */
    @Value("${webSocket.netty.path}")
    private String webSocketPath;


    /**
     * channel注册
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered channel注册");
        super.channelRegistered(ctx);
    }


    /**
     * channel注册
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered channel注册");
        super.channelUnregistered(ctx);
    }

    /**
     * 客户端与服务端第一次建立连接时 执行
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info(date + " " + ctx.channel().remoteAddress() + " 客户端连接成功！");
        ctx.writeAndFlush("连接成功！");
    }

    /**
     * 一旦连接，第一个被执行 客户端连接成功后执行的回调方法
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded 被调用" + ctx.channel().id().asLongText());
        // 添加到channelGroup 通道组
        NettyConfig.getChannelGroup().add(ctx.channel());
    }

    /**
     * 目前走这个方法
     * 接收到消息执行的回调方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //http请求和tcp请求分开处理
        if (msg instanceof HttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    /**
     * 如果不重写channelRead
     * 则走这个方法
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
         //接收到的消息
        log.info("服务器 收到[" + ctx.channel().remoteAddress() + "]消息：" + frame.text());

         //获取请求消息对象
        MessageRequest messageReq = JSONObject.parseObject(frame.text(), MessageRequest.class);
        MessageResponse messageResp = new MessageResponse(messageReq);
        String uid = messageReq.getSenderId();
         //发送者id为空
        if(StringUtils.isBlank(messageReq.getSenderId())){
            messageResp.setMsgEntity("发送用户不存在!");
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(messageResp)));
            return;
        }
         // 获取用户ID,关联channel
        bind(ctx, uid);

         //回复消息
        messageResp.setIsReceipt(1);
        messageResp.setCode(200);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(messageResp)));
    }


    /**
     * 第一次请求是http请求，请求头包括ws的信息
     */
    public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request)
            throws Exception {
        // 如果HTTP解码失败，返回HTTP异常
        if (null != request) {
            if (request instanceof HttpRequest) {
                HttpMethod method = request.method();
                // 如果是websocket请求就握手升级
                if (webSocketPath.equalsIgnoreCase(request.uri())) {
                    log.info(" req instanceof HttpRequest");
                    WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                            webSocketPath, null, false);
                    handshaker = wsFactory.newHandshaker(request);
                    if (handshaker == null) {
                        WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                    }
                    handshaker.handshake(ctx.channel(), request);
                }
            }
        }
    }

    /**
     * websocket消息处理
     * （只支持文本）
     */
    public void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // 关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // ping请求
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 只支持文本格式，不支持二进制消息
        if (frame instanceof TextWebSocketFrame) {
            //接收到的消息
            log.info("服务器 收到[" + ctx.channel().remoteAddress() + "]消息：" + ((TextWebSocketFrame) frame).text());

            //获取请求消息对象
            MessageRequest messageReq = JSONObject.parseObject(((TextWebSocketFrame) frame).text(), MessageRequest.class);
            MessageResponse messageResp = new MessageResponse(messageReq);
            String uid = messageReq.getSenderId();
            //发送者id为空
            if (StringUtils.isBlank(messageReq.getSenderId())) {
                messageResp.setMsgEntity("发送用户不存在!");
                ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(messageResp)));

                return;
            }
            // 获取用户ID,关联channel
            bind(ctx, uid);

            // 回复消息
            messageResp.setIsReceipt(1);
            messageResp.setCode(200);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(messageResp)));
        }

    }

    /**
     * channel读取数据完毕
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channel读取数据完毕");
        super.channelReadComplete(ctx);
    }

    /**
     * channel可写事件更改
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("channel可写事件更改");
        super.channelWritabilityChanged(ctx);
    }


    /**
     * 这里是保持服务器与客户端长连接  进行心跳检测 避免连接断开
     *
     * @param ctx
     * @param evt
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            switch (stateEvent.state()) {
                //读空闲（服务器端）
                case READER_IDLE:
                    String date1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    log.info(date1 + "【" + ctx.channel().remoteAddress() + "】读空闲（服务器端）");
                    break;
                //写空闲（客户端）
                case WRITER_IDLE:
                    String date2 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    log.info(date2 + "【" + ctx.channel().remoteAddress() + "】写空闲（客户端）");
                    break;
                case ALL_IDLE:
                    String date3 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    log.info(date3 + "【" + ctx.channel().remoteAddress() + "】读写空闲");
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 客户端与服务端 断连时 执行
     * 客户端下线
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info(date + " " + ctx.channel().remoteAddress() + " 客户端下线！");
    }

    /**
     * 断开连接
     *
     * @param ctx ChannelHandlerContext
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerRemoved 被调用" + ctx.channel().id().asLongText());
        // 删除通道
        NettyConfig.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
    }

    /**
     * 抛出异常
     *
     * @param ctx   ChannelHandlerContext
     * @param cause 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("异常：{}", cause.getMessage());
        // 删除通道
        NettyConfig.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
        ctx.close();
    }

    /**
     * 删除用户与channel的对应关系
     *
     * @param ctx ChannelHandlerContext
     */
    private void removeUserId(ChannelHandlerContext ctx) {
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        String userId = ctx.channel().attr(key).get();
        NettyConfig.getUserChannelMap().remove(userId);
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
        if (!exist(uid)) {
            NettyConfig.getUserChannelMap().put(uid, ctx.channel());
            // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户ID
            AttributeKey<String> key = AttributeKey.valueOf("userId");
            ctx.channel().attr(key).setIfAbsent(uid);
        }
    }

}
