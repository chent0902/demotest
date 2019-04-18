package com.family.demotest.dao.payment;

import java.util.List;

import com.family.base01.dao.Dao;
import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.demotest.entity.WxpayOrderModel;

public interface WxpayOrderDao
    extends QueryDao<WxpayOrderModel>, SaveDao<WxpayOrderModel>, DeleteDao<WxpayOrderModel>, Dao<WxpayOrderModel>
{

    /**
     * 通过订单号查询
     * 
     * @param orderNo
     * @return
     */
    public WxpayOrderModel findByPayNo(String orderNo);

    /**
     * 通过订单ID获取
     * 
     * @param partnerCode
     * @param orderId
     * @return
     */
    public List<WxpayOrderModel> findByOrderId(String partnerCode, String orderId);

    /**
     * 通过我方支付流水号获取
     * 
     * @param channelNo
     * @param payNo
     * @return
     */
    public WxpayOrderModel findByPayNo(String channelNo, String payNo);

}
