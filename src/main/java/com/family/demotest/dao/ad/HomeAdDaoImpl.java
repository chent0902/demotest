package com.family.demotest.dao.ad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.HomeAdModel;

/**
 * 
 * @author wujf
 *
 */
@Repository("home.ad.dao")
public class HomeAdDaoImpl
    extends
    DaoSupport<HomeAdModel>
    implements
    HomeAdDao
{

    private static final String LIST_BY_CHANNEL = " o.channelNo=? and o.type=? and  "+UN_DELETE;
    private static final String ORDER_BY = " o.sort desc";

    @Override
    protected Class<HomeAdModel> getModelClass()
    {
        return HomeAdModel.class;
    }

    @Override
    public List<HomeAdModel> getList(String channelNo, int type)
    {

        return list(newQueryBuilder().where(LIST_BY_CHANNEL).orderBy(ORDER_BY).setCacheEnable(true), new Object[]{channelNo, type}, Integer.MAX_VALUE, 1)
                .getList();
    }

    @Override
    public PageList<HomeAdModel> getPageList(HomeAdModel model, int pageSize, int page)
    {

        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<Object>();

        where.append(" o.channelNo=? and o.property=0 ");
        args.add(model.getChannelNo());

        PageList<HomeAdModel> pageList = list(newQueryBuilder().where(where.toString()).orderBy(" o.sort desc").setCacheEnable(true), args.toArray(), 100, 1);

        // 总页数
        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return null;
        }
        return pageList;
    }

}
