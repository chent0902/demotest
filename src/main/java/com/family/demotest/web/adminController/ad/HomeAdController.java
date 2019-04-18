package com.family.demotest.web.adminController.ad;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.base01.util.PageList;
import com.family.demotest.entity.HomeAdModel;
import com.family.demotest.service.ad.HomeAdService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 首页轮播
 * 
 * @author wujf
 */
@Controller("manageHomeAdController")
@RequestMapping("/manage/homeAd")
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
    @RequestMapping("/list.do")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String data = request.getParameter("data");

            // json转对象
            JSONObject dataJson = JSONObject.parseObject(data);
            int page = dataJson.getIntValue("page");
            int pageSize = dataJson.getIntValue("length");

            HomeAdModel model = JSON.parseObject(data, HomeAdModel.class);
            if(validator.isBlank(model.getChannelNo()))
            {
                result.setCode(-1);
                result.setInfo("商家编号不能为空");
                return JSON.toJSONString(result);
            }
            // if(model.getType()==0)
            // {
            // result.setCode(-1);
            // result.setInfo("分类未指定");
            // return JSON.toJSONString(result);
            // }

            PageList<HomeAdModel> pageList = homeAdService.getPageList(model, pageSize, page);
            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取轮播/导航图列表出错");
            logger.error(e, "获取轮播/导航图列表出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 获取轮播/导航图详情
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
            result.setInfo("ID不能空");
            return JSON.toJSONString(result);
        }

        HomeAdModel model = homeAdService.findById(id);

        result.setData(model);

        return JSON.toJSONString(result);
    }

    @RequestMapping("/save.do")
    @ResponseBody
    public String save(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String data = request.getParameter("data");
            // data
            HomeAdModel ad = JSON.parseObject(data, HomeAdModel.class);

            if(validator.isBlank(ad.getChannelNo())||validator.isBlank(ad.getName())||validator.isBlank(ad.getImg()))
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }

            HomeAdModel model = validator.isBlank(ad.getId())?new HomeAdModel():homeAdService.findById(ad.getId());

            if(validator.isBlank(ad.getId()))
            {
                model.setChannelNo(ad.getChannelNo());
                model.setCreateTime(new Date());
            }
            model.setName(ad.getName());
            model.setImg(ad.getImg());
            model.setType(1);
            model.setLinkUrl(ad.getLinkUrl());
            model.setSort(ad.getSort());
            homeAdService.save(model);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存轮播出错");
            logger.error(e, "保存轮播图出错");
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public String delete(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String id = request.getParameter("id");

            if(validator.isBlank(id))
            {
                result.setCode(-1);
                result.setInfo("ID不能空");
                return JSON.toJSONString(result);
            }

            HomeAdModel model = new HomeAdModel();
            model.setId(id);
            homeAdService.deleteToRecycle(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("删除轮播出错");
            logger.debug(e, "删除轮播出错");
        }
        return JSON.toJSONString(result);
    }

    @RequestMapping("/updateSort.do")
    @ResponseBody
    public String updateSort(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String data = request.getParameter("data");
            // data
            HomeAdModel model = JSON.parseObject(data, HomeAdModel.class);

            HomeAdModel ad = homeAdService.findById(model.getId());
            if(ad==null)
            {
                result.setCode(-1);
                result.setInfo("对象不存在");
                return JSON.toJSONString(result);
            }
            ad.setSort(model.getSort());
            homeAdService.save(ad);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("修改排序出错");
            logger.debug(e, "修改排序出错");
        }
        return JSON.toJSONString(result);
    }
}
