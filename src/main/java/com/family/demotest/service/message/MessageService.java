package com.family.demotest.service.message;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.MessageModel;
import com.family.demotest.web.util.ResultCode;

public interface MessageService
    extends SaveService<MessageModel>, DeleteService<MessageModel>, QueryService<MessageModel>
{

    /**
     * 群发图文消息
     * 
     * @param channelNo
     * @param messageArray
     * @return
     */
    public ResultCode sendPicTextMsg(String channelNo, String articles);

    /**
     * 群发模版消息
     * 
     * @param channelNo
     * @param msgType
     * @param title
     * @param link
     * @param appid
     * @return
     */
    public ResultCode groupsSendMsg(String channelNo, String msgType, String title, String link, String appid);

    /**
     * 当天是否已群发模版消息
     * 
     * @param channelNo
     * @return
     */
    public boolean isSend(String channelNo);

    /**
     * 当天是否已群发图文消息
     * 
     * @param channelNo
     * @return
     */
    public boolean isSendPicText(String channelNo);

    /**
     * 图文消息群发记录
     * 
     * @param channelNo
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode messageRecord(String channelNo, int page, int pageSize);

}
