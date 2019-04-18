package com.family.demotest.service.wxopen;

import java.io.IOException;

import org.jeewx.api.core.exception.WexinReqException;

import com.family.demotest.web.util.ResultCode;

public interface TicketService
{

    /**
     * 获取小程序sessionkey
     * 
     * @param channelNo
     * @param code
     * @return
     */
    public ResultCode getSessionKey(String channelNo, String code);

    /**
     * 授权事件接收 微信第三方平台---------微信推送Ticket消息10分钟一次-----------
     * 
     * @param nonce
     * @param timestamp
     * @param signature
     * @param msgSignature
     * @throws IOException
     */
    public void processAuthorizeEvent(String nonce, String timestamp, String signature, String msgSignature, String xml)
        throws IOException;

    /**
     * 获取第三方平台component_access_token
     * 
     * @return
     */
    public String getAccessToken();

    /**
     * 获取预授权码pre_auth_code
     * 
     * @return
     */
    public String getPreAuthCode();

    public ResultCode authorCallback(String channelNo, String auth_code)
        throws WexinReqException;

    /**
     * 获取用户进入授权页的url
     * 
     * @param channelNo
     * @return
     */
    public String getComponentloginpage(String channelNo);

    /**
     * 设置授权方接口调用凭据
     * 
     * @param channelNo
     *            渠道号
     * @param authorizerAccessToken
     *            授权方接口调用凭据
     */
    public void setAuthorizerAccessToken(String channelNo, String authorizerAccessToken);

    /**
     * 获取授权方接口调用凭据
     * 
     * @param channelNo
     * @return
     */
    public String getAuthorizerAccessToken(String channelNo);

    /**
     * 获取第三方appid
     * 
     * @return
     */
    public String getComponentAppid();

    /**
     * 获取第三方Appsecret(秘钥)
     * 
     * @return
     */
    public String getComponentAppsecret();

    /**
     * 获取第三方加密key
     * 
     * @return
     */
    public String getComponentEncodingaeskey();

    /**
     * 获取第三方token
     * 
     * @return
     */
    public String getComponentToken();

    /**
     * 微信扫一扫
     * 
     * @param channelNo
     * @return
     */
    public ResultCode getScanInfo(String channelNo);

    /**
     * 获取微信jsapi票据
     * 
     * @param channelNo
     * @return
     */
    public String getJsApiTicket(String channelNo);

}
