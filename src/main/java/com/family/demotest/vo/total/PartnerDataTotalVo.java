package com.family.demotest.vo.total;

import java.io.Serializable;

public class PartnerDataTotalVo
    implements Serializable
{

    private static final long serialVersionUID = 7873180100667176794L;

    private int activePointMoney;// 活动点充值金额
    private int smsMoney;// 短信充值金额
    private int activityNum;// 活动数

    public int getActivePointMoney()
    {
        return activePointMoney;
    }

    public void setActivePointMoney(int activePointMoney)
    {
        this.activePointMoney = activePointMoney;
    }

    public int getSmsMoney()
    {
        return smsMoney;
    }

    public void setSmsMoney(int smsMoney)
    {
        this.smsMoney = smsMoney;
    }

    public int getActivityNum()
    {
        return activityNum;
    }

    public void setActivityNum(int activityNum)
    {
        this.activityNum = activityNum;
    }

}
