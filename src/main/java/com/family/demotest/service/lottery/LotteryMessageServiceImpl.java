package com.family.demotest.service.lottery;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.LotterySummaryRedisBiz;
import com.family.demotest.common.biz.MessageSummaryRedisBiz;
import com.family.demotest.dao.lottery.LotteryMessageDao;
import com.family.demotest.entity.LotteryMessageModel;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.user.UserService;
import com.family.demotest.vo.total.LotterySummaryVo;
import com.family.demotest.vo.total.MessageSummaryVo;
import com.family.demotest.web.util.NicknameUtils;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
@Service("lottery.message.service")
public class LotteryMessageServiceImpl
    extends
    ServiceSupport<LotteryMessageModel>
    implements
    LotteryMessageService
{
    @Autowired
    private LotteryMessageDao lotteryMessageDao;
    @Autowired
    private Logger logger;
    @Autowired
    private LotterySummaryRedisBiz summaryRedisBiz;
    @Autowired
    private MessageSummaryRedisBiz messageSummaryRedisBiz;

    @Autowired
    private Validator validator;

    @Autowired
    private UserService userService;

    @Override
    protected QueryDao<LotteryMessageModel> getQueryDao()
    {
        return lotteryMessageDao;
    }

    @Override
    protected SaveDao<LotteryMessageModel> getSaveDao()
    {
        return lotteryMessageDao;
    }

    @Override
    protected DeleteDao<LotteryMessageModel> getDeleteDao()
    {
        return lotteryMessageDao;
    }

    @Override
    public ResultCode pageList(String channelNo, String lotteryId, int pageSize, int page)
    {
        ResultCode result = new ResultCode();
        try
        {
            PageList<LotteryMessageModel> pageList = lotteryMessageDao.pageList(channelNo, lotteryId, page, pageSize);
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
    public ResultCode saveOrUpdate(String channelNo, String lotteryId, String content)
    {
        ResultCode result = new ResultCode();

        LotteryMessageModel message = new LotteryMessageModel();
        message.setChannelNo(channelNo);
        message.setLotteryId(lotteryId);
        message.setContent(content);
        message.setCreateTime(new Date());
        message.setUserId("0");
        message.setChoiceness(1);
        Random random = new Random();// 定义随机类
        StringBuffer sb = new StringBuffer("00");
        sb.append(random.nextInt(200)+1);
        String url = sb.substring(sb.length()-3, sb.length());
        message.setNickName(NicknameUtils.getChineseName());
        message.setHeadurl("/../upload/heads/"+url+".jpg");

        try
        {
            this.save(message);
            LotterySummaryVo summary = summaryRedisBiz.get(lotteryId);
            if(summary==null)
            {
                summary = new LotterySummaryVo();
                summary.setMessage(1);
            }
            else
            {
                summary.setMessage(summary.getMessage()+1);
            }
            summaryRedisBiz.put(lotteryId, summary, 90, TimeUnit.DAYS);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存留言出错");
            logger.error(e, "保存留言出错");
        }
        return result;
    }

    @Override
    public ResultCode myMessage(String channelNo, String lotteryId, String userId)
    {
        ResultCode result = new ResultCode();
        try
        {
            List<LotteryMessageModel> list = lotteryMessageDao.myMessage(channelNo, lotteryId, userId);
            result.setData(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载我的留言信息出错");
            logger.error(e, "加载我的留言信息出错");
        }

        return result;
    }

    @Override
    public ResultCode save(String channelNo, String lotteryId, String userId, String content)
    {
        ResultCode result = new ResultCode();
        try
        {
            LotteryMessageModel model = new LotteryMessageModel();
            model.setChannelNo(channelNo);
            model.setLotteryId(lotteryId);
            model.setUserId(userId);
            model.setContent(content);
            model.setCreateTime(new Date());

            UserModel user = userService.findById(userId);
            if(user!=null)
            {
                model.setNickName(user.getNickName());
                model.setHeadurl(user.getHeadimgurl());
            }

            this.save(model);
            LotterySummaryVo summary = summaryRedisBiz.get(lotteryId);
            if(summary==null)
            {
                summary = new LotterySummaryVo();
                summary.setMessage(1);
            }
            else
            {
                summary.setMessage(summary.getMessage()+1);
            }
            summaryRedisBiz.put(lotteryId, summary, 90, TimeUnit.DAYS);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存留言出错");
            logger.error(e, "保存留言出错");
        }
        return result;
    }

    @Override
    public ResultCode lotteryMessage(String lotteryId, int page, int pageSize)
    {
        ResultCode result = new ResultCode();
        try
        {
            // 留言
            List<LotteryMessageModel> messageList = lotteryMessageDao.choicenessList(lotteryId, page, pageSize);
            if(messageList!=null&&messageList.size()>0)
            {
                Random random = new Random();// 定义随机类
                for(LotteryMessageModel message : messageList)
                {
                    if("0".equals(message.getUserId()))
                    {
                        if(validator.isBlank(message.getNickName()))
                        {
                            StringBuffer sb = new StringBuffer("00");
                            sb.append(random.nextInt(200)+1);
                            String url = sb.substring(sb.length()-3, sb.length());
                            message.setNickName(NicknameUtils.getChineseName());
                            message.setHeadurl("/../upload/heads/"+url+".jpg");
                        }
                    }
                    else
                    {
                        if(message.getUserId().length()==36&&validator.isBlank(message.getNickName()))
                        {
                            UserModel user = userService.findById(message.getUserId());
                            if(user!=null)
                            {
                                message.setNickName(user.getNickName());
                                message.setHeadurl(user.getHeadimgurl());
                            }
                            else
                            {
                                message.setNickName("");
                                message.setHeadurl("");
                            }
                        }

                    }
                    // 缓存数据
                    MessageSummaryVo mesVo = messageSummaryRedisBiz.get(message.getId());
                    if(mesVo!=null)
                    {
                        message.setLike(mesVo.getLike());
                        message.setReplyLike(mesVo.getReplyLike());
                    }

                    // list.add(model);
                }
            }

            result.setData(messageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载抽奖留言出错");
            logger.error(e, "加载抽奖留言出错");
        }

        return result;
    }

    @Override
    public ResultCode reply(String messageId, String reply)
    {
        ResultCode result = new ResultCode();
        try
        {
            LotteryMessageModel message = this.findById(messageId);
            if(message==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return result;
            }
            message.setReply(reply);
            this.save(message);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("留言回复出错");
            logger.error(e, "留言回复出错");
        }
        return result;
    }
}
