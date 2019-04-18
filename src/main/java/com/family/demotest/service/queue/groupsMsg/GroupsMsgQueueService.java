package com.family.demotest.service.queue.groupsMsg;

import com.family.demotest.vo.msg.SendMsgVo;

/**
 * @author wujf
 */
public interface GroupsMsgQueueService
{
    /**
     * 消息队列引用key。
     */
    public static final String MSG_SEND_QUEUE_KEY = "groups.msg.send.queue-key";

    /**
     * 汇总订单实时数据
     * 
     * @param order
     */
    public void addQueue(SendMsgVo msg);

    /**
     * 关闭数据库
     */
    public void close();

}
