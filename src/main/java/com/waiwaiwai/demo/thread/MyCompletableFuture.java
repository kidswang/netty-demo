package com.waiwaiwai.demo.thread;

import org.junit.Test;
import org.springframework.cache.annotation.Cacheable;

import java.util.concurrent.*;

/**
 * 异步编程
 */
public class MyCompletableFuture {

    // 烧水泡茶 completableFuture版本

//    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Test
    public void test() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> ft1 = CompletableFuture.runAsync(() -> {
            try {
                System.out.println("洗水壶");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("烧开水");
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        CompletableFuture<String> ft2 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("洗茶壶");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("洗茶杯");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("拿茶叶");
                TimeUnit.SECONDS.sleep(1);
                return "龙井";
            } catch (InterruptedException e) {
                e.printStackTrace();
                return "";
            }
        });

        CompletableFuture<String> ft3 = ft1.thenCombine(ft2, (__, tf) -> {
            System.out.println("T1:拿到茶叶:" + tf);
            return "上茶" + tf;
        });

        System.out.println(ft3.get());
    }


    // 烧水 洗茶壶
    public static class MyTask1 implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("洗水壶");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("烧开水");
            TimeUnit.SECONDS.sleep(15);
            return null;
        }
    }

    public static class MyTask2 implements Callable<String> {
        @Override
        public String call() throws Exception {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("洗茶杯");
            TimeUnit.SECONDS.sleep(2);
            System.out.println("拿茶叶");
            TimeUnit.SECONDS.sleep(1);
            return "龙井";
        }
    }


}
