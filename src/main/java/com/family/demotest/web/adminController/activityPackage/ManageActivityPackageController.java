package com.family.demotest.web.adminController.activityPackage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.demotest.entity.ActivityPackageModel;
import com.family.demotest.service.activityPackage.ActivityPackageOrderService;
import com.family.demotest.service.activityPackage.ActivityPackageService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.IpUtils;
import com.family.demotest.web.util.ResultCode;

@Controller("adminActivityPackageController")
@RequestMapping("/admin/ActivityPackage")
public class ManageActivityPackageController
    extends
    BaseController
{

    @Autowired
    private ActivityPackageService packageService;
    @Autowired
    private ActivityPackageOrderService packageOrderService;

    /**
     * 套餐列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/buyList.do")
    @ResponseBody
    public String buyList(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        result = packageService.buyList();

        return JSON.toJSONString(result);
    }

    /**
     * 套餐列表
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

        result = packageService.list();

        return JSON.toJSONString(result);
    }

    /**
     * 保存套餐信息
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

        ActivityPackageModel model = JSON.parseObject(data, ActivityPackageModel.class);
        if(validator.isBlank(model.getName()))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = packageService.saveOrUpdate(model);

        return JSON.toJSONString(result);
    }

    /**
     * 获取套餐信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/get.do")
    @ResponseBody
    public String get(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String id = request.getParameter("id");

        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        try
        {
            ActivityPackageModel model = packageService.findById(id);
            if(model==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            result.setData(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取套餐信息出错");
            logger.error(e, "获取套餐信息出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 删除套餐
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
        try
        {
            ActivityPackageModel model = packageService.findById(id);
            if(model==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            model.setProperty(1);
            packageService.save(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("删除套餐出错");
            logger.error(e, "删除套餐出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 上下架
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/online.do")
    @ResponseBody
    public String online(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");
        int online = jsonObject.getIntValue("online");

        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        try
        {
            ActivityPackageModel model = packageService.findById(id);
            if(model==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            model.setOnline(online);
            packageService.save(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("删除套餐出错");
            logger.error(e, "删除套餐出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 购买套餐
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/b.do")
    @ResponseBody
    public String b(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String id = request.getParameter("id");
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(id)||validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        ActivityPackageModel model = packageService.findById(id);
        if(model==null)
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        String ip = IpUtils.getIpAddr(request);

        result = packageOrderService.buy(channelNo, model, ip);

        return JSON.toJSONString(result);
    }

    /**
     * 购买套餐
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/buy.do")
    @ResponseBody
    public String buy(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String channelNo = request.getParameter("channelNo");
        String name = request.getParameter("name");
        int num = this.getIntValue(request, "num", 1);
        int price = this.getIntValue(request, "price", 3000);// 单位(分)

        if(validator.isBlank(name)||validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        ActivityPackageModel model = new ActivityPackageModel();
        model.setNum(num);
        model.setName(name);
        model.setPrice(price);
        String ip = IpUtils.getIpAddr(request);

        result = packageOrderService.buy(channelNo, model, ip);

        return JSON.toJSONString(result);
    }

}
