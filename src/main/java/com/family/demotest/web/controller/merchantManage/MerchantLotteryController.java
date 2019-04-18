package com.family.demotest.web.controller.merchantManage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.service.lottery.LotteryService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller
@RequestMapping("/merchant/lottery")
public class MerchantLotteryController
    extends
    BaseController
{

    @Autowired
    private LotteryService lotteryService;

    /**
     * 商户后台活动列表
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
        String channelNo = request.getParameter("channelNo");
        String merchantId = request.getParameter("merchantId");
        int status = this.getIntValue(request, "status", 0); // 0-进行中 1-已结束
                                                             // 2-已过期
        int page = this.getIntValue(request, "page", 1);
        int pageSize = this.getIntValue(request, "length", 10);

        if(validator.isBlank(channelNo)||validator.isBlank(merchantId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = lotteryService.merchantList(channelNo, merchantId, status, page, pageSize);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 商户活动名称列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/nameList.do")
    @ResponseBody
    public String nameList(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        String merchantId = request.getParameter("merchantId");

        if(validator.isBlank(channelNo)||validator.isBlank(merchantId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = lotteryService.nameList(channelNo, merchantId);

        return JSON.toJSONString(result);
    }

}
