package com.pansky.user;

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
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
