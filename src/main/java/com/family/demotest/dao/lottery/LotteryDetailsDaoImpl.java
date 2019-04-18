package com.family.demotest.dao.lottery;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.demotest.entity.LotteryDetailsModel;

/**
 * 
 * @author lm
 *
 */
@Repository("lottery.details.dao")
public class LotteryDetailsDaoImpl
    extends DaoSupport<LotteryDetailsModel>
    implements LotteryDetailsDao
{

    private static final String FIND_BY_LOTTERY_ID = " o.lotteryId = ? AND o.property = 0 ";

    @Override
    protected Class<LotteryDetailsModel> getModelClass()
    {
        return LotteryDetailsModel.class;
    }

    @Override
    public LotteryDetailsModel findByLotteryId(String lotteryId)
    {

        return this.findOne(newQueryBuilder().where(FIND_BY_LOTTERY_ID), new Object[]{lotteryId});
    }

}
