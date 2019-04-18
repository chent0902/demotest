package com.family.demotest.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.PageList;
import com.family.demotest.dao.base.SettingDao;
import com.family.demotest.entity.BaseSettingModel;
import com.family.demotest.vo.base.BaseConfigVo;

/**
 * 
 * @author wujf
 *
 */
@Service("base.setting.service")
public class SettingServiceImpl
    extends
    ServiceSupport<BaseSettingModel>
    implements
    SettingService
{

    @Autowired
    private SettingDao settingDao;

    @Override
    protected QueryDao<BaseSettingModel> getQueryDao()
    {

        return settingDao;
    }

    @Override
    protected SaveDao<BaseSettingModel> getSaveDao()
    {

        return settingDao;
    }

    @Override
    protected DeleteDao<BaseSettingModel> getDeleteDao()
    {

        return settingDao;
    }

    @Override
    public PageList<BaseSettingModel> getPageList(BaseConfigVo model, int pageSize, int page)
    {
        PageList<BaseSettingModel> pageList = settingDao.getPageList(model, pageSize, page);

        return pageList;
    }

    @Override
    public BaseSettingModel findByChannelNo(String channelNo)
    {

        return settingDao.findByChannelNo(channelNo);
    }

}
