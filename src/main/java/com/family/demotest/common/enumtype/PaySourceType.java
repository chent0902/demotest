package com.family.demotest.common.enumtype;

public enum PaySourceType
{

    POINT(1, "活动点数"), SMS(2, "短信");

    private final int code;
    private final String name;

    private PaySourceType(int code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public int getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

}
