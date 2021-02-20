package com.waiwaiwai.demo.config;

import com.waiwaiwai.demo.bean.TerminateBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 优雅的关闭程序
 */
@Configuration
public class ShutdownConfig {

    @Bean
    public TerminateBean getTerminateBean() {
        return new TerminateBean();
    }


}
