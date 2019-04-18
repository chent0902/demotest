package com.family.demotest.vo.wx;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wujf
 */

public class MessageVo
    implements Serializable
{

    private static final long serialVersionUID = 5189616405370237312L;
    private String channelNo;// 商家编号
    private String touser;// 接收者（用户）的 openid
    private String templateId;// 所需下发的模板消息的id
    private String url; // 模板跳转链接
    private JSONObject miniprogram;// 跳小程序所需数据，不需跳小程序可不用传该数据
    private String appid;// 所需跳转到的小程序appid
    private JSONObject data;// 模板内容，不填则下发空模板
    private String color;// 模板内容字体的颜色，不填默认黑色

    private String token;// token

    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    public String getTouser()
    {
        return touser;
    }

    public void setTouser(String touser)
    {
        this.touser = touser;
    }

    public String getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    public JSONObject getMiniprogram()
    {
        return miniprogram;
    }

    public void setMiniprogram(JSONObject miniprogram)
    {
        this.miniprogram = miniprogram;
    }

    public JSONObject getData()
    {
        return data;
    }

    public void setData(JSONObject data)
    {
        this.data = data;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

}
