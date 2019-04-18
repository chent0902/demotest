package com.family.demotest.dao.sms;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.SmsOrderModel;

public interface SmsOrderDao
    extends QueryDao<SmsOrderModel>, SaveDao<SmsOrderModel>, DeleteDao<SmsOrderModel>
{

    public PageList<SmsOrderModel> payRecord(String channelNo, int page, int pageSize);

}
