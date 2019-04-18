package com.family.demotest.service.payment;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.WxpayOrderModel;

/**
 * 
 * @author wujf
 *
 */
public interface WxpayOrderService
    extends QueryService<WxpayOrderModel>, SaveService<WxpayOrderModel>, DeleteService<WxpayOrderModel>
{

    /**
     * 通过订单号查询
     * 
     * @param payNo
     * @return
     */
    public WxpayOrderModel findByPayNo(String payNo);

    /**
     * 关闭数据库
     */
    public void close();

    /**
     * 通过我方支付流水号获取
     * 
     * @param partnerCode
     * @param payNo
     * @return
     */
    public WxpayOrderModel getWxpayOrder(String partnerCode, String payNo);

}
