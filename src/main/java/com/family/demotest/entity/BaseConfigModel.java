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

@Component("base.config.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "base.config")
@Table(name = "t_base_config")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BaseConfigModel
    extends
    ModelSupport
{

    private static final long serialVersionUID = -636482701570584096L;
    private String channelNo;// 商户编号
    private String appid;// 授权方appid
    private String nickName;// 授权方昵称
    private String headImg;// 授权方头像
    private int serviceTypeInfo;//
    private int verifyTypeInfo;// 授权方认证类型，-1代表未认证，0代表微信认证
    private String userName;// 小程序原始id
    private String qrcodeUrl;// 二维码图片的URL
    private String qrcode;// 二维码图片
    private String accessToken;//
    private String refreshToken;//
    private String overTime;// accessToken过期时间
    private Date createTime; // 创建时间

    private String companyName;// 公司名称
    private String address;// 地址
    private String contacts;// 联系人
    private String contactsTel;// 联系人电话
    private String siteName;// 站点名称
    private Date startTime; // 开始时间
    private Date endTime;// 结束时间
    private String businessTel;// 商户合作电话
    private String wxName;// 商户合作微信号
    private int activePoint;// 活动点
    private int forcedAttention;// 是否需要强制关注后参与活动 0-不需要 1-需要
    private String account;// 用户名
    private String passwd;// 密码
    private String cityName;// 城市名称
    private int smsNum;// 短信数量
    private String salesman;// 业务员

    private int openTel;// 开启手机验证0-否，1-是
    private int openImgtext;// 开启图文列表0-否，1-是
    private String introduce;// 站点介绍

    // 缓存
    private int activePointMoney;// 活动点充值金额
    private int smsMoney;// 短信充值金额
    private int activityNum;// 活动数

    @Column(name = " c_channel_no")
    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    @Column(name = "c_appid")
    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    @Column(name = "c_nick_name")
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Column(name = "c_head_img")
    public String getHeadImg()
    {
        return headImg;
    }

    public void setHeadImg(String headImg)
    {
        this.headImg = headImg;
    }

    @Column(name = "c_service_type_info")
    public int getServiceTypeInfo()
    {
        return serviceTypeInfo;
    }

    public void setServiceTypeInfo(int serviceTypeInfo)
    {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    @Column(name = "c_verify_type_info")
    public int getVerifyTypeInfo()
    {
        return verifyTypeInfo;
    }

    public void setVerifyTypeInfo(int verifyTypeInfo)
    {
        this.verifyTypeInfo = verifyTypeInfo;
    }

    @Column(name = "c_user_name")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
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

    @Column(name = "c_qrcode")
    public String getQrcode()
    {
        return qrcode;
    }

    public void setQrcode(String qrcode)
    {
        this.qrcode = qrcode;
    }

    @Column(name = "c_access_token")
    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    @Column(name = "c_refresh_token")
    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }

    @Column(name = "c_over_time")
    public String getOverTime()
    {
        return overTime;
    }

    public void setOverTime(String overTime)
    {
        this.overTime = overTime;
    }

    @Column(name = "c_active_point")
    public int getActivePoint()
    {
        return activePoint;
    }

    public void setActivePoint(int activePoint)
    {
        this.activePoint = activePoint;
    }

    @Column(name = "c_forced_attention")
    public int getForcedAttention()
    {
        return forcedAttention;
    }

    public void setForcedAttention(int forcedAttention)
    {
        this.forcedAttention = forcedAttention;
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

    @Column(name = "c_company_name")
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
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

    @Column(name = "c_site_name")
    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    @Column(name = "c_business_tel")
    public String getBusinessTel()
    {
        return businessTel;
    }

    public void setBusinessTel(String businessTel)
    {
        this.businessTel = businessTel;
    }

    @Column(name = "c_wx_name")
    public String getWxName()
    {
        return wxName;
    }

    public void setWxName(String wxName)
    {
        this.wxName = wxName;
    }

    @Column(name = "c_start_time")
    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    @Column(name = "c_end_time")
    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
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

    @Column(name = "c_city_name")
    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    @Column(name = "c_sms_num")
    public int getSmsNum()
    {
        return smsNum;
    }

    public void setSmsNum(int smsNum)
    {
        this.smsNum = smsNum;
    }

    @Column(name = "c_salesman")
    public String getSalesman()
    {
        return salesman;
    }

    public void setSalesman(String salesman)
    {
        this.salesman = salesman;
    }

    @Column(name = "c_open_tel")
    public int getOpenTel()
    {
        return openTel;
    }

    public void setOpenTel(int openTel)
    {
        this.openTel = openTel;
    }

    @Column(name = "c_open_imgtext")
    public int getOpenImgtext()
    {
        return openImgtext;
    }

    public void setOpenImgtext(int openImgtext)
    {
        this.openImgtext = openImgtext;
    }

    @Column(name = "c_introduce")
    public String getIntroduce()
    {
        return introduce;
    }

    public void setIntroduce(String introduce)
    {
        this.introduce = introduce;
    }

    @Transient
    public int getActivePointMoney()
    {
        return activePointMoney;
    }

    public void setActivePointMoney(int activePointMoney)
    {
        this.activePointMoney = activePointMoney;
    }

    @Transient
    public int getSmsMoney()
    {
        return smsMoney;
    }

    public void setSmsMoney(int smsMoney)
    {
        this.smsMoney = smsMoney;
    }

    @Transient
    public int getActivityNum()
    {
        return activityNum;
    }

    public void setActivityNum(int activityNum)
    {
        this.activityNum = activityNum;
    }

}
