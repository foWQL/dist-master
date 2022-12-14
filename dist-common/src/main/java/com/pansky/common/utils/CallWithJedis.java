package com.pansky.common.utils;

import redis.clients.jedis.Jedis;

@FunctionalInterface
public interface CallWithJedis {
    void call(Jedis jedis);
}
