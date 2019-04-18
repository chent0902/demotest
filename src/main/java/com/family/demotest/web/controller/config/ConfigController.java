package com.family.demotest.web.controller.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 首页广告
 * 
 * @author wujf
 *
 */
@Controller
@RequestMapping("/config")
public class ConfigController
    extends
    BaseController
{

    @Autowired
    private ConfigService configService;

    /**
     * 通过商家编号获取配置信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getInfo.do")
    @ResponseBody
    public String getInfo(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("渠道号不能为空");
            return JSON.toJSONString(result);
        }

        BaseConfigModel config = configService.findByChannelNo(channelNo);

        result.setData(config);
        // 设置跨域访问
        setCrosDomain(response);

        return JSON.toJSONString(result);
    }

    /**
     * 生成带参公众号二维码
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getQrCode.do")
    @ResponseBody
    public String getQrCode(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo"); // 商家编号
        String userId = request.getParameter("userId"); // 用户id
        String lotteryId = request.getParameter("lotteryId");// 活动id

        if(isBlank(channelNo)||isBlank(userId)||isBlank(lotteryId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = configService.getQrCode(channelNo, userId, lotteryId);

        // 设置跨域访问
        setCrosDomain(response);

        return JSON.toJSONString(result);
    }

}
