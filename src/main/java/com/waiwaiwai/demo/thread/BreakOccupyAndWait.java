package com.waiwaiwai.demo.thread;

import com.waiwaiwai.demo.domain.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 破坏循环并等待条件
 */
public class BreakOccupyAndWait {

    // 需要一个管理员
    private List<Object> als = new ArrayList<>();

    // 判断是否已经被使用了
    private synchronized boolean apply(Object from, Object to) {
        if (als.contains(from) || als.contains(to)) {
            return false;
        }
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        als.add(from);
        als.add(to);
        return true;
    }

    // 移除该资源
    private synchronized void remove(Object from, Object to) {
        als.remove(from);
        als.remove(to);
    }

    // 转账
    public void transfer(Account from, Account target, int amt) {
        while (!apply(from, target)) {
            try {
                synchronized (from) {
                    synchronized (target) {
                        if (from.getBalance() > amt) {
                            from.setBalance(from.getBalance() - amt);
                            target.setBalance(target.getBalance() + amt);
                        }
                    }
                }
            } finally {
                remove(this, target);
            }
        }
    }
}
