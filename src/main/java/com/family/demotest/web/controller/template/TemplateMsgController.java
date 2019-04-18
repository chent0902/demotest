package com.family.demotest.web.controller.template;

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
@Controller
@RequestMapping("/templateMsg")
public class TemplateMsgController
    extends
    BaseController
{

    @Autowired
    private TemplateMsgService templateMsgService;

    /**
     * 
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getMsg.do")
    @ResponseBody
    public String getMsg(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("渠道号不能为空");
            return JSON.toJSONString(result);
        }

        // List<TemplateMsgModel> list = templateMsgService.getList(channelNo);

        TemplateMsgModel model = templateMsgService.findByType(channelNo, 1);

        result.setData(model);

        return JSON.toJSONString(result);
    }

}
