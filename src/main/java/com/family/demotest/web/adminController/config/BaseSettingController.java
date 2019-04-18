package com.family.demotest.web.adminController.config;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.demotest.entity.BaseSettingModel;
import com.family.demotest.service.base.SettingService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 分享设置
 * 
 * @author wujf
 *
 */
@Controller("adminBaseSettingController")
@RequestMapping("/admin/base/setting")
public class BaseSettingController
    extends
    BaseController
{
    @Autowired
    private SettingService settingService;

    /**
     * 保存/修改分享设置
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/shareSetting.do")
    @ResponseBody
    public String siteSetting(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        try
        {
            String data = request.getParameter("data");
            JSONObject jsonObject = JSONObject.parseObject(data);
            String channelNo = jsonObject.getString("channelNo");
            String shareLogo = jsonObject.getString("shareLogo");// 分享logo
            String shareTitle = jsonObject.getString("shareTitle");// 分享标题
            String followReply = jsonObject.getString("followReply");// 自定义回复
            if(validator.isBlank(channelNo))
            {
                result.setCode(-1);
                result.setInfo("渠道号不能为空");
                return JSON.toJSONString(result);
            }

            BaseSettingModel settingModel = settingService.findByChannelNo(channelNo);
            if(settingModel==null)
            {
                settingModel = new BaseSettingModel();
                settingModel.setChannelNo(channelNo);
                settingModel.setCreateTime(new Date());
            }

            settingModel.setShareLogo(shareLogo);
            settingModel.setShareTitle(shareTitle);
            settingModel.setFollowReply(followReply);

            settingService.save(settingModel);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("修改分享设置出错");
            logger.error(e, "修改分享设置出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 通过商家编号获取分享设置信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getShareInfo.do")
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
