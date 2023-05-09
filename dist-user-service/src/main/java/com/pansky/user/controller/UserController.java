package com.pansky.user.controller;

import com.pansky.user.entity.User;
import com.pansky.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author  Fo
 * @date 2022/7/12 22:25
 */
@Slf4j
@RestController
@RequestMapping("/user/")
public class UserController {


    @Autowired
    private UserService userService;

    @Value("${logging.tx}")
    private String tx;

    @GetMapping("/gray")
    public String gray() {
        System.out.println("console : k8s gray --- 1" );
        String s = "k8s gray --- 1";
        System.out.println(tx);
        return s ;
    }

    @GetMapping("{id}")
    public User findById(@PathVariable("id") String id) {
        return userService.findById(id);
    }

    @PostMapping("/updateNormal")
    public ResponseEntity<String> updateNormal(@RequestParam("id") String id, @RequestParam("age") int age)  {
        userService.updateUser(id, age);
        return ResponseEntity.ok("更新成功");
    }



    @PostMapping("/updateUser")
    public ResponseEntity<String>  updateUser(@RequestParam("id") String id, @RequestParam("age") int age)  {
        userService.updateUser(id, age);
        return ResponseEntity.ok("成功");
    }

    @PostMapping("/insertselect")
    public void  insertselect(String id,String username,String address,int age)  {
        userService.insertSelect(id,username,address,age);
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d  yyyy", Locale.ENGLISH);
        Date date = new Date();
        String s = sdf.format(date);
        System.out.println(  date);
        System.out.println(  s);
    }
}
