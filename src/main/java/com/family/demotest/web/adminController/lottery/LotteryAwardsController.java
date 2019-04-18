package com.family.demotest.web.adminController.lottery;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.entity.LotteryAwardsModel;
import com.family.demotest.service.lottery.LotteryAwardsService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller("adminLotteryAwardsController")
@RequestMapping("/admin/lotteryAwards")
public class LotteryAwardsController
    extends
    BaseController
{

    @Autowired
    private LotteryAwardsService lotteryAwardsService;

    /**
     * 抽奖列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/list.do")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String data = request.getParameter("data");
        JSONObject dataJson = JSONObject.parseObject(data);
        String channelNo = dataJson.getString("channelNo");
        String lotteryId = dataJson.getString("lotteryId");

        if(validator.isBlank(channelNo)||validator.isBlank(lotteryId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = lotteryAwardsService.list(channelNo, lotteryId);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 保存抽奖信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/save.do")
    @ResponseBody
    public String save(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String data = request.getParameter("data");
            List<LotteryAwardsModel> list = JSON.parseArray(data, LotteryAwardsModel.class);
            if(list.size()!=5)
            {
                result.setCode(-1);
                result.setInfo("奖品数量不对");
                return JSON.toJSONString(result);
            }
            result = lotteryAwardsService.saveOrUpdate(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存奖项出错");
            logger.error(e, "保存奖项出错");
        }

        return JSON.toJSONString(result);
    }
}
