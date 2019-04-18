package com.family.demotest.dao.lottery;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.demotest.entity.LotteryAwardsModel;

/**
 * 
 * @author lm
 *
 */
@Repository("lottery.awards.dao")
public class LotteryAwardsDaoImpl
    extends
    DaoSupport<LotteryAwardsModel>
    implements
    LotteryAwardsDao
{

    private static final String LIST = " o.channelNo = ? AND o.lotteryId = ? AND o.property = 0 ";

    private static final String GET_LOWEST_PRICE = "  o.channelNo = ? AND o.lotteryId = ? AND o.grade = 0 AND o.property = 0  ";

    private static final String FIND_BY_GRADE = " o.lotteryId = ? AND o.grade = ? AND o.property = 0 ";

    private static final String ORDER_BY = " o.grade ASC ";

    @Override
    protected Class<LotteryAwardsModel> getModelClass()
    {
        return LotteryAwardsModel.class;
    }

    @Override
    public List<LotteryAwardsModel> list(String channelNo, String lotteryId)
    {

        return list(newQueryBuilder().where(LIST).orderBy(ORDER_BY), new Object[]{channelNo, lotteryId}, 10, 1).getList();
    }

    @Override
    public LotteryAwardsModel getlowestPrice(String channelNo, String lotteryId)
    {
        return this.findOne(newQueryBuilder().where(GET_LOWEST_PRICE), new Object[]{channelNo, lotteryId});
    }

    @Override
    public LotteryAwardsModel findByGrade(String lotteryId, int answer)
    {

        return this.findOne(newQueryBuilder().where(FIND_BY_GRADE), new Object[]{lotteryId, answer});
    }
}
