package com.family.demotest.common.biz;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.family.demotest.common.constant.Constants;
import com.family.demotest.common.nosql.redis.DefaultRedisOperate;
import com.family.demotest.vo.user.UserVo;

@Component
public class WxUserRedisBiz
{

    @Autowired
    private DefaultRedisOperate<String, UserVo> redisOperate;

    public void put(String key, UserVo vo, int time, TimeUnit unit)
    {

        redisOperate.set(Constants.WX_LOGIN_INFO_TOKEN+key, vo, time, unit);
    }

    public UserVo get(String key)
    {
        return redisOperate.get(Constants.WX_LOGIN_INFO_TOKEN+key);
    }

    public void del(String key)
    {
        redisOperate.del(Constants.WX_LOGIN_INFO_TOKEN+key);
    }

    public boolean exists(String key)
    {
        return redisOperate.exists(Constants.WX_LOGIN_INFO_TOKEN+key);
    }

}
