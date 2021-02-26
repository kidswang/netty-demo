package com.waiwaiwai.demo.thread;

import org.junit.Test;

import java.util.concurrent.*;

/**
 *
 */
public class MyFutureTask {

    private final FutureTask<Integer> futureTask = new FutureTask<>(() -> 1 + 2);

    ExecutorService es = Executors.newCachedThreadPool();

    @Test
    public void test() throws ExecutionException, InterruptedException {
        es.submit(futureTask);
        System.out.println(futureTask.get());
    }

}
