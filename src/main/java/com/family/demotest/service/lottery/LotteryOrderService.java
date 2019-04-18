package com.family.demotest.service.lottery;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.LotteryOrderModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
public interface LotteryOrderService
    extends SaveService<LotteryOrderModel>, DeleteService<LotteryOrderModel>, QueryService<LotteryOrderModel>
{

    /**
     * 
     * 用户优惠券列表
     * 
     * @param channelNo
     * @param userId
     * @param type
     * @param page
     * @param pageSize
     * @return
     */
    public ResultCode userList(String channelNo, String userId, int type, int page, int pageSize);

    /**
     * 密码核销
     * 
     * @param orderId
     * @param pwd
     * @param user
     * @return
     */
    public ResultCode pwdCheck(String orderId, String pwd, String userId, String nickName);

    /**
     * 根据核销码查订单
     * 
     * @param merchantId
     * @param usecode
     * @return
     */
    public ResultCode findByUsecode(String merchantId, String usecode);

    /**
     * 核销
     * 
     * @param orderId
     * @param user
     * @return
     */
    public ResultCode check(String orderId, String userId, String userName);

    /**
     * 核销列表
     * 
     * @param merchantId
     * @param lotteryId
     * @param startTime
     * @param endTime
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode checkList(String merchantId, String lotteryId, String startTime, String endTime, int page, int pageSize);

    /**
     * 统计用户未使用优惠券数量
     * 
     * @param channelNo
     * @param id
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
    public ResultCode adminUserList(String channelNo, String userId, int page, int pageSize);

    /**
     * 代理后台列表
     * 
     * @param channelNo
     * @param lotteryId
     * @param search
     * @param status
     * @param type
     * @param page
     * @param pageSize
     * @return
     */
    public ResultCode list(String channelNo, String lotteryId, String search, int status, int type, int page, int pageSize);

    /**
     * 后台首页领取Json
     * 
     * @param channelNo
     * @return
     */
    public JSONObject getReceiveJson(String channelNo);

    /**
     * 提前3天发送活动过期提醒
     */
    public void startExpireMsgTask();

    /**
     * 获取该活动抽奖最新10条
     * 
     * @param channelNo
     * @param lotteryId
     * @return
     */
    public List<LotteryOrderModel> getNewList(String channelNo, String lotteryId);

    /**
     * 删除优惠 券
     * 
     * @param id
     * @return
     */
    public ResultCode delete(String id);

    /**
     * 导出领取/核销列表
     * 
     * @param channelNo
     * @param lotteryId
     * @param search
     * @param status
     * @param type
     * @return
     */
    public Workbook newExport(String channelNo, String lotteryId, String search, String status, String type);

}
