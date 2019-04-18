package com.family.demotest.web.adminController.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.entity.NoticeModel;
import com.family.demotest.service.notice.NoticeService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller
@RequestMapping("/admin/notice")
public class NoticeController
    extends BaseController
{

    @Autowired
    private NoticeService noticeService;

    /**
     * 公告列表
     * 
     * @return
     */
    @RequestMapping("/list.do")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        int page = this.getIntValue(request, "page", 1);
        int pageSize = this.getIntValue(request, "length", 10);

        result = noticeService.list(page, pageSize);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 保存公告信息
     * 
     * @return
     */
    @RequestMapping("/save.do")
    @ResponseBody
    public String save(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String noticeId = request.getParameter("noticeId");
        if(validator.isBlank(title)||validator.isBlank(content))
        {
            result.setCode(-1);
            result.setData("参数错误");
            return JSON.toJSONString(result);
        }
        result = noticeService.saveOrUpdate(noticeId, title, content);

        return JSON.toJSONString(result);
    }

    /**
     * 获取公告详情
     * 
     * @return
     */
    @RequestMapping("/get.do")
    @ResponseBody
    public String get(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String noticeId = request.getParameter("noticeId");

        if(validator.isBlank(noticeId))
        {
            result.setCode(-1);
            result.setData("参数错误");
            return JSON.toJSONString(result);
        }
        try
        {
            NoticeModel notice = noticeService.findById(noticeId);
            result.setData(notice);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setData("获取公告详情出错");
            logger.error(e, "获取公告详情出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 删除公告
     * 
     * @return
     */
    @RequestMapping("/delete.do")
    @ResponseBody
    public String delete(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String noticeId = request.getParameter("noticeId");
        if(validator.isBlank(noticeId))
        {
            result.setCode(-1);
            result.setData("参数错误");
            return JSON.toJSONString(result);
        }
        try
        {
            NoticeModel notice = noticeService.findById(noticeId);
            notice.setProperty(1);
            noticeService.save(notice);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setData("删除公告出错");
            logger.error(e, "删除公告出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 上下架
     * 
     * @return
     */
    @RequestMapping("/online.do")
    @ResponseBody
    public String online(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String noticeId = request.getParameter("noticeId");
        int online = this.getIntValue(request, "online", 0);// 传0下架 传1上架
        if(validator.isBlank(noticeId))
        {
            result.setCode(-1);
            result.setData("参数错误");
            return JSON.toJSONString(result);
        }
        try
        {
            NoticeModel notice = noticeService.findById(noticeId);
            notice.setOnline(online);
            noticeService.save(notice);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setData("上下架公告出错");
            logger.error(e, "上下架公告出错");
        }
        return JSON.toJSONString(result);
    }

}
