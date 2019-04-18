package com.family.demotest.service.queue.templateMsg;

import java.util.Date;

import com.family.demotest.vo.queue.TemplateMsgQueueVo;

/**
 * @author wujf
 */
public interface TemplateMsgQueueService
{
    /**
     * 消息队列引用key。
     */
    public static final String MSG_SEND_QUEUE_KEY = "template.msg.send.queue-key";

    /**
     * 添加到队列对象
     * 
     * @param msgVo
     */
    public void addQueue(TemplateMsgQueueVo msgVo);

    /**
     * 助力成功通知
     * 
     * @param channelNo
     * @param templateId
     * @param openId
     * @param nickName
     * @param lotteryId
     * @param templateType
     *            抽奖样式 0-转盘 1-九宫格 2-翻牌子
     */
    public void sendMsg1(String channelNo, String templateId, String openId, String nickName, String lotteryId, int templateType);

    /**
     * 发送抽奖结果通知
     * 
     * @param channelNo
     * @param openId
     * @param prizeName
     * @param prize
     */
    public void sendMsg2(String channelNo, String openId, String prizeName, String prize);

    /**
     * 发送核销成功通知
     * 
     * @param channelNo
     * @param openId
     * @param name
     * @param merchantName
     */
    public void sendMsg3(String channelNo, String openId, String name, String merchantName);

    /**
     * 发送服务到期通知
     * 
     * @param channelNo
     * @param openId
     * @param title
     * @param merchantName
     * @param closeTime
     */
    public void sendMsg4(String channelNo, String openId, String title, String merchantName, Date closeTime);

    /**
     * 关闭数据库
     */
    public void close();

}
