package com.family.demotest.service.lottery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.demotest.dao.lottery.LotteryDetailsDao;
import com.family.demotest.entity.LotteryDetailsModel;

/**
 * 
 * @author lim
 *
 */
@Service("lottery.details.service")
public class LotteryDetailsServiceImpl
    extends ServiceSupport<LotteryDetailsModel>
    implements LotteryDetailsService
{

    @Autowired
    private LotteryDetailsDao lotteryDetailsDao;

    @Override
    protected QueryDao<LotteryDetailsModel> getQueryDao()
    {
        return lotteryDetailsDao;
    }

    @Override
    protected SaveDao<LotteryDetailsModel> getSaveDao()
    {
        return lotteryDetailsDao;
    }

    @Override
    protected DeleteDao<LotteryDetailsModel> getDeleteDao()
    {
        return lotteryDetailsDao;
    }

    @Override
    public LotteryDetailsModel findByLotteryId(String lotteryId)
    {
        return lotteryDetailsDao.findByLotteryId(lotteryId);
    }

}
