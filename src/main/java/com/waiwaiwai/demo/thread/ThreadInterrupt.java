package com.waiwaiwai.demo.thread;

import java.util.concurrent.TimeUnit;

/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/22 13:55
 * @Description: 测试中断线程
 */
public class ThreadInterrupt {

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            int j = 0;
            System.out.println(System.currentTimeMillis());
//            while ( )
            System.out.println(j);
            System.out.println(" wo mei you bei zhong duan");
        });

        t1.start();
        TimeUnit.MILLISECONDS.sleep(10);
        t1.interrupt();

        System.out.println("===============");
        TimeUnit.SECONDS.sleep(10);
    }

}
