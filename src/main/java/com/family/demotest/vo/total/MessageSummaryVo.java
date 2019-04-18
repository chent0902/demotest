package com.family.demotest.vo.total;

import java.io.Serializable;

/**
 * 
 * @author lim
 *
 */

public class MessageSummaryVo
    implements Serializable
{

    private static final long serialVersionUID = -7604855865478099615L;

    private int like;// 点赞数
    private int replyLike;// 回复点赞数

    public int getLike()
    {
        return like;
    }

    public void setLike(int like)
    {
        this.like = like;
    }

    public int getReplyLike()
    {
        return replyLike;
    }

    public void setReplyLike(int replyLike)
    {
        this.replyLike = replyLike;
    }

}
