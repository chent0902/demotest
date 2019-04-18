package com.family.demotest.common.enumtype;

/**
 * 短信发送结果状态
 * 
 * @author WUJF
 */
public enum SmsStatus
{

    OK(0, "发送成功"), SMS01(1001, "短信提交失败，请重新提交或联系管理员"), SMS02(1002, "出现未知情况，请联系管理员"), SMS03(1003, "超出许可连接数"), SMS04(1004, "系统错误"), SMS09(1009,
            "用户名或者密码错误"), SMS12(1012, "充值条数不足"), SMS26(1026, "点对点超过最大提交量");

    private final int code;
    private final String msg;

    private SmsStatus(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public int getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }

    public static SmsStatus validate(int code)
    {
        for(SmsStatus status : SmsStatus.values())
        {
            if(status.getCode()==code)
                return status;
        }

        return SMS04;
    }

}
