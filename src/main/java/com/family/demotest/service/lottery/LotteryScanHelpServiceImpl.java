package com.family.demotest.service.lottery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.demotest.dao.lottery.LotteryScanHelpDao;
import com.family.demotest.entity.LotteryScanHelpModel;

/**
 * 
 * @author lim
 *
 */
@Service("lottery.scan.help.service")
public class LotteryScanHelpServiceImpl
    extends
    ServiceSupport<LotteryScanHelpModel>
    implements
    LotteryScanHelpService
{

    @Autowired
    private LotteryScanHelpDao lotteryScanHelpDao;

    @Override
    protected QueryDao<LotteryScanHelpModel> getQueryDao()
    {
        return lotteryScanHelpDao;
    }

    @Override
    protected SaveDao<LotteryScanHelpModel> getSaveDao()
    {
        return lotteryScanHelpDao;
    }

    @Override
    protected DeleteDao<LotteryScanHelpModel> getDeleteDao()
    {
        return lotteryScanHelpDao;
    }

}
