package com.family.demotest.common.biz;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.family.demotest.common.constant.Constants;
import com.family.demotest.common.nosql.redis.DefaultRedisOperate;
import com.family.demotest.vo.total.PartnerDataTotalVo;

@Component
public class PartnerSummaryRedisBiz
{
    @Autowired
    private DefaultRedisOperate<String, PartnerDataTotalVo> redisOperate;

    public void put(String key, PartnerDataTotalVo totalVo, int time, TimeUnit unit)
    {
        redisOperate.set(Constants.PARTNER_SUMMARY_KEY+key, totalVo, time, unit);
    }

    public void put(String key, PartnerDataTotalVo totalVo)
    {
        redisOperate.set(Constants.PARTNER_SUMMARY_KEY+key, totalVo);
    }

    public PartnerDataTotalVo get(String key)
    {
        return redisOperate.get(Constants.PARTNER_SUMMARY_KEY+key);
    }

    public void del(String key)
    {
        redisOperate.del(Constants.PARTNER_SUMMARY_KEY+key);
    }

    public boolean exists(String key)
    {
        return redisOperate.exists(Constants.PARTNER_SUMMARY_KEY+key);
    }
}
