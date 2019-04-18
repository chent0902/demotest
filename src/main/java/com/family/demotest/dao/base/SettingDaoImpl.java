package com.family.demotest.dao.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.entity.BaseSettingModel;
import com.family.demotest.vo.base.BaseConfigVo;

/**
 * 
 * @author wujf
 *
 */
@Repository("base.setting.dao")
public class SettingDaoImpl
    extends
    DaoSupport<BaseSettingModel>
    implements
    SettingDao
{

    private static final String FIND_BY_CHANNEL_NO = " o.channelNo=? and "+UN_DELETE;

    @Autowired
    private Validator validator;

    @Override
    protected Class<BaseSettingModel> getModelClass()
    {
        return BaseSettingModel.class;
    }

    @Override
    public PageList<BaseSettingModel> getPageList(BaseConfigVo model, int pageSize, int page)
    {

        StringBuilder where = new StringBuilder(" 1=1");
        List<Object> args = new ArrayList<Object>();

        if(!validator.isBlank(model.getCompanyName()))
        {
            where.append(" and ( o.cityName like ? or o.companyName like ? or o.account like ?  ) ");
            args.add("%"+model.getCompanyName()+"%");
            args.add("%"+model.getCompanyName()+"%");
            args.add("%"+model.getCompanyName()+"%");

        }

        where.append(" and o.channelNo!='1000' and ").append(UN_DELETE);

        PageList<BaseSettingModel> pageList = list(newQueryBuilder().where(where.toString()).orderBy(" o.createTime desc ").setCacheEnable(true),
            args.toArray(), pageSize, page);

        // 总页数
        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return null;
        }
        return pageList;
    }

    @Override
    public BaseSettingModel findByChannelNo(String channelNo)
    {
        return findOne(newQueryBuilder().where(FIND_BY_CHANNEL_NO).setCacheEnable(true), new Object[]{channelNo});
    }

}
