package com.family.demotest.service.wxopen;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
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
import com.family.demotest.common.biz.IntegerRedisBiz;
import com.family.demotest.common.biz.StringRedisBiz;
import com.family.demotest.common.enumtype.EventType;
import com.family.demotest.common.enumtype.QrcodeSource;
import com.family.demotest.common.notify.NotifyManager;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.entity.BaseSettingModel;
import com.family.demotest.entity.LotteryModel;
import com.family.demotest.entity.LotteryScanHelpModel;
import com.family.demotest.entity.QrcodeModel;
import com.family.demotest.entity.TemplateMsgModel;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.base.SettingService;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.service.domain.DomainService;
import com.family.demotest.service.lottery.LotteryScanHelpService;
import com.family.demotest.service.lottery.LotteryService;
import com.family.demotest.service.qrcode.QrcodeService;
import com.family.demotest.service.queue.templateMsg.TemplateMsgQueueService;
import com.family.demotest.service.template.TemplateMsgService;
import com.family.demotest.service.user.UserService;
import com.family.demotest.vo.imgMsg.Article;
import com.family.demotest.vo.imgMsg.NewsMessage;
import com.family.demotest.vo.msg.KfTextVo;
import com.family.demotest.vo.total.UserLotteryTotalVo;
import com.family.demotest.vo.wx.QrsceneVo;
import com.family.demotest.web.util.MessageUtil;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author wujf
 *
 */
