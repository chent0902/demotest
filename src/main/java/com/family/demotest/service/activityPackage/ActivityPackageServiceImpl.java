package com.family.demotest.service.activityPackage;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.base01.util.Validator;
import com.family.demotest.dao.activityPackage.ActivityPackageDao;
import com.family.demotest.entity.ActivityPackageModel;
import com.family.demotest.web.util.ResultCode;

@Service("activity.package.service")
public class ActivityPackageServiceImpl
    extends ServiceSupport<ActivityPackageModel>
    implements ActivityPackageService
{

    @Autowired
    private ActivityPackageDao activityPackageDao;
    @Autowired
    private Logger logger;
    @Autowired
    private Validator validator;

    @Override
    protected QueryDao<ActivityPackageModel> getQueryDao()
    {
        return activityPackageDao;
    }

    @Override
    protected SaveDao<ActivityPackageModel> getSaveDao()
    {
        return activityPackageDao;
    }

    @Override
    protected DeleteDao<ActivityPackageModel> getDeleteDao()
    {
        return activityPackageDao;
    }

    @Override
    public ResultCode buyList()
    {
        ResultCode result = new ResultCode();

        try
        {
            List<ActivityPackageModel> list = activityPackageDao.buyList();
            result.setData(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载活动点套餐出错");
            logger.error(e, "加载活动点套餐出错");
        }

        return result;
    }

    @Override
    public ResultCode list()
    {
        ResultCode result = new ResultCode();

        try
        {
            List<ActivityPackageModel> list = activityPackageDao.list();
            result.setData(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载活动点套餐出错");
            logger.error(e, "加载活动点套餐出错");
        }

        return result;
    }

    @Override
    public ResultCode saveOrUpdate(ActivityPackageModel model)
    {
        ResultCode result = new ResultCode();
        ActivityPackageModel instance = null;

        if(validator.isBlank(model.getId()))
        {
            instance = new ActivityPackageModel();
            instance.setCreateTime(new Date());
        }
        else
        {
            instance = this.findById(model.getId());
            if(instance==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return result;
            }
        }
        instance.setName(model.getName());
        instance.setNum(model.getNum());
        instance.setPrice(model.getPrice());
        instance.setOnline(1);
        if(model.getStartTime()!=null)
        {
            instance.setStartTime(model.getStartTime());
        }
        if(model.getEndTime()!=null)
        {
            instance.setEndTime(model.getEndTime());
        }
        try
        {
            this.save(instance);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存套餐信息出错");
            logger.error(e, "保存套餐信息出错");
        }

        return result;
    }

}
