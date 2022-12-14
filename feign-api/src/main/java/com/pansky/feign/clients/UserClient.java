package com.pansky.feign.clients;

import com.pansky.feign.entiy.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author
 * @date 2022/7/12 22:34
 */
//@FeignClient(name = "userService", contextId = "userService1")
@FeignClient(value = "userService")
public interface UserClient {

    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") String id);

    @PostMapping("/user/updateUser")
    String updateUser(@RequestParam("id")String id , @RequestParam("age")  int age);
}
