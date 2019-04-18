package com.family.demotest.dao.activityPackage;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.demotest.entity.ActivityPackageModel;

public interface ActivityPackageDao
    extends QueryDao<ActivityPackageModel>, SaveDao<ActivityPackageModel>, DeleteDao<ActivityPackageModel>
{

    /**
     * 套餐列表 过滤
     * 
     * @return
     */
    public List<ActivityPackageModel> buyList();

    /**
     * 套餐列表 不过滤
     * 
     * @return
     */
    public List<ActivityPackageModel> list();

}
