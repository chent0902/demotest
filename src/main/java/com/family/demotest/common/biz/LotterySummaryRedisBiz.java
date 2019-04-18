package com.family.demotest.common.biz;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.family.demotest.common.constant.Constants;
import com.family.demotest.common.nosql.redis.DefaultRedisOperate;
import com.family.demotest.vo.total.LotterySummaryVo;

@Component
public class LotterySummaryRedisBiz
{
    @Autowired
    private DefaultRedisOperate<String, LotterySummaryVo> redisOperate;

    public void put(String key, LotterySummaryVo totalVo, int time, TimeUnit unit)
    {
        redisOperate.set(Constants.LOTTERY_SUMMARY_KEY+key, totalVo, time, unit);
    }

    public void put(String key, LotterySummaryVo totalVo)
    {
        redisOperate.set(Constants.LOTTERY_SUMMARY_KEY+key, totalVo);
    }

    public LotterySummaryVo get(String key)
    {
        return redisOperate.get(Constants.LOTTERY_SUMMARY_KEY+key);
    }

    public void del(String key)
    {
        redisOperate.del(Constants.LOTTERY_SUMMARY_KEY+key);
    }

    public boolean exists(String key)
    {
        return redisOperate.exists(Constants.LOTTERY_SUMMARY_KEY+key);
    }
}
