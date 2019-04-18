package com.family.demotest.service.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.util.Logger;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.service.lottery.LotteryOrderService;
import com.family.demotest.service.lottery.LotteryService;
import com.family.demotest.service.user.UserService;
import com.family.demotest.web.util.ResultCode;

@Service("home.service")
public class HomeServiceImpl
    implements
    HomeService
{

    @Autowired
    private LotteryOrderService lotteryOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private Logger logger;
    @Autowired
    private ConfigService configService;

    @Override
    public ResultCode homeData(String channelNo)
    {
        ResultCode result = new ResultCode();

        // 返回json
        JSONObject resultJson = new JSONObject();
        try
        {
            BaseConfigModel config = configService.findByChannelNo(channelNo);
            if(config==null)
            {
                result.setCode(-1);
                result.setInfo("加载首页数据出错");
                return result;
            }
            result.setOther(Integer.valueOf(config.getActivePoint()).toString());

            JSONObject numJson = new JSONObject();
            numJson.put("activePoint", config.getActivePoint());// 活动点数
            numJson.put("smsNum", config.getSmsNum());// 短信数量
            resultJson.put("numJson", numJson);

            // 领取json
            JSONObject receiveJson = lotteryOrderService.getReceiveJson(channelNo);
            resultJson.put("receiveJson", receiveJson);

            // 用户json
            JSONObject userJson = userService.getUserJson(channelNo);
            resultJson.put("userJson", userJson);

            // 活动json
            JSONObject lotteryJson = lotteryService.getLotteryJson(channelNo);
            resultJson.put("lotteryJson", lotteryJson);
            result.setData(resultJson);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载首页数据出错");
            logger.error(e, "加载首页数据出错");
        }

        return result;
    }
}
