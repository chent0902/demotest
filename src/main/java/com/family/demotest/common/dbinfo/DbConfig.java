package com.family.demotest.common.dbinfo;




/**
 * 数据库配置
 * 
 * @author wujf
 */
public enum DbConfig
{
    pearl("pearl.database.url", "pearl.database.username", "pearl.database.password"), sub_pearl("pearl.sub.database.url", "pearl.sub.database.username",
            "pearl.sub.database.password");
    private String url;
    private String userName;
    private String password;

    private DbConfig(String url, String userName, String password)
    {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

}
