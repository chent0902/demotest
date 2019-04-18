package com.family.demotest.service.merchant;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.MerchantUserModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 商户-用户关系
 * 
 * @author lm
 *
 */
public interface MerchantUserService
    extends SaveService<MerchantUserModel>, DeleteService<MerchantUserModel>, QueryService<MerchantUserModel>
{

    /**
     * 获取店铺管理员列表
     * 
     * @param channelNo
     * @param merchantId
     * @return
     */
    ResultCode adminList(String channelNo, String merchantId);

    /**
     * 商户新增管理员
     * 
     * @param channelNo
     * @param merchantId
     * @param userId
     * @param roleCode
     * @return
     */
    ResultCode addAdmin(String channelNo, String merchantId, String userId, String roleCode);

    /**
     * 查询用户管理商户信息
     * 
     * @param channelNo
     * @param userId
     * @return
     */
    MerchantUserModel findByUserId(String channelNo, String userId);

}
