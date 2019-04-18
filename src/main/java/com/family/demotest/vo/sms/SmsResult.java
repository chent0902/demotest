package com.family.demotest.vo.sms;

import java.util.List;

public class SmsResult
{
    private int Status;// 响应码
    private String StatusMsg;// 返回值详情
    private String MsgID;// 短信MsgID，唯一码
    private List<ResultListVo> ResultList;//

    public int getStatus()
    {
        return Status;
    }

    public void setStatus(int status)
    {
        Status = status;
    }

    public String getStatusMsg()
    {
        return StatusMsg;
    }

    public void setStatusMsg(String statusMsg)
    {
        StatusMsg = statusMsg;
    }

    public String getMsgID()
    {
        return MsgID;
    }

    public void setMsgID(String msgID)
    {
        MsgID = msgID;
    }

    public List<ResultListVo> getResultList()
    {
        return ResultList;
    }

    public void setResultList(List<ResultListVo> resultList)
    {
        ResultList = resultList;
    }

}
