package com.family.demotest.web.adminController.template;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.entity.TemplateMsgModel;
import com.family.demotest.service.template.TemplateMsgService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 模版消息
 * 
 * @author wujf
 *
 */
@Controller("adminTemplateMsgController")
@RequestMapping("/admin/template")
public class TemplateMsgController
    extends BaseController
{

    @Autowired
    private TemplateMsgService templateMsgService;

    /**
     * 添加模块消息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/addTemplate.do")
    @ResponseBody
    public String addTemplate(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        // 1.
        // 到账通知，2-抽奖结果通知，3-核销成功通知，4-服务到期提醒,5-活动加入成功通知,6-活动成行通知,7-活动审核结果通知,8-报名成功通知,9-报名结果通知
        String type = request.getParameter("type");

        if(validator.isBlank(channelNo)||isBlank(type))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        TemplateMsgModel msg = templateMsgService.findByType(channelNo, Integer.parseInt(type));
        if(msg!=null)
        {
            result.setCode(-1);
            result.setInfo("消息模板已存在");
            return JSON.toJSONString(result);
        }
        result = templateMsgService.addTemplate(channelNo, Integer.parseInt(type));

        return JSON.toJSONString(result);
    }

    /**
     * 获取模块消息列表
     * 
     * @param request
     * @param response
     * @return
     */
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

        List<TemplateMsgModel> list = templateMsgService.getList(channelNo);

        result.setData(list);
        // 设置跨域访问
        setCrosDomain(response);
        return JSON.toJSONString(result);
    }

    /**
     * 删除模块消息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/delete.do")
    @ResponseBody
    public String delete(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String id = request.getParameter("id");

        if(isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("ID不能为空");
            return JSON.toJSONString(result);
        }

        templateMsgService.delete(id);

        return JSON.toJSONString(result);
    }

}
