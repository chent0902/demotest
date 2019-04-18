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

@Component("comment.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "comment")
@Table(name = "t_comment")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CommentModel
    extends ModelSupport
{
    private static final long serialVersionUID = 687099333100183263L;
    private String channelNo;// 渠道编号
    private String imgTextId;// 图文ID
    private String userId;// 用户ID
    private String content;// 留言内容
    private String reply;// 回复
    private int choiceness;// 精选 0-否 1-是
    private Date createTime;// 创建时间
    private String nickName;// 用户昵称
    private String headurl;// 用户头像
    private int like;// 点赞数
    private int replyLike;// 回复点赞数

    @Column(name = "c_channel_no")
    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    @Column(name = "c_img_text_id")
    public String getImgTextId()
    {
        return imgTextId;
    }

    public void setImgTextId(String imgTextId)
    {
        this.imgTextId = imgTextId;
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

    @Column(name = "c_content")
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    @Column(name = "c_reply")
    public String getReply()
    {
        return reply;
    }

    public void setReply(String reply)
    {
        this.reply = reply;
    }

    @Column(name = "c_choiceness")
    public int getChoiceness()
    {
        return choiceness;
    }

    public void setChoiceness(int choiceness)
    {
        this.choiceness = choiceness;
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

    @Column(name = "c_nick_Name")
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        if(nickName==null)
        {
            nickName = "";
        }
        this.nickName = nickName;
    }

    @Column(name = "c_headurl")
    public String getHeadurl()
    {
        return headurl;
    }

    public void setHeadurl(String headurl)
    {
        if(headurl==null)
        {
            headurl = "";
        }
        this.headurl = headurl;
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

    @Transient
    public int getReplyLike()
    {
        return replyLike;
    }

    public void setReplyLike(int replyLike)
    {
        this.replyLike = replyLike;
    }

}
