package com.family.demotest.dao.lottery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageInfoImpl;
import com.family.base01.util.PageList;
import com.family.base01.util.PageListImpl;
import com.family.demotest.entity.LotteryOrderModel;

/**
 * 
 * @author lm
 *
 */
@Repository("lottery.order.dao")
public class LotteryOrderDaoImpl
    extends DaoSupport<LotteryOrderModel>
    implements LotteryOrderDao
{

    private static final String FIND_BY_USECODE = " o.merchantId = ? AND o.usecode = ? AND o.isCheck = 0 AND o.property = 0 ";

    private static final String WAIT_USE = " o.channelNo = ? AND o.userId = ? AND o.isCheck = 0 AND o.closeTime >= ? AND o.property = 0 ";

    private static final String ADMIN_USER_LIST = " o.channelNo = ? AND o.userId = ? ";

    private static final String ORDER_BY = " o.createTime DESC ";

    private static final String CHECK_ORDER_BY = " o.checkTime DESC ";

    @Override
    protected Class<LotteryOrderModel> getModelClass()
    {
        return LotteryOrderModel.class;
    }

    @Override
    public List<LotteryOrderModel> userList(String where, Object[] args)
    {
        List<List<Object>> result = this.query(where, args);

        List<LotteryOrderModel> userList = new ArrayList<LotteryOrderModel>();
        LotteryOrderModel order = null;
        List<Object> list = null;
        if(result!=null&&result.size()>0)
        {
            for(int i = 0; i<result.size(); i++)
            {
                list = result.get(i);
                if(list!=null&&list.size()>0)
                {
                    order = new LotteryOrderModel();
                    order.setId(list.get(0)==null?"":list.get(0).toString());
                    order.setLotteryId(list.get(1)==null?"":list.get(1).toString());
                    order.setMerchantId(list.get(2)==null?"":list.get(2).toString());
                    order.setUserId(list.get(3)==null?"":list.get(3).toString());
                    order.setAwardsId(list.get(4)==null?"":list.get(4).toString());
                    order.setGrade(list.get(5)==null?0:Integer.valueOf(list.get(5).toString()));
                    order.setUsecode(list.get(6)==null?"":list.get(6).toString());
                    order.setQrcode(list.get(7)==null?"":list.get(7).toString());
                    order.setIsCheck(list.get(8)==null?0:Integer.valueOf(list.get(8).toString()));
                    order.setCheckTime(list.get(9)==null?null:(Date)list.get(9));
                    order.setCreateTime(list.get(10)==null?null:(Date)list.get(10));
                    order.setMerchantName(list.get(11)==null?"":list.get(11).toString());
                    order.setCloseTime(list.get(12)==null?null:(Date)list.get(12));
                    order.setAwardsName(list.get(13)==null?"":list.get(13).toString());
                    userList.add(order);
                }
            }
        }
        return userList;
    }

    @Override
    public LotteryOrderModel findByUsecode(String merchantId, String usecode)
    {
        return this.findOne(newQueryBuilder().where(FIND_BY_USECODE), new Object[]{merchantId, usecode});
    }

    @Override
    public PageList<LotteryOrderModel> checkList(String where, Object[] args, int page, int pageSize)
    {
        PageList<LotteryOrderModel> pageList = list(newQueryBuilder().where(where).orderBy(CHECK_ORDER_BY), args, pageSize, page);

        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return null;
        }

        return pageList;
    }

    @Override
    public int waitUse(String channelNo, String userId)
    {
        return (int)this.count(newQueryBuilder().where(WAIT_USE), new Object[]{channelNo, userId, new Date()});
    }

    @Override
    public PageList<LotteryOrderModel> adminUserList(String channelNo, String userId, int page, int pageSize)
    {
        PageList<LotteryOrderModel> pageList = list(newQueryBuilder().where(ADMIN_USER_LIST).orderBy(ORDER_BY), new Object[]{channelNo, userId}, pageSize,
            page);

        int totalPage = pageList.getPageInfo().getTotalPage();

        if(page>totalPage)
        {
            return null;
        }

        return pageList;
    }

    @Override
    public PageList<LotteryOrderModel> list(StringBuffer sql, StringBuffer count, List<Object> args, int currentPage, int pageSize)
    {
        int totalRecord = Integer.valueOf(query(count.toString(), args.toArray()).get(0).get(0).toString());
        int page = (currentPage-1)*pageSize;
        args.add(page);
        args.add(pageSize);
        List<List<Object>> result = this.query(sql.toString(), args.toArray());
        List<LotteryOrderModel> orderList = new ArrayList<LotteryOrderModel>();
        LotteryOrderModel order = null;
        if(result!=null&&result.size()>0)
        {
            List<Object> list = null;
            for(int i = 0; i<result.size(); i++)
            {
                list = result.get(i);
                if(list!=null)
                {
                    order = new LotteryOrderModel();
                    order.setId(list.get(0).toString());
                    order.setGrade(list.get(1)==null?0:Integer.valueOf(list.get(1).toString()));
                    order.setCreateTime(list.get(2)==null?null:(Date)list.get(2));
                    order.setCheckTime(list.get(3)==null?null:(Date)list.get(3));
                    order.setNickName(list.get(4)==null?"":list.get(4).toString());
                    order.setTel(list.get(5)==null?"":list.get(5).toString());
                    order.setIsCheck(list.get(6)==null?0:Integer.valueOf(list.get(6).toString()));
                    orderList.add(order);
                }
            }
        }

        PageList<LotteryOrderModel> pageList = new PageListImpl<LotteryOrderModel>(new PageInfoImpl(totalRecord, pageSize, currentPage, 0), orderList);

        return pageList;
    }

    @Override
    public int count(String where, Object[] args)
    {
        return (int)this.count(newQueryBuilder().where(where), args);
    }

    // 用户活动到期提醒
    private static final String LIST_BY_LOTTERYID_SQL = "SELECT t.c_user_id , t1.c_openid FROM t_lottery_order t LEFT JOIN t_wx_user t1 ON t.c_user_id = t1.c_id WHERE t.c_channel_no = ? AND t.c_lottery_id = ? AND t.c_is_check = 0 AND t.c_property = 0 GROUP BY t.c_user_id; ";

    @Override
    public List<LotteryOrderModel> getListByLotteryId(String channelNo, String lotteryId)
    {

        List<List<Object>> result = this.query(LIST_BY_LOTTERYID_SQL, new Object[]{channelNo, lotteryId});
        List<LotteryOrderModel> orderList = new ArrayList<LotteryOrderModel>();
        LotteryOrderModel model = null;
        if(result!=null&&result.size()>0)
        {
            List<Object> list = null;
            for(int i = 0; i<result.size(); i++)
            {
                list = result.get(i);
                if(list!=null)
                {
                    model = new LotteryOrderModel();
                    model.setUserId(list.get(0).toString());
                    model.setOpenId(list.get(1)==null?"":list.get(1).toString());
                    orderList.add(model);
                }
            }
        }

        return orderList;

    }

    private static final String NEW_LIST_SQL = "SELECT t.c_user_id , t.c_grade , t1.c_nick_name FROM t_lottery_order t LEFT JOIN t_wx_user t1 ON t.c_user_id = t1.c_id WHERE t.c_channel_no = ? AND t.c_lottery_id = ? AND t.c_property = 0 ORDER BY t.c_create_time LIMIT 8 ;";

    @Override
    public List<LotteryOrderModel> getNewList(String channelNo, String lotteryId)
    {
        List<List<Object>> result = this.query(NEW_LIST_SQL, new Object[]{channelNo, lotteryId});
        List<LotteryOrderModel> orderList = new ArrayList<LotteryOrderModel>();
        LotteryOrderModel model = null;
        if(result!=null&&result.size()>0)
        {
            List<Object> list = null;
            for(int i = 0; i<result.size(); i++)
            {
                list = result.get(i);
                if(list!=null)
                {
                    model = new LotteryOrderModel();
                    model.setUserId(list.get(0).toString());
                    model.setGrade(Integer.parseInt(list.get(1).toString()));
                    model.setNickName(list.get(2)==null?"":list.get(2).toString());
                    orderList.add(model);
                }
            }
        }

        return orderList;

    }

}
