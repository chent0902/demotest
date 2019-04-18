package com.family.demotest.service.login;

import com.family.demotest.entity.UserModel;

public interface LoginService
{

    /**
     * 获取微信公众号授权地址
     * 
     * @param channelNo
     * @param redirectUri
     * @return
     */
    public String getAuthUrl(String channelNo, String redirectUri);

    /**
     * 通过code换取access_token并且获取用户信息
     * 
     * @param channelNo
     * @param code
     * @return
     */

    /**
     * 通过code换取access_token并且获取用户信息
     * 
     * @param channelNo
     * @param code
     * @param appid
     *            公众号appid
     * @return
     */
    public UserModel getUserInfo(String channelNo, String code, String appid);

    /**
     * 获取项目地址
     * 
     * @return
     */
    public String getHostUrl();

}
