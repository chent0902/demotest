package com.family.demotest.common.enumtype;

/**
 * 
 * @author wujf
 */
public enum QrcodeSource
{

    // wujfqrcode-自定义带参，wujfuserid-抽奖带参
    QRCODE("wujfqrcode"), USERID("wujfuserid");

    private String code;

    private QrcodeSource(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
}
