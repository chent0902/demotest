package com.family.demotest.vo.wx;

/**
 * 微信通用接口凭证
 * 
 * @author wujf
 * 
 */
public class QrsceneVo
{
    private String lotteryId; // 活动id
    private String userId; // 用户id

    public String getLotteryId()
    {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId)
    {
        this.lotteryId = lotteryId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

}