@Service("wx.msgreply.service")
public class WxMsgReplyServiceImpl
    implements
    WxMsgReplyService
{

    public static final String OSS_DOMAIN = "http://demotest.oss-cn-hangzhou.aliyuncs.com/";
    @Autowired
    private ConfigService configService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisBiz stringRedisBiz;
    @Autowired
    private IntegerRedisBiz integerRedisBiz;
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private LotteryScanHelpService lotteryScanHelpService;
    @Autowired
    private TemplateMsgService templateMsgService;
    @Autowired
    private DomainService domainService;
    @Autowired
    private Https https;
    @Autowired
    private Validator validator;
    @Autowired
    private Logger logger;
    @Autowired
    private TemplateMsgQueueService templateMsgQueueService;

    @Autowired
    private QrcodeService qrcodeService;

    @Autowired
    private SettingService settingService;

    // 关注后获取用户信息
    @Value("${weixin.subscribe.userinfo.url}")
    private String subscribeUserinfoUrl;

    // @Value("${demotest.site.host.name.url}")
    // private String siteHostUrl;

    @Override
    public UserModel saveUser(String channelNo, String token, String openid, int follow)
    {
        try
        {
            // 用户取消关注
            if(follow==-1)
            {
                UserModel model = userService.findByChannelNoAndOpenId(channelNo, openid);
                if(model!=null)
                {
                    model.setFollow(-1);// 取消关注
                    model.setCancelTime(new Date());
                    userService.save(model);
                }
                return model;
            }

            String subUserInfoUrl = subscribeUserinfoUrl.replace("ACCESS_TOKEN", token).replace("OPENID", openid);
            String jsonStr = https.get(subUserInfoUrl);

            // logger.info("****userinf=" + jsonStr);

            if(!validator.isBlank(jsonStr))
            {
                JSONObject jsonObject = JSON.parseObject(jsonStr);
                if(jsonObject!=null)
                {
                    // openid = jsonObject.getString("openid");
                    int subscribe = jsonObject.getIntValue("subscribe");
                    String nickname = jsonObject.getString("nickname");
                    int sex = jsonObject.getIntValue("sex");
                    String city = jsonObject.getString("city");
                    String province = jsonObject.getString("province");
                    String country = jsonObject.getString("country");
                    String headimgurl = jsonObject.getString("headimgurl");
                    String subscribe_time = jsonObject.getString("subscribe_time");
                    String remark = jsonObject.getString("remark");
                    String groupid = jsonObject.getString("groupid");

                    // 前端请求两次，防止数据库保存两条相同数据
                    String openidKey = "subscribe.synkey."+openid;
                    String openidStr = stringRedisBiz.get(openidKey);
                    if(!validator.isBlank(openidStr))
                    {
                        return null;
                    }
                    stringRedisBiz.put(openidKey, openid, 1, TimeUnit.SECONDS);

                    UserModel model = userService.findByChannelNoAndOpenId(channelNo, openid);
                    if(model==null)
                    {
                        model = new UserModel();
                        model.setChannelNo(channelNo);
                        model.setCreateTime(new Date());
                        model.setOpenid(openid);
                    }

                    model.setNickName(nickname);
                    model.setSex(sex);
                    model.setCity(city);
                    model.setProvince(province);
                    model.setCountry(country);
                    model.setHeadimgurl(headimgurl);
                    model.setSubscribeTime(subscribe_time);
                    model.setSubscribe(subscribe);
                    model.setRemark(remark);
                    model.setGroupid(groupid);
                    model.setFollow(1);// 1-关注 ，-1 取消关注
                    model.setProperty(0);
                    userService.save(model);
                    // logger.info("*******userinfo:"+JSON.toJSONString(model));
                    return model;

                }
            }

        }
        catch(Exception e)
        {
            logger.error(e, "获取用户基本信息出错");
        }

        return null;
    }

    @Override
    public JSONObject getUserInfo(String token, String openid)
    {

        String subUserInfoUrl = subscribeUserinfoUrl.replace("ACCESS_TOKEN", token).replace("OPENID", openid);
        String jsonStr = https.get(subUserInfoUrl);

        logger.info("****网页授权时获取userinfo="+jsonStr);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        // if(jsonObject!=null)
        // {
        // openid = jsonObject.getString("openid");
        // String nickname = jsonObject.getString("nickname");
        // int sex = jsonObject.getIntValue("sex");
        // String city = jsonObject.getString("city");
        // String province = jsonObject.getString("province");
        // String country = jsonObject.getString("country");
        // String headimgurl = jsonObject.getString("headimgurl");
        // String subscribe_time = jsonObject.getString("subscribe_time");
        // String remark = jsonObject.getString("remark");
        // String groupid = jsonObject.getString("groupid");
        //
        // }

        return jsonObject;
    }

    @Override
    public ResultCode refreshFollow(String userId)
    {
        ResultCode result = new ResultCode();
        UserModel user = userService.findById(userId);
        if(user==null)
        {
            result.setCode(999);
            result.setInfo("token失效，请重新登录");
            return result;
        }

        // 先调用获取用户基本信息接口，看看用户是否已关注了该公众号
        String authorizerAccessToken = ticketService.getAuthorizerAccessToken(user.getChannelNo());
        if(!validator.isBlank(authorizerAccessToken))
        {
            JSONObject userObject = this.getUserInfo(authorizerAccessToken, user.getOpenid());
            // subscribe 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息
            if(userObject!=null)
            {
                if(userObject.getIntValue("subscribe")==1)
                {

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

                    user.setSubscribe(1);
                    user.setFollow(1);// 用户已关注
                    userService.save(user);
                }
                else
                {
                    result.setCode(2);
                    result.setInfo("您还没关注哦！");
                }

            }
            else
            {
                result.setCode(999);
                result.setInfo("token失效，请重新登录");
            }
        }
        else
        {
            result.setCode(999);
            result.setInfo("token失效，请重新登录");
        }

        return result;
    }

    /**
     * 生成key
     * 
     * @param helpUserId
     *            帮助人id
     * @param userId
     *            需要帮助用户id
     * 
     * @param lotteryId
     *            活动id
     * @return
     */
    private String getHelpKey(String helpUserId, String userId, String lotteryId)
    {
        return new StringBuffer().append("scan.help-").append(helpUserId).append(userId).append(lotteryId).toString();
    }

    /**
     * 扫码用户是否已扫码过
     * 
     * @param helpUserId
     *            帮助人id
     * @param userId
     *            需要帮助用户id
     * 
     * @param lotteryId
     *            活动id
     * @return
     */

    private String getHelp(String helpUserId, String userId, String lotteryId)
    {
        String key = getHelpKey(helpUserId, userId, lotteryId);
        return stringRedisBiz.get(key);
    }

    /**
     * 设置已帮扫码
     * 
     * @param helpUserId
     * @param userId
     * @param lotteryId
     */
    private void setHelp(String helpUserId, String userId, String lotteryId)
    {
        String key = getHelpKey(helpUserId, userId, lotteryId);
        stringRedisBiz.put(key, "1", 90, TimeUnit.DAYS);
    }

    /**
     * 帮助次数key
     * 
     * @param helpUserId
     * @param lotteryId
     * @return
     */
    private String getHelpNumKey(String helpUserId, String lotteryId)
    {
        return new StringBuffer().append("scanhelpnmu-").append(helpUserId).append(lotteryId).toString();
    }

    /**
     * 获取帮忙次数
     * 
     * @param helpUserId
     * @param lotteryId
     * @return
     */
    private Integer getHelpNum(String helpUserId, String lotteryId)
    {
        String key = getHelpNumKey(helpUserId, lotteryId);
        return integerRedisBiz.get(key);
    }

    /**
     * 设置已帮扫码次数
     * 
     * @param helpUserId
     * @param lotteryId
     */
    private void setHelpNum(String helpUserId, String lotteryId)
    {
        String key = getHelpNumKey(helpUserId, lotteryId);
        Integer num = this.getHelpNum(helpUserId, lotteryId);
        if(num==null)
        {
            num = 0;
        }
        integerRedisBiz.put(key, num+1, 90, TimeUnit.DAYS);
    }

    @Override
    public void replyEventMessage(HttpServletRequest request, HttpServletResponse response, String authorizerAppid, Map<String, String> map)
    {
        BaseConfigModel config = configService.findByAppid(authorizerAppid);
        if(config==null)
        {
            logger.info("****通过appid找不到公众号配置信息 appid="+authorizerAppid);
            return;
        }
        String accessToken = ticketService.getAuthorizerAccessToken(config.getChannelNo());

        if(validator.isBlank(accessToken))
        {
            logger.info("****token获取不到，channelno="+config.getChannelNo());
            return;
        }

        String toUserName = map.get("toUserName");// 公众号原始id
        String fromUserName = map.get("fromUserName");// 用户openid
        String event = map.get("event"); // 事件名称
        String eventKey = map.get("eventKey");//

        // "ToUserName":"gh_47012afd1695",
        // "FromUserName":"oQksr1O0hlE3RqEIIhHAYNPNl4VE",
        // "CreateTime":"1532948978",
        // "MsgType":"event",
        // "Event":"subscribe",
        // "EventKey":""

        // 处理订阅事件
        if(event.equals(EventType.SUBSCRIBE.getCode()))
        {
            // 用户未关注，扫码关注
            if(!validator.isBlank(eventKey))
            {
                this.replyEventMsg(response, config, accessToken, map, 0);
            }
            else
            {
                // 通过公众号关注 回复客服消息
                // KfTextVo msgVo = new KfTextVo();
                // msgVo.setTouser(fromUserName);
                // msgVo.setMsgtype("text");
                // msgVo.setText("感谢你们关注【吃探网】，本平台汇集了最好吃的、最优惠的美食。美食快到我碗里来吧！！！(客服)");
                // msgVo.setToken(accessToken);// token
                // NotifyManager.instance.notifyKefuMsg(msgVo);

                String replyContent = "";
                BaseSettingModel setting = settingService.findByChannelNo(config.getChannelNo());
                // 自定义回复
                if(setting!=null&&!validator.isBlank(setting.getFollowReply()))
                {
                    replyContent = setting.getFollowReply();
                }
                else
                {
                    // 主域名
                    String domainUrl = domainService.getDomainUrl(config.getChannelNo());
                    String url = domainUrl+"/list.html?channelNo="+config.getChannelNo();
                    // 普通关注，推送文本消息
                    replyContent = "感谢您关注【"+config.getNickName()+"】，本平台汇集了最好吃的，最优惠的美食活动推荐给您； <a href='"+url+"' >点这里了解更多优惠</a>";
                }

                String replyTextMsg = replyTextMessage(replyContent, toUserName, fromUserName);
                // 用户关注
                this.saveUser(config.getChannelNo(), accessToken, fromUserName, 1);
                output(response, replyTextMsg);
                return;
            }

        }
        else if(event.equals(EventType.SCAN.getCode()))
        {
            // 用户已关注扫码
            this.replyEventMsg(response, config, accessToken, map, 1);
        }
        else if(event.equals(EventType.UNSUBSCRIBE.getCode()))
        {
            // 用户取消关注
            this.saveUser(config.getChannelNo(), accessToken, fromUserName, -1);
            output(response, "success");
            return;
        }

    }

    /**
     * 回复事件消息
     * 
     * @param response
     * @param config
     * @param accessToken
     * @param map
     * @param type
     *            0-未关注扫码,1-已关注扫码
     */
    private void replyEventMsg(HttpServletResponse response, BaseConfigModel config, String accessToken, Map<String, String> map, int type)
    {

        String toUserName = map.get("toUserName");// 公众号原始id
        String fromUserName = map.get("fromUserName");// 用户openid
        // String event = map.get("event"); // 事件名称
        String eventKey = map.get("eventKey");//

        if(!validator.isBlank(eventKey))
        {
            // 二维码参数
            String sceneKey = "";
            if(type==0)
            {
                sceneKey = eventKey.substring(8);
            }
            else
            {
                sceneKey = eventKey;
            }

            QrsceneVo qrsceneVo = configService.getQrSceneVo(sceneKey);
            if(qrsceneVo!=null)
            {
                // 后台自定义带参二维码
                if(qrsceneVo.getUserId().equals(QrcodeSource.QRCODE.getCode()))
                {
                    QrcodeModel model = qrcodeService.findById(qrsceneVo.getLotteryId());
                    if(model!=null)
                    {
                        // 设置扫码数量
                        qrcodeService.putScanNum(model.getId(), 1);
                        if(model.getType()==0)
                        {
                            String replyContent = model.getContent();
                            // 文本
                            String replyMsg = this.replyTextMessage(replyContent, toUserName, fromUserName);
                            output(response, replyMsg);
                            return;
                        }
                        else if(model.getType()==1)
                        {
                            // 图文
                            String respMessage = this.processPicTextMsg2(toUserName, fromUserName, model);
                            output(response, respMessage);
                            return;
                        }
                    }

                }
                else
                {
                    LotteryModel lottery = lotteryService.findById(qrsceneVo.getLotteryId());
                    if(lottery==null||lottery.getOnline()==0||lottery.getProperty()==1)
                    {
                        // 主域名
                        String domainUrl = domainService.getDomainUrl(config.getChannelNo());
                        String url = domainUrl+"/list.html?channelNo="+config.getChannelNo();
                        // 活动已下架，推送文本消息
                        String replyContent = "感谢您关注【"+config.getNickName()+"】，本平台汇集了最好吃的，最优惠的美食活动推荐给您； <a href='"+url+"' >点这里了解更多优惠</a>";
                        String replyTextMsg = replyTextMessage(replyContent, toUserName, fromUserName);
                        output(response, replyTextMsg);
                        return;
                    }

                    // 用户未关注抽奖，需要扫商品带参码 userId固定为USER_ID
                    if(QrcodeSource.USERID.getCode().equals(qrsceneVo.getUserId()))
                    {
                        // 保存用户信息
                        this.saveUser(config.getChannelNo(), accessToken, fromUserName, 1);
                        // 回复图文
                        String respMessage = processPicTextMsg(config.getChannelNo(), toUserName, fromUserName, qrsceneVo.getLotteryId());

                        output(response, respMessage);
                        return;
                    }
                    else
                    {
                        // 被帮扫的用户
                        UserModel user = userService.findById(qrsceneVo.getUserId());
                        if(user!=null&&user.getOpenid().equals(fromUserName))
                        {
                            // 用户自己扫码自己的带参二维码，发送普通文本消息
                            String replyContent = "扫描自己的二维码不能增加抽奖次数哟～";
                            String replyMsg = this.replyTextMessage(replyContent, toUserName, fromUserName);
                            output(response, replyMsg);
                            return;
                        }
                        else
                        {

                            // 帮扫用户是否存在
                            UserModel helpUser = userService.findByChannelNoAndOpenId(config.getChannelNo(), fromUserName);
                            // 如果用户不存在或已取消关注了
                            if(helpUser==null||helpUser.getFollow()!=1)
                            {
                                helpUser = this.saveUser(config.getChannelNo(), accessToken, fromUserName, 1);
                            }

                            if(helpUser==null)
                            {
                                return;
                            }
                            // 扫码帮 qrsceneVo.getUserId()
                            // 这个用户增加一次抽奖活动，针对qrsceneVo.getLotteryId() 这个活动
                            // 先判断 fromUserName 该用户是否已帮助过
                            String isHelp = this.getHelp(helpUser.getId(), qrsceneVo.getUserId(), qrsceneVo.getLotteryId());
                            if(!validator.isBlank(isHelp))
                            {
                                // 已帮扫码过
                                // 发送普通文本消息
                                String replyContent = "这个活动，您已经帮过他了哟～";
                                String replyMsg = this.replyTextMessage(replyContent, toUserName, fromUserName);
                                output(response, replyMsg);
                                return;
                            }
                            else
                            {
                                // 该活动有限制帮扫次数
                                if(lottery.getHelpNum()>-1)
                                {
                                    Integer helpNum = this.getHelpNum(helpUser.getId(), lottery.getId());
                                    // 已达到帮扫次数
                                    if(helpNum!=null&&helpNum>=lottery.getHelpNum())
                                    {
                                        // 给帮助用户发文本提示消息
                                        KfTextVo msgVo = new KfTextVo();
                                        msgVo.setTouser(fromUserName);
                                        msgVo.setMsgtype("text");
                                        msgVo.setText("您的帮助次数已达到上限。");
                                        msgVo.setToken(accessToken);// token
                                        NotifyManager.instance.notifyKefuMsg(msgVo);
                                        // 回复图文
                                        String respMessage = processPicTextMsg(config.getChannelNo(), toUserName, fromUserName, qrsceneVo.getLotteryId());
                                        output(response, respMessage);
                                        return;
                                    }

                                }
                                // 给用户增加一次参与抽奖机会
                                UserLotteryTotalVo totalVo = lotteryService.getUserLotteryTotalVo(qrsceneVo.getLotteryId(), qrsceneVo.getUserId());
                                if(totalVo==null)
                                {
                                    totalVo = new UserLotteryTotalVo();
                                }
                                totalVo.setTimesNum(totalVo.getTimesNum()+1);
                                lotteryService.setUserLotteryTotalVo(qrsceneVo.getLotteryId(), qrsceneVo.getUserId(), totalVo);

                                // 设置已帮扫码
                                this.setHelp(helpUser.getId(), qrsceneVo.getUserId(), qrsceneVo.getLotteryId());
                                // 设置该用户帮扫次数
                                this.setHelpNum(helpUser.getId(), qrsceneVo.getLotteryId());
                                // 帮扫记录保存数据库中
                                LotteryScanHelpModel model = new LotteryScanHelpModel();
                                model.setChannelNo(config.getChannelNo());
                                model.setLotteryId(qrsceneVo.getLotteryId());
                                model.setUserId(qrsceneVo.getUserId());
                                model.setHelpUserId(helpUser.getId());
                                model.setCreateTime(new Date());

                                lotteryScanHelpService.save(model);

                                // 给帮助的用户发送。到账模版通知消息
                                if(user!=null)
                                {
                                    TemplateMsgModel template = templateMsgService.findByType(config.getChannelNo(), 1);
                                    if(template!=null&&!validator.isBlank(template.getTemplateId()))
                                    {
                                        templateMsgQueueService.sendMsg1(config.getChannelNo(), template.getTemplateId(), user.getOpenid(),
                                            helpUser.getNickName(), lottery.getId(), lottery.getTemplate());
                                    }

                                }

                                // 给帮助用户发文本提示消息
                                KfTextVo msgVo = new KfTextVo();
                                msgVo.setTouser(fromUserName);
                                msgVo.setMsgtype("text");
                                msgVo.setText("您帮助的好友，成功增加1次抽奖机会了～");
                                msgVo.setToken(accessToken);// token
                                NotifyManager.instance.notifyKefuMsg(msgVo);
                                // 回复图文
                                String respMessage = processPicTextMsg(config.getChannelNo(), toUserName, fromUserName, qrsceneVo.getLotteryId());
                                output(response, respMessage);
                                return;
                            }

                        }
                    }
                }

            }

        }
    }

    /**
     * 回复微信服务器"文本消息"
     * 
     * @param request
     * @param response
     * @param content
     * @param toUserName
     * @param fromUserName
     * @throws DocumentException
     * @throws IOException
     */
    private String replyTextMessage(String content, String toUserName, String fromUserName)
    {
        String replyMsg = "";
        try
        {
            Long createTime = Calendar.getInstance().getTimeInMillis()/1000;
            StringBuffer sb = new StringBuffer();
            sb.append("<xml>");
            sb.append("<ToUserName><![CDATA["+fromUserName+"]]></ToUserName>");
            sb.append("<FromUserName><![CDATA["+toUserName+"]]></FromUserName>");
            sb.append("<CreateTime>"+createTime+"</CreateTime>");
            sb.append("<MsgType><![CDATA[text]]></MsgType>");
            sb.append("<Content><![CDATA["+content+"]]></Content>");
            sb.append("</xml>");
            String replyMsgContent = sb.toString();

            // logger.info("*******44444444回复内容:" + replyMsgContent);

            WXBizMsgCrypt pc = new WXBizMsgCrypt(ticketService.getComponentToken(), ticketService.getComponentEncodingaeskey(),
                    ticketService.getComponentAppid());

            replyMsg = pc.encryptMsg(replyMsgContent, createTime.toString(), "easemob");

            // logger.info("*******5555returnvaleue:" + replyMsg);
        }
        catch(AesException e)
        {
            logger.error(e, "全网发布检测文本消息出错");
        }

        return replyMsg;
    }

    /**
     * 回复图文信息
     * 
     * @param channelNo
     * @param toUserName
     * @param fromUserName
     * @param authorizerAppid
     *            公众号appid
     * @param lotteryId
     *            活动id
     * @return
     */
    private String processPicTextMsg(String channelNo, String toUserName, String fromUserName, String lotteryId)
    {
        String returnvaleue = "";
        try
        {
            // 回复图文消息
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            // newsMessage.setFuncFlag(0);

            LotteryModel lottery = lotteryService.findById(lotteryId);
            String title = "";
            String picUrl = "";

            int templateType = 0;
            if(lottery!=null)
            {
                title = lottery.getTitle();
                picUrl = OSS_DOMAIN+lottery.getImg()+"/360x200";
                templateType = lottery.getTemplate();
            }

            // 主域名
            String domainUrl = domainService.getDomainUrl(channelNo);
            String url = "";
            if(templateType==0)
            {
                url = domainUrl+"/details.html?channelNo="+channelNo+"&id="+lotteryId;
            }
            else if(templateType==1)
            {
                url = domainUrl+"/details1.html?channelNo="+channelNo+"&id="+lotteryId;
            }
            else if(templateType==2)
            {
                url = domainUrl+"/details2.html?channelNo="+channelNo+"&id="+lotteryId;
            }

            List<Article> articleList = new ArrayList<Article>();
            Article article1 = new Article();
            article1.setTitle(title);
            article1.setDescription("点击这里，免费领券！");
            article1.setPicUrl(picUrl);
            article1.setUrl(url);
            articleList.add(article1);

            // 设置项目个数，最多8个
            newsMessage.setArticleCount(articleList.size());
            newsMessage.setArticles(articleList);

            String respMessage = MessageUtil.newsMessageToXml(newsMessage);
            Long createTime = Calendar.getInstance().getTimeInMillis()/1000;

            String componentToken = ticketService.getComponentToken();
            String encodingaeskey = ticketService.getComponentEncodingaeskey();
            String componentAppid = ticketService.getComponentAppid();
            // 加密
            WXBizMsgCrypt pc = new WXBizMsgCrypt(componentToken, encodingaeskey, componentAppid);
            returnvaleue = pc.encryptMsg(respMessage, createTime+"", "demotestwujf");
        }
        catch(AesException e)
        {
            logger.error(e, "扫码关注发送图文消息出错");
        }

        return returnvaleue;
    }

    /**
     * 回复图文信息
     * 
     * @param channelNo
     * @param toUserName
     * @param fromUserName
     * @param authorizerAppid
     *            公众号appid
     * @param lotteryId
     *            活动id
     * @return
     */
    private String processPicTextMsg2(String toUserName, String fromUserName, QrcodeModel model)
    {
        String returnvaleue = "";
        try
        {
            // 回复图文消息
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            // newsMessage.setFuncFlag(0);

            List<Article> articleList = JSON.parseArray(model.getArticlesContent(), Article.class);

            // Article article1 = new Article();
            // article1.setTitle(title);
            // article1.setDescription("点击这里，免费领券！");
            // article1.setPicUrl(picUrl);
            // article1.setUrl(url);
            // articleList.add(article1);

            // 设置项目个数，最多8个
            newsMessage.setArticleCount(articleList.size());
            newsMessage.setArticles(articleList);

            String respMessage = MessageUtil.newsMessageToXml(newsMessage);
            Long createTime = Calendar.getInstance().getTimeInMillis()/1000;
            String componentToken = ticketService.getComponentToken();
            String encodingaeskey = ticketService.getComponentEncodingaeskey();
            String componentAppid = ticketService.getComponentAppid();
            // 加密
            WXBizMsgCrypt pc = new WXBizMsgCrypt(componentToken, encodingaeskey, componentAppid);
            returnvaleue = pc.encryptMsg(respMessage, createTime+"", "demotestwujf");

        }
        catch(AesException e)
        {
            logger.error(e, "扫码关注发送图文消息出错");
        }

        return returnvaleue;
    }

    /**
     * 工具类：回复微信服务器"文本消息"
     * 
     * @param response
     * @param returnvaleue
     */
    private void output(HttpServletResponse response, String returnvaleue)
    {
        try
        {
            userService.close();// 关闭数据库
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.write(returnvaleue);
            pw.flush();
        }
        catch(IOException e)
        {
            logger.error(e, "***输出信息出错");
        }
    }

}
