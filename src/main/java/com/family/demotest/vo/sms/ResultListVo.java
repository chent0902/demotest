package com.family.demotest.vo.sms;

public class ResultListVo
{
    private String Mobile;// 响应码
    private String RespCode;// 返回值详情
    private String RespMsg;// 短信MsgID，唯一码

    public String getMobile()
    {
        return Mobile;
    }

    public void setMobile(String mobile)
    {
        Mobile = mobile;
    }

    public String getRespCode()
    {
        return RespCode;
    }

    public void setRespCode(String respCode)
    {
        RespCode = respCode;
    }

    public String getRespMsg()
    {
        return RespMsg;
    }

    public void setRespMsg(String respMsg)
    {
        RespMsg = respMsg;
    }

}
