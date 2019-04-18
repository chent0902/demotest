package com.family.demotest.web.controller.lottery;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.common.biz.MessageSummaryRedisBiz;
import com.family.demotest.service.lottery.LotteryMessageService;
import com.family.demotest.vo.total.MessageSummaryVo;
import com.family.demotest.vo.user.UserVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller
@RequestMapping("/lottery/message")
public class LotteryMessageController
    extends BaseController
{
    @Autowired
    private LotteryMessageService messageService;
    @Autowired
    private MessageSummaryRedisBiz messageSummaryRedisBiz;

    /**
     * 留言列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/lotteryMessage.do")
    @ResponseBody
    public String lotteryMessage(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String lotteryId = request.getParameter("lotteryId");
        int page = this.getIntValue(request, "page", 1);
        int pageSize = this.getIntValue(request, "length", 10);

        if(validator.isBlank(lotteryId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = messageService.lotteryMessage(lotteryId, page, pageSize);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 我的留言
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/myMessage.do")
    @ResponseBody
    public String myMessage(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        String lotteryId = request.getParameter("lotteryId");
        String token = request.getParameter("token");
        if(validator.isBlank(channelNo)||validator.isBlank(lotteryId)||validator.isBlank(token))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        UserVo user = this.getUserInfo(token);
        if(user==null)
        {
            result.setCode(999);
            result.setInfo("token失效");
            return JSON.toJSONString(result);
        }
        result = messageService.myMessage(channelNo, lotteryId, user.getId());
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 留言
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/save.do")
    @ResponseBody
    public String save(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        String lotteryId = request.getParameter("lotteryId");
        String token = request.getParameter("token");
        String content = request.getParameter("content");
        if(validator.isBlank(channelNo)||validator.isBlank(lotteryId)||validator.isBlank(token)||validator.isBlank(content))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        UserVo user = this.getUserInfo(token);
        if(user==null)
        {
            result.setCode(999);
            result.setInfo("token失效");
            return JSON.toJSONString(result);
        }
        result = messageService.save(channelNo, lotteryId, user.getId(), content);
        return JSON.toJSONString(result);
    }

    /**
     * 留言点赞
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/like.do")
    @ResponseBody
    public String like(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String messageId = request.getParameter("messageId");
        int type = this.getIntValue(request, "type", 0);// 0-留言点赞 1-回复点赞
        if(validator.isBlank(messageId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        try
        {
            MessageSummaryVo vo = messageSummaryRedisBiz.get(messageId);
            if(vo==null)
            {
                vo = new MessageSummaryVo();
            }
            if(type==1)
            {
                vo.setReplyLike(vo.getReplyLike()+1);
            }
            else
            {
                vo.setLike(vo.getLike()+1);
            }
            messageSummaryRedisBiz.put(messageId, vo, 30, TimeUnit.DAYS);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("留言点赞出错");
            logger.error(e, "留言点赞出错");
        }

        return JSON.toJSONString(result);
    }

}
