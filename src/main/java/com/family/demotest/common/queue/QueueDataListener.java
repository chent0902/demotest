package com.family.demotest.common.queue;





import java.util.Collection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.family.base01.dao.orm.Session;
import com.family.base01.util.BeanFactory;
import com.family.base01.util.Logger;



/**
 * 队列数据监听器。
 * 
 * @author wujf
 */
public class QueueDataListener
    implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        reload(BeanFactory.getBeans(QueueDataReloader.class));
    }

    protected void reload(Collection<QueueDataReloader> reloaders)
    {
        if(reloaders==null||reloaders.isEmpty())
            return;

        for(QueueDataReloader reloader : reloaders)
        {
            try
            {
                reloader.reload();
            }
            catch(Exception e)
            {
                BeanFactory.getBean(Logger.class).warn(e, "初始化实时数据至内存出错!"+reloader.getClass().getName());
            }
        }
        BeanFactory.getBean(Session.class).close();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
    }
}
