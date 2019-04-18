package com.family.demotest.vo.total;

import java.io.Serializable;

/**
 * 
 * @author lim
 *
 */

public class LotterySummaryVo
    implements Serializable
{

    private static final long serialVersionUID = -7604855865478099615L;

    private int attention;// 关注量
    private int join;// 参与数
    private int receive;// 领取数
    private int like;// 点赞数
    private int message;// 留言数
    private int useNum;// 使用数

    public int getAttention()
    {
        return attention;
    }

    public void setAttention(int attention)
    {
        this.attention = attention;
    }

    public int getJoin()
    {
        return join;
    }

    public void setJoin(int join)
    {
        this.join = join;
    }

    public int getReceive()
    {
        return receive;
    }

    public void setReceive(int receive)
    {
        this.receive = receive;
    }

    public int getLike()
    {
        return like;
    }

    public void setLike(int like)
    {
        this.like = like;
    }

    public int getMessage()
    {
        return message;
    }

    public void setMessage(int message)
    {
        this.message = message;
    }

    public int getUseNum()
    {
        return useNum;
    }

    public void setUseNum(int useNum)
    {
        this.useNum = useNum;
    }

}
