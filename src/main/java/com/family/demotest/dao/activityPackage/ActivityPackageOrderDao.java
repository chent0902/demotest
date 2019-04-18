package com.family.demotest.dao.activityPackage;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.ActivityPackageOrderModel;

public interface ActivityPackageOrderDao
    extends QueryDao<ActivityPackageOrderModel>, SaveDao<ActivityPackageOrderModel>, DeleteDao<ActivityPackageOrderModel>
{

    /**
     * 充值记录
     * 
     * @param channelNo
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<ActivityPackageOrderModel> payRecord(String channelNo, int page, int pageSize);

}
