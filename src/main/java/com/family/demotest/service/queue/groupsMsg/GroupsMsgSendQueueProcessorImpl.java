package com.family.demotest.service.queue.groupsMsg;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jeewx.api.core.common.WxstoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.util.BeanFactory;
import com.family.base01.util.Logger;
import com.family.demotest.common.queue.QueueProcessorSupport;
import com.family.demotest.entity.UserModel;
import com.family.demotest.vo.msg.SendMsgVo;
import com.family.demotest.vo.wx.MessageVo;

/**
 * @author wujf
 */
@Service("groups.msg.send.queue-processor")
public class GroupsMsgSendQueueProcessorImpl
    extends QueueProcessorSupport<SendMsgVo, SendMsgVo>
    implements GroupsMsgSendQueueProcessor
{

    /**
     * 发送公众号模板消息
     */
    private static final String MB_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    /**
     * 发送客服消息
     */
    private static final String KF_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

    @Autowired
    private Logger logger;

    @Override
    public String getQueueKey()
    {
        return GroupsMsgQueueService.MSG_SEND_QUEUE_KEY;
    }

    @Override
    protected String getKey(SendMsgVo value)
    {
        // 组成缓存key
        String key = value.getChannelNo()+"-"+"groupsmsg";
        return key;
    }

    @Override
    protected void process(String dataKey, String time, SendMsgVo msg)
    {
        try
        {
            List<UserModel> list = msg.getUserList();
            switch(msg.getMsgType())
            {
            case "1":
                this.sendMsg5(msg, list);
                break;
            case "2":
                this.sendMsg6(msg, list);
                break;
            case "3":
                this.sendMsg7(msg, list);
                break;
            case "4":
                this.sendMsg8(msg, list);
                break;
            case "5":
                this.sendMsg9(msg, list);
                break;
            case "9":
                // 群发图文消息
                this.sendMessage(msg, list);
                break;

            default:
                break;
            }
        }
        catch(Exception e)
        {
            logger.error(e, "发送群发模版消息出错");
        }

    }

    private void sendMsg5(SendMsgVo msg, List<UserModel> list)
    {
        try
        {
            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", "收到一个新的提醒：");
            // first.put("color", "#FE433F");
            keyList.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", msg.getTitle());
            keyword1.put("color", "#FF0000");
            keyList.put("keyword1", keyword1);

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", "所有人");
            // keyword2.put("color", "#173177");
            keyList.put("keyword2", keyword2);

            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", "今天");
            // keyword3.put("color", "#173177");
            keyList.put("keyword3", keyword3);

            JSONObject remark = new JSONObject();
            remark.put("value", "点击了解一下");
            // remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo(msg.getChannelNo());
            msgVo.setToken(msg.getToken());
            msgVo.setTemplateId(msg.getTemplateId());
            msgVo.setData(keyList);
            msgVo.setAppid(msg.getAppid());
            msgVo.setUrl(msg.getLink());

            // 小程序跳转
            if(msg.getAppid()!=null&!"".equals(msg.getAppid()))
            {
                JSONObject miniprogram = new JSONObject();
                miniprogram.put("appid", msg.getAppid());
                miniprogram.put("pagepath", msg.getLink());
                msgVo.setMiniprogram(miniprogram);
            }

            for(UserModel user : list)
            {
                msgVo.setTouser(user.getOpenid());
                // 发送模版消息
                notifyTemplateMsg(msgVo);
            }
        }
        catch(Exception e)
        {
            logger.error(e, "发送活动加入成功通知模版消息出错");

        }

    }

    private void sendMsg6(SendMsgVo msg, List<UserModel> list)
    {
        try
        {
            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", "收到一个新的提醒：");
            // first.put("color", "#FE433F");
            keyList.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", msg.getTitle());
            keyword1.put("color", "#FF0000");
            keyList.put("keyword1", keyword1);

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", "即将满员");
            // keyword2.put("color", "#173177");
            keyList.put("keyword2", keyword2);

            JSONObject remark = new JSONObject();
            remark.put("value", "点击了解一下");
            // remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo(msg.getChannelNo());
            msgVo.setToken(msg.getToken());
            msgVo.setTemplateId(msg.getTemplateId());
            msgVo.setData(keyList);
            msgVo.setAppid(msg.getAppid());
            msgVo.setUrl(msg.getLink());

            // 小程序跳转
            if(msg.getAppid()!=null&!"".equals(msg.getAppid()))
            {
                JSONObject miniprogram = new JSONObject();
                miniprogram.put("appid", msg.getAppid());
                miniprogram.put("pagepath", msg.getLink());
                msgVo.setMiniprogram(miniprogram);
            }

            for(UserModel user : list)
            {
                msgVo.setTouser(user.getOpenid());
                // 发送模版消息
                notifyTemplateMsg(msgVo);
            }

        }
        catch(

        Exception e)
        {
            logger.error(e, "活动成行通知模版消息出错");
        }

    }

    private void sendMsg7(SendMsgVo msg, List<UserModel> list)
    {
        try
        {
            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", "收到一个新的提醒：");
            // first.put("color", "#FE433F");
            keyList.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", msg.getTitle());
            keyword1.put("color", "#FF0000");
            keyList.put("keyword1", keyword1);

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", "通过");
            // keyword2.put("color", "#173177");
            keyList.put("keyword2", keyword2);

            JSONObject remark = new JSONObject();
            remark.put("value", "点击了解一下");
            // remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo(msg.getChannelNo());
            msgVo.setToken(msg.getToken());
            msgVo.setTemplateId(msg.getTemplateId());
            msgVo.setData(keyList);
            msgVo.setAppid(msg.getAppid());
            msgVo.setUrl(msg.getLink());

            // 小程序跳转
            if(msg.getAppid()!=null&!"".equals(msg.getAppid()))
            {
                JSONObject miniprogram = new JSONObject();
                miniprogram.put("appid", msg.getAppid());
                miniprogram.put("pagepath", msg.getLink());
                msgVo.setMiniprogram(miniprogram);
            }

            for(UserModel user : list)
            {
                msgVo.setTouser(user.getOpenid());
                // 发送模版消息
                notifyTemplateMsg(msgVo);
            }

        }
        catch(Exception e)
        {
            logger.error(e, "活动成行通知模版消息出错");
        }

    }

    private void sendMsg8(SendMsgVo msg, List<UserModel> list)
    {
        try
        {
            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", "收到一个新的提醒：");
            // first.put("color", "#FE433F");
            keyList.put("first", first);

            JSONObject keynote1 = new JSONObject();
            keynote1.put("value", msg.getTitle());
            keynote1.put("color", "#FF0000");
            keyList.put("keynote1", keynote1);

            JSONObject keynote2 = new JSONObject();
            keynote2.put("value", "今天");
            // keynote2.put("color", "#173177");
            keyList.put("keynote2", keynote2);

            JSONObject keynote3 = new JSONObject();
            keynote3.put("value", "线上");
            // keyword3.put("color", "#173177");
            keyList.put("keynote3", keynote3);

            JSONObject remark = new JSONObject();
            remark.put("value", "点击了解一下");
            // remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo(msg.getChannelNo());
            msgVo.setToken(msg.getToken());
            msgVo.setTemplateId(msg.getTemplateId());
            msgVo.setData(keyList);
            msgVo.setAppid(msg.getAppid());
            msgVo.setUrl(msg.getLink());

            // 小程序跳转
            if(msg.getAppid()!=null&!"".equals(msg.getAppid()))
            {
                JSONObject miniprogram = new JSONObject();
                miniprogram.put("appid", msg.getAppid());
                miniprogram.put("pagepath", msg.getLink());
                msgVo.setMiniprogram(miniprogram);
            }

            for(UserModel user : list)
            {
                msgVo.setTouser(user.getOpenid());
                // 发送模版消息
                notifyTemplateMsg(msgVo);
            }
        }
        catch(Exception e)
        {
            logger.error(e, "发送报名结果通知模版消息出错");

        }

    }

    private void sendMsg9(SendMsgVo msg, List<UserModel> list)
    {
        try
        {
            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", "收到一个新的提醒：");
            // first.put("color", "#FE433F");
            keyList.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", msg.getTitle());
            keyword1.put("color", "#FF0000");
            keyList.put("keyword1", keyword1);

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", "通过!");
            // keyword2.put("color", "#173177");
            keyList.put("keyword2", keyword2);

            JSONObject remark = new JSONObject();
            remark.put("value", "点击了解一下");
            // remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo(msg.getChannelNo());
            msgVo.setToken(msg.getToken());
            msgVo.setTemplateId(msg.getTemplateId());
            msgVo.setData(keyList);
            msgVo.setAppid(msg.getAppid());
            msgVo.setUrl(msg.getLink());

            // 小程序跳转
            if(msg.getAppid()!=null&!"".equals(msg.getAppid()))
            {
                JSONObject miniprogram = new JSONObject();
                miniprogram.put("appid", msg.getAppid());
                miniprogram.put("pagepath", msg.getLink());
                msgVo.setMiniprogram(miniprogram);
            }

            for(UserModel user : list)
            {
                msgVo.setTouser(user.getOpenid());
                // 发送模版消息
                notifyTemplateMsg(msgVo);
            }
        }
        catch(Exception e)
        {
            logger.error(e, "发送报名结果通知模版消息出错");

        }

    }

    private void sendMessage(SendMsgVo msg, List<UserModel> list)
    {
        try
        {
            JSONObject params = msg.getContent();
            String requestUrl = KF_SEND_URL+msg.getToken();
            for(UserModel user : list)
            {
                params.put("touser", user.getOpenid());
                // 发送模版消息
                net.sf.json.JSONObject res = WxstoreUtils.httpRequest(requestUrl, "POST", params.toJSONString());
                BeanFactory.getBean(Logger.class).info("群发图文消息结果:"+res.toString());
            }
        }
        catch(Exception e)
        {
            logger.error(e, "群发图文消息出错");
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

        // 跳转小程序
        if(msgVo.getMiniprogram()!=null)
        {
            param.put("miniprogram", msgVo.getMiniprogram());
        }

        param.put("data", msgVo.getData());

        // 发送内容
        String content = param.toString();

        // logger.info("****发送模版消息参数："+content);

        String requestUrl = MB_SEND_URL+msgVo.getToken();
        net.sf.json.JSONObject res = WxstoreUtils.httpRequest(requestUrl, "POST", content);

        logger.info("**发送群发模板消息结果:"+res.toString());

    }

    @Override
    protected SendMsgVo process(String time, SendMsgVo data, Map<Object, Object> context, SendMsgVo msg)
    {
        return null;
    }

    @Override
    protected void save()
    {

    }

    @Override
    protected Iterator<SendMsgVo> iterate(Date date)
    {

        return null;
    }

    @Override
    protected void close()
    {

    }

}
