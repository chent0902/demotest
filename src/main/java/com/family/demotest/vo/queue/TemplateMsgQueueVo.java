package com.family.demotest.vo.queue;

import java.io.Serializable;

/**
 * @author wujf
 */

public class TemplateMsgQueueVo
    implements
    Serializable
{

    private static final long serialVersionUID = 7465347923595627187L;
    private String channelNo;// 商家编号
    private String openId;
    private int msgType;// 1-到账通知，2-抽奖结果通知，3-核销成功通知,4-服务到期提醒
    private String token;// token
    private String templateId;// 所需下发的模板消息的id

    private String first;
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String keyword4;
    private String remark;

    private String url;

    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public int getMsgType()
    {
        return msgType;
    }

    public void setMsgType(int msgType)
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

    public String getFirst()
    {
        return first;
    }

    public void setFirst(String first)
    {
        this.first = first;
    }

    public String getKeyword1()
    {
        return keyword1;
    }

    public void setKeyword1(String keyword1)
    {
        this.keyword1 = keyword1;
    }

    public String getKeyword2()
    {
        return keyword2;
    }

    public void setKeyword2(String keyword2)
    {
        this.keyword2 = keyword2;
    }

    public String getKeyword3()
    {
        return keyword3;
    }

    public void setKeyword3(String keyword3)
    {
        this.keyword3 = keyword3;
    }

    public String getKeyword4()
    {
        return keyword4;
    }

    public void setKeyword4(String keyword4)
    {
        this.keyword4 = keyword4;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

}
