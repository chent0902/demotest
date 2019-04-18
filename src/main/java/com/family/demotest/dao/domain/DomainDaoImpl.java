package com.family.demotest.dao.domain;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.demotest.entity.DomainModel;

/**
 * 
 * @author wujf
 *
 */
@Repository("domain.dao")
public class DomainDaoImpl
    extends
    DaoSupport<DomainModel>
    implements
    DomainDao
{

    private static final String FIND_BY_CHANNEL_NO = " o.channelNo=? and "+UN_DELETE;

    @Override
    protected Class<DomainModel> getModelClass()
    {
        return DomainModel.class;
    }

    @Override
    public DomainModel findByChannelNo(String channelNo)
    {

        return findOne(newQueryBuilder().where(FIND_BY_CHANNEL_NO).setCacheEnable(true), new Object[]{channelNo});
    }

}
