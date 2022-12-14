package com.pansky.user.service;

import com.pansky.user.dao.UserMapper;
import com.pansky.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author  Fo
 * @date 2022/7/12 22:24
 */
@Service
@Slf4j
public class UService {

    @Autowired
    private UserMapper userMapper;

    @Async
    public User findById(String id) {
        return userMapper.findById(id);
    }

    @Transactional
    @Async
    public void insertUser(String id,String username,String address,int age){
         userMapper.insertUser(id,username,address,age);
    }



}
