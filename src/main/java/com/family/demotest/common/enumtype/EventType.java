package com.family.demotest.common.enumtype;

/**
 * 事件类型
 * 
 * @author wujf
 *
 * @date 2014-8-21
 */
public enum EventType
{

    SUBSCRIBE("subscribe"), UNSUBSCRIBE("unsubscribe"), CLICK("CLICK"), VIEW("view"), SCAN("SCAN");

    private String code;

    private EventType(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
}
