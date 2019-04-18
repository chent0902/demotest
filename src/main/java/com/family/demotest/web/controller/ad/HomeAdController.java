package com.family.demotest.web.controller.ad;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.entity.HomeAdModel;
import com.family.demotest.service.ad.HomeAdService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 首页轮播
 * 
 * @author wujf
 */
@Controller
@RequestMapping("/ad")
public class HomeAdController
    extends
    BaseController
{

    @Autowired
    private HomeAdService homeAdService;

    /**
     * 获取轮播
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getList.do")
    @ResponseBody
    public String getList(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String channelNo = request.getParameter("channelNo");

            if(validator.isBlank(channelNo))
            {
                result.setCode(-1);
                result.setInfo("商家编号不能为空");
                return JSON.toJSONString(result);
            }

            List<HomeAdModel> pageList = homeAdService.getList(channelNo, 1);
            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取轮播图列表出错");
            logger.error(e, "获取轮播图列表出错");
        }
        return JSON.toJSONString(result);
    }

}
