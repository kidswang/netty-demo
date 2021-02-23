package com.waiwaiwai.demo.test;

import org.apache.logging.log4j.util.Strings;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.function.BiConsumer;

public class MyTest {


    private static final ExecutorService es = Executors.newSingleThreadExecutor();


    @Test
    public void test5() {
        // 获取所有的系统配置信息
        Properties ssl = System.getProperties();
        String str = ssl.toString();
        String[] split = str.split(",");
        for (String s : split) {
            System.out.println(s);
        }
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Future<String> future1 = es.submit(() -> {
            System.out.println("提交任务1");
            Future<String> future2 = es.submit(() -> {
                System.out.println("提交任务2");
                return "任务 2 结果";
            });
            return future2.get();
        });
        System.out.println("fsdfsdfs");
        System.out.println("获取到" + future1.get());
//        System.out.println("获取到" + future1.future2.get());

        ArrayBlockingQueue<String> strings = new ArrayBlockingQueue<>(10);

//        strings.put();
        strings.take();



        new Thread(() -> {

        });
    }


//    public static void main(String[] args) throws InterruptedException {
////        System.out.println(NettyRuntime.availableProcessors());
//        Random random = new Random();
//        int waitTime = random.nextInt(50);
//        System.out.println(waitTime);
//        TimeUnit.MILLISECONDS.sleep(waitTime);
//        System.out.println("12312312");
//
////        Thread.sleep(random.nextInt(30));
////        System.out.println("sdffsdfsdfsd");
//    }

    private final Lock lock = new ReentrantLock(true); // 默认非公平锁
    final Condition notEmpty = lock.newCondition();

    public synchronized void add() {
        lock.lock();
        System.out.println("woshi xian cheng " + Thread.currentThread().getName());
//            try {
//                TimeUnit.SECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        try {
            int i = 1 / 0;

//        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    @Test
    public void test() throws InterruptedException {
        Thread t1 = new Thread(this::add);
        Thread t2 = new Thread(this::add);

        t1.start();
        t2.start();
        t1.join();
        t2.join();


    }

    @Test
    public void testBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String str = "abcde";
        buffer.put(str.getBytes());

        buffer.flip();

        byte[] dst = new byte[buffer.limit()];

        buffer.get(dst);
        System.out.println(new String(dst, 0, dst.length));
        System.out.println(buffer);
    }


    private long count = 0;

    synchronized long get () {
        return count;
    }

    void add10K () {
        int idx = 0;
        while (idx++ < 10000) {
            set(get() + 1);
        }
    }

    synchronized void set (long v){
        count = v;
    }

    @Test
    public void testAdd() throws InterruptedException {
        new ReentrantLock(true);
        Thread t1 = new Thread(this::add10K);
        Thread t2 = new Thread(this::add10K);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(count);

        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readWriteLock.readLock();
        readWriteLock.writeLock();

    }

    @Test
    public void testBoom() {
        double log = Math.log(2);
        System.out.println(log);
    }


}
