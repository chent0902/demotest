package com.family.demotest.service.lottery;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.LotteryModel;
import com.family.demotest.vo.total.UserLotteryTotalVo;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
public interface LotteryService
    extends
    SaveService<LotteryModel>,
    DeleteService<LotteryModel>,
    QueryService<LotteryModel>
{

    /**
     * 活动列表
     * 
     * @param channelNo
     * @param type
     * @param name
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode pageList(String channelNo, int type, String name, int pageSize, int page);

    /**
     * 保存活动信息
     * 
     * @param model
     * @return
     */
    public ResultCode saveOrUpdate(LotteryModel model);

    /**
     * 设置用户抽奖统计
     * 
     * @param lotteryId
     * @param userId
     * @param totalVo
     */
    public void setUserLotteryTotalVo(String lotteryId, String userId, UserLotteryTotalVo totalVo);

    /**
     * 获取用户抽奖统计
     * 
     * @param lotteryId
     * @param userId
     * @return
     */
    public UserLotteryTotalVo getUserLotteryTotalVo(String lotteryId, String userId);

    /**
     * 前端获取活动列表
     * 
     * @param channelNo
     * @param type
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode list(String channelNo, int type, int page, int pageSize);

    /**
     * 活动详情
     * 
     * @param userId
     * 
     * @param channelNo
     * @return
     */
    public ResultCode getInfo(String lotteryId, String userId);

    /**
     * 抽奖
     * 
     * @param lotteryId
     * @param userId
     * @return
     */
    public ResultCode lottery(String lotteryId, String userId);

    /**
     * 获取同步锁
     * 
     * @param lotteryId
     * @return
     */
    public String getRedisLockKey(String lotteryId);

    /**
     * 奖项库存缓存key
     * 
     * @param awardsId
     * @return
     */
    public String getLotteryAwardsKey(String awardsId);

    /**
     * 商户活动列表
     * 
     * @param channelNo
     * @param merchantId
     * @param status
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode merchantList(String channelNo, String merchantId, int status, int page, int pageSize);

    /**
     * 商户活动名称列表
     * 
     * @param channelNo
     * @param merchantId
     * @return
     */
    public ResultCode nameList(String channelNo, String merchantId);

    /**
     * 后台首页活动概况
     * 
     * @param channelNo
     * @return
     */
    public JSONObject getLotteryJson(String channelNo);

    /**
     * 生成海报 前端画
     * 
     * @param channelNo
     * @param id
     * @param lotteryId
     * @return
     */
    public ResultCode Haibao(String channelNo, String id, String lotteryId);

    /**
     * 海报 后台画
     * 
     * @param channelNo
     * @param id
     * @param lotteryId
     * @return
     */
    public ResultCode newHaibao(String channelNo, String userId, String lotteryId);

    /**
     * 总后台数据统计
     * 
     * @return
     */
    public JSONObject homeData();

    /**
     * 获取快过期的活动
     * 
     * @return
     */
    public List<LotteryModel> getExpireList();

    /**
     * 获取图文详情
     * 
     * @param lotteryId
     * @return
     */
    public ResultCode getDetails(String lotteryId);

    /**
     * 统计代理当前活动数
     * 
     * @param channelNo
     * @return
     */
    public int countLotteryNum(String channelNo);

    /**
     * 删除活动
     * 
     * @param id
     * @return
     */
    public ResultCode delete(String id);

    /**
     * 所有活动列表
     * 
     * @param type
     * @param name
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode allList(int type, String name, int pageSize, int page);

    /**
     * 通过商户ID获取活动列表
     * 
     * @param merchantId
     * @return
     */
    public List<LotteryModel> getLotteryList(String merchantId);

}
