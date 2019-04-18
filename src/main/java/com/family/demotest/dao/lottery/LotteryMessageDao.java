package com.family.demotest.dao.lottery;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.LotteryMessageModel;

/**
 * @author lm
 */
public interface LotteryMessageDao
    extends QueryDao<LotteryMessageModel>, SaveDao<LotteryMessageModel>, DeleteDao<LotteryMessageModel>
{

    /**
     * 留言列表
     * 
     * @param channelNo
     * @param lotteryId
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<LotteryMessageModel> pageList(String channelNo, String lotteryId, int page, int pageSize);

    /**
     * 活动精选留言列表
     * 
     * @param lotteryId
     * @param pageSize
     * @param page
     * @return
     */
    public List<LotteryMessageModel> choicenessList(String lotteryId, int page, int pageSize);

    /**
     * 我的留言
     * 
     * @param channelNo
     * @param lotteryId
     * @param userId
     * @return
     */
    public List<LotteryMessageModel> myMessage(String channelNo, String lotteryId, String userId);

}
