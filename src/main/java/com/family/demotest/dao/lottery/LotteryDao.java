package com.family.demotest.dao.lottery;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.LotteryModel;

/**
 * @author lm
 */
public interface LotteryDao
    extends
    QueryDao<LotteryModel>,
    SaveDao<LotteryModel>,
    DeleteDao<LotteryModel>
{

    /**
     * 活动列表
     * 
     * @param string
     * @param array
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<LotteryModel> pageList(String where, Object[] args, int page, int pageSize);

    /**
     * 商户活动名称列表
     * 
     * @param channelNo
     * @param merchantId
     * @return
     */
    public List<LotteryModel> nameList(String channelNo, String merchantId);

    /**
     * 代理首页统计
     * 
     * @param create
     * @param objects
     * @return
     */
    public int count(String where, Object[] args);

    /**
     * 获取快过期的活动
     * 
     * @return
     */
    public List<LotteryModel> getExpireList();

    /**
     * 统计代理当前活动数
     * 
     * @param channelNo
     * @return
     */
    public int countLotteryNum(String channelNo);

    /**
     * 通过商户id获取列表
     * 
     * @param merchantId
     * @return
     */
    public List<LotteryModel> getLotteryList(String merchantId);

}
