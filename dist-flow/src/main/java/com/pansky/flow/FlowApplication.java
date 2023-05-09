package com.pansky.flow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

/**
 * @author Fo
 * @date 2023/3/25 14:04
 */
@SpringBootApplication
@MapperScan("com.pansky.flow.dao")
public class FlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlowApplication.class, args);
    }
}
