package com.family.demotest.dao.lottery;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.LotteryOrderModel;

/**
 * @author lm
 */
public interface LotteryOrderDao
    extends QueryDao<LotteryOrderModel>, SaveDao<LotteryOrderModel>, DeleteDao<LotteryOrderModel>
{

    /**
     * 用户优惠券列表
     * 
     * @param count
     * @param string
     * @param array
     * @param page
     * @param pageSize
     * @return
     */
    public List<LotteryOrderModel> userList(String where, Object[] args);

    /**
     * 根据核销码查订单
     * 
     * @param merchantId
     * @param usecode
     * @return
     */
    public LotteryOrderModel findByUsecode(String merchantId, String usecode);

    /**
     * 核销列表
     * 
     * @param where
     * @param objects
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<LotteryOrderModel> checkList(String where, Object[] args, int page, int pageSize);

    /**
     * 统计用户未使用优惠券数量
     * 
     * @param channelNo
     * @param userId
     * @return
     */
    public int waitUse(String channelNo, String userId);

    /**
     * 后台用户优惠券列表
     * 
     * @param channelNo
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<LotteryOrderModel> adminUserList(String channelNo, String userId, int page, int pageSize);

    /**
     * 后台使用/核销列表
     * 
     * @param count
     * 
     * @param string
     * @param array
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<LotteryOrderModel> list(StringBuffer sql, StringBuffer count, List<Object> args, int page, int pageSize);

    /**
     * 统计
     * 
     * @param where
     * @param objects
     * @return
     */
    public int count(String where, Object[] args);

    /**
     * 通过商户编号和活动id获取列表
     * 
     * @param channelNo
     * @param lotteryId
     * @return
     */
    public List<LotteryOrderModel> getListByLotteryId(String channelNo, String lotteryId);

    /**
     * 获取该活动最新10条中奖记录
     * 
     * @param channelNo
     * @param lotteryId
     * @return
     */
    public List<LotteryOrderModel> getNewList(String channelNo, String lotteryId);

}
