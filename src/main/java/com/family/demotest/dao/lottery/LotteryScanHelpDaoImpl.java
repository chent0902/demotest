package com.family.demotest.dao.lottery;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.demotest.entity.LotteryScanHelpModel;

/**
 * 
 * @author wujf
 *
 */
@Repository("lottery.scan.help.dao")
public class LotteryScanHelpDaoImpl
    extends
    DaoSupport<LotteryScanHelpModel>
    implements
    LotteryScanHelpDao
{

    @Override
    protected Class<LotteryScanHelpModel> getModelClass()
    {
        return LotteryScanHelpModel.class;
    }

}
