package com.family.demotest.service.lottery;

import java.util.List;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.LotteryAwardsModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
public interface LotteryAwardsService
    extends SaveService<LotteryAwardsModel>, DeleteService<LotteryAwardsModel>, QueryService<LotteryAwardsModel>
{

    /**
     * 活动奖项list
     * 
     * @param channelNo
     * @param lotteryId
     * @return
     */
    public ResultCode list(String channelNo, String lotteryId);

    /**
     * 保存活动奖项
     * 
     * @param list
     * @return
     */
    public ResultCode saveOrUpdate(List<LotteryAwardsModel> list);

    /**
     * 获取活动最低价(特等奖奖项对象)
     * 
     * @param channelNo
     * @param id
     * @return
     */
    public LotteryAwardsModel getlowestPrice(String channelNo, String lotteryId);

    /**
     * 获取活动奖项list
     * 
     * @param lotteryId
     * @param lotteryId
     * @return
     */
    public List<LotteryAwardsModel> getList(String channelNo, String lotteryId);

    /**
     * 根据活动和奖项级别获取奖项
     * 
     * @param lotteryId
     * @param answer
     * @return
     */
    public LotteryAwardsModel findByGrade(String lotteryId, int answer);

    /**
     * 奖项库存缓存key
     * 
     * @param awardsId
     * @return
     */
    public String getLotteryAwardsKey(String awardsId);

}
