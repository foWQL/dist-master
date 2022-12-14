package com.pansky.common.utils;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author Fo
 * @date 2022/11/13 19:03
 */
@Component
public class JedisUtil {
    private JedisPool jedisPool;

    /**
     * 返回Redis对象
     */
    public JedisUtil(){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        //连接池最大空闲数
        config.setMaxIdle(300);
        //最大连接数
        config.setMaxTotal(1000);
        //连接最大等待时间
        config.setMaxWaitMillis(30000);
        //在空闲时检查有效性
        config.setTestOnBorrow(true);
        jedisPool=new JedisPool(config,"127.0.0.1",6379,30000,null);
    }

    public void execute(CallWithJedis callWithJedis){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            callWithJedis.call(jedis);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }
}

