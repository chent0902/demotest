package com.family.demotest.web.controller.merchant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.entity.MerchantModel;
import com.family.demotest.service.merchant.MerchantService;
import com.family.demotest.vo.user.UserVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller
@RequestMapping("/merchant")
public class MerchantController
    extends
    BaseController
{

    @Autowired
    private MerchantService merchantService;

    /**
     * 商户信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/info.do")
    @ResponseBody
    public String info(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();

        String channelNo = request.getParameter("channelNo");
        String token = request.getParameter("token");

        if(validator.isBlank(channelNo)||validator.isBlank(token))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        UserVo vo = this.getUserInfo(token);
        if(vo==null||!channelNo.equals(vo.getChannelNo()))
        {
            result.setCode(999);
            result.setInfo("token失效");
            return JSON.toJSONString(result);
        }
        result = merchantService.findByUserId(channelNo, vo.getId());

        return JSON.toJSONString(result);
    }

    /**
     * 修改核销密码
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/updatePwd.do")
    @ResponseBody
    public String updatePwd(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();

        String merchantId = request.getParameter("merchantId");//
        String pwd = request.getParameter("pwd");

        if(validator.isBlank(merchantId)||validator.isBlank(pwd))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        try
        {
            MerchantModel model = merchantService.findById(merchantId);
            if(model==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            model.setPassword(pwd);
            merchantService.save(model);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("修改核销密码出错");
            logger.error(e, "修改核销密码出错");
        }

        return JSON.toJSONString(result);
    }

}
