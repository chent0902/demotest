package com.family.demotest.common.dbinfo;



import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.family.base01.util.BeanFactory;
import com.family.base01.util.Logger;

/**
 * 获取资源文件
 * 
 * @author wujf
 */
public class PropertiesUtil
{
    private static final String PROPERTIES_FILE_NAME[] = {"database.config.properties"};
    private static Properties properties;

    private static void readProperties()
    {
        properties = getProperties(PROPERTIES_FILE_NAME[0]);
    }

    private static Properties getProperties(String fileName)
    {
        Properties prop = new Properties();
        try
        {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            prop.load(in);
            in.close();
        }
        catch(IOException e)
        {
            BeanFactory.getBean(Logger.class).warn(e, "读取资源文件出错!");
        }
        return prop;
    }

    public static String getText(String key)
    {
        if(properties==null)
            readProperties();
        return properties.getProperty(key);
    }
}
