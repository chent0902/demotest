package com.family.demotest.service.lottery;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.LotteryDetailsModel;

/**
 * 
 * @author lim
 *
 */
public interface LotteryDetailsService
    extends SaveService<LotteryDetailsModel>, DeleteService<LotteryDetailsModel>, QueryService<LotteryDetailsModel>
{

    /**
     * 根据抽奖ID查详情
     * 
     * @param id
     * @return
     */
    public LotteryDetailsModel findByLotteryId(String lotteryId);

}
