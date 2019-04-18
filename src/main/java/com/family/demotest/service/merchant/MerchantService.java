package com.family.demotest.service.merchant;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.MerchantModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
public interface MerchantService
    extends SaveService<MerchantModel>, DeleteService<MerchantModel>, QueryService<MerchantModel>
{

    /**
     * 后台商户列表
     * 
     * @param channelNo
     * @return
     */
    public ResultCode pageList(String channelNo, int page, int pageSize);

    /**
     * 保存商户信息
     * 
     * @param merchantVo
     * @return
     */
    public ResultCode createOrUpdate(MerchantModel merchant);

    /**
     * 获取全部商户(名称+id)
     * 
     * @param channelNo
     * @param name
     * @return
     */
    public ResultCode findAllName(String channelNo, String name);

    /**
     * 查询用户商户信息
     * 
     * @param channelNo
     * @param id
     * @return
     */
    public ResultCode findByUserId(String channelNo, String userId);

}
