package com.family.demotest.dao.lottery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.Converter;
import com.family.base01.util.PageList;
import com.family.demotest.entity.LotteryModel;
import com.family.demotest.web.util.DateUtils;

/**
 * 
 * @author lm
 *
 */
@Repository("lottery.dao")
public class LotteryDaoImpl
    extends
    DaoSupport<LotteryModel>
    implements
    LotteryDao
{

    private static final String NAME_LIST = " SELECT c_id,c_title FROM t_lottery WHERE c_channelNo = ? AND c_merchant_id = ? AND c_property = 0 ORDER BY c_sort DESC ";

    private static final String ORDER_BY = " o.sort DESC, o.createTime DESC ";

    @Autowired
    private Converter converter;

    @Override
    protected Class<LotteryModel> getModelClass()
    {
        return LotteryModel.class;
    }

    @Override
    public PageList<LotteryModel> pageList(String where, Object[] args, int page, int pageSize)
    {
        PageList<LotteryModel> pageList = list(newQueryBuilder().where(where).orderBy(ORDER_BY), args, pageSize, page);

        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return null;
        }

        return pageList;
    }

    @Override
    public List<LotteryModel> nameList(String channelNo, String merchantId)
    {
        List<LotteryModel> nameList = new ArrayList<LotteryModel>();
        LotteryModel model = null;
        List<Object> list = null;
        List<List<Object>> result = query(NAME_LIST, new Object[]{channelNo, merchantId});
        if(result!=null&&result.size()>0)
        {
            for(int i = 0; i<result.size(); i++)
            {
                list = result.get(i);
                if(list!=null&&list.size()>0)
                {
                    model = new LotteryModel();
                    model.setId(list.get(0).toString());
                    model.setTitle(list.get(1).toString());
                    nameList.add(model);
                }

            }
        }
        return nameList;
    }

    @Override
    public int count(String where, Object[] args)
    {
        return (int)this.count(newQueryBuilder().where(where), args);
    }

    // 活动到期提醒
    private static final String EXPIRE_LIST_SQL = "SELECT t.c_id , t.c_channel_no , t.c_title , t.c_close_time , t1.c_name FROM t_lottery t LEFT JOIN t_merchant t1 ON t.c_merchant_id = t1.c_id WHERE t.c_online = 1 AND t.c_property = 0 AND DATE_FORMAT(t.c_close_time , '%Y%m%d') = ? ORDER BY t.c_channel_no ASC; ";

    @Override
    public List<LotteryModel> getExpireList()
    {

        // 加3天
        Date date = DateUtils.addDay(new Date(), 3);
        String dateStr = converter.toString(date, "yyyyMMdd");
        // String dateStr = converter.toString(date, "20180820");

        List<List<Object>> result = this.query(EXPIRE_LIST_SQL, new Object[]{dateStr});
        List<LotteryModel> orderList = new ArrayList<LotteryModel>();
        LotteryModel model = null;
        if(result!=null&&result.size()>0)
        {
            List<Object> list = null;
            for(int i = 0; i<result.size(); i++)
            {
                list = result.get(i);
                if(list!=null)
                {
                    model = new LotteryModel();
                    model.setId(list.get(0).toString());
                    model.setChannelNo(list.get(1).toString());
                    model.setTitle(list.get(2).toString());
                    model.setCloseTime((Date)list.get(3));
                    model.setMerchantId(list.get(4)==null?"":list.get(4).toString());// 商户名称
                    orderList.add(model);
                }
            }
        }

        return orderList;

    }

    private static final String COUNT_LOTTERY_NUM = " o.channelNo = ? AND o.property = 0 ";

    @Override
    public int countLotteryNum(String channelNo)
    {
        return this.count(COUNT_LOTTERY_NUM, new Object[]{channelNo});
    }

    private static final String LIST_BY_MERCHANT = " SELECT c_id , c_channel_no , c_title FROM t_lottery WHERE c_merchant_id =? AND c_property = 0 ";

    @Override
    public List<LotteryModel> getLotteryList(String merchantId)
    {

        List<List<Object>> result = this.query(LIST_BY_MERCHANT, new Object[]{merchantId});
        List<LotteryModel> orderList = new ArrayList<LotteryModel>();
        LotteryModel model = null;
        if(result!=null&&result.size()>0)
        {
            List<Object> list = null;
            for(int i = 0; i<result.size(); i++)
            {
                list = result.get(i);
                if(list!=null)
                {
                    model = new LotteryModel();
                    model.setId(list.get(0).toString());
                    model.setChannelNo(list.get(1).toString());
                    model.setTitle(list.get(2).toString());
                    orderList.add(model);
                }
            }
        }

        return orderList;

    }

}
