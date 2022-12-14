package com.pansky.user.controller;

import com.pansky.feign.entiy.ResCode;
import com.pansky.feign.entiy.SysEx;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fo
 * @date 2022/10/19 14:07
 */
@RestController
public class ExController {
    @GetMapping("/no-ex")
    public ResponseEntity<String> noThrowsEx(){

        return ResponseEntity.ok("noThrowsEx");
    }

    @GetMapping("/has-ex")
    public ResponseEntity<String> hasThrowsEx(@RequestParam Integer code){
        switch (code){
            case 1:
                throw new SysEx(ResCode.E_1000);
            default:
                return ResponseEntity.ok("hasThrowsEx");
        }
    }



}
