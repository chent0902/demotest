package com.family.demotest.web.controller.lottery;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.base01.util.PageList;
import com.family.demotest.common.biz.IntegerRedisBiz;
import com.family.demotest.common.biz.LotterySummaryRedisBiz;
import com.family.demotest.entity.LotteryModel;
import com.family.demotest.service.lottery.LotteryService;
import com.family.demotest.vo.total.LotterySummaryVo;
import com.family.demotest.vo.total.UserLotteryTotalVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * 
 * @author wujf
 *
 */
@Controller
@RequestMapping("/lottery/total")
public class LotteryTotalController
    extends
    BaseController
{

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private IntegerRedisBiz integerRedisBiz;

    @Autowired
    private LotterySummaryRedisBiz summaryRedisBiz;

    /**
     * 获取缓存数据
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/getSummaryVo.do")
    @ResponseBody
    public String getSummaryVo(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String lotteryId = request.getParameter("lotteryId");

        String sign = request.getParameter("sign");

        if(validator.isBlank(lotteryId)||validator.isBlank(sign))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        if(!"wujf8888".equals(sign))
        {
            result.setCode(-1);
            result.setInfo("签名错误");
            return JSON.toJSONString(result);
        }

        LotterySummaryVo summaryVo = summaryRedisBiz.get(lotteryId);

        result.setData(summaryVo);
        String json = JSON.toJSONString(result);
        return json;
    }

    /**
     * 设置缓存数量
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/setSummaryVo.do")
    @ResponseBody
    public String setSummaryVo(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String lotteryId = request.getParameter("lotteryId");

        String sign = request.getParameter("sign");
        String receiveNum = request.getParameter("receiveNum");// 领取数
        String useNum = request.getParameter("useNum");// 使用数

        if(validator.isBlank(lotteryId)||validator.isBlank(sign))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        if(!"wujf8888".equals(sign))
        {
            result.setCode(-1);
            result.setInfo("签名错误");
            return JSON.toJSONString(result);
        }

        LotterySummaryVo summaryVo = summaryRedisBiz.get(lotteryId);

        if(summaryVo!=null)

        {
            summaryVo.setReceive(Integer.parseInt(receiveNum));
            summaryVo.setUseNum(Integer.parseInt(useNum));

            summaryRedisBiz.put(lotteryId, summaryVo, 90, TimeUnit.DAYS);
        }

        result.setData(summaryVo);
        return JSON.toJSONString(result);
    }

    /**
     * 获取缓存数据
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/getTotalVo.do")
    @ResponseBody
    public String getTotalVo(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String lotteryId = request.getParameter("lotteryId");
        String userId = request.getParameter("userId");
        String sign = request.getParameter("sign");

        if(validator.isBlank(lotteryId)||validator.isBlank(sign))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        if(!"wujf8888".equals(sign))
        {
            result.setCode(-1);
            result.setInfo("签名错误");
            return JSON.toJSONString(result);
        }

        UserLotteryTotalVo totalVo = lotteryService.getUserLotteryTotalVo(lotteryId, userId);

        result.setData(totalVo);
        String json = JSON.toJSONString(result);
        return json;
    }

    /**
     * 设置缓存数量
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/setTotalVo.do")
    @ResponseBody
    public String setTotalVo(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String lotteryId = request.getParameter("lotteryId");
        String userId = request.getParameter("userId");
        String sign = request.getParameter("sign");
        String specialNum = request.getParameter("specialNum");// 特等奖数量
        String oneNum = request.getParameter("oneNum");// 一等奖数量
        String twoNum = request.getParameter("twoNum");// 二等奖数量

        String threeNum = request.getParameter("threeNum");// 三等奖数量
        String fourNum = request.getParameter("fourNum");// 四等奖数量

        String timesNum = request.getParameter("timesNum");// 抽奖次数

        if(validator.isBlank(lotteryId)||validator.isBlank(sign))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        if(!"wujf8888".equals(sign))
        {
            result.setCode(-1);
            result.setInfo("签名错误");
            return JSON.toJSONString(result);
        }

        UserLotteryTotalVo totalVo = lotteryService.getUserLotteryTotalVo(lotteryId, userId);
        if(totalVo!=null)
        {
            totalVo.setSpecialNum(Integer.parseInt(specialNum));
            totalVo.setOneNum(Integer.parseInt(oneNum));
            totalVo.setTwoNum(Integer.parseInt(twoNum));
            totalVo.setThreeNum(Integer.parseInt(threeNum));
            totalVo.setFourNum(Integer.parseInt(fourNum));
            totalVo.setTimesNum(Integer.parseInt(timesNum));
            lotteryService.setUserLotteryTotalVo(lotteryId, userId, totalVo);
        }

        result.setData(totalVo);
        String json = JSON.toJSONString(result);
        return json;
    }

    /**
     * 设置多规格缓存数量
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/getAwardsTotalVo.do")
    @ResponseBody
    public String getAwardsTotalVo(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String awardsId = request.getParameter("awardsId");// 奖品ID
        String sign = request.getParameter("sign");

        if(validator.isBlank(awardsId)||validator.isBlank(sign))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        if(!"wujf8888".equals(sign))
        {
            result.setCode(-1);
            result.setInfo("签名错误");
            return JSON.toJSONString(result);
        }

        String key = lotteryService.getLotteryAwardsKey(awardsId);
        Integer stockNum = integerRedisBiz.get(key);

        result.setData(stockNum);

        return JSON.toJSONString(result);
    }

    /**
     * 设置多规格缓存数量
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/setAwardsTotalVo.do")
    @ResponseBody
    public String setAwardsTotalVo(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String awardsId = request.getParameter("awardsId");// 多规格ID
        String sign = request.getParameter("sign");
        String stockNum = request.getParameter("stockNum");// 库存数

        int stockNum1 = Integer.parseInt(stockNum);

        if(validator.isBlank(awardsId)||validator.isBlank(sign))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        if(!"wujf8888".equals(sign))
        {
            result.setCode(-1);
            result.setInfo("签名错误");
            return JSON.toJSONString(result);
        }

        String key = lotteryService.getLotteryAwardsKey(awardsId);

        integerRedisBiz.put(key, stockNum1, 90, TimeUnit.DAYS);

        result.setData(stockNum1);
        String json = JSON.toJSONString(result);
        return json;
    }

    /**
     * 获取缓存数据
     * 
     * @param request
     * @param response
     */
    @RequestMapping("/setJoin.do")
    @ResponseBody
    public String setJoin(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String sign = request.getParameter("sign");

        if(!"wujf8888".equals(sign))
        {
            result.setCode(-1);
            result.setInfo("签名错误");
            return JSON.toJSONString(result);
        }

        try
        {
            result = lotteryService.allList(0, null, 3000, 1);

            if(result.getCode()==0)
            {
                PageList<LotteryModel> pageList = (PageList<LotteryModel>)result.getData();

                List<LotteryModel> list = pageList.getList();
                for(LotteryModel model : list)
                {
                    LotterySummaryVo summaryVo = summaryRedisBiz.get(model.getId());
                    if(summaryVo!=null)
                    {
                        summaryVo.setJoin(summaryVo.getJoin()+summaryVo.getReceive());
                        summaryRedisBiz.put(model.getId(), summaryVo, 90, TimeUnit.DAYS);
                    }
                }
            }
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("设置出错");
            logger.error(e, "设置出错");
        }

        return JSON.toJSONString(result);
    }

}
