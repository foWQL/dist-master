package com.pansky.user;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author  Fo
 * @date 2022/7/12 22:25
 */
@SpringBootApplication
@MapperScan("com.pansky.user.dao")
@ComponentScan(basePackages = {"com.pansky.user","com.pansky"})
@Slf4j
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        for (int i = 0; i <10 ; i++) {
            log.warn("用户服务启动日志--{}",i);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
