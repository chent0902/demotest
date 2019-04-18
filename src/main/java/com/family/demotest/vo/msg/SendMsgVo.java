package com.family.demotest.vo.msg;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.family.demotest.entity.UserModel;

/**
 * @author wujf
 */

public class SendMsgVo
    implements
    Serializable
{

    private static final long serialVersionUID = -2786124101397350240L;
    private String id;
    private String channelNo;// 商家编号
    private String title;// 活动名称
    private String link;// 跳转网址
    private String appid;// 小程序appid

    private String msgType;// 群发模版消息类型 "9"-客服消息群发图文
    private String token;// token
    private String templateId;// 所需下发的模板消息的id
    private JSONObject content;// 图文数据
    private List<UserModel> userList;

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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    public String getMsgType()
    {
        return msgType;
    }

    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
    }

    public JSONObject getContent()
    {
        return content;
    }

    public void setContent(JSONObject content)
    {
        this.content = content;
    }

    public List<UserModel> getUserList()
    {
        return userList;
    }

    public void setUserList(List<UserModel> userList)
    {
        this.userList = userList;
    }

}
