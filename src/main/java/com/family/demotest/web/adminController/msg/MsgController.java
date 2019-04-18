package com.family.demotest.web.adminController.msg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.service.message.MessageService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 模版消息
 * 
 * @author wujf
 *
 */
@Controller("adminMsgController")
@RequestMapping("/admin/msg")
public class MsgController
    extends BaseController
{

    @Autowired
    private MessageService messageMessage;

    /**
     * 群发模版消息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/groupsMsg.do")
    @ResponseBody
    public String groupsMsg(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        String msgType = request.getParameter("msgType");// 消息类型
        String title = request.getParameter("title");// 标题
        String link = request.getParameter("link");// 链接
        String appid = request.getParameter("appid");// 小程序appid

        if(validator.isBlank(channelNo)||validator.isBlank(msgType)||validator.isBlank(title))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        // 当天是否已群发消息
        boolean isSend = messageMessage.isSend(channelNo);
        if(!isSend)
        {
            result = messageMessage.groupsSendMsg(channelNo, msgType, title, link, appid);
        }
        else
        {
            result.setCode(-1);
            result.setInfo("当天已群发模版消息了");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 群发图文消息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/sendMessage.do")
    @ResponseBody
    public String sendMessage(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        String articles = request.getParameter("articles");
        if(validator.isBlank(channelNo)||validator.isBlank(articles))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        // 当天是否已群发图文消息
        boolean isSend = messageMessage.isSendPicText(channelNo);
        if(!isSend)
        {
            result = messageMessage.sendPicTextMsg(channelNo, articles);
        }
        else
        {
            result.setCode(-1);
            result.setInfo("当天已群发图文消息了");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 图文消息群发记录
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/messageRecord.do")
    @ResponseBody
    public String messageRecord(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        int page = this.getIntValue(request, "page", 1);
        int pageSize = this.getIntValue(request, "length", 10);
        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = messageMessage.messageRecord(channelNo, page, pageSize);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

}
