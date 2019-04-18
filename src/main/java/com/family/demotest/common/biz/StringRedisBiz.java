package com.family.demotest.common.biz;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.family.demotest.common.nosql.redis.DefaultRedisOperate;

/**
 * 字符串缓存辅助类
 * 
 * @author wujf
 */
@Component
public class StringRedisBiz
{

    @Autowired
    private DefaultRedisOperate<String, String> redisOperate;

    public void put(String key, String content, int time, TimeUnit unit)
    {
        redisOperate.set(key, content, time, unit);
    }

    public String get(String key)
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
