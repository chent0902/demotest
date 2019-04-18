package com.family.demotest.service.activityPackage;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Converter;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.PartnerSummaryRedisBiz;
import com.family.demotest.common.enumtype.PaySourceType;
import com.family.demotest.dao.activityPackage.ActivityPackageOrderDao;
import com.family.demotest.entity.ActivityPackageModel;
import com.family.demotest.entity.ActivityPackageOrderModel;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.entity.WxpayOrderModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.service.payment.WxPaymentService;
import com.family.demotest.vo.total.PartnerDataTotalVo;
import com.family.demotest.vo.wx.WxpayParamVo;
import com.family.demotest.web.util.OrderGenerateUtil;
import com.family.demotest.web.util.ResultCode;

@Service("activity.package.order.service")
public class ActivityPackageOrderServiceImpl
    extends ServiceSupport<ActivityPackageOrderModel>
    implements ActivityPackageOrderService
{

    @Autowired
    private ActivityPackageOrderDao activityPackageOrderDao;
    @Autowired
    private Logger logger;
    @Autowired
    private Validator validator;
    @Autowired
    private ConfigService configService;
    @Autowired
    private WxPaymentService wxPaymentService;
    @Autowired
    private PartnerSummaryRedisBiz partnerSummaryRedisBiz;
    @Autowired
    private Converter converter;

    @Override
    protected QueryDao<ActivityPackageOrderModel> getQueryDao()
    {
        return activityPackageOrderDao;
    }

    @Override
    protected SaveDao<ActivityPackageOrderModel> getSaveDao()
    {
        return activityPackageOrderDao;
    }

    @Override
    protected DeleteDao<ActivityPackageOrderModel> getDeleteDao()
    {
        return activityPackageOrderDao;
    }

    @Override
    public ResultCode buy(String channelNo, ActivityPackageModel model, String ip)
    {
        ResultCode result = new ResultCode();
        try
        {
            BaseConfigModel config = configService.findByChannelNo(channelNo);
            if(config==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return result;
            }

            String outTradeNo = OrderGenerateUtil.generateOrderNo();

            ActivityPackageOrderModel order = new ActivityPackageOrderModel();
            order.setChannelNo(channelNo);
            if(!validator.isBlank(model.getId()))
            {
                order.setActivityPackageId(model.getId());
            }
            order.setNickName(config.getNickName());
            order.setNum(model.getNum());
            order.setPrice(model.getPrice());
            order.setStatus(0);
            order.setTradeNo(outTradeNo);
            order.setCreateTime(new Date());
            this.save(order);

            WxpayParamVo wxpayParam = new WxpayParamVo();
            wxpayParam.setPaySource(PaySourceType.POINT.getCode());
            wxpayParam.setChannelNo(channelNo);
            wxpayParam.setOutTradeNo(outTradeNo);
            wxpayParam.setTotalAmount(order.getPrice()+"");
            wxpayParam.setOrderId(order.getId());
            wxpayParam.setSubject(model.getName());
            wxpayParam.setIp(ip);
            wxpayParam.setSubject("活动点充值");

            result = wxPaymentService.scanPay(wxpayParam);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("购买活动点出错");
            logger.error(e, "购买活动点出错");
        }

        return result;
    }

    @Override
    public void wxpayCallback(WxpayOrderModel wxpayOrder)
    {

        try
        {

            ActivityPackageOrderModel order = activityPackageOrderDao.findById(wxpayOrder.getOrderId());
            if(order!=null)
            {
                order.setTransId(wxpayOrder.getTransactionId());// 微信订单号
                order.setTradeNo(wxpayOrder.getPayNo());// 我方订单号
                if(!validator.isBlank(wxpayOrder.getEndTime()))
                {
                    order.setPayTime(converter.stringToDate(wxpayOrder.getEndTime()));// 支付时间
                }
                order.setStatus(1);// 已付款
                order.setPrepayId(wxpayOrder.getPrepayId());// 预支付id
                int isDelete = order.getProperty();// 1-表示订单已过期了
                if(isDelete==1)
                {
                    order.setProperty(0);
                }
                activityPackageOrderDao.save(order);
                // 加活动点
                BaseConfigModel config = configService.findByChannelNo(order.getChannelNo());
                config.setActivePoint(config.getActivePoint()+order.getNum());
                configService.save(config);
                // 充值金额缓存
                PartnerDataTotalVo totalVo = partnerSummaryRedisBiz.get(config.getId());
                if(totalVo==null)
                {
                    totalVo = new PartnerDataTotalVo();
                    totalVo.setActivePointMoney(order.getPrice());
                }
                else
                {
                    totalVo.setActivePointMoney(totalVo.getActivePointMoney()+order.getPrice());
                }
                partnerSummaryRedisBiz.put(config.getId(), totalVo, 360, TimeUnit.DAYS);
            }
        }
        catch(Exception e)
        {
            logger.error(e, "活动点套餐支付回调处理出错");
        }

    }

    @Override
    public ResultCode payRecord(String channelNo, int page, int pageSize)
    {
        ResultCode result = new ResultCode();
        try
        {
            PageList<ActivityPackageOrderModel> pageList = activityPackageOrderDao.payRecord(channelNo, page, pageSize);
            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载充值记录出错");
            logger.error(e, "加载充值记录出错");
        }
        return result;
    }
}
