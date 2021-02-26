package com.waiwaiwai.demo.thread;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/26 15:29
 * @Description: 线程安全的写入时复制list
 */
public class MyCopyOnWriteList {

     private final List<Object> list = new CopyOnWriteArrayList<>();

    @Test
    public void test() {
        list.add(new Object());

        System.out.println(list.get(0));

        list.add(new Object());
        System.out.println(list.get(0));
//        list.add(new Object());
//        list.add(new Object());

    }

}
