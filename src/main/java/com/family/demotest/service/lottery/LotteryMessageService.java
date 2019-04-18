package com.family.demotest.service.lottery;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.LotteryMessageModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
public interface LotteryMessageService
    extends SaveService<LotteryMessageModel>, DeleteService<LotteryMessageModel>, QueryService<LotteryMessageModel>
{

    /**
     * 留言列表
     * 
     * @param channelNo
     * @param lotteryId
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode pageList(String channelNo, String lotteryId, int pageSize, int page);

    /**
     * 添加留言
     * 
     * @param channelNo
     * @param lotteryId
     * @param content
     * @return
     */
    public ResultCode saveOrUpdate(String channelNo, String lotteryId, String content);

    /**
     * 我的留言
     * 
     * @param channelNo
     * @param lotteryId
     * @param userId
     * @return
     */
    public ResultCode myMessage(String channelNo, String lotteryId, String userId);

    /**
     * 留言
     * 
     * @param channelNo
     * @param lotteryId
     * @param userId
     * @param content
     * @return
     */
    public ResultCode save(String channelNo, String lotteryId, String userId, String content);

    /**
     * 抽奖留言信息
     * 
     * @param lotteryId
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode lotteryMessage(String lotteryId, int page, int pageSize);

    /**
     * 留言回复
     * 
     * @param messageId
     * @param reply
     * @return
     */
    public ResultCode reply(String messageId, String reply);

}
