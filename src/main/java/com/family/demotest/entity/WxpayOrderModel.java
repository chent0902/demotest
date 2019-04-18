package com.family.demotest.entity;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.family.base01.model.ModelSupport;

@Component("wx.pay.order.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "wx.pay.order")
@Table(name = "t_wx_pay_order")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WxpayOrderModel
    extends ModelSupport
{

    private static final long serialVersionUID = 8086489988107455501L;

    private String payNo;// 支付订单流水号
    private String orderId;// 订单ID号
    private String subject;// 订单标题
    private String transactionId;// 微信支付订单号
    private String openid;// 微信openid
    private String tradeType;// 交易类型
    private String bankType;// 付款银行
    private String settleDate;// 对账日期
    private int totalFee;// 订单金额(分)
    private int cashFee;// 现金支付金额(分)
    private int refundFee;// 退款金额
    private String endTime;// 交易支付完成时间
    private int payStatus;// 支付状态0-未支付,1-支付成功,-2-已退款
    private int paySource;// 支付来源1-抢购
    private String prepayId;// 预支付交易会话标识
    private String refundNo;// 退款流水号
    private Date createTime;// 对账日期

    @Column(name = "c_pay_no")
    public String getPayNo()
    {
        return payNo;
    }

    public void setPayNo(String payNo)
    {
        this.payNo = payNo;
    }

    @Column(name = "c_order_id")
    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    @Column(name = "c_subject")
    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    @Column(name = "c_transaction_id")
    public String getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }

    @Column(name = "c_openid")
    public String getOpenid()
    {
        return openid;
    }

    public void setOpenid(String openid)
    {
        this.openid = openid;
    }

    @Column(name = "c_trade_type")
    public String getTradeType()
    {
        return tradeType;
    }

    public void setTradeType(String tradeType)
    {
        this.tradeType = tradeType;
    }

    @Column(name = "c_bank_type")
    public String getBankType()
    {
        return bankType;
    }

    public void setBankType(String bankType)
    {
        this.bankType = bankType;
    }

    @Column(name = "c_settle_date")
    public String getSettleDate()
    {
        return settleDate;
    }

    public void setSettleDate(String settleDate)
    {
        this.settleDate = settleDate;
    }

    @Column(name = "c_total_fee")
    public int getTotalFee()
    {
        return totalFee;
    }

    public void setTotalFee(int totalFee)
    {
        this.totalFee = totalFee;
    }

    @Column(name = "c_cash_fee")
    public int getCashFee()
    {
        return cashFee;
    }

    public void setCashFee(int cashFee)
    {
        this.cashFee = cashFee;
    }

    @Column(name = "c_refund_fee")
    public int getRefundFee()
    {
        return refundFee;
    }

    public void setRefundFee(int refundFee)
    {
        this.refundFee = refundFee;
    }

    @Column(name = "c_end_time")
    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    @Column(name = "c_pay_status")
    public int getPayStatus()
    {
        return payStatus;
    }

    public void setPayStatus(int payStatus)
    {
        this.payStatus = payStatus;
    }

    @Column(name = "c_pay_source")
    public int getPaySource()
    {
        return paySource;
    }

    public void setPaySource(int paySource)
    {
        this.paySource = paySource;
    }

    @Column(name = "c_prepay_id")
    public String getPrepayId()
    {
        return prepayId;
    }

    public void setPrepayId(String prepayId)
    {
        this.prepayId = prepayId;
    }

    @Column(name = "c_refund_no")
    public String getRefundNo()
    {
        return refundNo;
    }

    public void setRefundNo(String refundNo)
    {
        this.refundNo = refundNo;
    }

    @Column(name = "c_create_time")
    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

}
