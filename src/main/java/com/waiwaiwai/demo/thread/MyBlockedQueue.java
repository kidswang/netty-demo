package com.waiwaiwai.demo.thread;

import org.junit.Test;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock 中的 condition
 *
 * @param
 */
public class MyBlockedQueue {
    private final ReentrantLock lock1 = new ReentrantLock();
    private final Condition notEmpty = lock1.newCondition();
    private final Condition notFull = lock1.newCondition();
    private final LinkedBlockingDeque<String> deque = new LinkedBlockingDeque<>(10);

    /**
     * 入队
     *
     * @param t
     */
    void enq(String t) {
        lock1.lock();
        try {
            while (deque.size() == 10) {
                // 等待队列已满
                notFull.await();
//                lock1.wait(); // java.lang.IllegalMonitorStateException
            }
            deque.add("不满");
            System.out.println("ru dui le");
            // 入队后 队列不空
            notEmpty.signalAll();
//            lock1.notifyAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
        }
    }

    /**
     * 出队
     */
    void deq() {
        lock1.lock();
        try {
            while (deque.size() == 0) {
                // 队列为空
                notEmpty.await();
//                lock1.wait();
            }
            deque.poll();
            System.out.println("出队了");
            // 出队后 队列不满
            notFull.signalAll();
//            lock1.notifyAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
        }
    }

    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                enq("fsd");
            }
        });

        Thread t2 = new Thread(() -> {
            while (true) {
                deq();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
