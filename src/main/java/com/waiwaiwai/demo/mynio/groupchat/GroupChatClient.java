package com.waiwaiwai.demo.mynio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/1 9:02
 * @Description: 客户端
 */
public class GroupChatClient {
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private SocketChannel socketChannel;
    private Selector selector;
    private String userName;


    public GroupChatClient() {
        try {
            this.selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST ,PORT));
            socketChannel.configureBlocking(false);
            socketChannel.register(this.selector, SelectionKey.OP_READ);
            userName = socketChannel.getRemoteAddress().toString().substring(1);
            System.out.println(userName + " is ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务端发送消息
     * @param info
     */
    public void sendInfo(String info) {
        info = userName + "说: " + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try {
            int readChannel = selector.select();
            if (readChannel > 0) {

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        SocketChannel sc = (SocketChannel) selectionKey.channel();
//                        sc.configureBlocking(false);
                        ByteBuffer buffer = ByteBuffer.allocate(2048);
                        sc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove(); //删除当前通道 防止重复操作
            } else {
                System.out.println("没有可用通道");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        GroupChatClient groupChatClient = new GroupChatClient();
        // 启动一个线程读取数据
        new Thread(() -> {
            while (true) {
                System.out.println(123);
                groupChatClient.readInfo();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 写入数据
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            groupChatClient.sendInfo(scanner.nextLine());
        }

    }


}
