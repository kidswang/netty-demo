package com.waiwaiwai.demo.test;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockInterruptibility {

    private final Lock lock = new ReentrantLock();


    public synchronized void nonStatic() {
//        lock.lock();
        System.out.println("zhe shi yi ge fei jing tai");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        lock.unlock();
    }
    public synchronized void nonStatic2() {
//        lock.lock();
        System.out.println("zhe shi yi ge fei jing tai");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        lock.unlock();
    }

    public synchronized static void oneStatic() {
        System.out.println("zhe shi yi ge jing tai fang fa");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void twoStatic() {
        System.out.println("zhe shi yi ge jing tai fang fa");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() throws InterruptedException {
//        LockInterruptibility test = new LockInterruptibility();
        System.out.println(this);
//        Class<LockInterruptibility> lockInterruptibilityClass = LockInterruptibility.class;
        System.out.println(LockInterruptibility.class);
        Thread t1 = new Thread(this::nonStatic);
        Thread t4 = new Thread(this::nonStatic2);
        Thread t2 = new Thread(LockInterruptibility::oneStatic);
        Thread t3 = new Thread(LockInterruptibility::twoStatic);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
    }

    public static void main(String[] args) {
//        LockInterruptibility test = new LockInterruptibility();
//        Thread t1 = new Thread(test::nonStatic);
//        Thread t2 = new Thread(LockInterruptibility::oneStatic);
//
//        t1.start();
//        t2.start();
//
//        t1.join();
//        t2.join();

        LockInterruptibility test = new LockInterruptibility();
        MyThread thread1 = new MyThread(test);
        MyThread thread2 = new MyThread(test);
        thread1.start();
        thread2.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread1.interrupt();
    }

    public void insert(Thread thread) throws InterruptedException {
        lock.lockInterruptibly();   //注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
//        lock.lock();
        try {
            System.out.println(thread.getName() + "得到了锁");
            long startTime = System.currentTimeMillis();
            for (; ; ) {
                if (System.currentTimeMillis() - startTime >= 100000)
                    break;
                //插入数据
//                System.out.println(Thread.currentThread().isInterrupted());
            }

        } finally {
            System.out.println(Thread.currentThread().getName() + "执行finally");
            lock.unlock();
            System.out.println(thread.getName() + "释放了锁");
        }
    }

    //  private final Lock lock = new ReentrantLock();






}




class MyThread extends Thread {
    private LockInterruptibility test = null;

    public MyThread(LockInterruptibility test) {
        this.test = test;
    }

    @Override
    public void run() {
        try {
            test.insert(Thread.currentThread());
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName() + "被中断");
        }
    }


}
