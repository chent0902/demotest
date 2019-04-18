package com.family.demotest.web.adminController.sms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.service.sms.SmsOrderService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.IpUtils;
import com.family.demotest.web.util.ResultCode;

@Controller("adminSmsController")
@RequestMapping("/admin/sms")
public class SmsController
    extends BaseController
{

    @Autowired
    private SmsOrderService smsOrderService;

    /**
     * 充值套餐
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/payment.do")
    @ResponseBody
    public String buy(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String channelNo = jsonObject.getString("channelNo");
        int smsType = jsonObject.getIntValue("smsType");// 1-50元，2-100元，3-500元

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("");
            return JSON.toJSONString(result);
        }

        if(smsType<1)
        {
            result.setCode(-1);
            result.setInfo("套餐不存在");
            return JSON.toJSONString(result);
        }

        String ip = IpUtils.getIpAddr(request);
        result = smsOrderService.payment(channelNo, smsType, ip);

        return JSON.toJSONString(result);
    }

    /**
     * 充值记录
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/payRecord.do")
    @ResponseBody
    public String payRecord(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String channelNo = jsonObject.getString("channelNo");
        int page = jsonObject.getIntValue("page");
        int pageSize = jsonObject.getIntValue("length");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = smsOrderService.payRecord(channelNo, page, pageSize);

        return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss", SerializerFeature.DisableCircularReferenceDetect);
    }

}
