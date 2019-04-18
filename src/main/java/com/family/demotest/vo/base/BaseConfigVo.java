package com.family.demotest.vo.base;

import java.util.Date;

public class BaseConfigVo

{

    private String id;
    private String channelNo;// 商户编号
    private String appid;// 授权方appid
    private String nickName;// 授权方昵称
    private String userName;// 小程序原始id
    private String qrcode;// 二维码图片
    private Date createTime;

    private String siteName;// 站点名称
    private String companyName;// 公司名称
    private String address;// 地址
    private String contacts;// 联系人
    private String contactsTel;// 联系人电话
    private String account; // 超级账号
    private String password;// 密码
    private Date startTime;// 开始时间
    private Date endTime;// 到期时间

    private String cityName; // 城市名称
    private int activePoint;// 活动点 10000-表示永久

    private String salesman;// 业务员

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getQrcode()
    {
        return qrcode;
    }

    public void setQrcode(String qrcode)
    {
        this.qrcode = qrcode;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getSiteName()
    {
        return siteName;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getContacts()
    {
        return contacts;
    }

    public void setContacts(String contacts)
    {
        this.contacts = contacts;
    }

    public String getContactsTel()
    {
        return contactsTel;
    }

    public void setContactsTel(String contactsTel)
    {
        this.contactsTel = contactsTel;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getCityName()
    {
        return cityName;
    }

    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public int getActivePoint()
    {
        return activePoint;
    }

    public void setActivePoint(int activePoint)
    {
        this.activePoint = activePoint;
    }

    public String getSalesman()
    {
        return salesman;
    }

    public void setSalesman(String salesman)
    {
        this.salesman = salesman;
    }

}
