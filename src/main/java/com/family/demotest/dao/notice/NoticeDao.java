package com.family.demotest.dao.notice;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.NoticeModel;

/**
 * 
 * @author lim
 *
 */
public interface NoticeDao
    extends QueryDao<NoticeModel>, SaveDao<NoticeModel>, DeleteDao<NoticeModel>
{

    public PageList<NoticeModel> list(int page, int pageSize);

}
