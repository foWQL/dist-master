package com.pansky.es;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author  Fo
 * @date 2022/7/12 22:25
 */
@SpringBootApplication
@Slf4j
public class ElkApplication {

    public static void main(String[] args) {

        SpringApplication.run(ElkApplication.class, args);
        log.trace("======trace");
        log.debug("======debug");
        log.info("======info");
        log.warn("======warn");
        log.error("======error");
    }
}
