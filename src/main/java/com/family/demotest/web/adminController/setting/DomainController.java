package com.family.demotest.web.adminController.setting;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.demotest.entity.DomainModel;
import com.family.demotest.service.domain.DomainService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 模版消息
 * 
 * @author wujf
 *
 */
@Controller("adminDomainController")
@RequestMapping("/admin/domain")
public class DomainController
    extends
    BaseController
{

    @Autowired
    private DomainService domainService;

    /**
     * 通过站点域名配置信息
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getInfo.do")
    @ResponseBody
    public String getInfo(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String channelNo = request.getParameter("channelNo");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("商家编号不能为空");
            return JSON.toJSONString(result);
        }

        DomainModel domain = domainService.findByChannelNo(channelNo);

        result.setData(domain);
        // 设置跨域访问
        setCrosDomain(response);

        return JSON.toJSONString(result);
    }

    /**
     * 保存信息
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

            DomainModel domain = JSON.parseObject(data, DomainModel.class);
            if(validator.isBlank(domain.getChannelNo())||validator.isBlank(domain.getName())||validator.isBlank(domain.getName1()))
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }

            DomainModel model = domainService.findByChannelNo(domain.getChannelNo());
            if(model==null)
            {
                model = new DomainModel();
                model.setChannelNo(domain.getChannelNo());
                model.setCreateTime(new Date());
            }

            model.setName(domain.getName());// 主域名
            model.setName1(domain.getName1());// 业务域名

            domainService.save(model);

            // 保存到缓存
            domainService.putRedis(model.getChannelNo(), model);

            result.setData(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存域名设置出错");
            logger.error(e, "保存域名设置出错");

        }
        return JSON.toJSONString(result);
    }

}
