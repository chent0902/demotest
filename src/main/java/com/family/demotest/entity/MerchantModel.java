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

@Component("merchant.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "merchant")
@Table(name = "t_merchant")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MerchantModel
    extends ModelSupport
{

    private static final long serialVersionUID = 1396848650931009451L;

    private String channelNo;// 代理商编号
    private String name;// 商家名称
    private String logo;// 商家LOGO
    private String address;// 商家地址
    private String tel;// 电话
    private String longitude;// 经度
    private String latitude;// 纬度
    private String service;// 服务
    private String password;// 商家核销密码
    private String contacts;// 联系人
    private String contactsTel;// 联系人电话
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

    @Column(name = "c_name")
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name = "c_logo")
    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    @Column(name = "c_address")
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @Column(name = "c_tel")
    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    @Column(name = "c_longitude")
    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    @Column(name = "c_latitude")
    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    @Column(name = "c_service")
    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    @Column(name = "c_password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Column(name = "c_contacts")
    public String getContacts()
    {
        return contacts;
    }

    public void setContacts(String contacts)
    {
        this.contacts = contacts;
    }

    @Column(name = "c_contacts_tel")
    public String getContactsTel()
    {
        return contactsTel;
    }

    public void setContactsTel(String contactsTel)
    {
        this.contactsTel = contactsTel;
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
