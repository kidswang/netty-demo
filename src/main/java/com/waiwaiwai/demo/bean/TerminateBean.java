package com.waiwaiwai.demo.bean;

import javax.annotation.PreDestroy;

/**
 * 在程序关闭之前执行
 */
public class TerminateBean {

    @PreDestroy
    public void preDestroy() {
        System.out.println("TerminalBean is destroyed");
    }

}
