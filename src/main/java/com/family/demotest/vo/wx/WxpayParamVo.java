package com.family.demotest.vo.wx;

import java.io.Serializable;

/**
 * 请求参数vo
 * 
 * @author wujf
 */
public class WxpayParamVo
    implements
    Serializable
{

    private static final long serialVersionUID = -2922899219237033109L;

    // 支付参数
    private String channelNo;// 商家编号
    private String outTradeNo;// 我方请求订单号(自己生成的订单号)
    private String totalAmount;// 订单金额
    private String subject;// 订单标题
    private String orderId; // 订单id
    private String ip;// 本机ip地址
    private int paySource;// 1-抢购,2-优惠券,3-会员卡
    private String mchId;// 银行商户号
    private String mchKey;// 商户秘钥
    private String openId;// 用户openid
    private String userId;// 用户ID

    private String refundFee;// 退款总金额，订单总金额，单位为分，只能为整数
    private String refundDesc;// 若商户传入，会在下发给用户的退款消息中体现退款原因

    private String userName;// 用户姓名
    private String bankCode;// 用户银行卡卡号
    private String bankNo;// 银行编号
    private String extra;// 额外参数

    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    public String getOutTradeNo()
    {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo)
    {
        this.outTradeNo = outTradeNo;
    }

    public String getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getPaySource()
    {
        return paySource;
    }

    public void setPaySource(int paySource)
    {
        this.paySource = paySource;
    }

    public String getMchId()
    {
        return mchId;
    }

    public void setMchId(String mchId)
    {
        this.mchId = mchId;
    }

    public String getMchKey()
    {
        return mchKey;
    }

    public void setMchKey(String mchKey)
    {
        this.mchKey = mchKey;
    }

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getRefundFee()
    {
        return refundFee;
    }

    public void setRefundFee(String refundFee)
    {
        this.refundFee = refundFee;
    }

    public String getRefundDesc()
    {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc)
    {
        this.refundDesc = refundDesc;
    }

    public String getBankNo()
    {
        return bankNo;
    }

    public void setBankNo(String bankNo)
    {
        this.bankNo = bankNo;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getBankCode()
    {
        return bankCode;
    }

    public void setBankCode(String bankCode)
    {
        this.bankCode = bankCode;
    }

    public String getExtra()
    {
        return extra;
    }

    public void setExtra(String extra)
    {
        this.extra = extra;
    }

}
