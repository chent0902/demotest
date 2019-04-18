package com.family.demotest.service.template;

import java.util.Date;
import java.util.List;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.TemplateMsgModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author wujf
 *
 */
public interface TemplateMsgService
    extends
    SaveService<TemplateMsgModel>,
    DeleteService<TemplateMsgModel>,
    QueryService<TemplateMsgModel>
{

    /**
     * 通过商家编号获取模块消息列表
     * 
     * @param channelNo
     * @return
     */
    public List<TemplateMsgModel> getList(String channelNo);

    /**
     * 通过类型查询
     * 
     * @param channelNo
     * @param type
     *            消息类型
     * @return
     */
    public TemplateMsgModel findByType(String channelNo, int type);

    /**
     * 添加模版消息
     * 
     * @param channelNo
     * @param parseInt
     * @return
     */
    public ResultCode addTemplate(String channelNo, int parseInt);

    /**
     * 删除模版消息
     * 
     * @param id
     * @return
     */
    public void delete(String id);

    /**
     * 发送到账通知
     * 
     * @param channelNo
     * @param templateId
     * @param openId
     * @param nickName
     */
    public void sendMsg1(String channelNo, String templateId, String openId, String nickName, String lotteryId);

    /**
     * 发送抽奖结果通知
     * 
     * @param channelNo
     * @param openId
     * @param prizeName
     * @param prize
     */
    public void sendMsg2(String channelNo, String openId, String prizeName, String prize);

    /**
     * 发送核销成功通知
     * 
     * @param channelNo
     * @param openId
     * @param name
     * @param merchantName
     */
    public void sendMsg3(String channelNo, String openId, String name, String merchantName);

    /**
     * 发送服务到期通知
     * 
     * @param channelNo
     * @param openId
     * @param title
     * @param merchantName
     * @param closeTime
     */
    public void sendMsg4(String channelNo, String openId, String title, String merchantName, Date closeTime);

}
