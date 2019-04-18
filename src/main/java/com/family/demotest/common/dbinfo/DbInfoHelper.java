package com.family.demotest.common.dbinfo;




import com.family.base01.dao.DbInfo;

/**
 * 获取数据库信息
 * 
 * @author wujf
 */

public class DbInfoHelper
{
	
    private static final DbInfoHelper instance = new DbInfoHelper();

    private String url;
    private String userName;
    private String password;



    public static DbInfoHelper getInstnace() {
        return instance;
    }

    /**
     * 获取数据库配置
     * 
     * @param dbConfig
     * @return
     */
    public DbInfo getDbInfo(DbConfig dbConfig)
    {
        if(dbConfig!=null)
        {
            url = PropertiesUtil.getText(dbConfig.getUrl());
            userName = PropertiesUtil.getText(dbConfig.getUserName());
            password = PropertiesUtil.getText(dbConfig.getPassword());
            return new DbInfo(url, userName, password);
        }
        return null;
    }
}
