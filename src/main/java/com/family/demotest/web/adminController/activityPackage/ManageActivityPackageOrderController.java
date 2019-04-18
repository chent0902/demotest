package com.family.demotest.web.adminController.activityPackage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.service.activityPackage.ActivityPackageOrderService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller("adminActivityPackageOrderController")
@RequestMapping("/admin/ActivityPackageOrder")
public class ManageActivityPackageOrderController
    extends BaseController
{

    @Autowired
    private ActivityPackageOrderService packageOrderService;

    /**
     * 充值记录
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/payRecord.do")
    @ResponseBody
    public String payRecord(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String channelNo = jsonObject.getString("channelNo");
        int page = jsonObject.getIntValue("page");
        int pageSize = jsonObject.getIntValue("pageSize");
        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = packageOrderService.payRecord(channelNo, page, pageSize);

        return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss", SerializerFeature.DisableCircularReferenceDetect);
    }

}
