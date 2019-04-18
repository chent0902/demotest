package com.family.demotest.dao.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.vo.base.BaseConfigVo;

/**
 * 
 * @author wujf
 *
 */
@Repository("base.config.dao")
public class ConfigDaoImpl
    extends DaoSupport<BaseConfigModel>
    implements ConfigDao
{

    private static final String MAX_CODE = " select max(c_channel_no) from t_base_config where c_property=0";
    private static final String START_CODE = "1001"; // 起始code值

    private static final String CONFIG_BY_CHANNEL_NO = " o.channelNo=? and "+UN_DELETE;
    private static final String CONFIG_BY_APPID = " o.appid=? and "+UN_DELETE;

    private static final String LIST_ALL = " SELECT c_id,c_channel_no from t_base_config where c_property=0 ORDER BY c_channel_no asc;";

    @Autowired
    private Validator validator;

    @Override
    protected Class<BaseConfigModel> getModelClass()
    {
        return BaseConfigModel.class;
    }

    @Override
    public PageList<BaseConfigModel> getPageList(BaseConfigVo model, int pageSize, int page)
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

        PageList<BaseConfigModel> pageList = list(newQueryBuilder().where(where.toString()).orderBy(" o.createTime desc ").setCacheEnable(true),
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
    public String maxCode()
    {
        List<List<Object>> list = super.query(MAX_CODE, new Object[0]);
        if(validator.isEmpty(list)||validator.isEmpty(list.get(0))||validator.isEmpty(list.get(0).get(0)))
            return START_CODE;
        return list.get(0).get(0).toString();
    }

    @Override
    public BaseConfigModel findByChannelNo(String channelNo)
    {

        return findOne(newQueryBuilder().where(CONFIG_BY_CHANNEL_NO).setCacheEnable(true), new Object[]{channelNo});
    }

    @Override
    public BaseConfigModel findByAppid(String appid)
    {
        return findOne(newQueryBuilder().where(CONFIG_BY_APPID).setCacheEnable(true), new Object[]{appid});
    }

    @Override
    public List<BaseConfigModel> getListAll()
    {

        List<List<Object>> result = this.query(LIST_ALL, new Object[]{});
        List<BaseConfigModel> configList = new ArrayList<BaseConfigModel>();
        BaseConfigModel model = null;
        if(result!=null&&result.size()>0)
        {
            List<Object> list = null;
            for(int i = 0; i<result.size(); i++)
            {
                list = result.get(i);
                if(list!=null)
                {
                    model = new BaseConfigModel();
                    model.setId(list.get(0).toString());
                    model.setChannelNo(list.get(1).toString());
                    configList.add(model);
                }
            }
        }

        return configList;

    }

}
