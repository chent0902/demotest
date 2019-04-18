package com.family.demotest.common.biz;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.family.demotest.common.constant.Constants;
import com.family.demotest.common.nosql.redis.DefaultRedisOperate;
import com.family.demotest.vo.total.ImgTextSummaryVo;

@Component
public class ImgTextSummaryRedisBiz
{
    @Autowired
    private DefaultRedisOperate<String, ImgTextSummaryVo> redisOperate;

    public void put(String key, ImgTextSummaryVo totalVo, int time, TimeUnit unit)
    {
        redisOperate.set(Constants.IMG_TEXT_SUMMARY_KEY+key, totalVo, time, unit);
    }

    public void put(String key, ImgTextSummaryVo totalVo)
    {
        redisOperate.set(Constants.IMG_TEXT_SUMMARY_KEY+key, totalVo);
    }

    public ImgTextSummaryVo get(String key)
    {
        return redisOperate.get(Constants.IMG_TEXT_SUMMARY_KEY+key);
    }

    public void del(String key)
    {
        redisOperate.del(Constants.IMG_TEXT_SUMMARY_KEY+key);
    }

    public boolean exists(String key)
    {
        return redisOperate.exists(Constants.IMG_TEXT_SUMMARY_KEY+key);
    }
}
