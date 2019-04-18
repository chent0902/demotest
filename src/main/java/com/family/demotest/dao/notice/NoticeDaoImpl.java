package com.family.demotest.dao.notice;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.NoticeModel;

/**
 * 
 * @author lim
 *
 */
@Repository("notice.dao")
public class NoticeDaoImpl
    extends DaoSupport<NoticeModel>
    implements NoticeDao
{

    private static final String LIST = " o.property = 0 ";

    private static final String ORDER_BY = " o.createTime DESC ";

    @Override
    protected Class<NoticeModel> getModelClass()
    {
        return NoticeModel.class;
    }

    @Override
    public PageList<NoticeModel> list(int page, int pageSize)
    {
        PageList<NoticeModel> pageList = list(newQueryBuilder().where(LIST).orderBy(ORDER_BY), null, pageSize, page);
        return pageList;

    }

}
