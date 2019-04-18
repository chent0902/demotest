package com.family.demotest.vo.user;

import java.io.Serializable;

public class UserVo
    implements Serializable
{

    private static final long serialVersionUID = 3207029105400301816L;

    private String id;
    private String channelNo;
    private String openid;
    private String nickName;

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

    public String getOpenid()
    {
        return openid;
    }

    public void setOpenid(String openid)
    {
        this.openid = openid;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

}
