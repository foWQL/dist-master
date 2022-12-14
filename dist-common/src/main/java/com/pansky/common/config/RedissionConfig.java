package com.pansky.common.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Fo
 * @date 2022/11/13 15:23
 */
@Configuration
public class RedissionConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;

    //@Value("${spring.redis.password}")
    //private String password;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        System.out.println("---------------自定义的Redission");
        Config config = new Config();
        //config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        return Redisson.create(config);
    }
}
