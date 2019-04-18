package com.family.demotest.common.notify;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jeewx.api.core.common.WxstoreUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.base01.util.BeanFactory;
import com.family.base01.util.Logger;
import com.family.demotest.vo.msg.KfBaseMsgVo;
import com.family.demotest.vo.wx.MessageVo;

/**
 * 模板消息发送管理器
 * 
 * @author wujf
 */
public class NotifyManager
{

    /**
     * 发送客服消息
     */
    private static final String KF_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";

    /**
     * 发送公众号模板消息
     */
    private static final String MB_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    /**
     * 日志对象
     */
    // private final static Logger logger =
    // LoggerFactory.getLogger(NotifyManager.class);

    public static final NotifyManager instance = new NotifyManager();

    private NotifyManager()
    {

    }

    private Thread asyncSendThread = null;
    private volatile boolean asyncSendThread_forcedStopped = false;

    private BlockingQueue<SendRecord> sendRecordQueue = new LinkedBlockingQueue<NotifyManager.SendRecord>();

    public void init()
    {
        BeanFactory.getBean(Logger.class).info("初始化消息发送管理类");
        asyncSendThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(!asyncSendThread_forcedStopped)
                {
                    try
                    {
                        SendRecord sendRecord = sendRecordQueue.poll(3L, TimeUnit.SECONDS);
                        if(null!=sendRecord)
                        {
                            realDoSend(sendRecord);
                        }

                    }
                    catch(Exception e)
                    {
                        BeanFactory.getBean(Logger.class).error(e, "***从队列中获取微信消息对象出错");
                    }
                }

            }
        });
        asyncSendThread.start();

    }

    public void clearNotifyQueue()
    {
        sendRecordQueue.clear();
    }

    public void shutdown()
    {
        asyncSendThread_forcedStopped = true;
        asyncSendThread.interrupt();
        sendRecordQueue.clear();
        asyncSendThread = null;
    }

    private static class SendRecord
    {

        private int type; // 0-消息模板,1-客服消息
        private String token; // 微信toke
        private String content;// 发送内容

        public SendRecord(String token, String content, int type)
        {
            this.token = token;
            this.content = content;
            this.type = type;
        }
    }

    /**
     * 放到队列中处理
     * 
     * @param token
     * @param content
     * @return
     */

    /**
     * 放到队列中处理
     * 
     * @param token
     *            微信token
     * @param content
     *            发送内天
     * @param type
     *            发送类型0-模板消息，1-客服消息
     * @return
     */
    private boolean doSend(String token, String content, int type)
    {
        return sendRecordQueue.add(new SendRecord(token, content, type));
    }

    /**
     * 发送到微信去
     * 
     * @param sendRecord
     * @return
     */
    private boolean realDoSend(SendRecord sendRecord)
    {

        boolean isSuc = true;
        if(sendRecord.type==0)
        {
            isSuc = this.realDoSendTemplate(sendRecord);
        }
        else if(sendRecord.type==1)
        {
            isSuc = this.realDoSendKefu(sendRecord);
        }

        return isSuc;
    }

    /**
     * 发送模板消息
     * 
     * @param sendRecord
     * @return
     */
    private boolean realDoSendTemplate(SendRecord sendRecord)
    {

        boolean isSuc = true;

        try
        {
            // 等待3秒在发送
            Thread.sleep(3*1000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }

        // BeanFactory.getBean(Logger.class).info("****发送模版消息参数："+sendRecord.content);

        String requestUrl = MB_SEND_URL+sendRecord.token;

        net.sf.json.JSONObject res = WxstoreUtils.httpRequest(requestUrl, "POST", sendRecord.content);

        BeanFactory.getBean(Logger.class).info("发送模板消息返回结果"+res.toString());
        // logger.info("发送模板消息结果"+res.toString());
        if(res.getInt("errcode")!=0)
        {
            isSuc = false;
        }

        return isSuc;
    }

    /**
     * 发送客服消息
     * 
     * @param sendRecord
     * @return
     */
    private boolean realDoSendKefu(SendRecord sendRecord)
    {

        boolean isSuc = true;

        String requestUrl = KF_SEND_URL+sendRecord.token;

        net.sf.json.JSONObject res = WxstoreUtils.httpRequest(requestUrl, "POST", sendRecord.content);

        BeanFactory.getBean(Logger.class).info("发送客服消息结果"+res.toString());
        // logger.info("发送客服消息结果"+res.toString());
        if(res.getInt("errcode")!=0)
        {
            isSuc = false;
        }

        return isSuc;

    }

    /**
     * 发送微信客服消息提醒
     *
     * @param token
     * @param msgVo
     * @return
     */
    public boolean notifyKefuMsg(KfBaseMsgVo msgVo)
    {
        // 发送内容
        String content = JSON.toJSONString(msgVo);
        return doSend(msgVo.getToken(), content, 1);
    }

    /**
     * 发送微信客服消息图文
     *
     * @param token
     * @param msgVo
     * @return
     */
    public boolean notifyKefuMsgPic(String token, String content)
    {
        // 发送内容
        return doSend(token, content, 1);
    }

    /**
     * 发送公众号模板消息
     *
     * @param msgVo
     * @return
     */
    public boolean notifyTemplateMsg(MessageVo msgVo)
    {
        JSONObject param = new JSONObject();
        param.put("touser", msgVo.getTouser());
        param.put("template_id", msgVo.getTemplateId());

        if(null!=msgVo.getUrl()&&!"".equals(msgVo.getUrl()))
        {
            param.put("url", msgVo.getUrl());
        }

        if(msgVo.getMiniprogram()!=null)
        {
            param.put("miniprogram", msgVo.getMiniprogram());
        }

        param.put("data", msgVo.getData());

        // 发送内容
        String content = param.toString();
        // BeanFactory.getBean(Logger.class).info("****发送模版消息参数："+content);
        return doSend(msgVo.getToken(), content, 0);
    }

}
