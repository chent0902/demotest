package com.family.demotest.dao.merchant;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.MerchantModel;

/**
 * 
 * @author lim
 *
 */
public interface MerchantDao
    extends QueryDao<MerchantModel>, SaveDao<MerchantModel>, DeleteDao<MerchantModel>
{

    /**
     * 后台商户列表
     * 
     * @param channelNo
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<MerchantModel> pageList(String channelNo, int page, int pageSize);

    /**
     * 获取全部商户(名称+id)
     * 
     * @param sql
     * @param args
     * @return
     */
    public List<MerchantModel> findAllName(StringBuffer sql, List<Object> args);

}
