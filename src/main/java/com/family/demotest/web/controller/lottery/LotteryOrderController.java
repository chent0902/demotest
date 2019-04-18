package com.family.demotest.web.controller.lottery;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.entity.LotteryModel;
import com.family.demotest.entity.LotteryOrderModel;
import com.family.demotest.service.lottery.LotteryOrderService;
import com.family.demotest.service.lottery.LotteryService;
import com.family.demotest.vo.user.UserVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller
@RequestMapping("/lottery/order")
public class LotteryOrderController
    extends
    BaseController
{
    @Autowired
    private LotteryOrderService lotteryOrderService;
    @Autowired
    private LotteryService lotteryService;

    /**
     * 用户券列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/userList.do")
    @ResponseBody
    public String userList(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        String token = request.getParameter("token");
        int type = this.getIntValue(request, "type", 0);// 0-未使用 1-已使用 2-已过期
        int page = this.getIntValue(request, "page", 1);
        int pageSize = this.getIntValue(request, "length", 10);
        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        if(validator.isBlank(token))
        {
            result.setCode(999);
            result.setInfo("token失效");
            return JSON.toJSONString(result);
        }

        UserVo user = this.getUserInfo(token);
        if(user==null)
        {
            result.setCode(999);
            result.setInfo("token失效");
            return JSON.toJSONString(result);
        }

        result = lotteryOrderService.userList(channelNo, user.getId(), type, page, pageSize);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 我的优惠券详情
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
        String id = request.getParameter("id");
        String token = request.getParameter("token");
        if(validator.isBlank(id)||validator.isBlank(token))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        try
        {
            UserVo user = this.getUserInfo(token);
            if(user==null)
            {
                result.setCode(999);
                result.setInfo("token失效");
                return JSON.toJSONString(result);
            }

            LotteryOrderModel model = lotteryOrderService.findById(id);
            LotteryModel lotteryModel = lotteryService.findById(model.getLotteryId());
            if(lotteryModel==null)
            {
                result.setCode(-1);
                result.setInfo("该抽奖活动不存在");
                return JSON.toJSONString(result);
            }
            model.setTemplate(lotteryModel.getTemplate());
            if(model==null||model.getProperty()==1)
            {
                result.setCode(-1);
                result.setInfo("优惠券不存在");
                return JSON.toJSONString(result);
            }

            if(!model.getUserId().equals(user.getId()))
            {
                result.setCode(-100);
                result.setInfo("该优惠券不是您的");
                return JSON.toJSONString(result);
            }

            result.setData(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载优惠券详情出错");
            logger.error(e, "加载优惠券详情出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 删除优惠券
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/delete.do")
    @ResponseBody
    public String delete(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String id = request.getParameter("id");
        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = lotteryOrderService.delete(id);

        return JSON.toJSONString(result);
    }

    /**
     * 密码核销
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/pwdCheck.do")
    @ResponseBody
    public String pwdCheck(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String orderId = request.getParameter("orderId");
        String pwd = request.getParameter("pwd");
        String token = request.getParameter("token");
        String channelNo = request.getParameter("channelNo");
        if(validator.isBlank(orderId)||validator.isBlank(pwd)||validator.isBlank(token)||validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        UserVo user = this.getUserInfo(token);
        if(user==null||!channelNo.equals(user.getChannelNo()))
        {
            result.setCode(999);
            result.setInfo("token失效");
            return JSON.toJSONString(result);
        }

        result = lotteryOrderService.pwdCheck(orderId, pwd, user.getId(), user.getNickName());

        return JSON.toJSONString(result);
    }

    /**
     * 获取最新中奖的10条记录
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getNewList.do")
    @ResponseBody
    public String getNewList(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");
        String lotteryId = request.getParameter("lotteryId");// 获取id

        if(validator.isBlank(channelNo)||validator.isBlank(lotteryId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        List<LotteryOrderModel> list = lotteryOrderService.getNewList(channelNo, lotteryId);

        result.setData(list);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    @RequestMapping("/test.do")
    @ResponseBody
    public String test(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        lotteryOrderService.startExpireMsgTask();

        return JSON.toJSONString(result);
    }

}
