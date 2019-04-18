package com.family.demotest.dao.lottery;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.demotest.entity.LotteryScanHelpModel;

/**
 * @author lm
 */
public interface LotteryScanHelpDao
    extends
    QueryDao<LotteryScanHelpModel>,
    SaveDao<LotteryScanHelpModel>,
    DeleteDao<LotteryScanHelpModel>
{

}
