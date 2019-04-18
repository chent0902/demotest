package com.family.demotest.web.adminController.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.entity.AdminModel;
import com.family.demotest.service.admin.AdminService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller("adminController")
@RequestMapping("/admin/admin")
public class AdminController
    extends
    BaseController
{

    @Autowired
    private AdminService adminService;

    /**
     * 管理员列表
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

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String channelNo = jsonObject.getString("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = adminService.list(channelNo);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 保存管理员
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

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);

        String channelNo = jsonObject.getString("channelNo");
        String account = jsonObject.getString("account");
        String password = jsonObject.getString("password");
        if(validator.isBlank(channelNo)||validator.isBlank(account)||validator.isBlank(password))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = adminService.save(channelNo, account, password);

        return JSON.toJSONString(result);
    }

    /**
     * 删除管理员
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

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");

        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        try
        {
            AdminModel model = adminService.findById(id);
            if(model==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            model.setProperty(1);
            adminService.save(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("删除代理商管理员出错");
            logger.error(e, "删除代理商管理员出错");
        }

        return JSON.toJSONString(result);
    }

}
