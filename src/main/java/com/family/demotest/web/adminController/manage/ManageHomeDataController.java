package com.family.demotest.web.adminController.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.service.manage.ManageService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller("adminManageHomeDataController")
@RequestMapping("/admin/manageHomeData")
public class ManageHomeDataController
    extends BaseController
{

    @Autowired
    private ManageService manageService;

    /**
     * 总后台首页数据
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/countData.do")
    @ResponseBody
    public String countData(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();

        result = manageService.homeData();

        return JSON.toJSONString(result);
    }
}
