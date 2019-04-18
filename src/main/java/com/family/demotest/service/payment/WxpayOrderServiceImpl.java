package com.family.demotest.service.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.demotest.dao.payment.WxpayOrderDao;
import com.family.demotest.entity.WxpayOrderModel;

/**
 * 
 * @author wujf
 *
 */
@Service("wxpay.order.service")
public class WxpayOrderServiceImpl
    extends ServiceSupport<WxpayOrderModel>
    implements WxpayOrderService
{

    @Autowired
    private WxpayOrderDao wxpayOrderDao;

    @Override
    protected QueryDao<WxpayOrderModel> getQueryDao()
    {
        return wxpayOrderDao;
    }

    @Override
    protected SaveDao<WxpayOrderModel> getSaveDao()
    {
        return wxpayOrderDao;
    }

    @Override
    protected DeleteDao<WxpayOrderModel> getDeleteDao()
    {
        return wxpayOrderDao;
    }

    @Override
    public WxpayOrderModel findByPayNo(String payNo)
    {

        return wxpayOrderDao.findByPayNo(payNo);
    }

    @Override
    public void close()
    {
        wxpayOrderDao.close();

    }

    @Override
    public WxpayOrderModel getWxpayOrder(String partnerCode, String payNo)
    {

        return wxpayOrderDao.findByPayNo(partnerCode, payNo);
    }

}
