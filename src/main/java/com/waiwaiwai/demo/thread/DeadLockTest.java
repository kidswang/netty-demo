package com.waiwaiwai.demo.thread;

import org.junit.Test;

import java.sql.ResultSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockTest {

    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();


    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    ArrayBlockingQueue<String> strings = new ArrayBlockingQueue<>(10);
                    try {
                        String take = strings.take(); // 阻塞队列
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("===================");

                    System.out.println(".....");
                }
            }
        });

        Thread t2 = new Thread(() -> {
//            synchronized (lock2) {
//                try {
//                    TimeUnit.SECONDS.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                synchronized (lock1) {
//                    System.out.println(".....");
//                }
//            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }

}
