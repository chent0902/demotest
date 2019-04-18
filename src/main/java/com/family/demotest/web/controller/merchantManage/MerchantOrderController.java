package com.family.demotest.web.controller.merchantManage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.entity.LotteryModel;
import com.family.demotest.service.lottery.LotteryOrderService;
import com.family.demotest.service.lottery.LotteryService;
import com.family.demotest.vo.user.UserVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller
@RequestMapping("/merchant/order")
public class MerchantOrderController
    extends
    BaseController
{

    @Autowired
    private LotteryOrderService lotteryOrderService;

    @Autowired
    private LotteryService lotteryService;

    /**
     * 根据核销码查订单
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/findByUsecode.do")
    @ResponseBody
    public String findByUsecode(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String merchantId = request.getParameter("merchantId");
        String usecode = request.getParameter("usecode");
        if(validator.isBlank(merchantId)||validator.isBlank(usecode))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = lotteryOrderService.findByUsecode(merchantId, usecode);

        return JSON.toJSONString(result);
    }

    /**
     * 确认核销
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/check.do")
    @ResponseBody
    public String check(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String orderId = request.getParameter("orderId");
        String token = request.getParameter("token");
        String channelNo = request.getParameter("channelNo");
        if(validator.isBlank(orderId)||validator.isBlank(token))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        UserVo user = this.getUserInfo(token);
        if(user==null||!channelNo.equals(user.getChannelNo()))
        {
            result.setCode(999);
            result.setInfo("token失效");
            return JSON.toJSONString(result);
        }

        result = lotteryOrderService.check(orderId, user.getId(), user.getNickName());

        return JSON.toJSONString(result);
    }

    /**
     * 核销列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/checkList.do")
    @ResponseBody
    public String checkList(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String merchantId = request.getParameter("merchantId");
        String lotteryId = request.getParameter("lotteryId");// 不传或者传0
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        int page = this.getIntValue(request, "page", 1);
        int pageSize = this.getIntValue(request, "length", 10);
        if(validator.isBlank(merchantId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = lotteryOrderService.checkList(merchantId, lotteryId, startTime, endTime, page, pageSize);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 核销列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getLotteryList.do")
    @ResponseBody
    public String getLotteryList(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String merchantId = request.getParameter("merchantId");// 商户id
        if(validator.isBlank(merchantId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        List<LotteryModel> list = lotteryService.getLotteryList(merchantId);
        result.setData(list);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

}
