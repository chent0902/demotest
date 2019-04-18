package com.family.demotest.service.activityPackage;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.ActivityPackageModel;
import com.family.demotest.entity.ActivityPackageOrderModel;
import com.family.demotest.entity.WxpayOrderModel;
import com.family.demotest.web.util.ResultCode;

public interface ActivityPackageOrderService
    extends SaveService<ActivityPackageOrderModel>, DeleteService<ActivityPackageOrderModel>, QueryService<ActivityPackageOrderModel>
{

    /**
     * 购买
     * 
     * @param channelNo
     * @param model
     * @param ip
     * @return
     */
    public ResultCode buy(String channelNo, ActivityPackageModel model, String ip);

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
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode payRecord(String channelNo, int page, int pageSize);

}
