package com.family.demotest.vo.wx;

/**
 * 微信公众号发送模版消息
 * 
 * @author wujf
 * 
 */
public class TemplateMsgSendVo
{

    private String channelNo; // 商家编号
    private String touser; // 接收者openid
    private String templateId; // 模板ID
    private String url; // 模板跳转链接
    private String appid; // 所需跳转到的小程序appid（
    private String pagepath; // 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）
    private String data; // 模板数据
    private String color; // 模板内容字体颜色，不填默认为黑色

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

    public String getPagepath()
    {
        return pagepath;
    }

    public void setPagepath(String pagepath)
    {
        this.pagepath = pagepath;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
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

}
