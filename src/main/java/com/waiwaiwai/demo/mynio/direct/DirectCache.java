package com.waiwaiwai.demo.mynio.direct;

import org.junit.Test;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/20 15:46
 * @Description: 直接缓冲区
 */
public class DirectCache {

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
