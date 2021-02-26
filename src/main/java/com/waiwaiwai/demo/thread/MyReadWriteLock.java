package com.waiwaiwai.demo.thread;


import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 * 读锁不可以升级为写锁
 * 写锁可以降级为读锁
 *
 * 读写锁不支持条件变量。
 */
public class MyReadWriteLock {

    Object data;
    volatile boolean cacheValid;
    final ReadWriteLock rwl = new ReentrantReadWriteLock();
    // 读锁
    final Lock r = rwl.readLock();
    //写锁
    final Lock w = rwl.writeLock();

    void processCachedData() {
        r.lock();
        if (!cacheValid) {
//            r.unlock();

            w.lock();
            System.out.println("读锁错误的升级成了写锁对象。，。。。");
            try {
                if (!cacheValid) {
                    data = new Object();
                    cacheValid = true;
                }
                // 这里写锁降级成了读锁
                r.lock();
                // 读锁
//                System.out.println("shi shi neng fou jie suo");
//                r.unlock();
//                System.out.println("jie suo cheng gong");
            } finally {
//                r.lock();
                w.unlock();
            }
        }
        try {
            System.out.println(data);
        } finally {
            r.unlock();
        }
    }

    @Test
    public void test1() {
        processCachedData();
    }

}
