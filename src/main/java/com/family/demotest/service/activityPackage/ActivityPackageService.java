package com.family.demotest.service.activityPackage;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.ActivityPackageModel;
import com.family.demotest.web.util.ResultCode;

public interface ActivityPackageService
    extends SaveService<ActivityPackageModel>, DeleteService<ActivityPackageModel>, QueryService<ActivityPackageModel>
{

    /**
     * 套餐列表 过滤
     * 
     * @return
     */
    public ResultCode buyList();

    /**
     * 保存套餐信息
     * 
     * @param id
     * @param name
     * @param num
     * @param price
     * @return
     */
    public ResultCode saveOrUpdate(ActivityPackageModel model);

    /**
     * 套餐列表 不过滤
     * 
     * @return
     */

    public ResultCode list();

}
