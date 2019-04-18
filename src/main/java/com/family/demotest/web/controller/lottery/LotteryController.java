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
import com.family.demotest.common.biz.LotterySummaryRedisBiz;
import com.family.demotest.service.lottery.LotteryService;
import com.family.demotest.vo.total.LotterySummaryVo;
import com.family.demotest.vo.total.UserLotteryTotalVo;
import com.family.demotest.vo.user.UserVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller
@RequestMapping("/lottery")
public class LotteryController
    extends BaseController
{
    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private LotterySummaryRedisBiz lotterySummaryRedisBiz;

    /**
     * 活动列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/list.do")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        int type = this.getIntValue(request, "type", 0);// 0-火热进行中 1-已抢完
        int page = this.getIntValue(request, "page", 1);
        int pageSize = this.getIntValue(request, "length", 10);
        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = lotteryService.list(channelNo, type, page, pageSize);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 活动详情
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getInfo.do")
    @ResponseBody
    public String getInfo(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String lotteryId = request.getParameter("lotteryId");
        String token = request.getParameter("token");
        String channelNo = request.getParameter("channelNo");

        result = checkToken(result, token, channelNo);
        if(result.getCode()!=0)
        {
            return JSON.toJSONString(result);
        }

        UserVo vo = (UserVo)result.getData();

        // logger.info("***lotteryInfo,uservo:"+JSON.toJSONString(vo));

        result = lotteryService.getInfo(lotteryId, vo.getId());
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 获取图文详情
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getDetails.do")
    @ResponseBody
    public String getDetails(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String lotteryId = request.getParameter("lotteryId");
        if(validator.isBlank(lotteryId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = lotteryService.getDetails(lotteryId);

        return JSON.toJSONString(result);
    }

    /**
     * 抽奖
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/lottery.do")
    @ResponseBody
    public String lottery(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String lotteryId = request.getParameter("lotteryId");
        String token = request.getParameter("token");
        String channelNo = request.getParameter("channelNo");
        result = checkToken(result, token, channelNo);
        if(result.getCode()!=0)
        {
            return JSON.toJSONString(result);
        }

        UserVo userVo = (UserVo)result.getData();
        result = lotteryService.lottery(lotteryId, userVo.getId());
        return JSON.toJSONString(result);
    }

    /**
     * 生成海报 前端画
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/haibao.do")
    @ResponseBody
    public String haibao(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String lotteryId = request.getParameter("lotteryId");
        String token = request.getParameter("token");
        String channelNo = request.getParameter("channelNo");

        result = checkToken(result, token, channelNo);
        if(result.getCode()!=0)
        {
            return JSON.toJSONString(result);
        }

        UserVo vo = (UserVo)result.getData();

        result = lotteryService.Haibao(vo.getChannelNo(), vo.getId(), lotteryId);

        return JSON.toJSONString(result);
    }

    /**
     * 生成海报 后台画
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/newHaibao.do")
    @ResponseBody
    public String newHaibao(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String lotteryId = request.getParameter("lotteryId");
        String token = request.getParameter("token");
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(lotteryId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = checkToken(result, token, channelNo);
        if(result.getCode()!=0)
        {
            return JSON.toJSONString(result);
        }

        UserVo vo = (UserVo)result.getData();

        result = lotteryService.newHaibao(vo.getChannelNo(), vo.getId(), lotteryId);

        return JSON.toJSONString(result);
    }

    /**
     * 抽奖机会
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/add.do")
    @ResponseBody
    public String add(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String lotteryId = request.getParameter("lotteryId");
        String userId = request.getParameter("userId");
        if(validator.isBlank(lotteryId)||validator.isBlank(userId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        UserLotteryTotalVo vo = new UserLotteryTotalVo();
        vo.setTimesNum(999);
        lotteryService.setUserLotteryTotalVo(lotteryId, userId, vo);
        return JSON.toJSONString(result);
    }

    /**
     * 活动点赞
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
        String lotteryId = request.getParameter("lotteryId");
        if(validator.isBlank(lotteryId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        try
        {
            LotterySummaryVo vo = lotterySummaryRedisBiz.get(lotteryId);
            if(vo==null)
            {
                vo = new LotterySummaryVo();
            }
            vo.setLike(vo.getLike()+1);
            lotterySummaryRedisBiz.put(lotteryId, vo, 30, TimeUnit.DAYS);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("活动点赞出错");
            logger.error(e, "活动点赞出错");
        }

        return JSON.toJSONString(result);
    }

}
