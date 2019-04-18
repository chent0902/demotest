package com.family.demotest.service.lottery;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.LotteryScanHelpModel;

/**
 * 
 * @author lim
 *
 */
public interface LotteryScanHelpService
    extends
    SaveService<LotteryScanHelpModel>,
    DeleteService<LotteryScanHelpModel>,
    QueryService<LotteryScanHelpModel>
{

}
