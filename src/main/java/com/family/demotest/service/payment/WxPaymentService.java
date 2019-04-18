package com.family.demotest.service.payment;

import javax.servlet.http.HttpServletRequest;

import com.family.demotest.vo.wx.WxpayParamVo;
import com.family.demotest.web.util.ResultCode;

/**
 * 微信支付接口
 * 
 * @author wujf
 *
 */
public interface WxPaymentService
{

    public ResultCode wxJsapiPay(WxpayParamVo reqParam);

    /**
     * 微信异步通知结果接口
     * 
     * @param request
     * 
     * @return
     */
    public String callback(HttpServletRequest request);

    /**
     * 扫码支付
     * 
     * @param wxpayParam
     * @return
     */
    public ResultCode scanPay(WxpayParamVo wxpayParam);

}
