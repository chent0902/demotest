package com.family.demotest.dao.lottery;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.demotest.entity.LotteryDetailsModel;

/**
 * 
 * @author lim
 *
 */
public interface LotteryDetailsDao
    extends QueryDao<LotteryDetailsModel>, SaveDao<LotteryDetailsModel>, DeleteDao<LotteryDetailsModel>
{

    /**
     * 根据抽奖ID查详情
     * 
     * @param lotteryId
     * @return
     */
    public LotteryDetailsModel findByLotteryId(String lotteryId);

}
