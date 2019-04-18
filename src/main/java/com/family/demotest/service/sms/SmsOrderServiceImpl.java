package com.family.demotest.service.sms;

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
import com.family.demotest.common.enumtype.SmsPackageType;
import com.family.demotest.dao.sms.SmsOrderDao;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.entity.SmsOrderModel;
import com.family.demotest.entity.WxpayOrderModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.service.payment.WxPaymentService;
import com.family.demotest.vo.total.PartnerDataTotalVo;
import com.family.demotest.vo.wx.WxpayParamVo;
import com.family.demotest.web.util.OrderGenerateUtil;
import com.family.demotest.web.util.ResultCode;

@Service("sms.order.service")
public class SmsOrderServiceImpl
    extends ServiceSupport<SmsOrderModel>
    implements SmsOrderService
{

    @Autowired
    private SmsOrderDao smsOrderDao;
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
    protected QueryDao<SmsOrderModel> getQueryDao()
    {
        return smsOrderDao;
    }

    @Override
    protected SaveDao<SmsOrderModel> getSaveDao()
    {
        return smsOrderDao;
    }

    @Override
    protected DeleteDao<SmsOrderModel> getDeleteDao()
    {
        return smsOrderDao;
    }

    @Override
    public ResultCode payment(String channelNo, int smsType, String ip)
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

            SmsPackageType sms = SmsPackageType.validate(smsType);// 短信套餐枚举类型

            SmsOrderModel order = new SmsOrderModel();
            order.setChannelNo(channelNo);

            order.setNum(sms.getNum());
            order.setMoney(sms.getPrice());
            order.setName(sms.getMsg());
            order.setStatus(0);
            order.setTradeNo(outTradeNo);
            order.setCreateTime(new Date());
            this.save(order);

            // 微信支付
            WxpayParamVo wxpayParam = new WxpayParamVo();
            wxpayParam.setPaySource(PaySourceType.SMS.getCode());
            wxpayParam.setChannelNo(channelNo);
            wxpayParam.setOutTradeNo(outTradeNo);
            wxpayParam.setTotalAmount(order.getMoney()+"");
            wxpayParam.setOrderId(order.getId());
            wxpayParam.setSubject("短信充值");
            wxpayParam.setIp(ip);
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
            SmsOrderModel order = smsOrderDao.findById(wxpayOrder.getOrderId());
            if(order!=null)
            {
                order.setTransId(wxpayOrder.getTransactionId());// 微信订单号
                order.setTradeNo(wxpayOrder.getPayNo());// 我方订单号
                if(!validator.isBlank(wxpayOrder.getEndTime()))
                {
                    order.setPayTime(converter.stringToDate(wxpayOrder.getEndTime()));// 支付时间
                }
                order.setStatus(1);// 已付款

                smsOrderDao.save(order);
                // 加短信数
                BaseConfigModel config = configService.findByChannelNo(order.getChannelNo());
                config.setSmsNum(config.getSmsNum()+order.getNum());
                configService.save(config);
                // 充值金额缓存
                PartnerDataTotalVo totalVo = partnerSummaryRedisBiz.get(config.getId());
                if(totalVo==null)
                {
                    totalVo = new PartnerDataTotalVo();
                    totalVo.setSmsMoney(order.getMoney());
                }
                else
                {
                    totalVo.setSmsMoney(totalVo.getSmsMoney()+order.getMoney());
                }
                partnerSummaryRedisBiz.put(config.getId(), totalVo, 360, TimeUnit.DAYS);
            }
        }
        catch(Exception e)
        {
            logger.error(e, "短信充值支付回调处理出错");
        }

    }

    @Override
    public ResultCode payRecord(String channelNo, int page, int pageSize)
    {
        ResultCode result = new ResultCode();
        try
        {
            PageList<SmsOrderModel> pageList = smsOrderDao.payRecord(channelNo, page, pageSize);
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
