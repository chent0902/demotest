package com.family.demotest.dao.merchant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.MerchantModel;

/**
 * 
 * @author lim
 *
 */
@Repository("merchant.dao")
public class MerchantDaoImpl
    extends DaoSupport<MerchantModel>
    implements MerchantDao
{

    private static final String PAGE_LIST = " o.channelNo = ? AND o.property = 0 ";

    @Override
    protected Class<MerchantModel> getModelClass()
    {
        return MerchantModel.class;
    }

    @Override
    public PageList<MerchantModel> pageList(String channelNo, int page, int pageSize)
    {
        PageList<MerchantModel> pageList = list(newQueryBuilder().where(PAGE_LIST), new Object[]{channelNo}, pageSize, page);

        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return null;
        }
        return pageList;
    }

    @Override
    public List<MerchantModel> findAllName(StringBuffer sql, List<Object> args)
    {
        List<MerchantModel> mercList = new ArrayList<MerchantModel>();
        MerchantModel model = null;

        List<List<Object>> result = query(sql.toString(), args.toArray());
        if(result!=null&&result.size()>0)
        {
            List<Object> list = null;
            for(int i = 0; i<result.size(); i++)
            {
                list = result.get(i);
                if(list!=null&&list.size()>0)
                {
                    model = new MerchantModel();
                    model.setId(list.get(0).toString());
                    model.setName(list.get(1).toString());
                    mercList.add(model);
                }
            }
        }
        return mercList;
    }

}
