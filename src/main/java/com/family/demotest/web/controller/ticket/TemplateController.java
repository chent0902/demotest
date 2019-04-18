package com.family.demotest.web.controller.ticket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.service.wxopen.WxTemplateService;
import com.family.demotest.vo.wx.TemplateMsgSendVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 微信公众账号模版消息
 */
@Controller
@RequestMapping("/template")
public class TemplateController
    extends
    BaseController
{

    @Autowired
    private WxTemplateService wxTemplateService;

    @RequestMapping("/send.do")
    @ResponseBody
    public String send(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        TemplateMsgSendVo sendVo = new TemplateMsgSendVo();
        sendVo.setChannelNo(channelNo);

        result = wxTemplateService.sendMsg(sendVo);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/setIndustry.do")
    @ResponseBody
    public String setIndustry(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = wxTemplateService.setIndustry(channelNo);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/getIndustry.do")
    @ResponseBody
    public String getIndustry(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = wxTemplateService.getIndustry(channelNo);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/getTemplateId.do")
    @ResponseBody
    public String getTemplateId(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        String templateIdShort = request.getParameter("templateIdShort");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        // templateIdShort = "OPENTM405464954";
        result = wxTemplateService.getTemplateId(channelNo, templateIdShort);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/getTemplateList.do")
    @ResponseBody
    public String getTemplateList(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = wxTemplateService.getTemplateList(channelNo);
        return JSON.toJSONString(result);
    }

    @RequestMapping("/deleteTemplate.do")
    @ResponseBody
    public String deleteTemplate(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        String templateId = request.getParameter("templateId");
        if(validator.isBlank(channelNo)||validator.isBlank(templateId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = wxTemplateService.deleteTemplate(channelNo, templateId);
        return JSON.toJSONString(result);
    }
}
