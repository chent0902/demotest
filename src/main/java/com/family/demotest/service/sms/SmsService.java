package com.family.demotest.service.sms;

import com.family.demotest.vo.sms.SmsResult;
import com.family.demotest.web.util.ResultCode;

public interface SmsService
{

    /**
     * 发送短信
     * 
     * @param tel
     * @param content
     */
    public SmsResult sendSms(String tel, String content);

    /**
     * 生成验证码
     * 
     * @return
     */
    public String generateCode();

    /**
     * 获取短信验证码
     * 
     * @param phone
     * @return
     */
    public ResultCode getSmsCode(String phone);

    /**
     * 设置当前注册用户的验证码
     * 
     * @param phone
     * @param vcode
     */
    public void setSMSCode(String phone, String vcode);

    /**
     * 获取用户的验证码
     * 
     * @param phone
     */
    public String getSMSCode(String phone);

}
