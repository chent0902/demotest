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

@Component("admin.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "admin")
@Table(name = "t_admin")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AdminModel
    extends ModelSupport
{

    private static final long serialVersionUID = -4363040029920938423L;

    private String channelNo;// 代理商编号
    private String account;// 用户名
    private String passwd;// 密码
    private int status;// 是否禁止登陆0-否，1-是
    private int manage;// 0-子账号 1-总帐号
    private Date updateTime;// 更新时间
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

    @Column(name = "c_account")
    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    @Column(name = "c_passwd")
    public String getPasswd()
    {
        return passwd;
    }

    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
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

    @Column(name = "c_manage")
    public int getManage()
    {
        return manage;
    }

    public void setManage(int manage)
    {
        this.manage = manage;
    }

    @Column(name = "c_update_time")
    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
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
