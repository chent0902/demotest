package com.family.demotest.web.controller.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.family.demotest.service.payment.WxPaymentService;
import com.family.demotest.web.controller.BaseController;

/**
 * 银联支付
 * 
 * @author wujf
 *
 */
@Controller
@RequestMapping("/payment")
public class PaymentController
    extends
    BaseController
{

    @Autowired
    private WxPaymentService wxpaymentService;

    /**
     * 微信异步回调通知接口
     * 
     * @param request
     * @param response
     */

    @RequestMapping("/wxcallback.do")
    @ResponseBody
    public void wxCallback(HttpServletRequest request, HttpServletResponse response)
    {

        String result = wxpaymentService.callback(request);
        OputHtml(response, result);

    }

}
