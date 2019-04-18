package com.family.demotest.web.adminController.merchant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.demotest.entity.MerchantModel;
import com.family.demotest.service.merchant.MerchantService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 商户管理
 * 
 * @author lim
 *
 */
@Controller("adminMerchantController")
@RequestMapping("/admin/merchant")
public class MerchantController
    extends BaseController
{
    @Autowired
    private MerchantService merchantService;

    /**
     * 获取商户列表
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
            JSONObject jsonObject = JSONObject.parseObject(data);
            String channelNo = jsonObject.getString("channelNo");
            int page = jsonObject.getIntValue("page");
            int pageSize = jsonObject.getIntValue("length");

            if(validator.isBlank(channelNo))
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }

            result = merchantService.pageList(channelNo, page, pageSize);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取商家信息出错");
            logger.error(e, "获取商家信息出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 新增商户
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
        try
        {
            String data = request.getParameter("data");

            // json转对象
            MerchantModel merchant = JSON.parseObject(data, MerchantModel.class);
            if(validator.isBlank(merchant.getChannelNo())||validator.isBlank(merchant.getName())||validator.isBlank(merchant.getLogo())
                    ||validator.isBlank(merchant.getAddress())||validator.isBlank(merchant.getTel())||validator.isBlank(merchant.getPassword()))
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            result = merchantService.createOrUpdate(merchant);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存商家信息出错");
            logger.error(e, "保存商家信息出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 获取商户信息
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
        try
        {
            String data = request.getParameter("data");
            JSONObject jsonObject = JSONObject.parseObject(data);
            String merchantId = jsonObject.getString("id");
            if(validator.isBlank(merchantId))
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            MerchantModel model = merchantService.findById(merchantId);
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
            result.setInfo("获取商家信息出错");
            logger.error(e, "获取商家信息出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 修改商家核销密码
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/updatePassword.do")
    @ResponseBody
    public String updatePassword(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String data = request.getParameter("data");
            JSONObject jsonObject = JSONObject.parseObject(data);
            String merchantId = jsonObject.getString("id");
            String password = jsonObject.getString("password");

            if(validator.isBlank(merchantId)||validator.isBlank(password))
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            MerchantModel merchant = merchantService.findById(merchantId);
            if(merchant==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            merchant.setPassword(password);
            merchantService.save(merchant);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("修改商家核销密码出错");
            logger.error(e, "修改商家核销密码出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 删除商户
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
        try
        {
            String data = request.getParameter("data");
            JSONObject jsonObject = JSONObject.parseObject(data);
            String merchantId = jsonObject.getString("id");

            if(validator.isBlank(merchantId))
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            MerchantModel merchant = merchantService.findById(merchantId);
            if(merchant==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            merchant.setProperty(1);
            merchantService.save(merchant);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("删除商家出错");
            logger.error(e, "删除商家出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 获取商户列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/findAllName.do")
    @ResponseBody
    public String findAllName(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String channelNo = jsonObject.getString("channelNo");
        String name = jsonObject.getString("name");// 模糊查询 商户名

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = merchantService.findAllName(channelNo, name);

        return JSON.toJSONString(result);
    }

}
