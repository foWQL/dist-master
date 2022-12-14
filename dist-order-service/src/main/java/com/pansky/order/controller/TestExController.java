package com.pansky.order.controller;

import com.pansky.feign.clients.TestApi;
import com.pansky.feign.entiy.SysEx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fo
 * @date 2022/10/19 14:09
 */
@RestController
@Slf4j
public class TestExController {
    @Autowired
    private TestApi testApi;

    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestParam Integer code) {
        switch (code){
            case 1:
            case 2:
                try {
                    String s = testApi.hasThrowsEx(code);
                    return ResponseEntity.ok(s);
                } catch (SysEx e){
                    log.error(e.getResCode().getMsg());
                    return ResponseEntity.ok("抛出异常");
                }
            case 3:
                return ResponseEntity.ok(testApi.noThrowsEx());
            default:
                return ResponseEntity.ok("异常");
        }
    }

}
