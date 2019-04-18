package com.family.demotest.service.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.util.Logger;
import com.family.demotest.service.lottery.LotteryService;
import com.family.demotest.service.user.UserService;
import com.family.demotest.web.util.ResultCode;

@Service("manage.service")
public class ManageServiceImpl
    implements
    ManageService
{

    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private UserService userService;
    @Autowired
    private Logger logger;

    @Override
    public ResultCode homeData()
    {
        ResultCode result = new ResultCode();

        JSONObject resultJson = new JSONObject();
        try
        {
            // 总数据 昨日活动 本月活动 上月活动 总活动数
            JSONObject lotteryData = lotteryService.homeData();
            resultJson.put("lotteryData", lotteryData);
            // 昨日用户 本月用户 上月用户 总用户数
            JSONObject userData = userService.homeData();
            resultJson.put("userData", userData);
            // 财务统计 昨日收入 本月收入 上月收入 总收入
            // JSONObject moneyData = new JSONObject();
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取总后台首页数据出差");
            logger.error(e, "获取总后台首页数据出差");
        }

        result.setData(resultJson);
        return result;
    }

}
