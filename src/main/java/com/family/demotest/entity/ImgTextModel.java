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

@Component("img.text.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "img.text")
@Table(name = "t_img_text")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ImgTextModel
    extends ModelSupport
{
    private static final long serialVersionUID = -6584416064996647937L;
    private String channelNo;// 商户编号
    private String title;// 图文标题
    private String img;// 图片
    private String intro;// 图文简介
    private String content;// 图文内容
    private int messageSwitch;// 留言开关 0-关闭，1-开启
    private int sort;// 顺序
    private int virVisit;// 虚拟访问数
    private int virClick;// 虚拟点赞数
    private Date createTime; // 创建时间
    private int visitNum;// 访问数
    private int message;// 评价数
    private int like;// 点赞数

    @Column(name = " c_channel_no")
    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    @Column(name = " c_title")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Column(name = " c_img")
    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    @Column(name = " c_intro")
    public String getIntro()
    {
        return intro;
    }

    public void setIntro(String intro)
    {
        this.intro = intro;
    }

    @Column(name = " c_content")
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Column(name = " c_message_switch")
    public int getMessageSwitch()
    {
        return messageSwitch;
    }

    public void setMessageSwitch(int messageSwitch)
    {
        this.messageSwitch = messageSwitch;
    }

    @Column(name = " c_create_time")
    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Column(name = " c_sort")
    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    @Column(name = " c_vir_visit")
    public int getVirVisit()
    {
        return virVisit;
    }

    public void setVirVisit(int virVisit)
    {
        this.virVisit = virVisit;
    }

    @Column(name = " c_vir_click")
    public int getVirClick()
    {
        return virClick;
    }

    public void setVirClick(int virClick)
    {
        this.virClick = virClick;
    }

    @Transient
    public int getVisitNum()
    {
        return visitNum;
    }

    public void setVisitNum(int visitNum)
    {
        this.visitNum = visitNum;
    }

    @Transient
    public int getMessage()
    {
        return message;
    }

    public void setMessage(int message)
    {
        this.message = message;
    }

    @Transient
    public int getLike()
    {
        return like;
    }

    public void setLike(int like)
    {
        this.like = like;
    }

}
