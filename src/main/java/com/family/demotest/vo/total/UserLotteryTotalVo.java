package com.family.demotest.vo.total;

import java.io.Serializable;

/**
 * @author wujf
 */

public class UserLotteryTotalVo
    implements
    Serializable
{

    private static final long serialVersionUID = -2957044731089883500L;

    private int specialNum;// 特等奖数量
    private int oneNum;// 一等奖数量
    private int twoNum;// 二等奖数量
    private int threeNum;// 三等奖数量
    private int fourNum;// 四等奖数量
    private int timesNum;// 还剩余抽奖次数
    private int participated;// 是否参与过了 0-未参与过 1-已参与过
    private int lotteryNum;// 已抽奖次数

    public int getSpecialNum()
    {
        return specialNum;
    }

    public void setSpecialNum(int specialNum)
    {
        this.specialNum = specialNum;
    }

    public int getOneNum()
    {
        return oneNum;
    }

    public void setOneNum(int oneNum)
    {
        this.oneNum = oneNum;
    }

    public int getTwoNum()
    {
        return twoNum;
    }

    public void setTwoNum(int twoNum)
    {
        this.twoNum = twoNum;
    }

    public int getThreeNum()
    {
        return threeNum;
    }

    public void setThreeNum(int threeNum)
    {
        this.threeNum = threeNum;
    }

    public int getFourNum()
    {
        return fourNum;
    }

    public void setFourNum(int fourNum)
    {
        this.fourNum = fourNum;
    }

    public int getTimesNum()
    {
        return timesNum;
    }

    public void setTimesNum(int timesNum)
    {
        this.timesNum = timesNum;
    }

    public int getParticipated()
    {
        return participated;
    }

    public void setParticipated(int participated)
    {
        this.participated = participated;
    }

    public int getLotteryNum()
    {
        return lotteryNum;
    }

    public void setLotteryNum(int lotteryNum)
    {
        this.lotteryNum = lotteryNum;
    }

}
