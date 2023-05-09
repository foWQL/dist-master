package com.pansky.user.controller;


import com.pansky.user.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * (TbUser)表控制层
 *
 * @author Fo
 * @since 2022-11-12 18:04:35
 */
@RestController
@RequestMapping("redisDis")
public class RedisDisController {
    @Autowired
    private TbUserService tbUserService;

    @PostMapping("/delete")
    public String deleteKey(String key) {
        return this.tbUserService.deleteKey(key);
    }




}

