package com.waiwaiwai.demo.thread;

import java.util.concurrent.locks.StampedLock;

/**
 * @Author: wangzhenglei
 * @DateTime: 2021/2/26 13:39
 * @Description: 比读写(ReentrantReadWriteLock)锁性能更高的一种锁  支持 乐观读、悲观读、写锁
 *
 * StampedLock 不支持重入 也不支持条件变量
 */
public class MyStamptedLock {
    // for example
    private int x, y;
    final StampedLock sl = new StampedLock();

    double distanceFromOrigin() {
        long stamp = sl.tryOptimisticRead();
        int curX = x;
        int curY = y;
        if (!sl.validate(stamp)) {
            // 升级为悲观读
            stamp = sl.readLock();
            try {
                curX = x;
                curY = y;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        return Math.sqrt(curX * curX + curY * curY);
    }
}
