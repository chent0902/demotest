package com.family.demotest.vo.wx;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 微信通用接口凭证
 * 
 * @author wujf
 * 
 */
public class AccessToken
    implements
    Serializable
{

    private static final long serialVersionUID = 1166022319548828381L;

    private String channelNo;

    private String token; // 获取到的凭证

    private int expiresIn; // 凭证有效时间，单位：秒
    private Date expiresTime;

    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public int getExpiresIn()
    {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn)
    {
        this.expiresIn = expiresIn;
    }

    public Date getExpiresTime()
    {
        Calendar nowTime = Calendar.getInstance();
        nowTime.setTime(expiresTime);
        nowTime.add(Calendar.SECOND, expiresIn);
        return nowTime.getTime();
    }

    public void setExpiresTime(Date expiresTime)
    {
        this.expiresTime = expiresTime;
    }

}
