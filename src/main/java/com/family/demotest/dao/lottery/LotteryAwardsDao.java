package com.family.demotest.dao.lottery;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.demotest.entity.LotteryAwardsModel;

/**
 * @author lm
 */
public interface LotteryAwardsDao
    extends QueryDao<LotteryAwardsModel>, SaveDao<LotteryAwardsModel>, DeleteDao<LotteryAwardsModel>
{

    /**
     * 获取奖项列表
     * 
     * @param channelNo
     * @param lotteryId
     * @return
     */
    public List<LotteryAwardsModel> list(String channelNo, String lotteryId);

    /**
     * 获取活动最低价(特等奖奖项对象)
     * 
     * @param channelNo
     * @param lotteryId
     * @return
     */
    public LotteryAwardsModel getlowestPrice(String channelNo, String lotteryId);

    /**
     * 根据活动和奖项级别获取奖项
     * 
     * @param lotteryId
     * @param answer
     * @return
     */
    public LotteryAwardsModel findByGrade(String lotteryId, int answer);

}
