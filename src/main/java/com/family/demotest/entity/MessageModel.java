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

@Component("message.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "message")
@Table(name = "t_message")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MessageModel
    extends ModelSupport
{

    private static final long serialVersionUID = 1572039061835945819L;

    private String channelNo;// 代理商编号
    private int type;// 群发类型 0-图文消息 1-模板消息
    private String news;// 消息
    private int sendNum;// 发送人数
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

    @Column(name = "c_type")
    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    @Column(name = "c_news")
    public String getNews()
    {
        return news;
    }

    public void setNews(String news)
    {
        this.news = news;
    }

    @Column(name = "c_send_num")
    public int getSendNum()
    {
        return sendNum;
    }

    public void setSendNum(int sendNum)
    {
        this.sendNum = sendNum;
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
