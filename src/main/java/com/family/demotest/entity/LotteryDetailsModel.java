package com.family.demotest.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.family.base01.model.ModelSupport;

@Component("lottery.details.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "lottery.details")
@Table(name = "t_lottery_details")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LotteryDetailsModel
    extends ModelSupport
{

    private static final long serialVersionUID = -4398932922829749439L;

    private String channelNo;// 渠道编号
    private String lotteryId;// 抽奖ID
    private String details;// 详情
    private Date createTime;// 创建时间

    private List<LotteryAwardsModel> awardsList;// 奖项

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

    @Column(name = "c_details")
    public String getDeatils()
    {
        return details;
    }

    public void setDeatils(String details)
    {
        this.details = details;
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

    @Transient
    public List<LotteryAwardsModel> getAwardsList()
    {
        return awardsList;
    }

    public void setAwardsList(List<LotteryAwardsModel> awardsList)
    {
        this.awardsList = awardsList;
    }

}
