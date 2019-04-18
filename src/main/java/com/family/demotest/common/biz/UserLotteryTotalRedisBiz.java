package com.family.demotest.common.biz;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.family.demotest.common.constant.Constants;
import com.family.demotest.common.nosql.redis.DefaultRedisOperate;
import com.family.demotest.vo.total.UserLotteryTotalVo;

@Component
public class UserLotteryTotalRedisBiz
{
    @Autowired
    private DefaultRedisOperate<String, UserLotteryTotalVo> redisOperate;

    public void put(String key, UserLotteryTotalVo totalVo, int time, TimeUnit unit)
    {
        redisOperate.set(Constants.USER_LOTTERY_TOTAL_KEY+key, totalVo, time, unit);
    }

    public void put(String key, UserLotteryTotalVo totalVo)
    {
        redisOperate.set(Constants.USER_LOTTERY_TOTAL_KEY+key, totalVo);
    }

    public UserLotteryTotalVo get(String key)
    {
        return redisOperate.get(Constants.USER_LOTTERY_TOTAL_KEY+key);
    }

    public void del(String key)
    {
        redisOperate.del(Constants.USER_LOTTERY_TOTAL_KEY+key);
    }

    public boolean exists(String key)
    {
        return redisOperate.exists(Constants.USER_LOTTERY_TOTAL_KEY+key);
    }
}
