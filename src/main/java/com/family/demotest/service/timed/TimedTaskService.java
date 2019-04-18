package com.family.demotest.service.timed;

/**
 * 
 * @author wujf
 *
 */
public interface TimedTaskService
{

    /**
     * 定时处理快过期活动提醒（提前3天）
     */
    public void executeExpireTask();

}
