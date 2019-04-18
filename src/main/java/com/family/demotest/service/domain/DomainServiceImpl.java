package com.family.demotest.service.domain;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.demotest.common.constant.Constants;
import com.family.demotest.common.nosql.redis.DefaultRedisOperate;
import com.family.demotest.dao.domain.DomainDao;
import com.family.demotest.entity.DomainModel;

/**
 * 
 * @author wujf
 *
 */
@Service("domain.service")
public class DomainServiceImpl
    extends
    ServiceSupport<DomainModel>
    implements
    DomainService
{

    @Autowired
    private DomainDao domainDao;
    @Autowired
    private DefaultRedisOperate<String, DomainModel> redisOperate;

    @Value("${demotest.default.domain.url}")
    private String defaultDomainUrl;
    @Value("${demotest.default.business.domain.url}")
    private String defaultBusinessDomainUrl;
    @Value("${demotest.h5.site.name}")
    private String siteName;

    @Override
    public DomainModel findByChannelNo(String channelNo)
    {
        DomainModel model = redisOperate.get(Constants.DOMAIN_SETTING_KEY+channelNo);
        if(model==null)
        {
            model = domainDao.findByChannelNo(channelNo);
            if(model!=null&&!model.getChannelNo().equals(""))
            {
                redisOperate.set(Constants.DOMAIN_SETTING_KEY+channelNo, model, 30, TimeUnit.DAYS);
            }

        }
        return model;

    }

    @Override
    public void putRedis(String key, DomainModel model)
    {
        redisOperate.set(Constants.DOMAIN_SETTING_KEY+key, model, 30, TimeUnit.DAYS);
    }

    @Override
    protected QueryDao<DomainModel> getQueryDao()
    {

        return domainDao;
    }

    @Override
    protected SaveDao<DomainModel> getSaveDao()
    {

        return domainDao;
    }

    @Override
    protected DeleteDao<DomainModel> getDeleteDao()
    {

        return domainDao;
    }

    @Override
    public String getDomainUrl(String channelNo)
    {
        StringBuffer domainUrl = new StringBuffer("http://");
        DomainModel model = this.findByChannelNo(channelNo);
        if(model!=null)
        {
            domainUrl.append(model.getName());
        }
        else
        {
            domainUrl.append(defaultDomainUrl);
        }
        domainUrl.append("/").append(siteName);

        return domainUrl.toString();
    }

    @Override
    public String getBusinessDomainUrl(String channelNo)
    {

        StringBuffer domainUrl = new StringBuffer("http://");
        DomainModel model = this.findByChannelNo(channelNo);
        if(model!=null)
        {
            domainUrl.append(model.getName1());
        }
        else
        {
            domainUrl.append(defaultBusinessDomainUrl);
        }
        domainUrl.append("/").append(siteName);

        return domainUrl.toString();

    }

}
