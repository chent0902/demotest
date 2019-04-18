package com.family.demotest.web.adminController.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.base01.util.PageList;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.vo.base.BaseConfigVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 站点
 * 
 * @author wujf
 *
 */
@Controller("adminConfigController")
@RequestMapping("/admin/config")
public class ConfigController
    extends
    BaseController
{

    @Autowired
    private ConfigService configService;

    /**
     * 获取站点列表
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
        try
        {
            String data = request.getParameter("data");

            JSONObject dataJson = JSONObject.parseObject(data);
            int page = dataJson.getIntValue("page");
            int pageSize = dataJson.getIntValue("length");

            BaseConfigVo model = JSON.parseObject(data, BaseConfigVo.class);

            PageList<BaseConfigModel> pageList = configService.getPageList(model, pageSize, page);
            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取代理商列表出错");
            logger.error(e, "获取代理商列表出错");
        }
        return json(result);
    }

    /**
     * 保存站点信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/saveInfo.do")
    @ResponseBody
    public String save(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String data = request.getParameter("data");
        // data
        BaseConfigVo configVo = JSON.parseObject(data, BaseConfigVo.class);

        if(validator.isBlank(configVo.getId())&&validator.isBlank(configVo.getAccount()))
        {
            result.setCode(-1);
            result.setInfo("账号不能为空");
            return JSON.toJSONString(result);
        }
        result = configService.createOrUpdate(configVo);

        return json(result);
    }

    /**
     * 通过商家编号获取配置信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getInfo2.do")
    @ResponseBody
    public String getInfo2(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("商家编号不能为空");
            return JSON.toJSONString(result);
        }

        BaseConfigModel config = configService.findByChannelNo(channelNo);

        result.setData(config);
        // 设置跨域访问
        setCrosDomain(response);

        return JSON.toJSONString(result);
    }

    /**
     * 通过id获取配置信息
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
        String id = request.getParameter("id");

        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("ID不能为空");
            return JSON.toJSONString(result);
        }

        BaseConfigModel config = configService.findById(id);

        result.setData(config);
        // 设置跨域访问
        setCrosDomain(response);

        return JSON.toJSONString(result);
    }

    /**
     * 通过商家编号获取站点信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getSiteInfo.do")
    @ResponseBody
    public String getSiteInfo(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("商家编号不能为空");
            return JSON.toJSONString(result);
        }

        BaseConfigModel config = configService.findByChannelNo(channelNo);

        BaseConfigModel model = new BaseConfigModel();
        if(config!=null)
        {
            model.setId(config.getId());
            model.setChannelNo(config.getChannelNo());
            model.setSiteName(config.getSiteName()==null?"":config.getSiteName());
            model.setBusinessTel(config.getBusinessTel()==null?"":config.getBusinessTel());
            model.setWxName(config.getWxName()==null?"":config.getWxName());
            model.setForcedAttention(config.getForcedAttention());
            model.setOpenImgtext(config.getOpenImgtext());
            model.setIntroduce(config.getIntroduce());
        }

        result.setData(model);
        // 设置跨域访问
        setCrosDomain(response);

        return JSON.toJSONString(result);
    }

    /**
     * 修改站点设置
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/siteSetting.do")
    @ResponseBody
    public String siteSetting(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        try
        {
            String data = request.getParameter("data");
            JSONObject jsonObject = JSONObject.parseObject(data);
            String channelNo = jsonObject.getString("channelNo");
            int forcedAttention = jsonObject.getIntValue("forcedAttention");// 强制关注状态
                                                                            // 0-不需要关注
                                                                            // 1-需要
            String businessTel = jsonObject.getString("businessTel");// 商户合作电话
            String wxName = jsonObject.getString("wxName");// 商户合作微信号
            String siteName = jsonObject.getString("siteName");// 站点名称
            int openImgtext = jsonObject.getIntValue("openImgtext");// 开启图文列表0-否，1-是
            String introduce = jsonObject.getString("introduce");// 站点介绍

            if(validator.isBlank(channelNo)||validator.isBlank(siteName)||validator.isBlank(businessTel)||validator.isBlank(wxName))
            {
                result.setCode(-1);
                result.setInfo("渠道号不能为空");
                return JSON.toJSONString(result);
            }

            BaseConfigModel config = configService.findByChannelNo(channelNo);
            config.setForcedAttention(forcedAttention);
            config.setBusinessTel(businessTel);
            config.setWxName(wxName);
            config.setSiteName(siteName);
            config.setOpenImgtext(openImgtext);
            config.setIntroduce(introduce);

            // System.out.println(JSON.toJSONString(config));
            configService.save(config);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("修改站点设置出错");
            logger.error(e, "修改站点设置出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 修改活动点数
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/updatePoint.do")
    @ResponseBody
    public String updatePoint(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("商家编号不能空");
            return JSON.toJSONString(result);
        }

        BaseConfigModel config = configService.findByChannelNo(channelNo);
        if(config!=null)
        {
            config.setActivePoint(10000);
            configService.save(config);
        }

        return JSON.toJSONString(result);
    }
}
