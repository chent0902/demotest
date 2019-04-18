package com.family.demotest.service.timed;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.family.base01.util.Converter;
import com.family.base01.util.Logger;
import com.family.demotest.common.constant.Constants;
import com.family.demotest.common.nosql.redis.RedisLock;
import com.family.demotest.service.lottery.LotteryOrderService;

/**
 * 
 * @author wujf
 *
 */
@Service("timed.task.service")
public class TimedTaskServiceImpl
    implements
    TimedTaskService
{

    @Autowired
    private Converter converter;
    @Autowired
    private Logger logger;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private LotteryOrderService lotteryOrderService;

    @Value("${demotest.online.value}")
    private String onlineValue; // 正式环境 -online，测试环境 -test

    @Scheduled(cron = "${demotest.lottery.signin.expire.scheduler.date.cron}")
    @Override
    public void executeExpireTask()
    {

        if("online".equals(onlineValue))
        {

            if(logger.isInfoEnable())
            {
                logger.info("开始处理活动过期提醒。"+converter.toTimeString(new Date()));
            }
            long start = System.currentTimeMillis();

            RedisLock lock = new RedisLock(redisTemplate, Constants.LOCK_LOTTERY_EXPIRE_KEY, 1*1000, 55*60*1000);// 55分钟
            try
            {
                if(lock.lock())
                {
                    // 需要加锁的代码
                    lotteryOrderService.startExpireMsgTask();
                }

            }
            catch(InterruptedException e)
            {
                logger.error(e, "处理活动快过期提醒出错");
            }
            finally
            {
                // lock.unlock();
            }

            long end = System.currentTimeMillis();
            if(logger.isInfoEnable())
            {
                logger.info("处理活动快过期提醒完成。总消耗时间："+(end-start)/1000+"秒");
            }
        }

    }

}
