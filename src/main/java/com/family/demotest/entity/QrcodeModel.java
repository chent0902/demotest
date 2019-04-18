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

@Component("qr.code.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "qr.code")
@Table(name = "t_qrcode")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QrcodeModel
    extends
    ModelSupport
{

    private static final long serialVersionUID = 7477057177886999411L;
    private String channelNo;// 渠道号
    private int type;// 类型:0-文字，1-图文，2-图片
    private String content;// 文字内容
    private String articlesContent;// 图文内容
    private String remark;// 备注
    private String qrcodeUrl;// 二维码url
    private Date createTime;// 创建时间

    private int scanNum;// 扫码次数

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

    @Column(name = "c_content")
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Column(name = "c_articles_content")
    public String getArticlesContent()
    {
        return articlesContent;
    }

    public void setArticlesContent(String articlesContent)
    {
        this.articlesContent = articlesContent;
    }

    @Column(name = "c_remark")
    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Column(name = "c_qrcode_url")
    public String getQrcodeUrl()
    {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl)
    {
        this.qrcodeUrl = qrcodeUrl;
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
    public int getScanNum()
    {
        return scanNum;
    }

    public void setScanNum(int scanNum)
    {
        this.scanNum = scanNum;
    }

}
