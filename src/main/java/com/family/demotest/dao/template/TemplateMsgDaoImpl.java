package com.family.demotest.dao.template;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.demotest.entity.TemplateMsgModel;

/**
 * 
 * @author wujf
 *
 */
@Repository("template.msg.dao")
public class TemplateMsgDaoImpl
    extends
    DaoSupport<TemplateMsgModel>
    implements
    TemplateMsgDao
{

    private static final String LIST = " o.channelNo=?  and "+UN_DELETE;
    private static final String FIND_BY_TYPE = " o.channelNo=? and o.type=? and "+UN_DELETE;

    @Override
    protected Class<TemplateMsgModel> getModelClass()
    {
        return TemplateMsgModel.class;
    }

    @Override
    public List<TemplateMsgModel> getList(String channelNo)
    {
        return list(newQueryBuilder().where(LIST).orderBy(" o.type desc ").setCacheEnable(true), new Object[]{channelNo}, 1000, 1).getList();
    }

    @Override
    public TemplateMsgModel findByType(String channelNo, int type)
    {
        return this.findOne(newQueryBuilder().where(FIND_BY_TYPE), new Object[]{channelNo, type});
    }

}
