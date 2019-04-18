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

@Component("activity.package.order.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "activity.package.order")
@Table(name = "t_activity_package_order")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityPackageOrderModel
    extends ModelSupport
{

    private static final long serialVersionUID = -6573010963486717012L;

    private String channelNo;// 渠道号
    private String activityPackageId;// 套餐Id
    private String nickName;// 授权方名称
    private int num;// 套餐点数
    private int price;// 价格 单位分
    private int status;// 支付状态 0-待支付 1-已支付
    private String transId;// 商户订单号
    private String tradeNo;// 商户订单号
    private String prepayId;// 预支付ID
    private Date payTime;// 创建时间
    private Date createTime;// 创建时间

    @Column(name = "c_channel_no")
    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    @Column(name = "c_activity_package_id")
    public String getActivityPackageId()
    {
        return activityPackageId;
    }

    public void setActivityPackageId(String activityPackageId)
    {
        this.activityPackageId = activityPackageId;
    }

    @Column(name = "c_nick_name")
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Column(name = "c_num")
    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    @Column(name = "c_price")
    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    @Column(name = "c_status")
    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    @Column(name = "c_trans_id")
    public String getTransId()
    {
        return transId;
    }

    public void setTransId(String transId)
    {
        this.transId = transId;
    }

    @Column(name = "c_trade_no")
    public String getTradeNo()
    {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
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

    @Column(name = "c_pay_time")
    public Date getPayTime()
    {
        return payTime;
    }

    public void setPayTime(Date payTime)
    {
        this.payTime = payTime;
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
