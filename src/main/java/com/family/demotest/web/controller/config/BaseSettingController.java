package com.family.demotest.web.controller.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.entity.BaseSettingModel;
import com.family.demotest.service.base.SettingService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 站点
 * 
 * @author wujf
 *
 */
@Controller("baseConfigController")
@RequestMapping("/base/setting")
public class BaseSettingController
    extends BaseController
{
    @Autowired
    private SettingService settingService;

    /**
     * 通过商家编号获取分享设置信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/get.do")
    @ResponseBody
    public String getShareInfo(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("商家编号不能为空");
            return JSON.toJSONString(result);
        }

        BaseSettingModel settingModel = settingService.findByChannelNo(channelNo);
        result.setData(settingModel);
        return JSON.toJSONString(result);
    }

}
