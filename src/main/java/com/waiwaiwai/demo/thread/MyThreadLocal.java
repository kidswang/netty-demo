package com.waiwaiwai.demo.thread;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

/**
 * ThreadLocal
 */
public class MyThreadLocal {

    static final AtomicLong nextId = new AtomicLong();
    static final ThreadLocal<Long> tl = ThreadLocal.withInitial(nextId::getAndIncrement);

    //此方法会为每个线程分配一个唯一的ID
    static long get() {
        Long aLong = tl.get();
        System.out.println(aLong);
        return aLong;
    }

    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            long l = get();
            get();
            get();
        });

        t1.start();
        t1.join();
    }
}
