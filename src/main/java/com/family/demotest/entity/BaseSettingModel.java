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

@Component("base.setting.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "base.setting")
@Table(name = "t_base_setting")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BaseSettingModel
    extends ModelSupport
{

    private static final long serialVersionUID = -4996936600632013131L;
    private String channelNo;// 商户编号
    private String shareTitle;// 分享标题
    private String shareLogo;// 分享logo
    private String followReply;// 公众号关注回复
    private Date createTime; // 创建时间

    @Column(name = " c_channel_no")
    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    @Column(name = " c_share_title")
    public String getShareTitle()
    {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle)
    {
        this.shareTitle = shareTitle;
    }

    @Column(name = " c_share_logo")
    public String getShareLogo()
    {
        return shareLogo;
    }

    public void setShareLogo(String shareLogo)
    {
        this.shareLogo = shareLogo;
    }

    @Column(name = " c_follow_reply")
    public String getFollowReply()
    {
        return followReply;
    }

    public void setFollowReply(String followReply)
    {
        this.followReply = followReply;
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

}
