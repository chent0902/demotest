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

@Component("admin.token.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "admin.token")
@Table(name = "t_admin_token")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AdminTokenModel
    extends ModelSupport
{
    private static final long serialVersionUID = 5637152324931980584L;

    private String adminId;// 代理商ID
    private String token;// token
    private Date expiredTime;// 更新时间
    private Date createTime;// 创建时间

    @Column(name = "c_admin_id")
    public String getAdminId()
    {
        return adminId;
    }

    public void setAdminId(String adminId)
    {
        this.adminId = adminId;
    }

    @Column(name = "c_token")
    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    @Column(name = "c_expired_time")
    public Date getExpiredTime()
    {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime)
    {
        this.expiredTime = expiredTime;
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
