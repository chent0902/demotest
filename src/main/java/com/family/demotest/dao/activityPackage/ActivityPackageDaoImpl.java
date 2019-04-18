package com.family.demotest.dao.activityPackage;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.ActivityPackageModel;

@Repository("activity.package.dao")
public class ActivityPackageDaoImpl
    extends DaoSupport<ActivityPackageModel>
    implements ActivityPackageDao
{

    private static final String BUY_LIST = " o.startTime <= ? AND o.endTime >= ? o.online = 1 AND  o.property = 0 ";

    private static final String LIST = " o.property = 0 ";

    @Override
    protected Class<ActivityPackageModel> getModelClass()
    {
        return ActivityPackageModel.class;
    }

    @Override
    public List<ActivityPackageModel> buyList()
    {
        PageList<ActivityPackageModel> pageList = list(newQueryBuilder().where(BUY_LIST), new Object[]{new Date(), new Date()}, Integer.MAX_VALUE, 1);

        return pageList.getList();
    }

    @Override
    public List<ActivityPackageModel> list()
    {
        PageList<ActivityPackageModel> pageList = list(newQueryBuilder().where(LIST), null, Integer.MAX_VALUE, 1);
        return pageList.getList();
    }

}
