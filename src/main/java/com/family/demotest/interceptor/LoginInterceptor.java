package com.family.demotest.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.family.demotest.service.admin.AdminTokenService;

public class LoginInterceptor
    implements
    HandlerInterceptor
{

    @Autowired
    private AdminTokenService adminTokenService;

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
        throws Exception
    {

    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
        throws Exception
    {

    }

    @Override
    public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2)
        throws Exception
    {
        // arg1.setCharacterEncoding("utf-8");
        // arg1.setContentType("text/json;charset=utf-8");
        // ResultCode result = new ResultCode();
        // String token = arg0.getParameter("token");
        //
        // if(StringUtils.isEmpty(token))
        // {
        // result.setCode(9999);
        // result.setInfo("登录超时");
        //
        // PrintWriter writer = arg1.getWriter();
        // writer.write(JSON.toJSONString(result));
        // writer.close();
        // return false;
        // }
        // else
        // {
        // AdminTokenModel tokenModel = adminTokenService.findByToken(token);
        // if(tokenModel==null)
        // {
        // result.setCode(9999);
        // result.setInfo("登录超时");
        //
        // PrintWriter writer = arg1.getWriter();
        // writer.write(JSON.toJSONString(result));
        // writer.close();
        // return false;
        // }
        //
        // // TODO 全局参数应该传channelNo，channelNo和token再做一次匹配
        // }
        return true;
    }

}
