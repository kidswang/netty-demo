package com.waiwaiwai.demo.mynio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/1 9:02
 * @Description: 服务端
 */
public class GroupChatServer {

    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听
     */
    public void listen() {
        while (true) {
            try {
                int selectedCount = selector.select();
                if (selectedCount > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println("客户端" + sc.getRemoteAddress() + "已上线");
                        }
                        // 频道发送 read 事件,即通道是可读事件
                        if (selectionKey.isReadable()) {
                            readData(selectionKey);
                        }
                        // 移除该 selectionKey 防止重复操作
                        iterator.remove();
                    }
                } else {
//                    System.out.println("deng dai ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readData(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            // 通过 key 反向获取相应的 socketChannel
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(2048);
            int count = socketChannel.read(buffer);
            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println("from 客户端 " + msg);
                // 发送给其他客户端
                sendInfoToOtherClients(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + " 离线了");
                selectionKey.cancel();
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void sendInfoToOtherClients(String msg, SocketChannel selfSocketChannel) {
        System.out.println("服务器转发消息中....");
        // 获取所有注册到该 selector 的连接
        try {
            for (SelectionKey selectionKey : selector.keys()) {
                System.out.println(selectionKey);
                Channel targetChannel = selectionKey.channel();
                if (targetChannel instanceof SocketChannel && targetChannel != selfSocketChannel) {
                    SocketChannel dest = (SocketChannel) targetChannel;

                    // 将消息存储到 buffer 中
                    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                    dest.write(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();


    }

}
