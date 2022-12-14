package com.pansky.feign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @author  Fo
 * @date 2022/7/31 21:51
 */
public class DefaultFeignConfiguration {

    @Bean
    public Logger.Level logLevel() {
        return Logger.Level.BASIC;
    }
}
