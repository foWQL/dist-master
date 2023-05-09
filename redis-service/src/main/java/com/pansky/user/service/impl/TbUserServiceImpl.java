package com.pansky.user.service.impl;


import com.pansky.user.service.TbUserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * (TbUser)表服务实现类
 *
 * @author Fo
 * @since 2022-11-12 18:04:36
 */
@Slf4j
@Service("tbUserService")
public class TbUserServiceImpl implements TbUserService {


    @Autowired
    private RedisTemplate redisTemplate;




    @Override
    public String deleteKey(String key) {
        String res = "";
        Boolean flag = redisTemplate.hasKey(key);
         if (flag) {
            res  = (String) redisTemplate.opsForValue().get(key);
            System.out.println("key对应的值是 " + res);
             Boolean delete = redisTemplate.delete(key);
             System.out.println("删除对应的key--------"+delete);
         }else {
             System.out.println("不存在对应的key " + key);
         }
         return res;
    }
}
