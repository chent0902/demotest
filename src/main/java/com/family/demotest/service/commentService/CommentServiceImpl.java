package com.family.demotest.service.commentService;

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
import com.family.demotest.common.biz.ImgTextSummaryRedisBiz;
import com.family.demotest.common.biz.MessageSummaryRedisBiz;
import com.family.demotest.dao.comment.CommentDao;
import com.family.demotest.entity.CommentModel;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.user.UserService;
import com.family.demotest.vo.total.ImgTextSummaryVo;
import com.family.demotest.vo.total.MessageSummaryVo;
import com.family.demotest.web.util.NicknameUtils;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author Chen
 *
 */
@Service("comment.service")
public class CommentServiceImpl
    extends ServiceSupport<CommentModel>
    implements CommentService
{

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private Logger logger;
    @Autowired
    private ImgTextSummaryRedisBiz summaryRedisBiz;
    @Autowired
    private Validator validator;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSummaryRedisBiz messageSummaryRedisBiz;

    @Override
    protected QueryDao<CommentModel> getQueryDao()
    {

        return commentDao;
    }

    @Override
    protected SaveDao<CommentModel> getSaveDao()
    {

        return commentDao;
    }

    @Override
    protected DeleteDao<CommentModel> getDeleteDao()
    {

        return commentDao;
    }

    @Override
    public ResultCode pageList(String channelNo, String imgTextId, int pageSize, int page)
    {
        ResultCode result = new ResultCode();
        try
        {
            PageList<CommentModel> pageList = commentDao.pageList(channelNo, imgTextId, page, pageSize);
            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载评价出错");
            logger.error(e, "加载评价出错");
        }

        return result;
    }

    @Override
    public ResultCode saveOrUpdate(String channelNo, String imgTextId, String content)
    {
        ResultCode result = new ResultCode();

        CommentModel commentModel = new CommentModel();
        commentModel.setChannelNo(channelNo);
        commentModel.setImgTextId(imgTextId);
        commentModel.setContent(content);
        commentModel.setCreateTime(new Date());
        commentModel.setUserId("0");
        commentModel.setChoiceness(1);
        Random random = new Random();// 定义随机类
        StringBuffer sb = new StringBuffer("00");
        sb.append(random.nextInt(200)+1);
        String url = sb.substring(sb.length()-3, sb.length());
        commentModel.setNickName(NicknameUtils.getChineseName());
        commentModel.setHeadurl("/../upload/heads/"+url+".jpg");

        try
        {
            this.save(commentModel);
            ImgTextSummaryVo summary = summaryRedisBiz.get(imgTextId);
            if(summary==null)
            {
                summary = new ImgTextSummaryVo();
                summary.setMessage(1);
            }
            else
            {
                summary.setMessage(summary.getMessage()+1);
            }
            summaryRedisBiz.put(imgTextId, summary, 90, TimeUnit.DAYS);
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
    public ResultCode reply(String messageId, String reply)
    {
        ResultCode result = new ResultCode();
        try
        {
            CommentModel comment = this.findById(messageId);
            if(comment==null)
            {
                result.setCode(-1);
                result.setInfo("该留言不存在");
                return result;
            }
            comment.setReply(reply);
            this.save(comment);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("留言回复出错");
            logger.error(e, "留言回复出错");
        }
        return result;
    }

    @Override
    public ResultCode imgTextMessage(String imgTextId, int page, int pageSize)
    {
        ResultCode result = new ResultCode();
        try
        {
            // 留言
            List<CommentModel> messageList = commentDao.choicenessList(imgTextId, page, pageSize);
            if(messageList!=null&&messageList.size()>0)
            {
                Random random = new Random();// 定义随机类
                for(CommentModel message : messageList)
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
    public ResultCode myMessage(String channelNo, String imgTextId, String userId)
    {
        ResultCode result = new ResultCode();
        try
        {
            List<CommentModel> list = commentDao.myMessage(channelNo, imgTextId, userId);
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
    public ResultCode save(String channelNo, String imgTextId, String userId, String content)
    {
        ResultCode result = new ResultCode();
        try
        {
            CommentModel model = new CommentModel();
            model.setChannelNo(channelNo);
            model.setImgTextId(imgTextId);
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
            ImgTextSummaryVo summary = summaryRedisBiz.get(imgTextId);
            if(summary==null)
            {
                summary = new ImgTextSummaryVo();
                summary.setMessage(1);
            }
            else
            {
                summary.setMessage(summary.getMessage()+1);
            }
            summaryRedisBiz.put(imgTextId, summary, 90, TimeUnit.DAYS);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存留言出错");
            logger.error(e, "保存留言出错");
        }
        return result;
    }

}
