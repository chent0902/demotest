package com.family.demotest.service.wxopen;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.family.demotest.entity.UserModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author wujf
 *
 */
public interface WxMsgReplyService
{

    /**
     * 回复事件消息
     * 
     * @param request
     * @param response
     * @param authorizerAppid
     *            授权方公众号appid
     * @param map
     *            事件返回信息
     */
    public void replyEventMessage(HttpServletRequest request, HttpServletResponse response, String authorizerAppid, Map<String, String> map);

    /**
     * 保存用户信息
     * 
     * @param channelNo
     * @param token
     * @param openid
     * @param follow
     *            -1-取消关注，1-关注
     * @return
     */
    public UserModel saveUser(String channelNo, String token, String openid, int follow);

    /**
     * 保存用户信息
     * 
     * @param channelNo
     * @param token
     * @param openid
     * @param follow
     *            -1-取消关注，1-关注
     * @return
     */

    /**
     * 从微信接口获取用户基本信息
     * 
     * @param token
     * @param openid
     * @return
     */
    public JSONObject getUserInfo(String token, String openid);

    /**
     * 刷新用户状态
     * 
     * @param userId
     * @return
     */
    public ResultCode refreshFollow(String userId);

}
