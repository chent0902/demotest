package com.family.demotest.vo.total;

import java.io.Serializable;

/**
 * 
 * @author Chen
 *
 */

public class ImgTextSummaryVo
    implements Serializable
{

    private static final long serialVersionUID = -7604855865478099615L;

    private int visitNum;// 访问量
    private int like;// 点赞数
    private int message;// 留言数

    public int getVisitNum()
    {
        return visitNum;
    }

    public void setVisitNum(int visitNum)
    {
        this.visitNum = visitNum;
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

}
