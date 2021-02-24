package com.waiwaiwai.demo.netty.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.waiwaiwai.demo.domain.Account;
import com.waiwaiwai.demo.netty.onedemo.config.NettyConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/22 15:49
 * @Description: TextWebSocketFrame 标书一个文本帧
 */
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> { // 规定了是以 TextWebSocketFrame 类型的数据传输,使用别的类型收不到

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

        String text = textWebSocketFrame.text();
        System.out.println(text);
        if ("ping".equals(text)) {
            System.out.println("这是一个心跳");
            channelHandlerContext.writeAndFlush(new TextWebSocketFrame(text));
        } else {
            Account account = JSONObject.parseObject(text, Account.class);
            System.out.println(account);
            System.out.println("服务器拿到数据" + textWebSocketFrame.text());
            System.out.println(textWebSocketFrame.content());
//        System.out.println("f服务器拿到数据" + textWebSocketFrame);
            // 回复消息
            channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("我收到消息了"));
        }
    }


    /**
     * 目前走这个方法
     * 接收到消息执行的回调方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        //http请求和tcp请求分开处理
//        if (msg instanceof HttpRequest) {
//            handleHttpRequest(ctx, (FullHttpRequest) msg);
//        } else if (msg instanceof WebSocketFrame) {
//            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
//        }
        //首次连接是FullHttpRequest，处理参数
        if (null != msg && msg instanceof FullHttpRequest) {
            System.out.println("wo shi http");
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            System.out.println(uri);
            Map<String, String> paramMap=getUrlParams(uri);
            System.out.println("接收到的参数是："+ JSON.toJSONString(paramMap));
            //如果url包含参数，需要处理
            if(uri.contains("?")){
                String newUri=uri.substring(0,uri.indexOf("?"));
                System.out.println(newUri);
                request.setUri(newUri);
            }
            System.out.println(ctx.channel().id().asLongText());

            MyChannelHandlerPool.getChannelGroup().add(ctx.channel());
            paramMap.get("id");
            MyChannelHandlerPool.getMap().put(paramMap.get("id"), ctx.channel());

            super.channelRead(ctx, msg);
        }else if(msg instanceof TextWebSocketFrame){
            System.out.println("wo shi TextWebSocketFrame");
            System.out.println(ctx.channel().id().asLongText());
            //正常的TEXT消息类型
            TextWebSocketFrame frame=(TextWebSocketFrame)msg;
            System.out.println("客户端收到服务器数据：" +frame.text());
            System.out.println(MyChannelHandlerPool.getMap().get("fsdfsd"));

            ctx.writeAndFlush(msg);
        }

    }

//    private static Map getUrlParams(String url){
//        Map<String,String> map = new HashMap<>();
//        url = url.replace("?",";");
//        if (!url.contains(";")){
//            return map;
//        }
//        if (url.split(";").length > 0){
//            String[] arr = url.split(";")[1].split("&");
//            for (String s : arr){
//                String key = s.split("=")[0];
//                String value = s.split("=")[1];
//                map.put(key,value);
//            }
//            return  map;
//
//        }else{
//            return map;
//        }
//    }

    private static Map<String, String> getUrlParams(String url){
        Map<String,String> map = new HashMap<>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr){
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key,value);
            }
            return  map;

        }else{
            return map;
        }
    }

//    private void sendAllMessage(String message){
//        //收到信息后，群发给所有channel
//        MyChannelHandlerPool.getChannelGroup().writeAndFlush(new TextWebSocketFrame(message));
//    }

    private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg) {
        System.out.println("zou zhe ge");
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        System.out.println("http");
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(req.uri());
        Map<String, List<String>> parameters = queryStringDecoder.parameters();

        System.out.println(parameters.entrySet());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // id 表示一个唯一的值  一个是 longText 是唯一的
        // 一个是 shortText 这个有可能不是唯一的
        System.out.println("handlerAdded 被调用了" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用了" + ctx.channel().id().asShortText());
//        NettyConfig.getChannelGroup().add(ctx.channel());
    }



    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用了" + ctx.channel().id().asLongText());
        MyChannelHandlerPool.getChannelGroup().remove(ctx.channel());
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生了" + cause.getMessage());
        System.out.println("================================");
        ChannelFuture close = ctx.close();
        System.out.println(close.isSuccess());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            switch (stateEvent.state()) {
                case WRITER_IDLE:
                    System.out.println("xie kong xian ");
                    break;
                case READER_IDLE:
                    System.out.println("读空闲");
                    break;
                case ALL_IDLE:
                    System.out.println("du xie kong xian ");
                    break;
            }
        }
    }
}
