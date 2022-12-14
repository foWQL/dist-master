package com.pansky.order;

import com.pansky.feign.clients.UserClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author  Fo
 * @date 2022/7/12 22:37
 */
@EnableFeignClients(basePackages = "com.pansky.feign.clients")
@SpringBootApplication
@MapperScan("com.pansky.order.mapper")
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
