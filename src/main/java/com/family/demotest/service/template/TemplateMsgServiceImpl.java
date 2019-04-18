package com.family.demotest.service.template;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Converter;
import com.family.base01.util.Logger;
import com.family.base01.util.Validator;
import com.family.demotest.common.notify.NotifyManager;
import com.family.demotest.dao.template.TemplateMsgDao;
import com.family.demotest.entity.TemplateMsgModel;
import com.family.demotest.service.domain.DomainService;
import com.family.demotest.service.wxopen.TicketService;
import com.family.demotest.service.wxopen.WxTemplateService;
import com.family.demotest.vo.wx.MessageVo;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author wujf
 *
 */
@Service("template.msg.service")
public class TemplateMsgServiceImpl
    extends ServiceSupport<TemplateMsgModel>
    implements TemplateMsgService
{

    @Autowired
    private TemplateMsgDao templateMsgDao;
    @Autowired
    private WxTemplateService wxTemplateService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private Validator validator;
    @Autowired
    private Logger logger;
    @Autowired
    private Converter converter;
    @Autowired
    private DomainService domainService;

    @Override
    protected QueryDao<TemplateMsgModel> getQueryDao()
    {

        return templateMsgDao;
    }

    @Override
    protected SaveDao<TemplateMsgModel> getSaveDao()
    {

        return templateMsgDao;
    }

    @Override
    protected DeleteDao<TemplateMsgModel> getDeleteDao()
    {

        return templateMsgDao;
    }

    @Override
    public List<TemplateMsgModel> getList(String channelNo)
    {
        return templateMsgDao.getList(channelNo);
    }

    @Override
    public TemplateMsgModel findByType(String channelNo, int type)
    {
        return templateMsgDao.findByType(channelNo, type);
    }

    @Override
    public ResultCode addTemplate(String channelNo, int type)
    {

        ResultCode result = new ResultCode();
        // wxTemplateService.getIndustry(channelNo);

        // param.put("template_id_short", "OPENTM405464954"); // 到账通知
        String templateIdShort = "";
        // 1. 到账通知，2-抽奖结果通知，3-核销成功通知，4-服务到期提醒
        switch(type)
        {
        case 1:
            templateIdShort = "OPENTM407369197";// OPENTM405464954
            break;
        case 2:
            templateIdShort = "OPENTM412181311";
            break;
        case 3:
            templateIdShort = "OPENTM416793159";
            break;
        case 4:
            templateIdShort = "OPENTM201779702";
            break;
        case 5:
            templateIdShort = "OPENTM411328149";
            break;
        case 6:
            templateIdShort = "OPENTM407447368";
            break;
        case 7:
            templateIdShort = "OPENTM200602458";
            break;
        case 8:
            templateIdShort = "TM00575";
            break;
        case 9:
            templateIdShort = "OPENTM415137048";
            break;
        default:
            break;
        }

        result = wxTemplateService.getTemplateId(channelNo, templateIdShort);

        if(result.getCode()==0)
        {
            String templateId = result.getOther();// 消息模块ID
            TemplateMsgModel msg = new TemplateMsgModel();
            msg.setChannelNo(channelNo);
            msg.setType(type);
            msg.setTemplateId(templateId);
            msg.setCreateTime(new Date());
            templateMsgDao.save(msg);
        }

        return result;

    }

    @Override
    public void delete(String id)
    {
        TemplateMsgModel model = templateMsgDao.findById(id);
        if(model!=null)
        {
            String templateId = model.getTemplateId();
            // 删除公众号里面的模版id
            wxTemplateService.deleteTemplate(model.getChannelNo(), templateId);
            templateMsgDao.delete(model);
        }
    }

    @Override
    public void sendMsg1(String channelNo, String templateId, String openId, String nickName, String lotteryId)
    {
        try
        {
            String accessToken = ticketService.getAuthorizerAccessToken(channelNo);

            if(validator.isBlank(accessToken))
            {
                logger.info("发送模版消息，token失效");
                return;
            }

            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", "恭喜您：您的好友帮您增加1次抽奖机会");
            first.put("color", "#173177");
            keyList.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", nickName);
            keyword1.put("color", "#173177");
            keyList.put("keyword1", keyword1);

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", "1次抽奖机会");
            keyword2.put("color", "#173177");
            keyList.put("keyword2", keyword2);

            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", converter.toTimeString(new Date()));
            keyword3.put("color", "#173177");
            keyList.put("keyword3", keyword3);

            JSONObject remark = new JSONObject();
            remark.put("value", "点击查看，继续抽奖");
            remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo("1000001");
            msgVo.setToken(accessToken);
            msgVo.setTouser(openId);
            msgVo.setTemplateId(templateId);
            // msgVo.setTemplateId("XMhBqdtLcedJjeTLVfNnO5TPM6HYnw5TaWrhgh4Fz5I");
            msgVo.setData(keyList);

            // 主域名
            String domainUrl = domainService.getDomainUrl(channelNo);
            String url = domainUrl+"/details.html?channelNo="+channelNo+"&id="+lotteryId;
            msgVo.setUrl(url);
            NotifyManager.instance.notifyTemplateMsg(msgVo);
        }
        catch(Exception e)
        {
            logger.error(e, "发送到账通知模版消息出错");

        }

    }

    @Override
    public void sendMsg2(String channelNo, String openId, String prizeName, String prize)
    {
        try
        {

            TemplateMsgModel template = templateMsgDao.findByType(channelNo, 2);
            if(template==null)
            {
                logger.info("抽奖结果模版消息不存在");
                return;
            }
            String accessToken = ticketService.getAuthorizerAccessToken(channelNo);

            if(validator.isBlank(accessToken))
            {
                logger.info("发送模版消息，token失效");
                return;
            }

            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", "恭喜您抽中"+prize);
            first.put("color", "#173177");
            keyList.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", prizeName);
            keyword1.put("color", "#173177");
            keyList.put("keyword1", keyword1);

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", converter.toTimeString(new Date()));
            keyword2.put("color", "#173177");
            keyList.put("keyword2", keyword2);

            JSONObject remark = new JSONObject();
            remark.put("value", "点击即可查看和使用");
            remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo(channelNo);
            msgVo.setToken(accessToken);
            msgVo.setTouser(openId);
            msgVo.setTemplateId(template.getTemplateId());
            // msgVo.setTemplateId("XMhBqdtLcedJjeTLVfNnO5TPM6HYnw5TaWrhgh4Fz5I");
            msgVo.setData(keyList);

            // 主域名
            String domainUrl = domainService.getDomainUrl(channelNo);
            String url = domainUrl+"/couponlist.html?channelNo="+channelNo;
            msgVo.setUrl(url);
            NotifyManager.instance.notifyTemplateMsg(msgVo);
        }
        catch(Exception e)
        {
            logger.error(e, "发送抽奖结果通知模版消息出错");

        }

    }

    @Override
    public void sendMsg3(String channelNo, String openId, String name, String merchantName)
    {

        try
        {

            TemplateMsgModel template = templateMsgDao.findByType(channelNo, 3);
            if(template==null)
            {
                logger.info("抽奖结果模版消息不存在");
                return;
            }

            String accessToken = ticketService.getAuthorizerAccessToken(channelNo);

            if(validator.isBlank(accessToken))
            {
                logger.info("发送模版消息，token失效");
                return;
            }

            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", "恭喜您：成功核销兑换券一张！");
            first.put("color", "#173177");
            keyList.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", name);
            keyword1.put("color", "#173177");
            keyList.put("keyword1", keyword1);

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", converter.toTimeString(new Date()));
            keyword2.put("color", "#173177");
            keyList.put("keyword2", keyword2);

            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", merchantName);
            keyword3.put("color", "#173177");
            keyList.put("keyword3", keyword3);

            JSONObject remark = new JSONObject();
            remark.put("value", "感谢您的再次使用！");
            remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo(channelNo);
            msgVo.setToken(accessToken);
            msgVo.setTouser(openId);
            msgVo.setTemplateId(template.getTemplateId());
            // msgVo.setTemplateId("XMhBqdtLcedJjeTLVfNnO5TPM6HYnw5TaWrhgh4Fz5I");
            msgVo.setData(keyList);
            // String url = siteHostUrl+"/details.html?channelNo="+channelNo;
            String url = "";
            msgVo.setUrl(url);
            NotifyManager.instance.notifyTemplateMsg(msgVo);
        }
        catch(Exception e)
        {
            logger.error(e, "发送核销成功通知模版消息出错");

        }

    }

    @Override
    public void sendMsg4(String channelNo, String openId, String title, String merchantName, Date closeTime)
    {

        try
        {

            TemplateMsgModel template = templateMsgDao.findByType(channelNo, 4);
            if(template==null)
            {
                logger.info("服务到期模版消息不存在");
                return;
            }

            String accessToken = ticketService.getAuthorizerAccessToken(channelNo);

            if(validator.isBlank(accessToken))
            {
                logger.info("发送模版消息，token失效");
                return;
            }

            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", "您参加的活动还有三天过期啦！");
            first.put("color", "#173177");
            keyList.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", merchantName);
            keyword1.put("color", "#173177");
            keyList.put("keyword1", keyword1);

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", title);
            keyword2.put("color", "#173177");
            keyList.put("keyword2", keyword2);

            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", converter.toTimeString(closeTime));
            keyword3.put("color", "#173177");
            keyList.put("keyword3", keyword3);

            JSONObject remark = new JSONObject();
            remark.put("value", "点击查看和使用，以免过期失效。");
            remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo(channelNo);
            msgVo.setToken(accessToken);
            msgVo.setTouser(openId);
            msgVo.setTemplateId(template.getTemplateId());
            msgVo.setData(keyList);
            // 主域名
            String domainUrl = domainService.getDomainUrl(channelNo);
            String url = domainUrl+"/couponlist.html?channelNo="+channelNo;

            msgVo.setUrl(url);
            NotifyManager.instance.notifyTemplateMsg(msgVo);
        }
        catch(Exception e)
        {
            logger.error(e, "发送服务到期通知模版消息出错");
        }

    }

}
