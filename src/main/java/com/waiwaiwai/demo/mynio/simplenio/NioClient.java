package com.waiwaiwai.demo.mynio.simplenio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {


    public static void main(String[] args) throws Exception {
        SocketChannel channel = SocketChannel.open();

        channel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        // 连接服务器
        if (!channel.connect(inetSocketAddress)) {
            while (!channel.finishConnect()) {
                System.out.println("因为连接需要时间,客户端不会阻塞.");
            }
        }

        // 这里是连接成功了
        StringBuilder str = new StringBuilder("hello world");
        int count = 0;
        while (true) {
            str.append(count);
            ByteBuffer byteBuffer = ByteBuffer.wrap(str.toString().getBytes());
            channel.write(byteBuffer);
            if (count == 10) break;
            count += 1;
            System.out.println(count);
        }
        // 阻塞在这里
        System.in.read();


    }

}
