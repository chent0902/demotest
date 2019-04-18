package com.family.demotest.web.adminController.login;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.demotest.entity.AdminModel;
import com.family.demotest.entity.AdminTokenModel;
import com.family.demotest.service.admin.AdminService;
import com.family.demotest.service.admin.AdminTokenService;
import com.family.demotest.vo.login.ResetPswVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.CookieUtils;
import com.family.demotest.web.util.ResultCode;

@Controller("adminLoginController")
@RequestMapping("/admin/login")
public class LoginController
    extends
    BaseController
{

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminTokenService adminTokenService;

    @RequestMapping("/login.do")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        if(StringUtils.isEmpty(account)||StringUtils.isEmpty(password))
        {
            result.setCode(-1);
            result.setInfo("帐号密码错误");
            return JSON.toJSONString(result);
        }

        result = adminService.login(account, password, response);

        return json(result);
    }

    /**
     * 总后台登录
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/ctLogin.do")
    @ResponseBody
    public String ctLogin(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String account = request.getParameter("account");
        String password = request.getParameter("password");
        if(validator.isBlank(account)||validator.isBlank(password))
        {
            result.setCode(-1);
            result.setInfo("账号或密码错误");
            return JSON.toJSONString(result);
        }

        try
        {
            AdminModel admin = adminService.ctLogin(account, password);
            if(admin==null)
            {
                result.setCode(-1);
                result.setInfo("账号或密码错误");
                return JSON.toJSONString(result);
            }
            AdminTokenModel tokenModel = adminTokenService.refresh(admin.getId());
            String token = tokenModel.getToken();
            CookieUtils.addCookie(response, "token", token, 2592000);

            JSONObject data = new JSONObject();
            data.put("token", token);
            data.put("channelNo", admin.getChannelNo());
            data.put("account", admin.getAccount());
            // data.put("roleID", admin.getRoleId());

            result.setData(data);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("登录异常");
            logger.error(e, "登录异常");
        }

        return json(result);
    }

    @RequestMapping("/refresh.do")
    @ResponseBody
    public String refresh(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String token = request.getParameter("token");

            AdminTokenModel currentTokenModel = adminTokenService.findByToken(token);
            if(currentTokenModel.getExpiredTime().after(Calendar.getInstance().getTime()))
            {
                throw new Exception("token已经过期");
            }

            AdminTokenModel tokenModel = adminTokenService.refresh(currentTokenModel.getAdminId());
            String newToken = tokenModel.getToken();
            CookieUtils.addCookie(response, "token", newToken, 2592000);

            JSONObject data = new JSONObject();
            data.put("token", newToken);
            result.setData(data);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("刷新token出错");
            logger.error(e, "刷新token出错");
        }
        return json(result);
    }

    /**
     * 获取token信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/tokenInfo.do")
    @ResponseBody
    public String tokenInfo(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String token = request.getParameter("token");

            AdminTokenModel currentTokenModel = adminTokenService.findByToken(token);
            if(currentTokenModel.getExpiredTime().after(Calendar.getInstance().getTime()))
            {
                throw new Exception("token已经过期");
            }

            result.setData(currentTokenModel);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取token信息出错");
            logger.error(e, "获取token信息出错");
        }
        return json(result);
    }

    /**
     * 重置密码
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/resetPws.do")
    @ResponseBody
    public String resetPws(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String data = request.getParameter("data");

            ResetPswVo vo = JSON.parseObject(data, ResetPswVo.class);

            if(validator.isBlank(vo.getChannelNo())||validator.isBlank(vo.getAccount())||validator.isBlank(vo.getNewPassword()))
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            result = adminService.resetPws(vo);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("重置用户密码出错");
            logger.error(e, "重置用户密码出错");
        }
        return json(result);
    }

}
