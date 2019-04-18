package com.family.demotest.dao.domain;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.demotest.entity.DomainModel;

/**
 * 
 * @author wujf
 *
 */
public interface DomainDao
    extends
    QueryDao<DomainModel>,
    SaveDao<DomainModel>,
    DeleteDao<DomainModel>
{

    /**
     * 通过渠道号获取信息
     * 
     * @param channelNo
     * @return
     */
    public DomainModel findByChannelNo(String channelNo);

}
