package com.family.demotest.dao.sms;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.SmsOrderModel;

@Repository("sms.order.dao")
public class SmsOrderDaoImpl
    extends DaoSupport<SmsOrderModel>
    implements SmsOrderDao
{

    private static final String PAY_RECORD = " o.channelNo = ? AND o.status = 1 AND o.property = 0 ";

    private static final String ORDER_BY = " o.createTime DESC ";

    @Override
    protected Class<SmsOrderModel> getModelClass()
    {
        return SmsOrderModel.class;
    }

    @Override
    public PageList<SmsOrderModel> payRecord(String channelNo, int page, int pageSize)
    {
        PageList<SmsOrderModel> pageList = list(newQueryBuilder().where(PAY_RECORD).orderBy(ORDER_BY), new Object[]{channelNo}, pageSize, page);

        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return null;
        }
        return pageList;
    }

}
