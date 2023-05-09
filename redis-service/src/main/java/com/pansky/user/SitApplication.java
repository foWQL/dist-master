package com.pansky.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author  Fo
 * @date 2022/7/12 22:25
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.pansky.user","com.pansky"})
public class SitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SitApplication.class, args);
    }
}
