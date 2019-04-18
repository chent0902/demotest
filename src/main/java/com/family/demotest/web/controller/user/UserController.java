package com.family.demotest.web.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.lottery.LotteryOrderService;
import com.family.demotest.service.merchant.MerchantUserService;
import com.family.demotest.service.user.UserService;
import com.family.demotest.service.wxopen.WxMsgReplyService;
import com.family.demotest.vo.user.UserVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller
@RequestMapping("/user")
public class UserController
    extends
    BaseController
{

    @Autowired
    private UserService userService;
    @Autowired
    private LotteryOrderService lotteryOrderService;
    @Autowired
    private MerchantUserService merchantUserService;

    @Autowired
    private WxMsgReplyService wxMsgReplyService;

    /**
     * 用户信息
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
        String token = request.getParameter("token");
        String channelNo = request.getParameter("channelNo");

        result = checkToken(result, token, channelNo);
        if(result.getCode()!=0)
        {
            return JSON.toJSONString(result);
        }

        UserVo vo = (UserVo)result.getData();

        try
        {
            UserModel user = userService.findById(vo.getId());
            int waitUser = lotteryOrderService.waitUse(user.getChannelNo(), user.getId());
            user.setWaitUse(waitUser);
            int isAdmin = merchantUserService.findByUserId(user.getChannelNo(), user.getId())==null?0:1;
            user.setIsAdmin(isAdmin);
            result.setData(user);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载用户资料出错");
            logger.error(e, "加载用户资料出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 用户关注+登陆状态
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/userStatus.do")
    @ResponseBody
    public String userStatus(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String token = request.getParameter("token");
        String channelNo = request.getParameter("channelNo");

        // logger.info("****userStatus,token="+token+",channelNo="+channelNo);
        if(isBlank(token)||isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        UserVo vo = this.getUserInfo(token);
        if(vo==null||!channelNo.equals(vo.getChannelNo()))
        {
            result.setCode(999);
            result.setInfo("token失效");
            return JSON.toJSONString(result);
        }

        // logger.info("****uservo:"+JSON.toJSONString(vo));

        result = userService.userStatus(vo.getId());
        if(result.getCode()==999)
        {
            wxUserRedisBiz.del(token);// 清除用户缓存数据
        }

        return JSON.toJSONString(result);
    }

    /**
     * 刷新用户关注状态
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/refreshFollow.do")
    @ResponseBody
    public String refreshFollow(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String token = request.getParameter("token");
        String channelNo = request.getParameter("channelNo");

        result = checkToken(result, token, channelNo);
        if(result.getCode()!=0)
        {
            return JSON.toJSONString(result);
        }

        UserVo vo = this.getUserInfo(token);

        result = wxMsgReplyService.refreshFollow(vo.getId());
        return JSON.toJSONString(result);
    }

}
