package com.waiwaiwai.demo.thread;

import com.waiwaiwai.demo.domain.Account;

/**
 * 破坏循环等待条件
 */
public class BreakCycleWait {

    // 破坏循环等待条件 可以给资源按照 id 排个序,申请资源的时候按照从小到大的顺序获取
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
