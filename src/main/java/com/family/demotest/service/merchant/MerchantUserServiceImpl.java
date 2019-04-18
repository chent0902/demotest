package com.family.demotest.service.merchant;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.demotest.dao.merchant.MerchantUserDao;
import com.family.demotest.entity.MerchantUserModel;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.user.UserService;
import com.family.demotest.web.util.ResultCode;

@Service("merchant.user.service")
public class MerchantUserServiceImpl
    extends
    ServiceSupport<MerchantUserModel>
    implements
    MerchantUserService
{

    @Autowired
    private MerchantUserDao merchantUserDao;
    @Autowired
    private UserService userService;
    @Autowired
    private Logger logger;

    @Override
    protected QueryDao<MerchantUserModel> getQueryDao()
    {

        return merchantUserDao;
    }

    @Override
    protected SaveDao<MerchantUserModel> getSaveDao()
    {

        return merchantUserDao;
    }

    @Override
    protected DeleteDao<MerchantUserModel> getDeleteDao()
    {

        return merchantUserDao;
    }

    @Override
    public ResultCode adminList(String channelNo, String merchantId)
    {
        ResultCode result = new ResultCode();

        try
        {
            List<MerchantUserModel> list = merchantUserDao.adminList(channelNo, merchantId);
            result.setData(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取店铺管理员列表出错");
            logger.error(e, "获取店铺管理员列表出错");
        }

        return result;
    }

    @Override
    public ResultCode addAdmin(String channelNo, String merchantId, String userId, String roleCode)
    {

        ResultCode result = new ResultCode();

        MerchantUserModel instance = merchantUserDao.findByMercIdAndUserId(channelNo, merchantId, userId);
        if(instance!=null)
        {
            result.setCode(-1);
            result.setInfo("该用户已经是此店铺的管理员");
            return result;
        }
        UserModel user = userService.findById(userId);
        if(user==null)
        {
            result.setCode(-1);
            result.setInfo("用户id不存在");
            return result;
        }

        MerchantUserModel model = new MerchantUserModel();
        model.setChannelNo(channelNo);
        model.setMerchantId(merchantId);
        model.setUserId(userId);
        model.setRoleCode(roleCode);
        model.setCreateTime(new Date());
        try
        {
            merchantUserDao.save(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存商户管理员出错");
            logger.error(e, "保存商户管理员出错");
        }

        return result;
    }

    @Override
    public MerchantUserModel findByUserId(String channelNo, String userId)
    {
        return merchantUserDao.findByUserId(channelNo, userId);
    }
}
