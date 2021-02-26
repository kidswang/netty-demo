package com.waiwaiwai.demo.thread;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * @Author: wangzhenglei
 * @Description: 可循环的实现线程同步的方式
 */
public class MyCyclicBarrier {
    Executor executor = Executors.newFixedThreadPool(2);
    Executor checkExecutor = Executors.newFixedThreadPool(1);
    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
        // 这里使用线程数量为1的线程池是为了保证回调是按照顺序执行
        checkExecutor.execute(() -> {
            // 结束后的回调
            System.out.println("wo zhi xing wan cheng le");
        });
    });


    @Test
    public void test() throws InterruptedException {
        executor.execute(() -> {
            // doSomething
            try {
                while (true) {
                    System.out.println("wo shi test 1");
                    cyclicBarrier.await();
//                    System.out.println("wo mei zou zhe li");
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            // doSomething
            try {
                TimeUnit.SECONDS.sleep(100);
                while (true) {
                    System.out.println(" wo shi test 2");
                    cyclicBarrier.await();
                }
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        TimeUnit.MINUTES.sleep(10);
    }

}
