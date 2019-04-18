package com.family.demotest.dao.lottery;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.LotteryMessageModel;

/**
 * 
 * @author lm
 *
 */
@Repository("lottery.message.dao")
public class LotteryMessageDaoImpl
    extends DaoSupport<LotteryMessageModel>
    implements LotteryMessageDao
{

    private static final String PAGE_LIST = " o.channelNo = ? AND o.lotteryId = ? AND o.property = 0  ";

    private static final String ORDER_BY = " o.createTime DESC ";

    private static final String ORDER_BY_TOP = " o.top DESC ,o.createTime DESC ";

    private static final String CHOICENESS_LIST = " o.lotteryId = ? AND o.choiceness = 1 AND o.property = 0 ";

    private static final String MY_MESSAGE = " o.channelNo = ? AND o.lotteryId = ? AND o.userId = ? AND o.property = 0 ";

    @Override
    protected Class<LotteryMessageModel> getModelClass()
    {
        return LotteryMessageModel.class;
    }

    @Override
    public PageList<LotteryMessageModel> pageList(String channelNo, String lotteryId, int page, int pageSize)
    {
        PageList<LotteryMessageModel> pageList = list(newQueryBuilder().where(PAGE_LIST).orderBy(ORDER_BY_TOP), new Object[]{channelNo, lotteryId}, pageSize,
            page);

        int totalPage = pageList.getPageInfo().getTotalPage();

        if(page>totalPage)
        {
            return null;
        }

        return pageList;
    }

    @Override
    public List<LotteryMessageModel> choicenessList(String lotteryId, int page, int pageSize)
    {
        PageList<LotteryMessageModel> pageList = list(newQueryBuilder().where(CHOICENESS_LIST).orderBy(ORDER_BY_TOP), new Object[]{lotteryId}, pageSize, page);
        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return new ArrayList<LotteryMessageModel>();
        }
        return pageList.getList();
    }

    @Override
    public List<LotteryMessageModel> myMessage(String channelNo, String lotteryId, String userId)
    {
        PageList<LotteryMessageModel> pageList = list(newQueryBuilder().where(MY_MESSAGE).orderBy(ORDER_BY), new Object[]{channelNo, lotteryId, userId},
            Integer.MAX_VALUE, 1);

        return pageList.getList();
    }

}
