package com.family.demotest.service.payment;

import java.io.BufferedReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.family.base01.util.Logger;
import com.family.base01.util.Validator;
import com.family.demotest.common.enumtype.PaySourceType;
import com.family.demotest.common.wxpay.WXPay;
import com.family.demotest.common.wxpay.WXPayUtil;
import com.family.demotest.entity.WxpayOrderModel;
import com.family.demotest.service.activityPackage.ActivityPackageOrderService;
import com.family.demotest.service.oss.OssService;
import com.family.demotest.service.sms.SmsOrderService;
import com.family.demotest.vo.wx.WxpayParamVo;
import com.family.demotest.web.util.IpUtils;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * 微信支付相关接口
 * 
 * @author wujf
 *
 */
@Service("wx.payment.service")
public class WxPaymentServiceImpl
    implements
    WxPaymentService
{
    @Autowired
    private Logger logger;
    @Autowired
    private Validator validator;
    @Autowired
    private WxpayOrderService wxpayOrderService;
    @Autowired
    private OssService ossService;
    @Autowired
    private ActivityPackageOrderService packageOrderService;

    @Autowired
    private SmsOrderService smsOrderService;

    // 存放各个渠道支付类
    private Map<String, WXPay> wxPayMap = new HashMap<String, WXPay>();

    @Value("${weixin.payment.cert.path}")
    private String wxCertPath;
    @Value("${weixin.payment.callback.url}")
    private String wxCallbackUrl;

    @Value("${weixin.payment.config.appId}")
    private String appId;
    @Value("${weixin.payment.config.mchId}")
    private String mchId;
    @Value("${weixin.payment.config.payKey}")
    private String payKey;

    /**
     * 获取支付类
     * 
     * @return
     */
    private WXPay getWxpay()
        throws Exception
    {
        WXPay wxpay = wxPayMap.get(appId);
        if(wxpay==null)
        {
            WXPayConfigImpl wxpayconfig = new WXPayConfigImpl(appId, mchId, payKey, wxCertPath);
            wxpay = new WXPay(wxpayconfig);
            wxPayMap.put(appId, wxpay);
        }

        return wxpay;
    }

    @Override
    public ResultCode scanPay(WxpayParamVo reqParam)
    {

        ResultCode result = new ResultCode();
        String reqMsgId = reqParam.getOutTradeNo();// 请求流水号
        try
        {
            Map<String, String> params = new HashMap<String, String>();

            params.put("appid", appId); // 微信分配的小程序ID
            params.put("mch_id", mchId);// 商户号
            params.put("total_fee", reqParam.getTotalAmount());// 总金额
            params.put("out_trade_no", reqParam.getOutTradeNo());// 商户订单号
            params.put("body", reqParam.getSubject());// 商品描述
            params.put("notify_url", wxCallbackUrl);// 微信公众号支付回调地址
            params.put("trade_type", "NATIVE ");// 交易类型(扫码)

            if(validator.isBlank(reqParam.getIp()))
            {
                params.put("spbill_create_ip", IpUtils.getLocalIpAddr());// 终端IP
            }
            else
            {
                params.put("spbill_create_ip", reqParam.getIp());// 终端IP
            }

            logger.info("***微信扫码支付请求参数:"+JSON.toJSONString(params));
            Map<String, String> resMap = this.getWxpay().unifiedOrder(params);

            if(logger.isInfoEnable())
            {
                logger.info("微信扫码支付支付，返回结果："+JSON.toJSONString(resMap));
            }

            if(resMap==null)
            {
                result.setCode(-1);
                result.setInfo("微信扫码支付出错");
                return result;
            }

            if("SUCCESS".equals(resMap.get("return_code")))
            {
                if("SUCCESS".equals(resMap.get("result_code")))
                {
                    // 生成充值记录，保存到数据库
                    WxpayOrderModel order = new WxpayOrderModel();
                    order.setPaySource(reqParam.getPaySource());// 支付来源
                    order.setPayNo(reqMsgId);
                    order.setSubject(reqParam.getSubject());// 标题
                    order.setOrderId(reqParam.getOrderId());
                    order.setPrepayId(resMap.get("prepay_id"));// 预支付交易会话标识
                    order.setTotalFee(Integer.parseInt(reqParam.getTotalAmount()));// 数据库保存分
                    order.setCreateTime(new Date());
                    // 保存订单
                    wxpayOrderService.save(order);

                    String codeUrl = resMap.get("code_url");
                    String qrCodeUrl = ossService.createQrcode(codeUrl);
                    result.setData(qrCodeUrl);
                }
                else
                {
                    result.setCode(-1);
                    result.setInfo(resMap.get("err_msg"));
                    return result;
                }
            }
            else
            {
                result.setCode(-1);
                result.setInfo(resMap.get("return_msg"));
                return result;
            }

        }
        catch(Exception e)
        {
            logger.error(e, "微信扫码支付出错！");
            result.setCode(-1);
            result.setInfo("微信扫码支付出错！");
            return result;
        }

        return result;

    }

    @Override
    public ResultCode wxJsapiPay(WxpayParamVo reqParam)
    {
        ResultCode result = new ResultCode();
        String reqMsgId = reqParam.getOutTradeNo();// 请求流水号
        try
        {
            Map<String, String> params = new HashMap<String, String>();

            params.put("appid", appId); // 微信分配的小程序ID
            params.put("mch_id", mchId);// 商户号
            params.put("total_fee", reqParam.getTotalAmount());// 总金额
            params.put("out_trade_no", reqParam.getOutTradeNo());// 商户订单号
            params.put("body", reqParam.getSubject());// 商品描述
            params.put("notify_url", wxCallbackUrl);// 微信公众号支付回调地址
            params.put("trade_type", "NATIVE ");// 交易类型

            if(validator.isBlank(reqParam.getIp()))
            {
                params.put("spbill_create_ip", IpUtils.getLocalIpAddr());// 终端IP
            }
            else
            {
                params.put("spbill_create_ip", reqParam.getIp());// 终端IP
            }

            logger.info("***公众号支付请求参数:"+JSON.toJSONString(params));
            Map<String, String> resMap = this.getWxpay().unifiedOrder(params);

            if(logger.isInfoEnable())
            {
                logger.info("微信公众号支付，返回结果："+JSON.toJSONString(resMap));
            }

            if(resMap==null)
            {
                result.setCode(-1);
                result.setInfo("微信支付出错");
                return result;
            }

            if("SUCCESS".equals(resMap.get("return_code")))
            {
                if("SUCCESS".equals(resMap.get("result_code")))
                {
                    // 生成充值记录，保存到数据库
                    WxpayOrderModel order = new WxpayOrderModel();
                    order.setPayNo(reqMsgId);
                    order.setSubject("吃探-活动点充值");
                    order.setOrderId(reqParam.getOrderId());
                    order.setPrepayId(resMap.get("prepay_id"));// 预支付交易会话标识
                    order.setTotalFee(Integer.parseInt(reqParam.getTotalAmount()));// 数据库保存分
                    order.setCreateTime(new Date());
                    // 保存订单
                    wxpayOrderService.save(order);

                    String codeUrl = resMap.get("code_url");
                    String qrCodeUrl = ossService.createQrcode(codeUrl);
                    result.setData(qrCodeUrl);
                }
                else
                {
                    result.setCode(-1);
                    result.setInfo(resMap.get("err_msg"));
                    return result;
                }
            }
            else
            {
                result.setCode(-1);
                result.setInfo(resMap.get("return_msg"));
                return result;
            }

        }
        catch(Exception e)
        {
            logger.error(e, "微信支付出错！");
            result.setCode(-1);
            result.setInfo("微信支付出错！");
            return result;
        }

        return result;

    }

    /**
     * IO解析获取微信的数据,xml转map
     * 
     * @param request
     * @return
     */
    public Map<String, String> xmlToMap(HttpServletRequest request)
        throws Exception
    {
        BufferedReader reader = null;
        String line = "";
        String xmlString = null;

        reader = request.getReader();
        StringBuffer inputString = new StringBuffer();
        while((line = reader.readLine())!=null)
        {
            inputString.append(line);
        }
        xmlString = inputString.toString();

        // logger.info("******微信回调参数xml格式："+xmlString);

        if(!validator.isBlank(xmlString))
        {
            return WXPayUtil.xmlToMap(xmlString);
        }

        return null;
    }

    @Override
    public String callback(HttpServletRequest request)
    {

        String resultFail = "<xml>"+"<return_code><![CDATA[FAIL]]></return_code>"+"<return_msg><![CDATA[报文为空]]></return_msg>"+"</xml> ";

        String resultSuccess = "<xml>"+"<return_code><![CDATA[SUCCESS]]></return_code>"+"<return_msg><![CDATA[OK]]></return_msg>"+"</xml> ";

        Map<String, String> map = null;
        try
        {
            // map = parseXml(request);
            map = xmlToMap(request);
        }
        catch(Exception e)
        {
            logger.error(e, "微信回调参数解析出错");
            return resultFail;

        }

        logger.info("***微信回调返回结果"+JSON.toJSONString(map));

        if(null==map)
            return resultFail;

        String result_code = map.get("result_code").toString();

        try
        {
            if("SUCCESS".equals(result_code))
            {

                String totalFee = map.get("total_fee").toString();// 订单总金额，单位为分
                String cashFee = map.get("cash_fee").toString();// 现金支付金额订单现金支付金额
                String openid = map.get("openid").toString();// 用户标识
                String outTradeNo = map.get("out_trade_no").toString();// 商户订单号
                String transactionId = map.get("transaction_id").toString(); // 微信支付订单号
                String timeEnd = map.get("time_end").toString();// 支付完成时间

                WxpayOrderModel order = wxpayOrderService.findByPayNo(outTradeNo);
                if(null==order)
                {
                    logger.info("**通过我方订单号："+outTradeNo+",查找不到订单信息");
                    return resultFail;
                }

                if(order.getPayStatus()==1)
                {// 已支付成功
                    return resultSuccess;
                }

                // 设置支付信息
                order.setTransactionId(transactionId);// 支付渠道流水
                order.setOpenid(openid);
                order.setSettleDate(timeEnd==null?"":timeEnd.substring(0, 8));
                order.setTotalFee(Integer.parseInt(totalFee));// 数据库保存分
                if(!validator.isBlank(cashFee))
                {// 卖家付款金额
                    order.setCashFee(Integer.parseInt(cashFee));
                }
                order.setEndTime(timeEnd);
                order.setPayStatus(1);// 支付成功
                wxpayOrderService.save(order);

                if(order.getPaySource()==PaySourceType.POINT.getCode())
                {
                    // 活动点数充值异步回调后处理
                    packageOrderService.wxpayCallback(order);

                }
                else if(order.getPaySource()==PaySourceType.SMS.getCode())
                {
                    // 短信充值,异步回调后处理
                    smsOrderService.wxpayCallback(order);

                }

                return resultSuccess;

            }
            else
            {
                return resultFail;
            }
        }
        catch(Exception e)
        {
            logger.error(e, "支付回调处理结果出错");
        }

        return resultFail;
    }

}
