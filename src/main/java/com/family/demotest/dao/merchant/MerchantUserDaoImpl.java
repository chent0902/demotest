package com.family.demotest.dao.merchant;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.MerchantUserModel;

/**
 * @author lm
 */
@Repository("merchant.user.dao")
public class MerchantUserDaoImpl
    extends DaoSupport<MerchantUserModel>
    implements MerchantUserDao
{

    private static final String ADMIN_LIST = " o.channelNo = ? AND o.merchantId = ? AND o.property = 0 ";

    private static final String FIND_ONE = " o.channelNo = ? AND o.merchantId = ? AND o.userId = ? AND o.property = 0 ";

    private static final String FIND_BY_USER_ID = " o.channelNo = ? AND o.userId = ? AND o.property = 0 ";

    @Override
    protected Class<MerchantUserModel> getModelClass()
    {

        return MerchantUserModel.class;
    }

    @Override
    public List<MerchantUserModel> adminList(String channelNo, String merchantId)
    {

        PageList<MerchantUserModel> pageList = list(newQueryBuilder().where(ADMIN_LIST), new Object[]{channelNo, merchantId}, Integer.MAX_VALUE, 1);

        return pageList.getList();
    }

    @Override
    public MerchantUserModel findByMercIdAndUserId(String channelNo, String merchantId, String userId)
    {
        return this.findOne(newQueryBuilder().where(FIND_ONE), new Object[]{channelNo, merchantId, userId});
    }

    @Override
    public MerchantUserModel findByUserId(String channelNo, String userId)
    {
        return this.findOne(newQueryBuilder().where(FIND_BY_USER_ID), new Object[]{channelNo, userId});
    }

}
