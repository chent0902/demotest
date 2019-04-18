package com.family.demotest.dao.payment;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.demotest.entity.WxpayOrderModel;

/**
 * 
 * @author wujf
 *
 */

@Repository("wxpay.order.dao")
public class WxpayOrderDaoImpl
    extends DaoSupport<WxpayOrderModel>
    implements WxpayOrderDao
{

    private static final String FIND_BY_PAY_NO = " o.payNo=? ";

    private static final String FIND_BY_ORDER_ID = " o.partnerCode=?  and o.orderId=?  and o.payStatus=1 ";

    @Override
    protected Class<WxpayOrderModel> getModelClass()
    {
        return WxpayOrderModel.class;
    }

    @Override
    public WxpayOrderModel findByPayNo(String orderNo)
    {

        return findOne(newQueryBuilder().where(FIND_BY_PAY_NO), new Object[]{orderNo});
    }

    @Override
    public List<WxpayOrderModel> findByOrderId(String partnerCode, String orderId)
    {

        return list(newQueryBuilder().where(FIND_BY_ORDER_ID), new Object[]{partnerCode, orderId}, Integer.MAX_VALUE, 1).getList();
    }

    @Override
    public WxpayOrderModel findByPayNo(String partnerCode, String payNo)
    {

        return findOne(newQueryBuilder().where(FIND_BY_PAY_NO), new Object[]{partnerCode, payNo});
    }

}
