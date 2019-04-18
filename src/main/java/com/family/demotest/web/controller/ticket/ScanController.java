package com.family.demotest.web.controller.ticket;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.service.wxopen.TicketService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;
import com.family.demotest.web.util.WxOpenUtil;

/**
 * 微信扫一扫功能
 */
@Controller
@RequestMapping("/scan")
public class ScanController
    extends
    BaseController
{

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ConfigService configService;

    /**
     * 获取微信扫一扫信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getScanInfo.do")
    @ResponseBody
    public String getScanInfo(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String channelNo = request.getParameter("channelNo");// 商家编号
        String jsurl = request.getParameter("jsurl");

        if(validator.isBlank(channelNo)||validator.isBlank(jsurl))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        try
        {
            BaseConfigModel config = configService.findByChannelNo(channelNo);
            if(config==null)
            {
                result.setCode(-1);
                result.setInfo("该公众号配置不存在");
                return JSON.toJSONString(result);
            }

            String jsapi_ticket = ticketService.getJsApiTicket(channelNo);
            if(validator.isBlank(jsapi_ticket))
            {
                result.setCode(-1);
                result.setInfo("获取ticket失败，请重新获取");
                return JSON.toJSONString(result);
            }
            String timestamp = String.valueOf(System.currentTimeMillis()/1000);
            String nonceStr = WxOpenUtil.generateNonceStr();
            String signature = WxOpenUtil.getSignature(jsapi_ticket, timestamp, nonceStr, jsurl);
            Map<String, Object> wxcon = new HashMap<String, Object>();
            wxcon.put("appId", config.getAppid());
            wxcon.put("timestamp", timestamp);
            wxcon.put("nonceStr", nonceStr);
            wxcon.put("signature", signature);
            result.setData(wxcon);
        }
        catch(Exception e)
        {
            logger.error(e, "获取微信jsapi 签名出错");
            result.setCode(-1);
            result.setInfo("获取微信jsapi 签名出错");
        }
        return JSON.toJSONString(result);
    }

}
