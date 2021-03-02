package com.waiwaiwai.demo.thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 *  斐波那契数列 使用 fork join 计算
 */
public class MyFibonacii {

    private final ForkJoinPool fjp = new ForkJoinPool(4);

    @Test
    public void test() {
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < 30; i++) {
            Fibonacii fibonacii = new Fibonacii(i);
            Integer invoke = fjp.invoke(fibonacii);
            list.add(invoke);
        }
        list.forEach(System.out::println);
//        System.out.println(invoke);

    }


    public static class Fibonacii extends RecursiveTask<Integer> {

        private final Integer num;

        public Fibonacii(int count) {
            this.num = count;
        }

        @Override
        protected Integer compute() {
            // 0 1 1 2 3 5
            if (num <= 1) {
                return num;
            }

            Fibonacii f1 = new Fibonacii(num - 1);
            // 这里启动了一个额外的线程 (使用了异步任务)
            f1.fork();
            Fibonacii f2 = new Fibonacii(num - 2);
            // 这里调用方法 还是在主线程里面执行
            Integer compute = f2.compute();
            // 主线程的计算结果加上 异步任务的计算结果
            int result = compute + f1.join();

            return result;
        }
    }

}
