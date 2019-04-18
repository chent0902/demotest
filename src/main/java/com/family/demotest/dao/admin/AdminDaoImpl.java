package com.family.demotest.dao.admin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.AdminModel;

/**
 * 
 * @author lim
 *
 */
@Repository("admin.dao")
public class AdminDaoImpl
    extends DaoSupport<AdminModel>
    implements AdminDao
{

    private static final String FIND_BY_ACCOUNT = " o.account=?  and "+UN_DELETE;

    private static final String LIST = " o.channelNo = ? AND o.property = 0 ";

    private static final String ORDER_BY = " o.createTime DESC ";

    private static final String GET_MANAGE = " o.channelNo = ? AND o.manage = 1 AND o.property = 0 ";

    @Override
    protected Class<AdminModel> getModelClass()
    {
        return AdminModel.class;
    }

    @Override
    public AdminModel findByAccount(String account)
    {
        return this.findOne(newQueryBuilder().where(FIND_BY_ACCOUNT), new Object[]{account});
    }

    @Override
    public List<AdminModel> list(String channelNo)
    {
        PageList<AdminModel> pageList = list(newQueryBuilder().where(LIST).orderBy(ORDER_BY), new Object[]{channelNo}, Integer.MAX_VALUE, 1);

        return pageList.getList();
    }

    @Override
    public AdminModel getManage(String channelNo)
    {
        return this.findOne(newQueryBuilder().where(GET_MANAGE), new Object[]{channelNo});
    }
}
