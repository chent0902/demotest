package com.family.demotest.web.adminController.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.demotest.service.home.HomeService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller("adminHomeController")
@RequestMapping("/admin/home")
public class HomeController
    extends BaseController
{

    @Autowired
    private HomeService homeService;

    /**
     * 首页数据
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/homeData.do")
    @ResponseBody
    public String homeData(HttpServletRequest request, HttpServletResponse response)
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

        result = homeService.homeData(channelNo);

        return JSON.toJSONString(result);
    }

}
