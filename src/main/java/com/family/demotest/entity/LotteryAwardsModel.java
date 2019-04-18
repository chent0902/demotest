package com.family.demotest.entity;

import java.util.Date;

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

@Component("lottery.awards.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "lottery.awards")
@Table(name = "t_lottery_awards")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LotteryAwardsModel
    extends
    ModelSupport
{

    private static final long serialVersionUID = -7934003503699009474L;

    private String channelNo;// 渠道编号
    private String lotteryId;// 抽奖ID
    private int grade;// 奖项等级 0-特等奖 1-1等奖 2 3 4
    private String name;// 优惠名称
    private String price;// 价格
    private int num;// 限量多少份，-1-表示不限量
    private int luckyNum;// 每人中奖次数
    private Date createTime;// 创建时间

    private int minTimes;// 最少抽奖次数
    private int rate;// 中奖概率1-100

    private int stock;// 剩余库存数

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

    @Column(name = "c_grade")
    public int getGrade()
    {
        return grade;
    }

    public void setGrade(int grade)
    {
        this.grade = grade;
    }

    @Column(name = "c_name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name = "c_price")
    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
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

    @Column(name = "c_lucky_num")
    public int getLuckyNum()
    {
        return luckyNum;
    }

    public void setLuckyNum(int luckyNum)
    {
        this.luckyNum = luckyNum;
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

    @Column(name = "c_min_times")
    public int getMinTimes()
    {
        return minTimes;
    }

    public void setMinTimes(int minTimes)
    {
        this.minTimes = minTimes;
    }

    @Column(name = "c_rate")
    public int getRate()
    {
        return rate;
    }

    public void setRate(int rate)
    {
        this.rate = rate;
    }

    @Transient
    public int getStock()
    {
        return stock;
    }

    public void setStock(int stock)
    {
        this.stock = stock;
    }

}
