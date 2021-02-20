package com.waiwaiwai.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class DemoApplication {

    // 优雅的关闭 SpringBoot 将 appId 写到一个文件里面 然后调用 cat /tool/app.id | xargs kill 这样就优雅的关闭了
    public static void main(String[] args) {
//        SpringApplication.run(DemoApplication.class, args);
        SpringApplication application = new SpringApplication(DemoApplication.class);
        application.addListeners(new ApplicationPidFileWriter("/tool/app.pid"));
        application.run(args);
    }

}
