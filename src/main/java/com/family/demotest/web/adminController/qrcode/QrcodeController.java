package com.family.demotest.web.adminController.qrcode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.family.base01.util.PageList;
import com.family.demotest.entity.QrcodeModel;
import com.family.demotest.service.qrcode.QrcodeService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 带参二维码
 * 
 * @author wujf
 *
 */
@Controller("adminQrcodeController")
@RequestMapping("/admin/qrcode")
public class QrcodeController
    extends BaseController
{

    @Autowired
    private QrcodeService qrcodeService;

    /**
     * 获取二维码列表
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
        String channelNo = request.getParameter("channelNo");
        int page = this.getIntValue(request, "page", 1);
        int pageSize = this.getIntValue(request, "length", 10);

        PageList<QrcodeModel> pageList = qrcodeService.getPageList(channelNo, page, pageSize);
        result.setData(pageList);

        return JSON.toJSONString(result);
    }

    /**
     * 通过id获取对象
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
            result.setInfo("id不能空");
            return JSON.toJSONString(result);
        }

        QrcodeModel model = qrcodeService.findById(id);
        result.setData(model);

        return JSON.toJSONString(result);
    }

    /**
     * 保存二维码
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
        String id = request.getParameter("id");
        String channelNo = request.getParameter("channelNo");
        String content = request.getParameter("content");// 文字内容
        String articlesContent = request.getParameter("articlesContent");// 图文内容
        String remark = request.getParameter("remark");// 备注

        int type = this.getIntValue(request, "type", -1);// 类型:0-文字，1-图文，2-图片

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("商家编号不能空");
            return JSON.toJSONString(result);
        }

        if(type==0)
        {
            if(validator.isBlank(content))
            {
                result.setCode(-1);
                result.setInfo("文字内容不能空");
                return JSON.toJSONString(result);
            }
        }
        else if(type==1)
        {
            if(validator.isBlank(articlesContent))
            {
                result.setCode(-1);
                result.setInfo("图文内容不能空");
                return JSON.toJSONString(result);
            }
        }
        else
        {
            result.setCode(-1);
            result.setInfo("类型错误");
            return JSON.toJSONString(result);

        }

        QrcodeModel model = new QrcodeModel();
        model.setId(id);
        model.setChannelNo(channelNo);
        model.setType(type);
        model.setContent(content);
        model.setArticlesContent(articlesContent);
        model.setRemark(remark);

        result = qrcodeService.saveQrcode(model);

        return JSON.toJSONString(result);
    }

    /**
     * 通过id获取对象
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
            result.setInfo("id不能空");
            return JSON.toJSONString(result);
        }

        QrcodeModel model = qrcodeService.findById(id);
        model.setProperty(1);
        qrcodeService.save(model);

        return JSON.toJSONString(result);
    }

}
