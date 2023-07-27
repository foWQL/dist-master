package com.pansky.user.controller;

import com.pansky.user.entity.Student;
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

    @GetMapping("/test")
    public User test() {
       User user = new User();
       user.setAge(12345678);
       user.setUsername("张三丰菲菲");
       user.setAddress("中国陕西省西安市");
       System.out.println("user = " + user);
       return user ;

    }

    @GetMapping("/student")
    public Student getStudent(){
        Student student = new Student();
        student.setName("张三三");
        student.setIdCard("44082199612054343");
        student.setBankCard("62173300255879654448");
        student.setFixedPhone("3110026");
        student.setAddress("广东省广州市天河区");
        student.setEmail("1258398545@qq.com");
        student.setRemark("sadhaonsdoasnodnaonodsn是大祭司大祭司你");
        return student;
    }

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
