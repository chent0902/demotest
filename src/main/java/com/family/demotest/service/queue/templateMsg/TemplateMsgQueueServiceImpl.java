package com.family.demotest.service.queue.templateMsg;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.util.Converter;
import com.family.base01.util.Logger;
import com.family.base01.util.Queue;
import com.family.demotest.entity.TemplateMsgModel;
import com.family.demotest.service.domain.DomainService;
import com.family.demotest.service.template.TemplateMsgService;
import com.family.demotest.vo.queue.TemplateMsgQueueVo;

/**
 * @author wujf
 */
@Service("template.msg.queue.service")
public class TemplateMsgQueueServiceImpl
    implements
    TemplateMsgQueueService
{

    @Autowired
    protected Queue timeQueue;

    @Autowired
    private TemplateMsgService templateMsgService;
    @Autowired
    private DomainService domainService;
    @Autowired
    private Logger logger;
    @Autowired
    private Converter converter;

    @Override
    public void addQueue(TemplateMsgQueueVo model)
    {
        timeQueue.push(MSG_SEND_QUEUE_KEY, model);
    }

    @Override
    public void close()
    {

    }

    @Override
    public void sendMsg1(String channelNo, String templateId, String openId, String nickName, String lotteryId, int templateType)
    {
        TemplateMsgModel template = templateMsgService.findByType(channelNo, 1);
        if(template==null)
        {
            logger.info("抽奖结果模版消息不存在");
            return;
        }

        TemplateMsgQueueVo msg = new TemplateMsgQueueVo();
        msg.setChannelNo(channelNo);
        msg.setOpenId(openId);
        msg.setTemplateId(template.getTemplateId());
        msg.setMsgType(2);
        msg.setFirst("恭喜您：您的好友("+nickName+")帮您增加1次抽奖机会");
        msg.setKeyword1("1次抽奖机会");
        msg.setKeyword2(converter.toTimeString(new Date()));
        msg.setRemark("点击查看，继续抽奖");

        // 主域名
        String domainUrl = domainService.getDomainUrl(channelNo);
        String url = "";
        if(templateType==0)
        {
            url = domainUrl+"/details.html?channelNo="+channelNo+"&id="+lotteryId;
        }
        else if(templateType==1)
        {
            url = domainUrl+"/details1.html?channelNo="+channelNo+"&id="+lotteryId;
        }
        else if(templateType==2)
        {
            url = domainUrl+"/details2.html?channelNo="+channelNo+"&id="+lotteryId;
        }
        msg.setUrl(url);
        this.addQueue(msg);

    }

    @Override
    public void sendMsg2(String channelNo, String openId, String prizeName, String prize)
    {

        TemplateMsgModel template = templateMsgService.findByType(channelNo, 2);
        if(template==null)
        {
            logger.info("抽奖结果模版消息不存在");
            return;
        }

        TemplateMsgQueueVo msg = new TemplateMsgQueueVo();
        msg.setChannelNo(channelNo);
        msg.setOpenId(openId);
        msg.setTemplateId(template.getTemplateId());
        msg.setMsgType(2);
        msg.setFirst("恭喜您抽中"+prize);
        msg.setKeyword1(prizeName);
        msg.setKeyword2(converter.toTimeString(new Date()));
        msg.setRemark("点击即可查看和使用");
        // 主域名
        String domainUrl = domainService.getDomainUrl(channelNo);
        String url = domainUrl+"/couponlist.html?channelNo="+channelNo;
        msg.setUrl(url);
        this.addQueue(msg);

    }

    @Override
    public void sendMsg3(String channelNo, String openId, String name, String merchantName)
    {
        TemplateMsgModel template = templateMsgService.findByType(channelNo, 3);
        if(template==null)
        {
            logger.info("抽奖结果模版消息不存在");
            return;
        }

        TemplateMsgQueueVo msg = new TemplateMsgQueueVo();
        msg.setChannelNo(channelNo);
        msg.setOpenId(openId);
        msg.setTemplateId(template.getTemplateId());
        msg.setMsgType(3);
        msg.setFirst("恭喜您：成功核销兑换券一张！");
        msg.setKeyword1(name);
        msg.setKeyword2(converter.toTimeString(new Date()));
        msg.setKeyword3(merchantName);
        msg.setRemark("感谢您的再次使用！");

        // 主域名
        // String domainUrl = domainService.getDomainUrl(channelNo);
        // String url = domainUrl+"/details.html?channelNo="+channelNo;
        String url = "";
        msg.setUrl(url);
        this.addQueue(msg);

    }

    @Override
    public void sendMsg4(String channelNo, String openId, String title, String merchantName, Date closeTime)
    {
        TemplateMsgModel template = templateMsgService.findByType(channelNo, 4);
        if(template==null)
        {
            logger.info("服务到期模版消息不存在");
            return;
        }

        TemplateMsgQueueVo msg = new TemplateMsgQueueVo();
        msg.setChannelNo(channelNo);
        msg.setOpenId(openId);
        msg.setTemplateId(template.getTemplateId());
        msg.setMsgType(4);
        msg.setFirst("您参加的活动还有三天过期啦！");
        msg.setKeyword1(merchantName);
        msg.setKeyword2(title);
        msg.setKeyword3(converter.toTimeString(closeTime));
        msg.setRemark("点击查看和使用，以免过期失效。");
        // 主域名
        String domainUrl = domainService.getDomainUrl(channelNo);
        String url = domainUrl+"/couponlist.html?channelNo="+channelNo;
        msg.setUrl(url);
        // 添加到队列
        this.addQueue(msg);

    }

}
