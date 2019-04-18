package com.family.demotest.service.sms;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.SmsOrderModel;
import com.family.demotest.entity.WxpayOrderModel;
import com.family.demotest.web.util.ResultCode;

public interface SmsOrderService
    extends SaveService<SmsOrderModel>, DeleteService<SmsOrderModel>, QueryService<SmsOrderModel>
{

    /**
     * 短信支付
     * 
     * @param channelNo
     * @param smsType
     *            短信 1-50元，2-100元，3-200元
     * @return
     */

    /**
     * 
     * @param channelNo
     * @param smsType
     *            短信 1-50元，2-100元，3-500元
     * @param ip
     * @return
     */
    public ResultCode payment(String channelNo, int smsType, String ip);

    /**
     * 支付成功 回调
     * 
     * @param order
     */
    public void wxpayCallback(WxpayOrderModel wxpayOrder);

    /**
     * 充值记录
     * 
     * @param channelNo
     * @param page
     * @param pageSize
     * @return
     */
    public ResultCode payRecord(String channelNo, int page, int pageSize);

}
