package com.family.demotest.service.queue.templateMsg;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.util.Https;
import com.family.base01.util.Logger;
import com.family.demotest.common.queue.QueueProcessorSupport;
import com.family.demotest.service.wxopen.TicketService;
import com.family.demotest.vo.queue.TemplateMsgQueueVo;
import com.family.demotest.vo.wx.MessageVo;

/**
 * @author wujf
 */
@Service("template.msg.queue-processor")
public class TemplateMsgSendQueueProcessorImpl
    extends
    QueueProcessorSupport<TemplateMsgQueueVo, TemplateMsgQueueVo>
    implements
    TemplateMsgSendQueueProcessor
{

    /**
     * 发送公众号模板消息
     */
    private static final String MB_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Autowired
    private Logger logger;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private Https https;

    @Override
    public String getQueueKey()
    {
        return TemplateMsgQueueService.MSG_SEND_QUEUE_KEY;
    }

    @Override
    protected String getKey(TemplateMsgQueueVo value)
    {
        // 组成缓存key
        String key = value.getChannelNo()+"-"+"templatemsg";
        return key;
    }

    @Override
    protected void process(String dataKey, String time, TemplateMsgQueueVo msg)
    {

        try
        {
            String accessToken = ticketService.getAuthorizerAccessToken(msg.getChannelNo());
            if(null==accessToken||"".equals(accessToken))
            {
                logger.info("###发送模版消息，token失效");
                return;
            }

            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", msg.getFirst());
            first.put("color", "#173177");
            keyList.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", msg.getKeyword1());
            keyword1.put("color", "#173177");
            keyList.put("keyword1", keyword1);

            if(msg.getKeyword2()!=null)
            {
                JSONObject keyword2 = new JSONObject();
                keyword2.put("value", msg.getKeyword2());
                keyword2.put("color", "#173177");
                keyList.put("keyword2", keyword2);
            }

            if(msg.getKeyword3()!=null)
            {
                JSONObject keyword3 = new JSONObject();
                keyword3.put("value", msg.getKeyword3());
                keyword3.put("color", "#173177");
                keyList.put("keyword3", keyword3);
            }

            if(msg.getKeyword4()!=null)
            {
                JSONObject keyword4 = new JSONObject();
                keyword4.put("value", msg.getKeyword4());
                keyword4.put("color", "#173177");
                keyList.put("keyword4", keyword4);
            }

            JSONObject remark = new JSONObject();
            remark.put("value", msg.getRemark());
            remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo(msg.getChannelNo());
            msgVo.setToken(accessToken);
            msgVo.setTouser(msg.getOpenId());
            msgVo.setTemplateId(msg.getTemplateId());
            msgVo.setData(keyList);
            msgVo.setUrl(msg.getUrl());

            this.notifyTemplateMsg(msgVo);
        }
        catch(Exception e)
        {
            logger.error(e, "发送模版消息"+msg.getMsgType()+"出错");

        }

    }

    /**
     * 发送公众号模板消息
     *
     * @param msgVo
     * @return
     */
    public void notifyTemplateMsg(MessageVo msgVo)
    {
        JSONObject param = new JSONObject();
        param.put("touser", msgVo.getTouser());
        param.put("template_id", msgVo.getTemplateId());

        if(null!=msgVo.getUrl()&&!"".equals(msgVo.getUrl()))
        {
            param.put("url", msgVo.getUrl());
        }

        param.put("data", msgVo.getData());

        // 发送内容
        String content = param.toString();
        // logger.info("****发送模版消息参数："+content);
        String requestUrl = MB_SEND_URL+msgVo.getToken();

        String resStr = https.post(requestUrl, content);
        // net.sf.json.JSONObject res = WxstoreUtils.httpRequest(requestUrl,
        // "POST", content);
        logger.info("发送模板消息结果:"+resStr);

    }

    @Override
    protected TemplateMsgQueueVo process(String time, TemplateMsgQueueVo data, Map<Object, Object> context, TemplateMsgQueueVo msg)
    {
        return null;
    }

    @Override
    protected void save()
    {

    }

    @Override
    protected Iterator<TemplateMsgQueueVo> iterate(Date date)
    {

        return null;
    }

    @Override
    protected void close()
    {

    }

}
