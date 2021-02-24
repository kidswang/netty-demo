package com.waiwaiwai.demo.thread;

import com.waiwaiwai.demo.domain.Account;

/**
 * 破坏循环等待条件
 */
public class BreakCycleWait {

    // 破坏循环等待条件 可以给资源按照 id 排个序,申请资源的时候按照从小到大的顺序获取
    // 死锁的四个条件
    // 1.互斥条件,一个资源每次只能被一个线程持有
    // 2.占有且等待 一个线程因请求资源的时候阻塞了,对它持有的资源保持不放
    // 3.不可强行掠夺 进程已获取的资源,在没有使用完之前,不可强行掠夺
    // 4.循环等待 线程A等待线程B持有的资源,线程B等待线程A持有的资源
    public void transfer(Account from, Account target, int amt) {
        Integer left = from.getId();
        Integer right = target.getId();
        if (left > right) {
            left = target.getId();
            right = from.getId();
        }

        // 先锁定小的
        synchronized (left) {
            // 后锁定大的
            synchronized (right) {
                if (from.getBalance() > amt) {
                    from.setBalance(from.getBalance() - amt);
                    target.setBalance(target.getBalance() + amt);
                }
            }
        }
    }
}
