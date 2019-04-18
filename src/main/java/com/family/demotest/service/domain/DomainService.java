package com.family.demotest.service.domain;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.DomainModel;

/**
 * 
 * @author wujf
 *
 */
public interface DomainService
    extends
    SaveService<DomainModel>,
    DeleteService<DomainModel>,
    QueryService<DomainModel>
{

    /**
     * 通过渠道号获取信息
     * 
     * @param channelNo
     * @return
     */
    public DomainModel findByChannelNo(String channelNo);

    /**
     * 把对象添加到缓存中
     * 
     * @param key
     * @param object
     */
    public void putRedis(String key, DomainModel model);

    /**
     * 获取主域名
     * 
     * @param channelNo
     * @return
     */
    public String getDomainUrl(String channelNo);

    /**
     * 获取业务域名
     * 
     * @param channelNo
     * @return
     */
    public String getBusinessDomainUrl(String channelNo);

}
