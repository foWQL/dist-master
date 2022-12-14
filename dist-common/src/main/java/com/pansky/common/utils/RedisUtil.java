package com.pansky.common.utils;

/**
 * @author Fo
 * @date 2022/11/13 11:46
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * Author: zhf
 * Date: 2021/6/18 18:18
 * Company: PANSKY
 * Copyright: © 2021 PANSKY All Rights Reserved
 * Description: redis分布式锁工具类
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    // 上锁脚本  有过期时间
    private static final String LOCK_LUA = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then redis.call('expire', KEYS[1], ARGV[2]) return 'true' else return 'false' end";

    // 上锁脚本  不过期
    private static final String LOCK_LUA_PERSIST = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then return 'true' else return 'false' end";


    // 解锁脚本
    private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then redis.call('del', KEYS[1]) end return 'true' ";

    public static final int DEFAULT_SECOND_LEN = 10;

    private final RedisSerializer<String> argsSerializer = new StringRedisSerializer();

    private final RedisSerializer<String> resultSerializer = new StringRedisSerializer();

    /**
     * 如果超时未解锁，视为加锁线程死亡，其他线程可夺取锁
     *
     * @param lockKey        锁住的key
     * @param lockExpireMils 锁住的时长
     * @return boolean
     */
    public boolean lock(String lockKey, long lockExpireMils) {
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            long nowTime = System.currentTimeMillis();
            Boolean acquire = connection.setNX(lockKey.getBytes(), String.valueOf(nowTime + lockExpireMils + 1).getBytes());
            if (acquire) {
                return Boolean.TRUE;
            } else {
                byte[] value = connection.get(lockKey.getBytes());
                if (Objects.nonNull(value) && value.length > 0) {
                    long oldTime = Long.parseLong(new String(value));
                    if (oldTime < nowTime) {
                        //connection.getSet：返回这个key的旧值并设置新值。
                        byte[] oldValue = connection.getSet(lockKey.getBytes(), String.valueOf(nowTime + lockExpireMils + 1).getBytes());
                        //当key不存时会返回空，表示key不存在或者已在管道中使用
                        return oldValue == null ? false : Long.parseLong(new String(oldValue)) < nowTime;
                    }
                }
            }
            return Boolean.FALSE;
        });
    }

    /**
     * 加锁，默认锁的过期时间为10s
     *
     * @param lockKey 锁住的key
     * @param val     key值
     * @return boolean
     */
    public boolean lock(String lockKey, String val) {
        return this.lock(lockKey, val, DEFAULT_SECOND_LEN);
    }

    public boolean lockForPersist(String lockKey, String val) {
        RedisScript lockRedisScript = RedisScript.of(LOCK_LUA_PERSIST, String.class);
        List<String> keys = Collections.singletonList(lockKey);
         String flag = (String) redisTemplate.execute(lockRedisScript, argsSerializer, resultSerializer, keys, val, String.valueOf(""));
        return Boolean.valueOf(flag);
    }

    /**
     * 设置缓存，并加锁,自定义失效时间
     *
     * @param lockKey 锁住的key
     * @param val     值
     * @param second  过期时间，防止死锁
     * @return boolean
     */
    public boolean lock(String lockKey, String val, int second) {
        RedisScript lockRedisScript = RedisScript.of(LOCK_LUA, String.class);
        List<String> keys = Collections.singletonList(lockKey);
        String flag = (String) redisTemplate.execute(lockRedisScript, argsSerializer, resultSerializer, keys, val, String.valueOf(second));
        return Boolean.valueOf(flag);
    }

    /**
     * 通过key和val值解锁，只有满足key值和val值相等才能解锁，防止被其他线程并发解锁
     *
     * @param lockKey 锁的key
     * @param val     值
     */
    public void unlock(String lockKey, String val) {
        RedisScript unLockRedisScript = RedisScript.of(UNLOCK_LUA, String.class);
        List<String> keys = Collections.singletonList(lockKey);
        redisTemplate.execute(unLockRedisScript, argsSerializer, resultSerializer, keys, val);
    }
}
