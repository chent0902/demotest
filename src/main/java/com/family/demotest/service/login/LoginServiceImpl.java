package com.family.demotest.service.login;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.base01.util.Https;
import com.family.base01.util.Logger;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.StringRedisBiz;
import com.family.demotest.common.constant.Constants;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.service.user.UserService;
import com.family.demotest.service.wxopen.TicketService;
import com.family.demotest.service.wxopen.WxMsgReplyService;
import com.family.demotest.web.util.UUIDUtil;

@Service("wx.login.service")
public class LoginServiceImpl
    implements
    LoginService
{

    // 公众号授权地址
    private static final String AUTH2_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE&component_appid=CPT_ID#wechat_redirect";
    // 公众号获取access_token地址
    private static final String OAUTH_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=APPID&code=CODE&grant_type=authorization_code&component_appid=CPT_ID&component_access_token=COMPONENT_ACCESS_TOKEN";
    // 获取用户信息地址
    private static final String GET_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    @Autowired
    private UserService userService;

    @Autowired
    private WxMsgReplyService wxMsgReplyService;

    @Autowired
    private ConfigService configService;
    @Autowired
    private StringRedisBiz stringRedisBiz;
    @Autowired
    private Validator validator;
    @Autowired
    private Logger logger;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private Https https;

    // 微信授权引导连接
    @Value("${weixin.oauth2.baseinfo.url}")
    private String oauthBaseUserInfoUrl;

    @Value("${demotest.host.name.url}")
    private String hostUrl;

    @Override
    public String getAuthUrl(String channelNo, String redirectUri)
    {
        String authUrl = "";
        BaseConfigModel config = configService.findByChannelNo(channelNo);
        if(config!=null)
        {
            String authKey = UUIDUtil.get32UUID();
            try
            {
                String uri = URLEncoder.encode(hostUrl+"/wxlogin/auth.do?authkey="+authKey, "utf-8");
                authUrl = AUTH2_URL.replace("APPID", config.getAppid()).replace("REDIRECT_URI", uri).replace("STATE", channelNo).replace("CPT_ID",
                    ticketService.getComponentAppid());
            }
            catch(UnsupportedEncodingException e)
            {
                logger.error(e, "回调地址encode出错");
            }
            // 回调地址保存2分钟
            stringRedisBiz.put(Constants.WX_AUTH_CALL_URL+authKey, redirectUri, 1, TimeUnit.MINUTES);
        }

        return authUrl;
    }

    @Override
    public UserModel getUserInfo(String channelNo, String code, String appid)
    {
        UserModel user = null;
        try
        {
            if(validator.isBlank(appid))
            {
                BaseConfigModel config = configService.findByChannelNo(channelNo);
                if(config!=null)
                {
                    appid = config.getAppid();
                }
                else
                {
                    return null;
                }
            }

            String componentAppid = ticketService.getComponentAppid();
            String componentAccessToken = ticketService.getAccessToken();

            String oauthATUrl = OAUTH_ACCESS_TOKEN_URL.replace("APPID", appid).replace("CODE", code).replace("CPT_ID", componentAppid)
                    .replace("COMPONENT_ACCESS_TOKEN", componentAccessToken);
            // logger.info("******oauthATUrl="+oauthATUrl);
            String jsonStrAT = https.get(oauthATUrl);
            logger.info("*******通过code获取token信息"+jsonStrAT);
            if(!validator.isBlank(jsonStrAT))
            {
                JSONObject jsonObject = JSON.parseObject(jsonStrAT);
                String openId = jsonObject.getString("openid");
                String access_token = jsonObject.getString("access_token");
                // String refresh_token =
                // jsonObject.getString("refresh_token");
                if(validator.isBlank(openId))
                {
                    return null;
                }

                // 主要是处理同时两次请求
                String openIdValue = stringRedisBiz.get("wx.login.syn.key."+openId);
                if(!validator.isBlank(openIdValue))
                {
                    logger.info("****睡眠700毫秒");
                    Thread.sleep(700L);// 睡眠500毫秒
                    user = userService.findByChannelNoAndOpenId(channelNo, openId);
                    return user;
                }
                stringRedisBiz.put("wx.login.syn.key."+openId, openId, 600, TimeUnit.MILLISECONDS);

                user = userService.findByChannelNoAndOpenId(channelNo, openId);

                // 先调用获取用户基本信息接口，看看用户是否已关注了该公众号
                JSONObject userObject = null;
                String authorizerAccessToken = ticketService.getAuthorizerAccessToken(channelNo);

                if(user==null)
                {
                    user = new UserModel();
                    user.setChannelNo(channelNo);
                    user.setOpenid(openId);
                    user.setCreateTime(new Date());

                    if(!validator.isBlank(authorizerAccessToken))
                    {
                        userObject = wxMsgReplyService.getUserInfo(authorizerAccessToken, openId);
                        // subscribe
                        // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
                        if(userObject!=null&&userObject.getIntValue("subscribe")==1)
                        {
                            user.setFollow(1);// 用户已关注
                            user.setNickName(userObject.getString("nickname"));
                            user.setSex(userObject.getIntValue("sex"));
                            user.setCity(userObject.getString("city"));
                            user.setCountry(userObject.getString("country"));
                            user.setProvince(userObject.getString("province"));
                            user.setSubscribe(userObject.getIntValue("subscribe"));
                            user.setSubscribeTime(userObject.getString("subscribe_time"));
                            user.setHeadimgurl(userObject.getString("headimgurl"));
                            user.setUnionid(userObject.getString("unionid"));
                            user.setRemark(userObject.getString("remark"));
                            user.setGroupid(userObject.getString("groupid"));
                        }
                    }

                    userService.save(user);
                }
                else
                {
                    if(user.getFollow()!=1&&!validator.isBlank(authorizerAccessToken))
                    {
                        userObject = wxMsgReplyService.getUserInfo(authorizerAccessToken, openId);
                        // subscribe
                        // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
                        if(userObject!=null&&userObject.getIntValue("subscribe")==1)
                        {
                            user.setFollow(1);// 用户已关注
                            user.setNickName(userObject.getString("nickname"));
                            user.setSex(userObject.getIntValue("sex"));
                            user.setCity(userObject.getString("city"));
                            user.setCountry(userObject.getString("country"));
                            user.setProvince(userObject.getString("province"));
                            user.setSubscribe(userObject.getIntValue("subscribe"));
                            user.setSubscribeTime(userObject.getString("subscribe_time"));
                            user.setHeadimgurl(userObject.getString("headimgurl"));
                            user.setUnionid(userObject.getString("unionid"));
                            user.setRemark(userObject.getString("remark"));
                            user.setGroupid(userObject.getString("groupid"));

                            userService.save(user);
                        }

                    }
                }

                // 关闭数据库
                userService.close();

                // 用户未关注
                // if(user==null||user.getFollow()!=1)
                // {
                // // 先调用获取用户基本信息接口，看看用户是否已关注了该公众号
                // JSONObject userObject = null;
                // String authorizerAccessToken =
                // ticketService.getAuthorizerAccessToken(channelNo);
                // if(!validator.isBlank(authorizerAccessToken))
                // {
                // userObject =
                // wxMsgReplyService.getUserInfo(authorizerAccessToken, openId);
                // // subscribe 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
                // if(userObject!=null&&userObject.getIntValue("subscribe")==1)
                // {
                // if(user==null)
                // {
                // user = new UserModel();
                // user.setChannelNo(channelNo);
                // user.setOpenid(openId);
                // user.setCreateTime(new Date());
                // }
                //
                // user.setFollow(1);// 用户已关注
                // user.setNickName(userObject.getString("nickname"));
                // user.setSex(userObject.getIntValue("sex"));
                // user.setCity(userObject.getString("city"));
                // user.setCountry(userObject.getString("country"));
                // user.setProvince(userObject.getString("province"));
                // user.setSubscribe(userObject.getIntValue("subscribe"));
                // user.setSubscribeTime(userObject.getString("subscribe_time"));
                // user.setHeadimgurl(userObject.getString("headimgurl"));
                // user.setUnionid(userObject.getString("unionid"));
                // user.setRemark(userObject.getString("remark"));
                // user.setGroupid(userObject.getString("groupid"));
                // userService.save(user);
                // return user;
                // }
                // }

                // 通过网页授权获取用户信息
                // String getUserInfoUrl =
                // GET_USERINFO_URL.replace("ACCESS_TOKEN",
                // access_token).replace("OPENID", openId);
                // String userInfoJson = https.get(getUserInfoUrl);
                // if(!validator.isBlank(userInfoJson))
                // {
                //
                // if(user==null)
                // {
                // user = new UserModel();
                // user.setChannelNo(channelNo);
                // user.setOpenid(openId);
                // user.setCreateTime(new Date());
                // }
                //
                // user.setFollow(0);// 用户授权
                //
                // userObject = JSON.parseObject(userInfoJson);
                // user.setNickName(userObject.getString("nickname"));
                // user.setSex(userObject.getIntValue("sex"));
                // user.setProvince(userObject.getString("province"));
                // user.setCity(userObject.getString("city"));
                // user.setCountry(userObject.getString("country"));
                // user.setHeadimgurl(userObject.getString("headimgurl"));
                // user.setUnionid(userObject.getString("unionid"));
                // userService.save(user);
                //
                // return user;
                //
                // }

                // }

            }

        }
        catch(Exception e)
        {
            logger.error(e, "***用户登录时获取用户信息出错");
        }
        finally
        {
            // userService.close();
        }

        return user;
    }

    @Override
    public String getHostUrl()
    {
        return hostUrl;
    }

}
