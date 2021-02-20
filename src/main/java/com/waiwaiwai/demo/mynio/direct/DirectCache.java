package com.waiwaiwai.demo.mynio.direct;

import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/20 15:46
 * @Description: 直接缓冲区
 */
public class DirectCache {
    /**
     * 字符集
     */
    @Test
    public void test5() {
        // 所有可用的字符集
//        SortedMap<String, Charset> stringCharsetSortedMap = Charset.availableCharsets();
        try {
            Charset gbk = CharsetUtil.UTF_8;
            // 获取编码器
            CharsetEncoder charsetEncoder = gbk.newEncoder();
            // 获取解码器
            CharsetDecoder charsetDecoder = gbk.newDecoder();

            CharBuffer buffer = CharBuffer.allocate(2014);
            buffer.put("我是猪");
            buffer.flip();

            ByteBuffer encode = charsetEncoder.encode(buffer);
            for (int i = 0; i < 6; i++) {
                System.out.println(encode.get(i));
            }

            CharBuffer decode = charsetDecoder.decode(encode);
            System.out.println(decode.toString());

        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分散读取: scatter 将通道中的数据分散到多个缓冲区中
     * 按照顺序将多个缓冲区依次写满
     *
     * 聚集写入: gather 将多个缓冲区中的数据聚集到一个通道中
     */
    @Test
    public void test4() {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("E:\\vs_background.jpg", "rw");
            FileChannel channel = randomAccessFile.getChannel();

            ByteBuffer buffer1 = ByteBuffer.allocate(100);
            ByteBuffer buffer2 = ByteBuffer.allocate(20480);

            ByteBuffer[] buffers = {buffer1, buffer2};

            channel.read(buffers);

            for (ByteBuffer buffer : buffers) {
                buffer.flip();
            }

            System.out.println(new String(buffers[0].array(), 0, buffers[0].limit()));
            System.out.println("========================================");
            System.out.println(new String(buffers[1].array(), 0, buffers[1].limit()));

//            String s = new String(buffers[1].array(), 0, buffers[1].limit());
//            s = s.replace("Adobe Photoshop CS Windows 2019:05:27 10:27:12", "Adobe Photoshop CS Windows 2021:02:20 10:27:12");
//            buffer2.clear();
//            buffer2.put(s.getBytes());
//            buffer2.flip();

            RandomAccessFile randomAccessFile2 = new RandomAccessFile("E:\\vs_background3.jpg", "rw");
            // Adobe Photoshop CS Windows 2019:05:27 10:27:12
            FileChannel channel1 = randomAccessFile2.getChannel();
            channel1.write(buffers);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通道之间的数据传输(直接缓冲区的方式)
     */
    @Test
    public void test3() {

        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = FileChannel.open(Paths.get("E:\\vs_background.jpg"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("E:\\vs_background2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            inChannel.transferTo(0, inChannel.size(), outChannel);

            // 同上面那个
//            outChannel.transferFrom(inChannel, 0, inChannel.size());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 直接缓冲区
     */
    @Test
    public void test2() {
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = FileChannel.open(Paths.get("E:\\vs_background.jpg"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("E:\\vs_background2.jpg"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            // 内存映射文件
            MappedByteBuffer readBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            MappedByteBuffer writerBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

            // 直接对缓冲区进行数据的读写操作
            byte[] dest = new byte[readBuffer.limit()];
            readBuffer.get(dest);
            writerBuffer.put(dest);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
