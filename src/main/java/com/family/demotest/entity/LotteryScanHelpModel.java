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

@Component("lottery.scan.help.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "lottery.scan.help")
@Table(name = "t_lottery_scan_help")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LotteryScanHelpModel
    extends
    ModelSupport
{

    private static final long serialVersionUID = 6216141684339600301L;
    private String channelNo;// 代理商编号
    private String lotteryId;// 活动id
    private String userId;// 用户id
    private String helpUserId;// 帮扫用户id
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

    @Column(name = "c_lottery_id")
    public String getLotteryId()
    {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId)
    {
        this.lotteryId = lotteryId;
    }

    @Column(name = "c_user_id")
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    @Column(name = "c_help_user_id")
    public String getHelpUserId()
    {
        return helpUserId;
    }

    public void setHelpUserId(String helpUserId)
    {
        this.helpUserId = helpUserId;
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
