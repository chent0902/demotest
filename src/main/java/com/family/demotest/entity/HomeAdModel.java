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

@Component("home.ad.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "home.ad")
@Table(name = "t_home_ad")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class HomeAdModel
    extends
    ModelSupport
{

    private static final long serialVersionUID = 1455872435240798182L;
    private String channelNo;// 渠道号
    private int type;// 图片类型 1-首页轮播
    private String img;// 图片
    private String name;// 名称
    private int sort;// 显示顺序
    private String linkUrl;// 跳转网址
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

    @Column(name = "c_img")
    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
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

    @Column(name = "c_sort")
    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    @Column(name = "c_link_url")
    public String getLinkUrl()
    {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl)
    {
        this.linkUrl = linkUrl;
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
