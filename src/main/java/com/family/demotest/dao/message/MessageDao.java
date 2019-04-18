package com.family.demotest.dao.message;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.MessageModel;

public interface MessageDao
    extends QueryDao<MessageModel>, SaveDao<MessageModel>, DeleteDao<MessageModel>
{

    public PageList<MessageModel> messageRecord(String channelNo, int page, int pageSize);

}
