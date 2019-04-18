package com.family.demotest.service.wxopen;

import com.family.demotest.vo.wx.TemplateMsgSendVo;
import com.family.demotest.web.util.ResultCode;

public interface WxTemplateService
{

    /**
     * 设置所属行业
     * 
     * @param channelNo
     * @return
     */
    public ResultCode setIndustry(String channelNo);

    /**
     * 获取设置的行业信息
     * 
     * @param channelNo
     * @return
     */
    public ResultCode getIndustry(String channelNo);

    /**
     * 获得模板ID
     * 
     * @param channelNo
     * @return
     */

    /**
     * 获得模板ID
     * 
     * @param channelNo
     * @param templateIdShort
     *            模板库中模板的编号，有“TM**”和“OPENTMTM**”等形式
     * @return
     */
    public ResultCode getTemplateId(String channelNo, String templateIdShort);

    /**
     * 获取模板列表
     * 
     * @param channelNo
     * @return
     */
    public ResultCode getTemplateList(String channelNo);

    /**
     * 删除模板
     * 
     * @param channelNo
     * @return
     */
    public ResultCode deleteTemplate(String channelNo, String templateId);

    /**
     * 发送模板消息
     * 
     * @param channelNo
     * @return
     */
    public ResultCode sendMsg(TemplateMsgSendVo sendVo);

}
