package com.family.demotest.service.ad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.PageList;
import com.family.demotest.dao.ad.HomeAdDao;
import com.family.demotest.entity.HomeAdModel;

/**
 * 
 * @author wujf
 *
 */
@Service("home.ad.service")
public class HomeAdServiceImpl
    extends
    ServiceSupport<HomeAdModel>
    implements
    HomeAdService
{

    @Autowired
    private HomeAdDao homeAdDao;

    @Override
    protected QueryDao<HomeAdModel> getQueryDao()
    {

        return homeAdDao;
    }

    @Override
    protected SaveDao<HomeAdModel> getSaveDao()
    {

        return homeAdDao;
    }

    @Override
    protected DeleteDao<HomeAdModel> getDeleteDao()
    {

        return homeAdDao;
    }

    @Override
    public List<HomeAdModel> getList(String channelNo, int type)
    {

        return homeAdDao.getList(channelNo, type);
    }

    @Override
    public PageList<HomeAdModel> getPageList(HomeAdModel model, int pageSize, int page)
    {

        return homeAdDao.getPageList(model, pageSize, page);
    }

}
