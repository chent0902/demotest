package com.family.demotest.service.lottery;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.IntegerRedisBiz;
import com.family.demotest.common.biz.LotterySummaryRedisBiz;
import com.family.demotest.common.biz.PartnerSummaryRedisBiz;
import com.family.demotest.common.biz.StringRedisBiz;
import com.family.demotest.common.biz.UserLotteryTotalRedisBiz;
import com.family.demotest.common.constant.Constants;
import com.family.demotest.common.nosql.redis.RedisLock;
import com.family.demotest.dao.lottery.LotteryDao;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.entity.LotteryAwardsModel;
import com.family.demotest.entity.LotteryDetailsModel;
import com.family.demotest.entity.LotteryModel;
import com.family.demotest.entity.LotteryOrderModel;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.service.oss.OssService;
import com.family.demotest.service.queue.templateMsg.TemplateMsgQueueService;
import com.family.demotest.service.user.UserService;
import com.family.demotest.vo.total.LotterySummaryVo;
import com.family.demotest.vo.total.PartnerDataTotalVo;
import com.family.demotest.vo.total.UserLotteryTotalVo;
import com.family.demotest.web.util.DateUtils;
import com.family.demotest.web.util.HaibaoUtil;
import com.family.demotest.web.util.OrderGenerateUtil;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
@Service("lottery.service")
public class LotteryServiceImpl
    extends
    ServiceSupport<LotteryModel>
    implements
    LotteryService
{

    private static final String[] Awards = {"specialNum", "oneNum", "twoNum", "threeNum", "fourNum"};

    private static final String DIR = "xcx/";
    @Autowired
    private LotteryDao lotteryDao;
    @Autowired
    private Logger logger;
    @Autowired
    private Validator validator;
    @Autowired
    private LotteryDetailsService detailsService;
    @Autowired
    private UserLotteryTotalRedisBiz userLotteryTotalRedisBiz;
    @Autowired
    private LotterySummaryRedisBiz summaryRedisBiz;
    @Autowired
    private LotteryAwardsService awardsService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private IntegerRedisBiz integerRedisBiz;
    @Autowired
    private UserService userService;
    @Autowired
    private LotteryOrderService lotteryOrderService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private OssService ossService;
    @Autowired
    private StringRedisBiz stringRedisBiz;
    @Autowired
    private PartnerSummaryRedisBiz partnerSummaryRedisBiz;

    @Autowired
    private TemplateMsgQueueService templateMsgQueueService;

    @Override
    protected QueryDao<LotteryModel> getQueryDao()
    {
        return lotteryDao;
    }

    @Override
    protected SaveDao<LotteryModel> getSaveDao()
    {
        return lotteryDao;
    }

    @Override
    protected DeleteDao<LotteryModel> getDeleteDao()
    {
        return lotteryDao;
    }

    @Override
    public ResultCode pageList(String channelNo, int type, String name, int pageSize, int page)
    {
        ResultCode result = new ResultCode();

        StringBuffer where = new StringBuffer(" o.channelNo = ? AND  o.property = 0 ");
        List<Object> args = new ArrayList<Object>();
        args.add(channelNo);
        // 0-全部 1-进行中 2-已结束 3-已截止
        switch(type)
        {
        case 1:
            where.append(" AND o.startTime <= ? AND o.endTime >= ?  ");
            args.add(new Date());
            args.add(new Date());
            break;
        case 2:
            where.append(" AND o.endTime <= ? AND o.closeTime >= ?  ");
            args.add(new Date());
            args.add(new Date());
            break;
        case 3:
            where.append(" AND o.closeTime <= ? ");
            args.add(new Date());
            break;
        default:
            break;
        }
        if(!validator.isBlank(name))
        {
            where.append(
                " AND ( o.title LIKE ? OR o.merchantId = ( SELECT t.id FROM com.family.demotest.entity.MerchantModel t WHERE t.id = o.merchantId AND t.property = 0 AND t.name LIKE ? ) ) ");
            args.add("%"+name+"%");
            args.add("%"+name+"%");
        }
        where.append(" ORDER BY o.sort DESC ");
        try
        {
            PageList<LotteryModel> pageList = lotteryDao.pageList(where.toString(), args.toArray(), page, pageSize);
            if(pageList!=null&&pageList.getList().size()>0)
            {
                for(LotteryModel model : pageList.getList())
                {
                    LotterySummaryVo vo = summaryRedisBiz.get(model.getId());
                    if(vo!=null)
                    {
                        model.setAttention(vo.getAttention());
                        model.setJoin(vo.getJoin());
                        model.setReceive(vo.getReceive());
                        model.setMessage(vo.getMessage());
                        model.setLike(vo.getLike());
                        model.setUseNum(vo.getUseNum());
                    }
                }
            }

            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载活动列表出错");
            logger.error(e, "加载活动列表出错");
        }

        return result;
    }

    @Override
    public ResultCode allList(int type, String name, int pageSize, int page)
    {
        ResultCode result = new ResultCode();

        StringBuffer where = new StringBuffer(" o.property = 0 ");
        List<Object> args = new ArrayList<Object>();
        // 0-全部 1-进行中 2-已结束 3-已截止
        switch(type)
        {
        case 1:
            where.append(" AND o.startTime <= ? AND o.endTime >= ?  ");
            args.add(new Date());
            args.add(new Date());
            break;
        case 2:
            where.append(" AND o.endTime <= ? AND o.closeTime >= ?  ");
            args.add(new Date());
            args.add(new Date());
            break;
        case 3:
            where.append(" AND o.closeTime <= ? ");
            args.add(new Date());
            break;
        default:
            break;
        }
        if(!validator.isBlank(name))
        {
            where.append(
                " AND ( o.title LIKE ? OR o.merchantId = ( SELECT t.id FROM com.family.demotest.entity.MerchantModel t WHERE t.id = o.merchantId AND t.property = 0 AND t.name LIKE ? ) ) ");
            args.add("%"+name+"%");
            args.add("%"+name+"%");
        }
        where.append(" ORDER BY o.sort DESC , o.createTime DESC ");
        try
        {
            PageList<LotteryModel> pageList = lotteryDao.pageList(where.toString(), args.toArray(), page, pageSize);
            if(pageList!=null&&pageList.getList().size()>0)
            {
                for(LotteryModel model : pageList.getList())
                {
                    LotterySummaryVo vo = summaryRedisBiz.get(model.getId());
                    if(vo!=null)
                    {
                        model.setAttention(vo.getAttention());
                        model.setReceive(vo.getReceive());
                        model.setMessage(vo.getMessage());
                        model.setLike(vo.getLike());
                        model.setUseNum(vo.getUseNum());
                    }
                }
            }

            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载活动列表出错");
            logger.error(e, "加载活动列表出错");
        }

        return result;
    }

    @Override
    public ResultCode saveOrUpdate(LotteryModel model)
    {
        ResultCode result = new ResultCode();
        try
        {
            LotteryModel instance = null;
            boolean insert = validator.isBlank(model.getId())?true:false;
            BaseConfigModel config = null;
            if(insert)
            {
                config = configService.findByChannelNo(model.getChannelNo());
                if(config==null)
                {
                    result.setCode(-1);
                    result.setInfo("渠道不存在");
                    return result;
                }
                if(config.getActivePoint()<1)
                {
                    result.setCode(-1);
                    result.setInfo("活动点不足,请先充值");
                    return result;
                }
                if(config.getActivePoint()<9999)
                {
                    config.setActivePoint(config.getActivePoint()-1);
                    configService.save(config);
                }

                instance = new LotteryModel();
                instance.setChannelNo(model.getChannelNo());
                instance.setMerchantId(model.getMerchantId());
                instance.setTemplate(model.getTemplate());
                instance.setOnline(1);
                instance.setCreateTime(new Date());
            }
            else
            {
                instance = this.findById(model.getId());
                if(instance==null)
                {
                    result.setCode(-1);
                    result.setInfo("参数错误");
                    return result;
                }
            }

            instance.setTitle(model.getTitle());
            instance.setImg(model.getImg());
            if(!validator.isBlank(model.getPosterImg()))
            {
                instance.setPosterImg(model.getPosterImg());
            }
            instance.setPrice(model.getPrice());
            instance.setMessageOpen(model.getMessageOpen());
            instance.setStartTime(model.getStartTime()==null?null:model.getStartTime());
            instance.setEndTime(model.getEndTime()==null?null:model.getEndTime());
            instance.setCloseTime(model.getCloseTime()==null?null:model.getCloseTime());
            instance.setSurplusOpen(model.getSurplusOpen());
            if(model.getSurplusOpen()==1)
            {
                Random random = new Random();
                instance.setVirtualSurplus(random.nextInt(16)+5);
            }

            instance.setHelpNum(model.getHelpNum());// -1表示不限制
            instance.setTemplate(model.getTemplate());// 0-大转盘，1-九宫格，2-翻牌
            instance.setOpenTel(model.getOpenTel());// 开启手机验证0-否，1-是
            this.save(instance);
            // 保存详情
            if(!validator.isBlank(model.getDetails()))
            {
                LotteryDetailsModel details = null;
                if(insert)
                {
                    details = new LotteryDetailsModel();
                    details.setChannelNo(instance.getChannelNo());
                    details.setLotteryId(instance.getId());
                    details.setDeatils(model.getDetails());
                    details.setCreateTime(new Date());
                }
                else
                {
                    details = detailsService.findByLotteryId(instance.getId());
                    if(details==null)
                    {
                        details = new LotteryDetailsModel();
                        details.setChannelNo(instance.getChannelNo());
                        details.setLotteryId(instance.getId());
                        details.setCreateTime(new Date());
                    }
                    details.setDeatils(model.getDetails());
                }
                detailsService.save(details);
            }
            // 修改活动数缓存
            if(insert)
            {
                PartnerDataTotalVo totalVo = partnerSummaryRedisBiz.get(config.getId());
                if(totalVo==null)
                {
                    totalVo = new PartnerDataTotalVo();
                    // int activityNum =
                    // this.countLotteryNum(config.getChannelNo());
                    // totalVo.setActivityNum(activityNum+1);
                }

                totalVo.setActivityNum(totalVo.getActivityNum()+1);

                partnerSummaryRedisBiz.put(config.getId(), totalVo, 360, TimeUnit.DAYS);
            }

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存活动信息出错");
            logger.error(e, "保存活动信息出错");
        }
        finally
        {
            lotteryDao.close();
        }
        return result;
    }

    @Override
    public void setUserLotteryTotalVo(String lotteryId, String userId, UserLotteryTotalVo totalVo)
    {
        String key = lotteryId+"-"+userId;
        userLotteryTotalRedisBiz.put(key, totalVo);

    }

    @Override
    public UserLotteryTotalVo getUserLotteryTotalVo(String lotteryId, String userId)
    {
        return userLotteryTotalRedisBiz.get(lotteryId+"-"+userId);
    }

    @Override
    public ResultCode list(String channelNo, int type, int page, int pageSize)
    {
        ResultCode result = new ResultCode();
        StringBuffer where = new StringBuffer(" o.channelNo = ? AND o.online = 1  AND o.property = 0 ");
        List<Object> args = new ArrayList<Object>();
        args.add(channelNo);
        // 0-火热进行中 1-已抢完
        if(type==1)
        {
            where.append(" AND o.endTime <= ? ");
            args.add(new Date());
        }
        else
        {
            where.append("AND o.endTime >= ? ");
            args.add(new Date());
        }
        try
        {
            PageList<LotteryModel> pageList = lotteryDao.pageList(where.toString(), args.toArray(), page, pageSize);
            if(pageList==null||pageList.getList().size()==0)
            {
                result.setData(new ArrayList<LotteryModel>());
                return result;
            }
            for(LotteryModel lottery : pageList.getList())
            {
                // 缓存数据
                LotterySummaryVo vo = summaryRedisBiz.get(lottery.getId());
                if(vo!=null)
                {
                    lottery.setAttention(vo.getAttention());
                    lottery.setJoin(vo.getJoin());
                    lottery.setReceive(vo.getReceive());
                    lottery.setMessage(vo.getMessage());
                    lottery.setLike(vo.getLike());
                    lottery.setUseNum(vo.getUseNum());
                }
                // 最低价
                LotteryAwardsModel awards = awardsService.getlowestPrice(channelNo, lottery.getId());
                lottery.setLowestPrice(awards==null||validator.isBlank(awards.getPrice())?"0.0":awards.getPrice());
            }
            result.setData(pageList.getList());
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取活动列表出错");
            logger.error(e, "获取活动列表出错");
        }

        return result;
    }

    /**
     * 获取默认赠送一次机会key
     * 
     * @param lotteryId
     * @param userId
     * @return
     */
    private String getGiveKey(String lotteryId, String userId)
    {
        return new StringBuilder().append("give.").append(lotteryId).append(userId).toString();
    }

    @Override
    public ResultCode getInfo(String lotteryId, String userId)
    {
        ResultCode result = new ResultCode();
        try
        {
            LotteryModel lottery = this.findById(lotteryId);
            if(lottery==null||lottery.getProperty()==1)
            {
                result.setCode(-1);
                result.setInfo("活动不存在");
                return result;
            }

            if(lottery.getOnline()==0)
            {
                result.setCode(-1);
                result.setInfo("活动已下架");
                return result;
            }

            BaseConfigModel config = configService.findByChannelNo(lottery.getChannelNo());
            lottery.setServiceName(config.getNickName());
            lottery.setQrCode(config.getQrcode());
            // 缓存
            LotterySummaryVo summary = summaryRedisBiz.get(lotteryId);
            if(summary==null)
            {
                summary = new LotterySummaryVo();
            }
            summary.setAttention(summary.getAttention()+1);// 关注+1
            summaryRedisBiz.put(lotteryId, summary, 30, TimeUnit.DAYS);

            lottery.setAttention(summary.getAttention());
            lottery.setReceive(summary.getReceive());
            lottery.setJoin(summary.getJoin());
            lottery.setMessage(summary.getMessage());
            lottery.setLike(summary.getLike());
            lottery.setUseNum(summary.getUseNum());
            // 用户抽奖机会缓存
            UserLotteryTotalVo total = this.getUserLotteryTotalVo(lotteryId, userId);

            // 是否默认已给一次机会 ,值为1
            String giveKey = getGiveKey(lotteryId, userId);
            String give = stringRedisBiz.get(giveKey);
            if(validator.isBlank(give))
            {
                if(total==null)
                {
                    total = new UserLotteryTotalVo();
                }

                total.setTimesNum(total.getTimesNum()+1);
                this.setUserLotteryTotalVo(lotteryId, userId, total);

                stringRedisBiz.put(giveKey, "1", 180, TimeUnit.DAYS);
            }

            if(total!=null)
            {
                lottery.setTimesNum(total.getTimesNum());
                lottery.setParticipated(total.getParticipated());
            }

            lottery.setUserId(userId);
            result.setData(lottery);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取活动详情出错");
            logger.error(e, "获取活动详情出错");
        }
        return result;
    }

    @Override
    public ResultCode lottery(String lotteryId, String userId)
    {
        ResultCode result = new ResultCode();
        LotteryModel lottery = this.findById(lotteryId);
        if(lottery==null||lottery.getProperty()==1||lottery.getOnline()==0)
        {
            result.setCode(-1);
            result.setInfo("活动不存在或已下架");
            return result;
        }
        if(lottery.getStartTime().getTime()>new Date().getTime()||new Date().getTime()>lottery.getEndTime().getTime())
        {
            result.setCode(-1);
            result.setInfo("不在活动时间内");
            return result;
        }
        UserModel user = userService.findById(userId);
        if(user==null)
        {
            result.setCode(-1);
            result.setInfo("用户不存在");
            return result;
        }
        String lockKey = getRedisLockKey(lotteryId);
        RedisLock lock = new RedisLock(redisTemplate, lockKey, 10000, 20000);
        try
        {
            // 获取锁标识
            boolean locked = false;
            locked = lock.lock();// 获取锁
            if(locked)
            {
                UserLotteryTotalVo total = this.getUserLotteryTotalVo(lotteryId, userId);
                if(total==null||total.getTimesNum()==0)
                {
                    result.setCode(-1);
                    result.setInfo("抽奖次数不足");
                    return result;
                }

                int specialRate = 0;// 特等级概率
                int oneRate = 0; // 一等级概率
                int twoRate = 0;// 二等级概率
                int threeRate = 0;// 三等级概率
                int fourRate = 0;// 四等级概率

                // 定义奖池 0-特等奖 1-1等奖 2 3 4 -1未中奖 将可抽到奖项放入奖池 随机抽奖
                // List<Integer> poolList = new ArrayList<Integer>();
                Map<Integer, Integer> poolMap = new HashMap<Integer, Integer>();
                // pool.add(-1);// 未中奖
                // 奖项List
                List<LotteryAwardsModel> awardsList = awardsService.getList(lottery.getChannelNo(), lotteryId);
                // 库存对象转json对象方便循环获取
                net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(total);
                // 奖项设置是否正常 不正常则无法中奖
                if(awardsList.size()==5)
                {
                    for(int i = 0; i<5; i++)
                    {
                        // 是否还有机会
                        if(awardsList.get(i).getLuckyNum()>jsonObject.getInt(Awards[i]))
                        {
                            boolean stock = false;// 库存是否足够
                            // -1不限量
                            if(awardsList.get(i).getNum()==-1)
                            {
                                // 不限量
                                stock = true;
                            }
                            else
                            {
                                stock = integerRedisBiz.get(getLotteryAwardsKey(awardsList.get(i).getId()))!=null
                                        &&integerRedisBiz.get(getLotteryAwardsKey(awardsList.get(i).getId()))>0;
                            }
                            if(stock&&total.getLotteryNum()>=awardsList.get(i).getMinTimes())
                            {
                                // 符合条件，把奖品放入奖池中
                                // poolList.add(awardsList.get(i).getGrade());

                                poolMap.put(awardsList.get(i).getGrade(), awardsList.get(i).getGrade());

                                // 中奖概率
                                if(awardsList.get(i).getGrade()==0)
                                {
                                    specialRate = awardsList.get(i).getRate();
                                }
                                else if(awardsList.get(i).getGrade()==1)
                                {
                                    oneRate = awardsList.get(i).getRate();
                                }
                                else if(awardsList.get(i).getGrade()==2)
                                {
                                    twoRate = awardsList.get(i).getRate();
                                }
                                else if(awardsList.get(i).getGrade()==3)
                                {
                                    threeRate = awardsList.get(i).getRate();
                                }
                                else if(awardsList.get(i).getGrade()==4)
                                {
                                    fourRate = awardsList.get(i).getRate();
                                }

                            }

                        }
                    }
                }

                // 中几等奖，默认没中奖
                Integer answer = null;

                // 随机抽奖
                Random random = new Random();
                int index = random.nextInt(100);
                // 默认不中奖
                int grade = -1;
                for(int i = 0; i<poolMap.size(); i++)
                {
                    if(index<specialRate)
                    {
                        // 特等级概率
                        grade = 0;
                    }
                    else if(index<oneRate)
                    {
                        // 一等级概率
                        grade = 1;
                    }
                    else if(index<twoRate)
                    {
                        // 二等级概率
                        grade = 2;
                    }
                    else if(index<threeRate)
                    {
                        // 三等级概率
                        grade = 3;
                    }
                    else if(index<fourRate)
                    {
                        // 四等级概率
                        grade = 4;
                    }

                    // 中奖了
                    if(poolMap.get(grade)!=null)
                    {
                        answer = poolMap.get(grade);
                        break;
                    }

                }
                LotterySummaryVo summary = summaryRedisBiz.get(lotteryId);
                // Integer answer = poolMap.get(grade);
                // 中奖了
                if(answer!=null)
                {
                    LotteryAwardsModel awards = awardsService.findByGrade(lotteryId, answer);

                    // 订单
                    LotteryOrderModel order = new LotteryOrderModel();
                    order.setChannelNo(awards.getChannelNo());
                    order.setLotteryId(lotteryId);
                    order.setMerchantId(lottery.getMerchantId());
                    order.setUserId(userId);
                    order.setAwardsId(awards.getId());
                    order.setGrade(answer);
                    order.setTel(validator.isBlank(user.getTel())?"":user.getTel());
                    order.setUsername(validator.isBlank(user.getNickName())?"":user.getNickName());
                    order.setUsecode(OrderGenerateUtil.generateUseCode());
                    String qrCodeUrl = ossService.createQrcode(order.getUsecode());
                    order.setQrcode(qrCodeUrl);// 核销二维码
                    order.setCloseTime(lottery.getCloseTime());
                    order.setCreateTime(new Date());
                    lotteryOrderService.save(order);

                    // 奖品限量 需要处理缓存
                    if(awards.getNum()!=0)
                    {
                        int stock = integerRedisBiz.get(getLotteryAwardsKey(awards.getId()))-1;
                        integerRedisBiz.put(getLotteryAwardsKey(awards.getId()), stock, 90, TimeUnit.DAYS);
                    }

                    // 用户中奖缓存+奖品库存处理
                    total.setParticipated(1);
                    switch(answer)
                    {
                    case 0:
                        total.setSpecialNum(total.getSpecialNum()+1);
                        break;
                    case 1:
                        total.setOneNum(total.getOneNum()+1);
                        break;
                    case 2:
                        total.setTwoNum(total.getTwoNum()+1);
                        break;
                    case 3:
                        total.setThreeNum(total.getThreeNum()+1);
                        break;
                    case 4:
                        total.setFourNum(total.getFourNum()+1);
                        break;
                    default:
                        break;
                    }

                    result.setData(awards);

                    // 奖品领取数量

                    if(summary==null)
                    {
                        summary = new LotterySummaryVo();
                        summary.setReceive(1);
                    }
                    else
                    {
                        summary.setReceive(summary.getReceive()+1);
                    }

                    // 发送抽奖结果通知
                    String prize = "";
                    if(answer==0)
                    {
                        prize = "特等奖";
                    }
                    else if(answer==1)
                    {
                        prize = "一等奖";
                    }
                    else if(answer==2)
                    {
                        prize = "二等奖";
                    }
                    else if(answer==3)
                    {
                        prize = "三等奖";
                    }
                    else if(answer==4)
                    {
                        prize = "四等奖";
                    }

                    templateMsgQueueService.sendMsg2(lottery.getChannelNo(), user.getOpenid(), awards.getName(), prize);
                    // templateMsgService.sendMsg2(lottery.getChannelNo(),
                    // user.getOpenid(), awards.getName(), prize);
                }
                else
                {
                    LotteryAwardsModel awards = new LotteryAwardsModel();
                    awards.setGrade(5);
                    result.setData(awards);
                }
                // 参与数
                if(summary==null)
                {
                    summary = new LotterySummaryVo();
                    summary.setJoin(1);
                }
                else
                {
                    summary.setJoin(summary.getJoin()+1);
                }
                summaryRedisBiz.put(lotteryId, summary, 90, TimeUnit.DAYS);
                total.setTimesNum(total.getTimesNum()-1);// 还剩余抽奖次数
                total.setLotteryNum(total.getLotteryNum()+1);// 已抽奖次数
                this.setUserLotteryTotalVo(lotteryId, userId, total);
                result.setOther(Integer.valueOf(total.getTimesNum()).toString());

            }
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("参与抽奖出错");
            logger.error(e, "参与抽奖出错");
        }
        finally
        {
            lock.unlock();
        }

        return result;
    }

    @Override
    public String getRedisLockKey(String lotteryId)
    {
        return new StringBuilder().append(Constants.LOCK_LOTTERY_KEY).append('-').append(lotteryId).toString();
    }

    @Override
    public String getLotteryAwardsKey(String awardsId)
    {

        return new StringBuilder().append(Constants.LOTTERY_AWARDS_STOCK_KEY).append('-').append(awardsId).toString();
    }

    @Override
    public ResultCode merchantList(String channelNo, String merchantId, int status, int page, int pageSize)
    {
        ResultCode result = new ResultCode();

        StringBuffer where = new StringBuffer(" o.channelNo = ? AND o.merchantId = ? AND o.online = 1 AND o.property = 0 ");
        List<Object> args = new ArrayList<Object>();
        args.add(channelNo);
        args.add(merchantId);
        // status 0-进行中 1-已结束 2-已过期
        switch(status)
        {
        case 0:
            where.append(" AND o.endTime >= ? ");
            args.add(new Date());
            break;
        case 1:
            where.append(" AND o.endTime <= ? AND o.closeTime >= ? ");
            args.add(new Date());
            args.add(new Date());
            break;
        case 2:
            where.append(" AND o.closeTime <= ? ");
            args.add(new Date());
            break;
        default:
            break;
        }
        try
        {
            PageList<LotteryModel> pageList = lotteryDao.pageList(where.toString(), args.toArray(), page, pageSize);
            List<LotteryModel> list = null;
            if(pageList!=null)
            {
                list = pageList.getList();
            }
            else
            {
                list = new ArrayList<LotteryModel>();
            }
            if(list.size()>0)
            {
                for(LotteryModel lottery : list)
                {
                    // 缓存数据
                    LotterySummaryVo vo = summaryRedisBiz.get(lottery.getId());
                    if(vo!=null)
                    {
                        lottery.setAttention(vo.getAttention());
                        lottery.setReceive(vo.getReceive());
                        lottery.setMessage(vo.getMessage());
                        lottery.setLike(vo.getLike());
                        lottery.setUseNum(vo.getUseNum());
                    }
                    // 最低价
                    LotteryAwardsModel awards = awardsService.getlowestPrice(channelNo, lottery.getId());
                    lottery.setLowestPrice(awards==null||validator.isBlank(awards.getPrice())?"0.0":awards.getPrice());
                }
            }
            result.setData(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取商户活动列表出错");
            logger.error(e, "获取商户活动列表出错");
        }

        return result;
    }

    @Override
    public ResultCode nameList(String channelNo, String merchantId)
    {
        ResultCode result = new ResultCode();

        try
        {
            List<LotteryModel> list = lotteryDao.nameList(channelNo, merchantId);
            result.setData(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载商户活动昵称列表出错");
            logger.error(e, "加载商户活动昵称列表出错");
        }

        return result;
    }

    private static final String CREATE = " o.channelNo = ? AND o.createTime >= ? AND o.createTime <= ? ";

    private static final String AGOING = " o.channelNo = ?  AND o.endTime >= ? AND o.online = 1 AND o.property = 0 ";

    private static final String END = " o.channelNo = ?  AND ( o.endTime <= ? OR o.online = 0 OR o.property = 1 ) ";

    @Override
    public JSONObject getLotteryJson(String channelNo)
    {
        JSONObject lotteryJson = new JSONObject();

        Date todayStart = DateUtils.getDayStart(new Date());
        Date todayend = DateUtils.getDayEnd(new Date());
        Date yestStart = DateUtils.getDayStart(DateUtils.addDay(new Date(), -1));
        Date yestEnd = DateUtils.getDayEnd(DateUtils.addDay(new Date(), -1));

        int todayCreate = lotteryDao.count(CREATE, new Object[]{channelNo, todayStart, todayend});
        lotteryJson.put("todayCreate", todayCreate);

        int yesterdayCreate = lotteryDao.count(CREATE, new Object[]{channelNo, yestStart, yestEnd});
        lotteryJson.put("yesterdayCreate", yesterdayCreate);

        int agoing = lotteryDao.count(AGOING, new Object[]{channelNo, new Date()});
        lotteryJson.put("agoing", agoing);

        int end = lotteryDao.count(END, new Object[]{channelNo, new Date()});
        lotteryJson.put("end", end);

        return lotteryJson;
    }

    @Override
    public ResultCode Haibao(String channelNo, String userId, String lotteryId)
    {
        ResultCode result = new ResultCode();
        try
        {
            LotteryModel lottery = this.findById(lotteryId);
            if(lottery==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return result;
            }

            JSONObject jsonObject = JSONObject.parseObject(configService.getQrCode(channelNo, userId, lotteryId).getData().toString());
            jsonObject.put("posterImg", lottery.getPosterImg());
            result.setData(jsonObject);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载海报出错");
            logger.error(e, "加载海报出错");
        }
        return result;
    }

    @Override
    public ResultCode newHaibao(String channelNo, String userId, String lotteryId)
    {
        ResultCode result = new ResultCode();
        try
        {
            LotteryModel lottery = this.findById(lotteryId);
            if(lottery==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return result;
            }

            String haibaoKey = getHaibaoKey(lotteryId, userId);
            String qrCodeUrl = stringRedisBiz.get(haibaoKey);

            if(validator.isBlank(qrCodeUrl))
            {
                JSONObject jsonObject = JSONObject.parseObject(configService.getQrCode(channelNo, userId, lotteryId).getData().toString());
                String qrCode = jsonObject.getString("qrCode");
                String posterImg = lottery.getPosterImg();
                InputStream is = HaibaoUtil.getLotteryHaibao(posterImg+"/750x1334", qrCode+"/240x240");

                if(is==null)
                {
                    result.setCode(-1);
                    result.setInfo("生成海报失败，请重新生成");
                    return result;
                }

                // 新的图片名称
                String newFileName = UUID.randomUUID().toString().replace("-", "")+".png";
                String imgSign = ossService.uploadImg(is, newFileName);

                if(!validator.isBlank(imgSign))
                {
                    qrCodeUrl = DIR+newFileName;
                    stringRedisBiz.put(haibaoKey, qrCodeUrl, 20, TimeUnit.DAYS);
                }
                else
                {
                    result.setCode(-1);
                    result.setInfo("生成海报失败，请重新生成");
                }
            }
            result.setData(qrCodeUrl);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载海报出错");
            logger.error(e, "加载海报出错");
        }
        return result;
    }

    private String getHaibaoKey(String lotteryId, String userId)
    {
        StringBuffer sb = new StringBuffer();
        return sb.append(Constants.LOTTERY_HAIBAO_KEY).append(lotteryId).append("-").append(userId).toString();
    }

    private static final String COUNT_WHERE = " o.createTime >= ? AND o.createTime <= ? ";

    @Override
    public JSONObject homeData()
    {
        JSONObject jsonObject = new JSONObject();
        // 昨日活动 本月活动 上月活动 总活动数
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        Date yestStart = DateUtils.getDayStart(DateUtils.addDay(new Date(), -1));
        Date yestEnd = DateUtils.getDayEnd(DateUtils.addDay(new Date(), -1));
        Date monthStart = DateUtils.getMonthStart(year, month);
        Date monthEnd = DateUtils.getMonthEnd(year, month);
        Date lastMonthStart = DateUtils.getMonthStart(year, month-1);
        Date lastMonthEnd = DateUtils.getMonthEnd(year, month-1);
        int totalNum = lotteryDao.count(" 1 = 1 ", null);
        jsonObject.put("totalNum", totalNum);
        int yesterdayNum = lotteryDao.count(COUNT_WHERE, new Object[]{yestStart, yestEnd});
        jsonObject.put("yesterdayNum", yesterdayNum);
        int monthNum = lotteryDao.count(COUNT_WHERE, new Object[]{monthStart, monthEnd});
        jsonObject.put("monthNum", monthNum);
        int lastMonthNum = lotteryDao.count(COUNT_WHERE, new Object[]{lastMonthStart, lastMonthEnd});
        jsonObject.put("lastMonthNum", lastMonthNum);
        return jsonObject;
    }

    @Override
    public List<LotteryModel> getExpireList()
    {
        return lotteryDao.getExpireList();
    }

    @Override
    public ResultCode getDetails(String lotteryId)
    {
        ResultCode result = new ResultCode();
        try
        {
            LotteryDetailsModel details = detailsService.findByLotteryId(lotteryId);
            List<LotteryAwardsModel> awardsList = awardsService.getList(details.getChannelNo(), lotteryId);
            details.setAwardsList(awardsList);
            result.setData(details);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取图文详情出错");
            logger.error(e, "获取图文详情出错");
        }
        return result;
    }

    @Override
    public int countLotteryNum(String channelNo)
    {
        return lotteryDao.countLotteryNum(channelNo);
    }

    @Override
    public ResultCode delete(String id)
    {
        ResultCode result = new ResultCode();
        try
        {
            LotteryModel instance = this.findById(id);
            if(instance==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return result;
            }
            instance.setProperty(1);
            this.save(instance);
            // 缓存
            BaseConfigModel config = configService.findByChannelNo(instance.getChannelNo());
            PartnerDataTotalVo totalVo = partnerSummaryRedisBiz.get(config.getId());
            if(totalVo!=null)
            {
                totalVo.setActivityNum(totalVo.getActivityNum()-1);
                partnerSummaryRedisBiz.put(config.getId(), totalVo, 360, TimeUnit.DAYS);
            }
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("删除抽奖活动出错");
            logger.error(e, "删除抽奖活动出错");
        }
        return result;
    }

    @Override
    public List<LotteryModel> getLotteryList(String merchantId)
    {
        return lotteryDao.getLotteryList(merchantId);
    }

}
