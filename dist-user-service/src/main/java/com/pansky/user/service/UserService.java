package com.pansky.user.service;

import com.pansky.user.entity.User;
import com.pansky.user.dao.UserMapper;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author  Fo
 * @date 2022/7/12 22:24
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    UService uService;

    public User findById(String id) {
        return userMapper.findById(id);
    }

    @Transactional
    public void insertUser(String id,String username,String address,int age){
         userMapper.insertUser(id,username,address,age);
    }



    @Transactional
    public void insertSelect(String id,String username,String address,int age){
        this.insertUser(id,username,address,age);
        User user = uService.findById(id);
        System.out.println("---------------"+user.getUsername());
    }

    @Transactional
    public String updateUser(String id, int age) {
        log.info("user服务中全局事务id=================>{}", RootContext.getXID());
        userMapper.updateUser(id, age);

        if (id.equals("101")) {
            throw new RuntimeException("异常:模拟业务异常:user branch exception");
        }

        //睡眠10s, 模拟响应超时
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "success";

    }

}
