package com.family.demotest.common.constant;

/**
 * @author wujf
 */
public class Constants
{

    // 时间格式
    public static final String DATE_FORMAT_MONTH = "yyyyMM";
    public static final String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String TIME_FORMAT_MINUTE = "yyyy-MM-dd HH:mm";

    // 阿里云OSS目录
    public static final String OSS_DIR = "xcx/";

    // 第三方凭据key
    public static final String demotest_COMPONENT_TICKET = "demotest.component.ticket.key-";
    // 第三方令牌
    public static final String demotest_ACCESS_TOKEN = "demotest.access.token.key-";
    // 授权方接口调用令牌
    public static final String CHANNEL_ACCESS_TOKEN = "demotest.channel.access.token.key-";

    // 微信js-sdk票据key
    public static final String demotest_JSAPI_TICKET = "demotest.jsapi.ticket.key-";

    // 保存用户sessionKey和openid
    public static final String WXUSER_SESSION_KEY = "demotest.wxuser.session.key-";
    // 用户生成带参二维码
    public static final String QRCODE_USER_KEY = "demotest.qrcode.user.key-";

    // 用户活动中奖统计
    public static final String USER_LOTTERY_TOTAL_KEY = "demotest.user.lottery.total.key-";

    // 抽奖活动数据
    public static final String LOTTERY_SUMMARY_KEY = "demotest.lottery.summary.key-";
    // 抽奖活动数据
    public static final String MESSAGE_SUMMARY_KEY = "demotest.message.summary.key-";
    // 图文数据
    public static final String IMG_TEXT_SUMMARY_KEY = "demotest.img.text.summary.key-";

    // 活动奖项库存key
    public static final String LOTTERY_AWARDS_STOCK_KEY = "demotest.lottery.awards.stock.key-";
    // 抽奖锁KEY
    public static final String LOCK_LOTTERY_KEY = "lock.lottery.key";
    // 用户生成带参二维码
    public static final String LOTTERY_HAIBAO_KEY = "demotest.lottery.haibao.key-";
    // 代理充值金额及活动数缓存key
    public static final String PARTNER_SUMMARY_KEY = "demotest.partner.summary.key-";

    /**
     * 微信授权回调页
     */
    public static final String WX_AUTH_CALL_URL = "wx.auth.call.key.";
    /**
     * 微信登录token
     */
    public static final String WX_LOGIN_INFO_TOKEN = "wx.login.info.token.";

    // 域名设置缓存key
    public static final String DOMAIN_SETTING_KEY = "domain.setting.key-";

    // 活动过期提醒 锁key
    public static final String LOCK_LOTTERY_EXPIRE_KEY = "lock.lottery.expire.key-";

}
