package com.waiwaiwai.demo.thread;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: wangzhenglei
 * @Description: 计数器
 */
public class MyCountDownLatch {

    Executor executor = Executors.newFixedThreadPool(2);
    CountDownLatch countDownLatch = new CountDownLatch(2);
    @Test
    public void test() {
        executor.execute(() -> {
            // doSomething
            countDownLatch.countDown();
        });
        executor.execute(() -> {
            // doSomething
            countDownLatch.countDown();
        });

        try {
            countDownLatch.await();
            // 执行其他

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
