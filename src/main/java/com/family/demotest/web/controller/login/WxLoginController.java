package com.family.demotest.web.controller.login;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.common.biz.StringRedisBiz;
import com.family.demotest.common.constant.Constants;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.login.LoginService;
import com.family.demotest.service.sms.SmsService;
import com.family.demotest.service.user.UserService;
import com.family.demotest.vo.user.UserVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;
import com.family.demotest.web.util.UUIDUtil;

@Controller
@RequestMapping("/wxlogin")
public class WxLoginController
    extends
    BaseController
{

    @Autowired
    private StringRedisBiz stringRedisBiz;
    @Autowired
    private LoginService loginService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private UserService userService;

    /**
     * 登录入口
     * 
     * @param partnerCode
     * @param redirect_uri
     * @param response
     */
    @RequestMapping("/login.do")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();

        String channelNo = request.getParameter("channelNo");// 商家编号
        String redirectUri = request.getParameter("redirectUri");// 需要跳转的链接地址

        if(validator.isBlank(channelNo)||validator.isBlank(redirectUri))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        String authUrl = loginService.getAuthUrl(channelNo, redirectUri);
        if(validator.isBlank(authUrl))
        {
            result.setCode(-1);
            result.setInfo("获取授权地址出错，请重新获取");
            return JSON.toJSONString(result);
        }

        result.setData(authUrl);

        return JSON.toJSONString(result);
    }

    /**
     * 用户允许授权后，回调处理
     * 
     * @param state
     * @param code
     * @param response
     */
    @RequestMapping("/auth.do")
    public void auth(HttpServletRequest request, HttpServletResponse response)
    {
        // ResultCode result = new ResultCode();
        String channelNo = request.getParameter("state");// 商家编号
        String code = request.getParameter("code");// 获取accessToken的code
        String authkey = request.getParameter("authkey");// 保存重定向uri的缓存key
        String appid = request.getParameter("appid");// 公众号appid

        // logger.info("*********授权回调返回参数：channelNo="+channelNo+",code="+code+",authkey="+authkey+",appid="+appid);
        if(validator.isBlank(code))
        {
            return;
        }
        // 通过code换取access_token并且获取用户信息
        UserModel model = loginService.getUserInfo(channelNo, code, appid);

        // 如果授权获取用户信息失败，重新授权
        if(model==null)
        {
            logger.info("****没有获取到用户信息哦哦哦哦,重新来一次");
            // 如果获取不到openId,重新获取一次
            // try
            // {
            // Thread.sleep(500L);// 睡眠500毫秒
            // String redirectUri =
            // stringRedisBiz.get(Constants.WX_AUTH_CALL_URL+authkey);
            // String hostUrl = loginService.getHostUrl();
            // response.sendRedirect(hostUrl+"/wxlogin/login.do?channelNo="+channelNo+"&redirectUri="+redirectUri);
            // }
            // catch(Exception e)
            // {
            // logger.error(e, "****地址重定向跳转出错1");
            // }
            return;
        }

        // 用户登录
        doLogin(response, authkey, model);

    }

    /**
     * 登录
     * 
     * @param response
     * @param result
     * @param model
     * @return
     */
    private void doLogin(HttpServletResponse response, String authKey, UserModel user)
    {

        // 重定向跳转地址
        String redirectUri = stringRedisBiz.get(Constants.WX_AUTH_CALL_URL+authKey);
        // 生成token
        String token = UUIDUtil.get32UUID();
        // String token = "";
        // if(user!=null)
        // {
        // token = user.getId();
        // // 保存用户信息到缓存(保存7天)
        // userService.putUserVo(token, user);
        // }

        userService.putUserVo(token, user);
        // logger.info("*****回调地址："+redirectUri);
        // 保存到数据库
        try
        {
            response.sendRedirect(redirectUri+"&token="+token);
        }
        catch(IOException e)
        {
            logger.error(e, "****地址重定向跳转出错2");
        }
    }

    /**
     * 用户登录(手机号)
     * 
     * @param phone
     * @param response
     */
    @RequestMapping(value = "/telLogin.do")
    @ResponseBody
    public String telLogin(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();

        String channelNo = request.getParameter("channelNo");// 商家编号
        String token = request.getParameter("token"); // 用户id
        String phone = request.getParameter("phone"); // 手机号
        String code = request.getParameter("code"); // 验证码

        result = checkToken(result, token, channelNo);
        if(result.getCode()!=0)
        {
            return JSON.toJSONString(result);
        }

        UserVo vo = (UserVo)result.getData();

        if(isBlank(phone))
        {
            result.setCode(-1);
            result.setInfo("手机号不能为空");
            return JSON.toJSONString(result);
        }

        if(isBlank(code))
        {
            result.setCode(-1);
            result.setInfo("验证码不能为空");
            return JSON.toJSONString(result);
        }

        // 通过手机号拿到刚发送的验证
        String smsCode = smsService.getSMSCode(phone);
        // 判断输入的验证码是否正确
        if(!code.equals(smsCode))
        {
            result.setCode(-1);
            result.setInfo("验证码错误");
            return JSON.toJSONString(result);
        }

        // 验证该手机号是否已绑定了
        boolean isTel = userService.existTel(channelNo, phone);
        if(isTel)
        {
            result.setCode(-1);
            result.setInfo("该手机已注册,请更换手机号码");
            return JSON.toJSONString(result);
        }

        UserModel user = userService.findById(vo.getId());
        if(user!=null)
        {
            user.setTel(phone);
            userService.save(user);
        }

        return JSON.toJSONString(result);
    }

    /**
     * 获取手机验证码
     * 
     * @param phone
     * @param response
     */
    @RequestMapping(value = "/getSmsCode.do")
    @ResponseBody
    public String getSmsCode(String phone, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();

        if(validator.isBlank(phone))
        {
            result.setCode(-1);
            result.setInfo("手机号码不能为空");

            return JSON.toJSONString(result);
        }

        result = smsService.getSmsCode(phone);

        return JSON.toJSONString(result);
    }

    /**
     * 保存用户手机号
     * 
     * @param phone
     * @param response
     */
    @RequestMapping(value = "/saveTel.do")
    @ResponseBody
    public String saveTel(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();

        String channelNo = request.getParameter("channelNo");// 商家编号
        String token = request.getParameter("token"); // 用户id
        String phone = request.getParameter("phone"); // 手机号
        // String name = request.getParameter("name"); // 用户姓名

        result = checkToken(result, token, channelNo);
        if(result.getCode()!=0)
        {
            return JSON.toJSONString(result);
        }

        UserVo vo = (UserVo)result.getData();

        if(isBlank(phone))
        {
            result.setCode(-1);
            result.setInfo("手机号不能为空");
            return JSON.toJSONString(result);
        }

        // 验证该手机号是否已绑定了
        // boolean isTel = userService.existTel(channelNo, phone);
        // if(isTel)
        // {
        // result.setCode(-1);
        // result.setInfo("该手机已注册,请更换手机号码");
        // return JSON.toJSONString(result);
        // }

        UserModel user = userService.findById(vo.getId());
        if(user!=null)
        {
            user.setTel(phone);
            userService.save(user);
        }

        return JSON.toJSONString(result);
    }

}
