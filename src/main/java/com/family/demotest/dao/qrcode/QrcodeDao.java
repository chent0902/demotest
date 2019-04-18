package com.family.demotest.dao.qrcode;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.QrcodeModel;

/**
 * 
 * @author wujf
 *
 */
public interface QrcodeDao
    extends
    QueryDao<QrcodeModel>,
    SaveDao<QrcodeModel>,
    DeleteDao<QrcodeModel>
{

    public PageList<QrcodeModel> getPageList(String channelNo, int page, int pageSize);

}
