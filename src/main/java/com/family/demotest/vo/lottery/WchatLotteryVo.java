package com.family.demotest.vo.lottery;

import java.io.Serializable;

public class WchatLotteryVo
    implements
    Serializable
{

    private static final long serialVersionUID = -4560373188652114504L;

    private Integer id;

    private String prize; // 中奖名称

    private Integer v;// 中奖率

    public WchatLotteryVo()
    {

    }

    public WchatLotteryVo(Integer id, String prize, Integer v)
    {
        this.id = id;
        this.prize = prize;
        this.v = v;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getPrize()
    {
        return prize;
    }

    public void setPrize(String prize)
    {
        this.prize = prize;
    }

    public Integer getV()
    {
        return v;
    }

    public void setV(Integer v)
    {
        this.v = v;
    }
}
