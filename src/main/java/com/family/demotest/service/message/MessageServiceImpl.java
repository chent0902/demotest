package com.family.demotest.service.message;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Converter;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.StringRedisBiz;
import com.family.demotest.dao.message.MessageDao;
import com.family.demotest.entity.MessageModel;
import com.family.demotest.entity.TemplateMsgModel;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.queue.groupsMsg.GroupsMsgQueueService;
import com.family.demotest.service.template.TemplateMsgService;
import com.family.demotest.service.user.UserService;
import com.family.demotest.service.wxopen.TicketService;
import com.family.demotest.vo.msg.SendMsgVo;
import com.family.demotest.web.util.ResultCode;

@Service("message.service")
public class MessageServiceImpl
    extends ServiceSupport<MessageModel>
    implements MessageService
{

    @Autowired
    private TemplateMsgService templateMsgService;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private Logger logger;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private Converter converter;
    @Autowired
    private StringRedisBiz stringRedisBiz;
    @Autowired
    private GroupsMsgQueueService groupsMsgQueueService;
    @Autowired
    private Validator validator;

    @Override
    protected QueryDao<MessageModel> getQueryDao()
    {
        return messageDao;
    }

    @Override
    protected SaveDao<MessageModel> getSaveDao()
    {
        return messageDao;
    }

    @Override
    protected DeleteDao<MessageModel> getDeleteDao()
    {
        return messageDao;
    }

    @Override
    public ResultCode sendPicTextMsg(String channelNo, String articles)
    {
        ResultCode result = new ResultCode();
        try
        {
            List<UserModel> userList = userService.getList(channelNo);

            String accessToken = ticketService.getAuthorizerAccessToken(channelNo);
            // 通过公众号关注 回复客服消息
            JSONObject params = new JSONObject();
            params.put("msgtype", "news");
            JSONObject news = new JSONObject();
            JSONArray pms = JSON.parseArray(articles);
            news.put("articles", pms);
            params.put("news", news);
            // 加入队列
            SendMsgVo msgVo = new SendMsgVo();
            msgVo.setChannelNo(channelNo);
            msgVo.setContent(params);
            msgVo.setMsgType("9");
            msgVo.setToken(accessToken);
            msgVo.setUserList(userList);
            groupsMsgQueueService.addQueue(msgVo);

            // 设置当天已发送(图文消息)
            this.setToDaySendPicText(channelNo);
            // 群发记录
            MessageModel message = new MessageModel();
            message.setChannelNo(channelNo);
            message.setType(0);
            message.setNews(articles);
            message.setSendNum(userList.size());
            message.setCreateTime(new Date());
            this.save(message);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("发送群发模版消息出错");
            logger.error(e, "发送群发模版消息出错");
        }

        return result;
    }

    @Override
    public ResultCode groupsSendMsg(String channelNo, String msgType, String title, String link, String appid)
    {

        ResultCode result = new ResultCode();
        int type = -1;
        switch(msgType)
        {
        case "1":
            type = 5;
            break;
        case "2":
            type = 6;
            break;
        case "3":
            type = 7;
            break;
        case "4":
            type = 8;
            break;
        case "5":
            type = 9;
            break;
        default:
            break;
        }
        List<UserModel> userList = userService.getList(channelNo);

        TemplateMsgModel template = templateMsgService.findByType(channelNo, type);
        if(template==null)
        {
            result.setCode(-1);
            result.setInfo("模版不存在，请先配置模版消息");
            return result;
        }

        String accessToken = ticketService.getAuthorizerAccessToken(channelNo);

        if(validator.isBlank(accessToken))
        {
            result.setCode(-1);
            result.setInfo("发送模版消息，token失效, 请重新授权");
            return result;
        }

        SendMsgVo msgVo = new SendMsgVo();
        msgVo.setChannelNo(channelNo);
        msgVo.setTitle(title);
        msgVo.setLink(link);
        msgVo.setAppid(appid);
        msgVo.setMsgType(msgType);
        msgVo.setTemplateId(template.getTemplateId());
        msgVo.setToken(accessToken);
        msgVo.setUserList(userList);
        groupsMsgQueueService.addQueue(msgVo);
        // 设置当天已发送
        this.setToDaySend(channelNo);
        // 群发记录
        MessageModel message = new MessageModel();
        message.setChannelNo(channelNo);
        message.setType(1);
        message.setNews(title);
        message.setSendNum(userList.size());
        message.setCreateTime(new Date());
        this.save(message);
        return result;

    }

    @Override
    public boolean isSend(String channelNo)
    {
        String time = converter.toString(new Date(), "yyyyMMdd");
        String key = this.getTimeKey(channelNo, time);
        String timeValue = stringRedisBiz.get(key);
        if(timeValue==null)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean isSendPicText(String channelNo)
    {
        String time = converter.toString(new Date(), "yyyyMMdd");
        String key = this.getPicTimeKey(channelNo, time);
        String timeValue = stringRedisBiz.get(key);
        if(timeValue==null)
        {
            return false;
        }
        return true;
    }

    /**
     * 设置当天已发送
     * 
     * @param channelNo
     */
    private void setToDaySend(String channelNo)
    {
        String time = converter.toString(new Date(), "yyyyMMdd");
        String key = this.getTimeKey(channelNo, time);
        stringRedisBiz.put(key, time, 1, TimeUnit.DAYS);
    }

    /**
     * 设置当天已发送(图文消息)
     * 
     * @param channelNo
     */
    private void setToDaySendPicText(String channelNo)
    {
        String time = converter.toString(new Date(), "yyyyMMdd");
        String key = this.getPicTimeKey(channelNo, time);
        stringRedisBiz.put(key, time, 1, TimeUnit.DAYS);
    }

    /**
     * 
     * @param channelNo
     * @return
     */
    private String getTimeKey(String channelNo, String time)
    {
        return new StringBuilder().append("groups.template.key").append(channelNo).append("-").append(time).toString();
    }

    /**
     * 
     * @param channelNo
     * @return
     */
    private String getPicTimeKey(String channelNo, String time)
    {
        return new StringBuilder().append("groups.pictext.key").append(channelNo).append("-").append(time).toString();
    }

    @Override
    public ResultCode messageRecord(String channelNo, int page, int pageSize)
    {
        ResultCode result = new ResultCode();
        try
        {
            PageList<MessageModel> pageList = messageDao.messageRecord(channelNo, page, pageSize);
            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取图文群发记录出错");
            logger.error(e, "获取图文群发记录出错");
        }
        return result;
    }

}
