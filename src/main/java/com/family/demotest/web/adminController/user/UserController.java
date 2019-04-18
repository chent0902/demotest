package com.family.demotest.web.adminController.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.family.base01.util.PageList;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.user.UserService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller("adminUserController")
@RequestMapping("/admin/user")
public class UserController
    extends
    BaseController
{
    @Autowired
    private UserService userService;

    /**
     * 用户列表
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
        int page = jsonObject.getIntValue("page");
        int pageSize = jsonObject.getIntValue("pageSize");
        String search = jsonObject.getString("search");// 搜索关键字

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        PageList<UserModel> pageList = userService.getPageList(channelNo, search, page, pageSize);
        result.setData(pageList);

        return JSON.toJSONString(result);
    }
}
