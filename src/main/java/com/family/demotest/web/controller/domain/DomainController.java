package com.family.demotest.web.controller.domain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.service.domain.DomainService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 模版消息
 * 
 * @author wujf
 *
 */
@Controller
@RequestMapping("/domain")
public class DomainController
    extends
    BaseController
{

    @Autowired
    private DomainService domainService;

    /**
     * 获取业务域名
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getBusinessUrl.do")
    @ResponseBody
    public String getBusinessUrl(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("商家编号不能为空");
            return JSON.toJSONString(result);
        }

        String url = domainService.getBusinessDomainUrl(channelNo);

        result.setData(url);
        // 设置跨域访问
        setCrosDomain(response);

        return JSON.toJSONString(result);
    }

}
