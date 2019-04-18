package com.family.demotest.service.wxopen;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jeewx.api.core.common.WxstoreUtils;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.third.JwThirdAPI;
import org.jeewx.api.third.model.ApiAuthorizerToken;
import org.jeewx.api.third.model.ApiAuthorizerTokenRet;
import org.jeewx.api.third.model.ApiComponentToken;
import org.jeewx.api.third.model.ApiGetAuthorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.base01.util.Https;
import com.family.base01.util.Logger;
import com.family.base01.util.Validator;
import com.family.demotest.common.aes.AesException;
import com.family.demotest.common.aes.WXBizMsgCrypt;
import com.family.demotest.common.constant.Constants;
import com.family.demotest.common.nosql.redis.DefaultRedisOperate;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.service.oss.OssService;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author wujf
 *
 */
@Service("ticket.ticket.service")
public class TicketServiceImpl
    implements TicketService
{

    // 引导用户到第三方平台授权页面
    private static final String LOGINPAGE_URL = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=COMPONENT_APPID&pre_auth_code=PRE_AUTH_CODE&redirect_uri=REDIRECT_URI";

    // 调用JSAPI微信凭证票据
    private static final String API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    @Autowired
    private Validator validator;
    @Autowired
    private Logger logger;
    @Autowired
    private Https https;
    @Autowired
    private OssService ossService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private WxTemplateService wxTemplateService;

    @Autowired
    private DefaultRedisOperate<String, String> redisOperate;

    @Value("${demotest.auth.domain.url}")
    private String authDomainUrl;

    /**
     * 微信全网账号
     */
    @Value("${weixin.mp.component_appid}")
    private String COMPONENT_APPID;
    @Value("${weixin.mp.component_appsecret}")
    private String COMPONENT_APPSECRET;
    @Value("${weixin.mp.component_encodingaeskey}")
    private String COMPONENT_ENCODINGAESKEY;
    @Value("${weixin.mp.component_token}")
    private String COMPONENT_TOKEN;

    @Override
    public ResultCode getSessionKey(String channelNo, String code)
    {
        ResultCode result = new ResultCode();

        return result;
    }

    @Override
    public void processAuthorizeEvent(String nonce, String timestamp, String signature, String msgSignature, String xml)
        throws IOException
    {
        if(validator.isBlank(msgSignature))
        {
            return;// 微信推送给第三方开放平台的消息一定是加过密的，无消息加密无法解密消息
        }
        try
        {

            // logger.info("****COMPONENT_TOKEN="+COMPONENT_TOKEN+",COMPONENT_ENCODINGAESKEY="+COMPONENT_ENCODINGAESKEY+",COMPONENT_APPID="+COMPONENT_APPID
            // +",COMPONENT_APPSECRET="+COMPONENT_APPSECRET);

            // 此时加密的xml数据中ToUserName是非加密的，解析xml获取即可
            WXBizMsgCrypt pc = new WXBizMsgCrypt(COMPONENT_TOKEN, COMPONENT_ENCODINGAESKEY, COMPONENT_APPID);
            xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml);
            logger.info("每十分钟微信返回ticket数据:"+xml);
            processAuthorizationEvent(xml);
        }
        catch(AesException e)
        {
            logger.error(e, "解密tikcet出错");
            e.printStackTrace();
        }
    }

    /**
     * 授权事件处理 解析xml 并且保存票据
     * 
     * @param xml
     */
    private void processAuthorizationEvent(String xml)
    {
        Document doc;
        try
        {
            doc = DocumentHelper.parseText(xml);
            Element rootElt = doc.getRootElement();
            String ticket = rootElt.elementText("ComponentVerifyTicket");

            logger.info("每十分钟推送ticket="+ticket);
            if(!validator.isBlank(ticket))
            {
                redisOperate.set(Constants.demotest_COMPONENT_TICKET, ticket);
            }
        }
        catch(DocumentException e)
        {
            logger.error(e, "解析保存第三方票据ticket出错");
        }

    }

    @Override
    public String getAccessToken()
    {

        // 获取缓存中的token
        String accessToken = redisOperate.get(Constants.demotest_ACCESS_TOKEN);
        if(validator.isBlank(accessToken))
        {
            try
            {
                String componentVerifyTicket = redisOperate.get(Constants.demotest_COMPONENT_TICKET);

                if(!validator.isBlank(componentVerifyTicket))
                {// 票据不为空
                    ApiComponentToken apiComponentToken = new ApiComponentToken();
                    apiComponentToken.setComponent_appid(COMPONENT_APPID);
                    apiComponentToken.setComponent_appsecret(COMPONENT_APPSECRET);
                    apiComponentToken.setComponent_verify_ticket(componentVerifyTicket);
                    accessToken = JwThirdAPI.getAccessToken(apiComponentToken);

                    if(logger.isInfoEnable())
                    {
                        logger.info("**********accessToken="+accessToken);
                    }

                    if(!validator.isBlank(accessToken))
                    {// accessToken有效期（2小时）,提前10分钟去刷新
                        redisOperate.set(Constants.demotest_ACCESS_TOKEN, accessToken, 110, TimeUnit.MINUTES);
                    }
                }

            }
            catch(WexinReqException e)
            {
                logger.error(e, "获取第三方平台component_access_token 出错");
            }

        }

        return accessToken;
    }

    @Override
    public String getPreAuthCode()
    {
        String preAuthCode = null;
        String accessToken = this.getAccessToken();
        if(!validator.isBlank(accessToken))
        {
            try
            {
                preAuthCode = JwThirdAPI.getPreAuthCode(COMPONENT_APPID, accessToken);
                return preAuthCode;
            }
            catch(WexinReqException e)
            {
                logger.error(e, "获取预授权码pre_auth_code出错");
            }
        }

        return null;
    }

    /**
     * 上传公众号二维码到oss
     * 
     * @param qrcodeUrl
     * @return
     */
    private String uploadQrcode(String qrcodeUrl)
    {
        String qrCodeKey = "";
        String fileName = UUID.randomUUID().toString().replace("-", "")+".png";
        String imgSign = ossService.uploadImg(qrcodeUrl, fileName);
        if(!validator.isBlank(imgSign))
        {
            qrCodeKey = Constants.OSS_DIR+fileName;
        }
        return qrCodeKey;
    }

    @Override
    public ResultCode authorCallback(String channelNo, String auth_code)
        throws WexinReqException
    {
        ResultCode result = new ResultCode();

        try
        {
            String component_access_token = this.getAccessToken();
            // 使用授权码换取公众号或小程序的接口调用凭据和授权信息
            net.sf.json.JSONObject json = JwThirdAPI.getApiQueryAuthInfo(COMPONENT_APPID, auth_code, component_access_token);

            if(logger.isInfoEnable())
            {
                logger.info("***获取授权信息：channelNo="+channelNo+",info:"+json.toString());
            }

            // 授权信息
            net.sf.json.JSONObject authorization_info = json.getJSONObject("authorization_info");

            String authorizer_appid = authorization_info.getString("authorizer_appid");
            String authorizer_access_token = authorization_info.getString("authorizer_access_token");
            // 2xiashi
            int authorizer_expires_in = authorization_info.getInt("expires_in");
            String authorizer_refresh_token = authorization_info.getString("authorizer_refresh_token");
            // 获取授权列表
            net.sf.json.JSONArray func_info = authorization_info.getJSONArray("func_info");
            String func_ids = "";
            for(int i = 0; i<func_info.size(); i++)
            {
                func_ids += func_info.getJSONObject(i).getJSONObject("funcscope_category").getInt("id")+",";
            }

            BaseConfigModel config = configService.findByChannelNo(channelNo);
            if(config==null)
            {
                config = new BaseConfigModel();
                config.setCreateTime(new Date());
                config.setChannelNo(channelNo);
            }

            config.setAppid(authorizer_appid);
            config.setAccessToken(authorizer_access_token);
            config.setRefreshToken(authorizer_refresh_token);
            long overtime = (System.currentTimeMillis()+authorizer_expires_in*1000);
            config.setOverTime(overtime+"");// 过期时间

            ApiGetAuthorizer apiGetAuthorizer = new ApiGetAuthorizer();
            apiGetAuthorizer.setComponent_appid(COMPONENT_APPID);// 第三方平台appid
            apiGetAuthorizer.setAuthorizer_appid(authorizer_appid);// 授权方appid

            // 获取授权方的帐号基本信息
            net.sf.json.JSONObject apiGetAuthorizerRetJson = this.apiGetAuthorizerInfo(apiGetAuthorizer, component_access_token);

            if(logger.isInfoEnable())
            {
                logger.info("**获取授权方的帐号基本信息:channelNo="+channelNo+",info:"+apiGetAuthorizerRetJson.toString());
            }

            if(apiGetAuthorizerRetJson!=null)
            {

                net.sf.json.JSONObject authInfoJSON = apiGetAuthorizerRetJson.getJSONObject("authorizer_info");

                if(authInfoJSON.getJSONObject("service_type_info").getInt("id")==0&&authInfoJSON.getJSONObject("verify_type_info").getInt("id")!=-1)
                {

                }

                if(authInfoJSON.getJSONObject("verify_type_info").getInt("id")!=-1)
                {
                    config.setServiceTypeInfo(authInfoJSON.getJSONObject("service_type_info").getInt("id"));
                    config.setVerifyTypeInfo(authInfoJSON.getJSONObject("verify_type_info").getInt("id"));
                    config.setHeadImg(authInfoJSON.getString("head_img"));
                    config.setNickName(authInfoJSON.getString("nick_name"));
                    config.setQrcodeUrl(authInfoJSON.getString("qrcode_url"));
                    config.setUserName(authInfoJSON.getString("user_name"));

                    // 把公众号二维码上传oss
                    if(!validator.isBlank(config.getQrcodeUrl()))
                    {
                        config.setQrcode(uploadQrcode(config.getQrcodeUrl()));
                    }

                    // config.setStatus(1);
                    // 保存配置信息
                    configService.save(config);
                }

            }
            // 设置授权方接口调用凭据
            this.setAuthorizerAccessToken(channelNo, authorizer_access_token);

            // 设置模版消息所属行业
            wxTemplateService.setIndustry(channelNo);

        }
        catch(Exception e)
        {
            logger.error(e, "用户授权后获取信息出错");
            result.setCode(-1);
            result.setInfo("用户授权后获取信息出错");
            return result;
        }

        return result;
    }

    private static String api_get_authorizer_info_url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";

    /**
     * 获取授权方的帐号基本信息(重写方法，sdk有些属性没有)
     * 
     * @param apiGetAuthorizer
     * @param component_access_token
     * @return
     * @throws WexinReqException
     */
    private net.sf.json.JSONObject apiGetAuthorizerInfo(ApiGetAuthorizer apiGetAuthorizer, String component_access_token)
        throws WexinReqException
    {
        String requestUrl = api_get_authorizer_info_url.replace("COMPONENT_ACCESS_TOKEN", component_access_token);
        net.sf.json.JSONObject param = net.sf.json.JSONObject.fromObject(apiGetAuthorizer);

        net.sf.json.JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST", param.toString());
        return result;
    }

    @Override
    public String getComponentloginpage(String channelNo)
    {

        String componentAccessToken = this.getAccessToken();
        if(validator.isBlank(componentAccessToken))
        {
            // -1-获取token失败，请稍后再试
            return "-1";
        }

        String loginpageUrl = "";
        try
        {
            // 预授权码
            String preAuthCode = JwThirdAPI.getPreAuthCode(COMPONENT_APPID, componentAccessToken);
            String redirectUri = authDomainUrl+"/demotest/ticket/"+channelNo+"/authorCallback.do";
            loginpageUrl = LOGINPAGE_URL.replace("COMPONENT_APPID", COMPONENT_APPID).replace("PRE_AUTH_CODE", preAuthCode).replace("REDIRECT_URI", redirectUri);
        }
        catch(WexinReqException e)
        {
            logger.error(e, "***获取预授权码出错:"+e.getMessage());

            return "-1";
        }

        return loginpageUrl;
    }

    @Override
    public void setAuthorizerAccessToken(String channelNo, String authorizerAccessToken)
    {
        // 设置有效期110分钟
        redisOperate.set(Constants.CHANNEL_ACCESS_TOKEN+channelNo, authorizerAccessToken, 110, TimeUnit.MINUTES);

    }

    @Override
    public String getAuthorizerAccessToken(String channelNo)
    {
        String accessToken = redisOperate.get(Constants.CHANNEL_ACCESS_TOKEN+channelNo);
        if(validator.isBlank(accessToken))
        {

            BaseConfigModel config = configService.findByChannelNo(channelNo);

            ApiAuthorizerToken apiAuthorizerToken = new ApiAuthorizerToken();
            apiAuthorizerToken.setComponent_appid(COMPONENT_APPID);// 第三方平台appid
            apiAuthorizerToken.setAuthorizer_appid(config.getAppid());// 授权方appid
            apiAuthorizerToken.setAuthorizer_refresh_token(config.getRefreshToken());// 授权方的刷新令牌
            String component_access_token = this.getAccessToken();
            try
            {

                String url = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token="+component_access_token;

                String conent = JSON.toJSONString(apiAuthorizerToken);
                String rel = https.post(url, conent);

                logger.info("刷新token返回结果："+rel);

                ApiAuthorizerTokenRet apiAuthorizerTokenRet = null;
                if(!validator.isBlank(rel))
                {
                    apiAuthorizerTokenRet = JSON.parseObject(rel, ApiAuthorizerTokenRet.class);
                }

                // 新的accesstoken
                accessToken = apiAuthorizerTokenRet.getAuthorizer_access_token();
                if(!validator.isBlank(apiAuthorizerTokenRet.getAuthorizer_access_token()))
                {
                    // 设置accesstoken
                    this.setAuthorizerAccessToken(channelNo, apiAuthorizerTokenRet.getAuthorizer_access_token());
                    config.setAccessToken(apiAuthorizerTokenRet.getAuthorizer_access_token());
                    config.setRefreshToken(apiAuthorizerTokenRet.getAuthorizer_refresh_token());
                    long overtime = System.currentTimeMillis()+apiAuthorizerTokenRet.getExpires_in();
                    config.setOverTime(overtime+"");
                    configService.save(config);
                }
                else
                {
                    logger.info("获取授权方的令牌结果出错："+rel);
                }

            }

            catch(Exception e)
            {
                logger.error(e, "通过刷新令牌获取授权方的令牌出错，channelNo："+channelNo);

            }
        }

        return accessToken;
    }

    @Override
    public String getComponentAppid()
    {
        return COMPONENT_APPID;
    }

    @Override
    public String getComponentAppsecret()
    {
        return COMPONENT_APPSECRET;
    }

    @Override
    public String getComponentEncodingaeskey()
    {
        return COMPONENT_ENCODINGAESKEY;
    }

    @Override
    public String getComponentToken()
    {
        return COMPONENT_TOKEN;
    }

    @Override
    public ResultCode getScanInfo(String channelNo)
    {

        return null;
    }

    @Override
    public String getJsApiTicket(String channelNo)
    {

        // 1.获取jsApiTicket票据
        String jsApiTicket = redisOperate.get(Constants.demotest_JSAPI_TICKET+channelNo);

        if(validator.isBlank(jsApiTicket))
        {
            String token = this.getAuthorizerAccessToken(channelNo);
            // redisOperate.del(Constants.demotest_JSAPI_TICKET+channelNo);
            String requestUrl = API_TICKET_URL.replace("ACCESS_TOKEN", token);
            String jsonStr = https.get(requestUrl);

            logger.info("微信jsapi接口返回结果："+jsonStr);
            if(!validator.isBlank(jsonStr))
            {
                JSONObject jsonObject = JSON.parseObject(jsonStr);
                // 如果请求成功
                if(null!=jsonObject)
                {
                    jsApiTicket = jsonObject.getString("ticket");
                    // jsonObject.getIntValue("expires_in");
                }
                // 把jsApiTicket放到缓存中去
                redisOperate.set(Constants.demotest_JSAPI_TICKET+channelNo, jsApiTicket, 110, TimeUnit.MINUTES);
            }

        }

        return jsApiTicket;

    }
}
