package com.family.demotest.service.queue.groupsMsg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.util.Queue;
import com.family.demotest.vo.msg.SendMsgVo;

/**
 * @author wujf
 */
@Service("groups.msg.queue.service")
public class GroupsMsgQueueServiceImpl
    implements
    GroupsMsgQueueService
{

    @Autowired
    protected Queue timeQueue;

    @Override
    public void addQueue(SendMsgVo model)
    {
        timeQueue.push(MSG_SEND_QUEUE_KEY, model);
    }

    @Override
    public void close()
    {

    }

}
