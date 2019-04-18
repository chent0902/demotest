package com.family.demotest.common.enumtype;

/**
 * 充值短信套餐类型
 * 
 * @author WUJF
 */
public enum SmsPackageType
{

    SMS50(1, 5000, "50元1000条短信", 1000), SMS100(2, 10000, "100元2200条短信", 2200), SMS500(3, 50000, "500元12000条短信", 12000);

    private final int code; // 编号
    private final int price;// 价格(分)
    private final String msg;// 说明
    private final int num; // 短信条数

    private SmsPackageType(int code, int price, String msg, int num)
    {
        this.code = code;
        this.price = price;
        this.msg = msg;
        this.num = num;
    }

    public int getCode()
    {
        return code;
    }

    public int getPrice()
    {
        return price;
    }

    public String getMsg()
    {
        return msg;
    }

    public int getNum()
    {
        return num;
    }

    public static SmsPackageType validate(int code)
    {
        for(SmsPackageType type : SmsPackageType.values())
        {
            if(type.getCode()==code)
                return type;
        }

        return SMS50;
    }
}
