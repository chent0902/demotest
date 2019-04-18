package com.family.demotest.service.lottery;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.IntegerRedisBiz;
import com.family.demotest.common.constant.Constants;
import com.family.demotest.dao.lottery.LotteryAwardsDao;
import com.family.demotest.entity.LotteryAwardsModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
@Service("lottery.awards.service")
public class LotteryAwardsServiceImpl
    extends
    ServiceSupport<LotteryAwardsModel>
    implements
    LotteryAwardsService
{

    @Autowired
    private LotteryAwardsDao lotteryAwardsDao;
    @Autowired
    private Logger logger;
    @Autowired
    private Validator validator;
    @Autowired
    private IntegerRedisBiz integerRedisBiz;

    @Override
    protected QueryDao<LotteryAwardsModel> getQueryDao()
    {
        return lotteryAwardsDao;
    }

    @Override
    protected SaveDao<LotteryAwardsModel> getSaveDao()
    {
        return lotteryAwardsDao;
    }

    @Override
    protected DeleteDao<LotteryAwardsModel> getDeleteDao()
    {
        return lotteryAwardsDao;
    }

    @Override
    public ResultCode list(String channelNo, String lotteryId)
    {
        ResultCode result = new ResultCode();
        try
        {
            List<LotteryAwardsModel> list = lotteryAwardsDao.list(channelNo, lotteryId);
            if(list!=null&&list.size()>0)
            {
                for(LotteryAwardsModel model : list)
                {
                    Integer stock = integerRedisBiz.get(getLotteryAwardsKey(model.getId()));
                    if(stock!=null)
                    {
                        model.setStock(stock);
                    }
                }
            }
            result.setData(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取奖项列表出错");
            logger.error(e, "获取奖项列表出错");
        }
        return result;
    }

    @Override
    public ResultCode saveOrUpdate(List<LotteryAwardsModel> list)
    {
        ResultCode result = new ResultCode();
        LotteryAwardsModel instance = null;
        try
        {
            for(LotteryAwardsModel model : list)
            {
                int grow = 0; // 库存增量
                if(validator.isBlank(model.getId()))
                {
                    instance = new LotteryAwardsModel();
                    instance.setChannelNo(model.getChannelNo());
                    instance.setLotteryId(model.getLotteryId());
                    instance.setGrade(model.getGrade());
                    instance.setCreateTime(new Date());
                    // -1不限量
                    if(model.getNum()!=-1)
                    {
                        grow = model.getNum();
                    }
                }
                else
                {
                    instance = this.findById(model.getId());

                    // 原来不限量
                    // if(instance.getNum()==-1)
                    // {
                    // // 原来不限量
                    // grow = model.getNum();
                    // }
                    // else
                    // {
                    // grow = model.getNum()-instance.getNum();
                    // }

                    if(model.getNum()!=-1)
                    {
                        if(instance.getNum()!=-1)
                        {
                            grow = model.getNum()-instance.getNum();
                        }
                        else
                        {
                            grow = model.getNum();
                        }

                    }

                }
                instance.setName(model.getName());
                instance.setPrice(model.getPrice());
                instance.setNum(model.getNum());
                instance.setLuckyNum(model.getLuckyNum());
                instance.setMinTimes(model.getMinTimes());// 最少抽奖次数
                instance.setRate(model.getRate());
                this.save(instance);

                // 库存不限量
                if(instance.getNum()==-1)
                {
                    integerRedisBiz.put(getLotteryAwardsKey(instance.getId()), 0, 90, TimeUnit.DAYS);
                }
                else
                {
                    if(grow!=0)
                    {
                        int stock = 0;
                        if(integerRedisBiz.get(getLotteryAwardsKey(instance.getId()))!=null)
                        {
                            stock = integerRedisBiz.get(getLotteryAwardsKey(instance.getId()));
                        }
                        stock += grow;
                        if(stock<0)
                        {
                            stock = 0;
                        }
                        integerRedisBiz.put(getLotteryAwardsKey(instance.getId()), stock, 90, TimeUnit.DAYS);
                    }
                }
            }

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存奖项出错");
            logger.error(e, "保存奖项出错");
        }

        return result;
    }

    @Override
    public LotteryAwardsModel getlowestPrice(String channelNo, String lotteryId)
    {
        return lotteryAwardsDao.getlowestPrice(channelNo, lotteryId);
    }

    @Override
    public List<LotteryAwardsModel> getList(String channelNo, String lotteryId)
    {

        return lotteryAwardsDao.list(channelNo, lotteryId);
    }

    @Override
    public LotteryAwardsModel findByGrade(String lotteryId, int answer)
    {
        return lotteryAwardsDao.findByGrade(lotteryId, answer);
    }

    @Override
    public String getLotteryAwardsKey(String awardsId)
    {
        return new StringBuilder().append(Constants.LOTTERY_AWARDS_STOCK_KEY).append('-').append(awardsId).toString();
    }
}
