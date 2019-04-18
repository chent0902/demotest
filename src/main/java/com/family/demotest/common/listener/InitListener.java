package com.family.demotest.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.family.demotest.common.notify.NotifyManager;

public class InitListener
    implements
    ServletContextListener
{

    @Override
    public void contextInitialized(ServletContextEvent arg0)
    {

        NotifyManager.instance.init();

    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {

        NotifyManager.instance.shutdown();
        System.out.println("CpPnConfigManager destory success");
    }

}
