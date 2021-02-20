package com.waiwaiwai.demo.mynio.simplenio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {


    /*
        channel.read(buffer)  从管道中向buffer中写数据
        channel.write(buffer)  把 buffer 中的读取数据到管道中


     */

    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 得到一个 selector 对象
        Selector selector = Selector.open();

        // 绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(6666));

        // 使用非阻塞的方式
        serverSocketChannel.configureBlocking(false);

        // channel 注册到 selector 中  关心的是初次连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            // 如果没有连接
            if (selector.select(1000) == 0) {
//                System.out.println("等待客户端连接");
                continue;
            }

            // 这里是获取到连接之后
            // 得到所有的关心事件
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功 socketChannel 是 " + socketChannel);
                    socketChannel.configureBlocking(false);
                    // 将 socketChannel 注册到 selector 中,并分配一个 bytebuffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (selectionKey.isReadable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                    try {
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        channel.read(byteBuffer);
                        System.out.println(new String(byteBuffer.array()));
                    } catch (IOException e) {
                        System.out.println("xia xian le ");
                        // 去掉该 selectionKey
                        selectionKey.cancel();
                        // 关闭该通道
                        channel.close();
//                        e.printStackTrace();
                    }
                }
                // 防止重复  将 key 从当前的 selectionKeys中移除掉
                iterator.remove();
            }

//            selectionKeys.stream().filter(SelectionKey::isAcceptable).forEach(selectionKey -> {
//                // 给客户端生成一个socketChannel
//                try {
//                    SocketChannel socketChannel = serverSocketChannel.accept();
//                    // 将 socketChannel 注册到 selector 中,并分配一个 bytebuffer
//                    socketChannel.register(selector, SelectionKey.OP_READ, new ByteBuffer[1024]);
//                    // 使用非阻塞的方式
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            selectionKeys.stream().filter(SelectionKey::isReadable).forEach(selectionKey -> {
//                try {
//                    SocketChannel channel = (SocketChannel) selectionKey.channel();
//                    channel.configureBlocking(false);
//                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
//                    channel.read(byteBuffer);
//                    System.out.println(new String(byteBuffer.array()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });

        }
    }

}
