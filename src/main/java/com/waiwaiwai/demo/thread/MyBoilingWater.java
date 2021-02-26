package com.waiwaiwai.demo.thread;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * 烧水泡茶
 */
public class MyBoilingWater {

    // 使用 Future 这玩意有返回值

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Test
    public void test() throws ExecutionException, InterruptedException {
        FutureTask<String> ft2 = new FutureTask<>(new Task2());
        FutureTask<String> ft1 = new FutureTask<>(new Task1(ft2));

        executorService.execute(ft1);
        executorService.execute(ft2);

        System.out.println(ft1.get());

    }

    // 任务一 洗水壶、烧水、泡茶
    public static class Task1 implements Callable<String> {
        // 这里需要拿到 task2 的茶叶
        FutureTask<String> ft2;

        public Task1(FutureTask<String> ft2) {
            this.ft2 = ft2;
        }

        @Override
        public String call() throws Exception {
            System.out.println("t1 洗水壶...");
            TimeUnit.SECONDS.sleep(1);

            System.out.println("t1 烧开水...");
            TimeUnit.SECONDS.sleep(10);

            // 拿取 task1 中返回的茶叶
            String tea = ft2.get();
            System.out.println("t1 拿到茶叶 " + tea);

            System.out.println("t1 泡茶...");
            TimeUnit.SECONDS.sleep(1);
            return "上茶 " + tea;
        }
    }

    // 任务二 洗茶壶、洗茶杯、拿茶叶
    public static class Task2 implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("T2:洗茶壶...");
            TimeUnit.SECONDS.sleep(1);

            System.out.println("T2:洗茶杯...");
            TimeUnit.SECONDS.sleep(2);

            System.out.println("T2:拿茶叶...");
            TimeUnit.SECONDS.sleep(1);
            return "龙井";
        }
    }

}
