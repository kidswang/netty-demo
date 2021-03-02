package com.waiwaiwai.demo.thread;

import org.junit.Test;
import org.springframework.scheduling.concurrent.ForkJoinPoolFactoryBean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *  fork join test
 */
public class MyForkJoin {


    @Test
    public void test() {
        String[] fc = {"hello world", "hello me", "hello fork", "hello join", "fork join in world"};
        //创建ForkJoin线程池
        ForkJoinPool fjp = new ForkJoinPool(3);
        MR mr = new MR(fc, 0, fc.length);
        Map<String, Long> result = fjp.invoke(mr);

        result.forEach((k, v)-> System.out.println(k+":"+v));

    }

    // 计算文件中重复的单词的数量

    public static class MR extends RecursiveTask<Map<String, Long>> {

        private final String[] fc;
        private final int start;
        private final int end;

        public MR(String[] fc, int start, int end) {
            this.fc = fc;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Map<String, Long> compute() {
            if (end -start == 1) {
                // 拆分统计单词
                return cal(fc[start]);
            }

            int mid = start + (end - start) / 2;
            // (end + start) / 2 => start + (end - start) / 2;
            MR mr1 = new MR(fc, start, mid);
            mr1.fork();
            MR mr2 = new MR(fc, mid, end);
            return merge(mr2.compute(), mr1.join());
        }

        public Map<String, Long> merge(Map<String, Long> mr1, Map<String, Long> mr2) {
            Map<String, Long> result = new HashMap<>(mr1);
            mr2.forEach((k, v) -> result.merge(k, v, Long::sum));
            return result;
        }


        public Map<String, Long> cal(String line) {
            // 分割单词
            String [] words = line.split("\\s+");
            Map<String, Long> result = new HashMap<>();
            for (String word : words) {
                Long count = result.get(word);
                if (count != null) {
                    result.put(word, count + 1);
                } else {
                    result.put(word, 1L);
                }
            }
            return result;
        }
    }


}
