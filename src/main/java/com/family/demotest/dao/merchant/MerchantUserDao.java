package com.family.demotest.dao.merchant;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.demotest.entity.MerchantUserModel;

/**
 * 商户-用户关系
 * 
 * @author lm
 *
 */
public interface MerchantUserDao
    extends QueryDao<MerchantUserModel>, SaveDao<MerchantUserModel>, DeleteDao<MerchantUserModel>
{

    /**
     * 商户管理员列表
     * 
     * @param channelNo
     * @param merchantId
     * @return
     */
    public List<MerchantUserModel> adminList(String channelNo, String merchantId);

    /**
     * 商户是否存在该管理员
     * 
     * @param channelNo
     * @param merchantId
     * @param userId
     * @return
     */
    public MerchantUserModel findByMercIdAndUserId(String channelNo, String merchantId, String userId);

    /**
     * 查询用户管理商户信息
     * 
     * @param channelNo
     * @param userId
     * @return
     */
    public MerchantUserModel findByUserId(String channelNo, String userId);

}
