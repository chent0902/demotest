package com.family.demotest.common.biz;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.family.demotest.common.nosql.redis.DefaultRedisOperate;

@Component
public class IntegerRedisBiz
{

    @Autowired
    private DefaultRedisOperate<String, Integer> redisOperate;

    public void put(String key, Integer value)
    {
        redisOperate.set(key, value);
    }

    public void put(String key, Integer value, int time, TimeUnit unit)
    {
        redisOperate.set(key, value, time, unit);
    }

    public Integer get(String key)
    {
        return redisOperate.get(key);
    }

    public void del(String key)
    {
        redisOperate.del(key);
    }

    public boolean exists(String key)
    {
        return redisOperate.exists(key);
    }

}
