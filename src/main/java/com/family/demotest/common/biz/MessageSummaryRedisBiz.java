package com.family.demotest.common.biz;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.family.demotest.common.constant.Constants;
import com.family.demotest.common.nosql.redis.DefaultRedisOperate;
import com.family.demotest.vo.total.MessageSummaryVo;

@Component
public class MessageSummaryRedisBiz
{
    @Autowired
    private DefaultRedisOperate<String, MessageSummaryVo> redisOperate;

    public void put(String key, MessageSummaryVo totalVo, int time, TimeUnit unit)
    {
        redisOperate.set(Constants.MESSAGE_SUMMARY_KEY+key, totalVo, time, unit);
    }

    public void put(String key, MessageSummaryVo totalVo)
    {
        redisOperate.set(Constants.MESSAGE_SUMMARY_KEY+key, totalVo);
    }

    public MessageSummaryVo get(String key)
    {
        return redisOperate.get(Constants.MESSAGE_SUMMARY_KEY+key);
    }

    public void del(String key)
    {
        redisOperate.del(Constants.MESSAGE_SUMMARY_KEY+key);
    }

    public boolean exists(String key)
    {
        return redisOperate.exists(Constants.MESSAGE_SUMMARY_KEY+key);
    }
}
