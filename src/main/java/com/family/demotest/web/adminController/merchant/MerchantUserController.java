package com.family.demotest.web.adminController.merchant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.entity.MerchantUserModel;
import com.family.demotest.service.merchant.MerchantUserService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 商户管理员管理
 * 
 * @author
 */
@Controller("adminMerchantUserController")
@RequestMapping("/admin/merchantUser")
public class MerchantUserController
    extends BaseController
{

    @Autowired
    private MerchantUserService merchantUserService;

    /**
     * 获取店铺管理员列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/adminList.do")
    @ResponseBody
    public String adminList(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String data = request.getParameter("data");
        JSONObject jsonObject = JSON.parseObject(data);
        String channelNo = jsonObject.getString("channelNo"); // 渠道号
        String merchantId = jsonObject.getString("merchantId"); // 商户id

        if(validator.isBlank(channelNo)||validator.isBlank(merchantId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = merchantUserService.adminList(channelNo, merchantId);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 添加店铺管理员
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/addAdmin.do")
    @ResponseBody
    public String addAdmin(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();

        String data = request.getParameter("data");
        JSONObject jsonObject = JSON.parseObject(data);
        String channelNo = jsonObject.getString("channelNo"); // 渠道号
        String merchantId = jsonObject.getString("merchantId"); // 商户id
        String userId = jsonObject.getString("userId"); // 用户id
        String roleCode = jsonObject.getString("roleCode"); // 角色编码 0-店员 1-店长

        if(validator.isBlank(channelNo)||validator.isBlank(merchantId)||validator.isBlank(userId)||validator.isBlank(roleCode))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = merchantUserService.addAdmin(channelNo, merchantId, userId, roleCode);

        return JSON.toJSONString(result);
    }

    /**
     * 删除店铺管理员
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
        JSONObject jsonObject = JSON.parseObject(data);
        String id = jsonObject.getString("id"); // 店铺用户关系id

        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        try
        {
            MerchantUserModel model = merchantUserService.findById(id);
            if(model==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            model.setProperty(1);
            merchantUserService.save(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("删除店铺管理员出错");
            logger.error(e, "删除店铺管理员出错");
        }

        return JSON.toJSONString(result);
    }

}
