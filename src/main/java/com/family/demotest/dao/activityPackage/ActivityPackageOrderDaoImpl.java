package com.family.demotest.dao.activityPackage;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.ActivityPackageOrderModel;

@Repository("activity.package.order.dao")
public class ActivityPackageOrderDaoImpl
    extends DaoSupport<ActivityPackageOrderModel>
    implements ActivityPackageOrderDao
{

    private static final String PAY_RECORD = " o.channelNo = ? AND o.status = 1 AND o.property = 0 ";

    private static final String ORDER_BY = " o.createTime DESC ";

    @Override
    protected Class<ActivityPackageOrderModel> getModelClass()
    {
        return ActivityPackageOrderModel.class;
    }

    @Override
    public PageList<ActivityPackageOrderModel> payRecord(String channelNo, int page, int pageSize)
    {
        PageList<ActivityPackageOrderModel> pageList = list(newQueryBuilder().where(PAY_RECORD).orderBy(ORDER_BY), new Object[]{channelNo}, pageSize, page);

        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return null;
        }
        return pageList;
    }
}
