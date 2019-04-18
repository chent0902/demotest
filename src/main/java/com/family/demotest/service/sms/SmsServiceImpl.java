package com.family.demotest.service.sms;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.family.base01.util.Http;
import com.family.base01.util.Logger;
import com.family.base01.util.Validator;
import com.family.demotest.common.nosql.redis.DefaultRedisOperate;
import com.family.demotest.vo.sms.SmsResult;
import com.family.demotest.web.util.Base64;
import com.family.demotest.web.util.MD5Util;
import com.family.demotest.web.util.ResultCode;

@Service("sms.service")
public class SmsServiceImpl
    implements SmsService
{

    // 短信接口地址
    public static final String SMS_SERVER_URL = "http://122.152.192.131:28107";
    public static final String USERNAME = "ctyy";
    public static final String PASSWORD = "ctyy0731";

    // 验证码缓存key
    private static final String RESISTER_SMS_CODE_KEY = "register.sms.phone-";

    private static final String SMS_CONTENT = "尊敬的用户：您的验证码：%s，工作人员不会索取，请勿泄漏。";

    @Autowired
    private Http http;
    @Autowired
    private Validator validator;
    @Autowired
    private Logger logger;
    @Autowired
    private DefaultRedisOperate<String, String> redisOperate;

    @Override
    public SmsResult sendSms(String tel, String content)
    {
        try
        {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("Action", "sendsms");
            params.put("UserName", USERNAME);
            params.put("Password", MD5Util.getMD5String(PASSWORD).toUpperCase());
            params.put("Mobile", tel);
            params.put("Message", Base64.encode(content.getBytes()));

            String resJson = http.postByContent(SMS_SERVER_URL, JSON.toJSONString(params));
            if(validator.isBlank(resJson))
                return null;

            SmsResult smsResult = JSON.toJavaObject(JSON.parseObject(resJson), SmsResult.class);
            if(null==smsResult)
                return null;

            return smsResult;
        }
        catch(Exception e)
        {
            logger.error(e, "发送短信验证码出错");
        }

        return null;

    }

    @Override
    public String generateCode()
    {
        // 验证码
        String vcode = "";
        for(int i = 0; i<4; i++)
        {
            vcode = vcode+(int)(Math.random()*9);
        }
        return vcode;
    }

    @Override
    public ResultCode getSmsCode(String mobile)
    {
        ResultCode result = new ResultCode();
        String vcode = this.generateCode();
        // String content = "您的验证码为: "+ vcode + "，有效期为60秒";
        String content = String.format(SMS_CONTENT, vcode);
        SmsResult smsResult = this.sendSms(mobile, content);
        if(smsResult.getStatus()==0)
        {
            // 发送成功
            setSMSCode(mobile, vcode);// 把验证码放到缓存中
        }
        else
        {
            result.setCode(-1);
        }

        result.setInfo(smsResult.getStatus()+":"+smsResult.getStatusMsg());

        return result;
    }

    @Override
    public void setSMSCode(String mobile, String vcode)
    {
        redisOperate.set(RESISTER_SMS_CODE_KEY+mobile, vcode, 3, TimeUnit.MINUTES);

    }

    @Override
    public String getSMSCode(String mobile)
    {
        return redisOperate.get(RESISTER_SMS_CODE_KEY+mobile);
    }

}
