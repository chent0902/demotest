package com.family.demotest.web.adminController.imgText;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.base01.util.PageList;
import com.family.demotest.entity.ImgTextModel;
import com.family.demotest.service.imgText.ImgTextService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 图文
 * 
 * @author Chen
 *
 */
@Controller("adminImgTextController")
@RequestMapping("/admin/img/text")
public class ImgTextController
    extends BaseController
{
    @Autowired
    private ImgTextService imgTextService;

    /**
     * 图文列表
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
        JSONObject dataJson = JSONObject.parseObject(data);
        String channelNo = dataJson.getString("channelNo");
        int page = dataJson.getIntValue("page");
        int pageSize = dataJson.getIntValue("length");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        PageList<ImgTextModel> pageList = imgTextService.pageList(channelNo, pageSize, page);
        result.setData(pageList);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 保存/修改图文
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/saveOrUpdate.do")
    @ResponseBody
    public String saveOrUpdate(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        try
        {
            String data = request.getParameter("data");
            ImgTextModel model = JSON.parseObject(data, ImgTextModel.class);
            if(validator.isBlank(model.getChannelNo()))
            {
                result.setCode(-1);
                result.setInfo("渠道号不能为空");
                return JSON.toJSONString(result);
            }
            ImgTextModel imgTextModel = null;
            if(validator.isBlank(model.getId()))
            {
                imgTextModel = new ImgTextModel();
                imgTextModel.setChannelNo(model.getChannelNo());
            }
            else
            {

                imgTextModel = imgTextService.findById(model.getId());
                if(imgTextModel==null)
                {
                    result.setCode(-1);
                    result.setInfo("该图文不存在");
                    return JSON.toJSONString(result);
                }
            }

            imgTextModel.setImg(model.getImg());
            imgTextModel.setIntro(model.getIntro());
            imgTextModel.setTitle(model.getTitle());
            imgTextModel.setContent(model.getContent());
            imgTextModel.setMessageSwitch(model.getMessageSwitch());
            imgTextModel.setCreateTime(new Date());
            imgTextService.save(imgTextModel);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存或修改图文出错");
            logger.error(e, "保存或修改图文出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 获取图文详情
     * 
     * @return
     */
    @RequestMapping("/get.do")
    @ResponseBody
    public String get(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String id = request.getParameter("id");// 图文id

        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setData("图文id出错");
            return JSON.toJSONString(result);
        }
        try
        {
            ImgTextModel imgTextModel = imgTextService.findById(id);
            result.setData(imgTextModel);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setData("获取图文详情出错");
            logger.error(e, "获取图文详情出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 根据图文id删除图文
     * 
     * @return
     */
    @RequestMapping("/delete.do")
    @ResponseBody
    public String delete(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String id = request.getParameter("id");// 图文id

        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setData("图文id出错");
            return JSON.toJSONString(result);
        }
        try
        {
            ImgTextModel imgTextModel = imgTextService.findById(id);
            imgTextService.deleteToRecycle(imgTextModel);
            result.setData(imgTextModel);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setData("删除图文出错");
            logger.error(e, "删除图文出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 修改图文排序
     * 
     * @return
     */
    @RequestMapping("/changeSort.do")
    @ResponseBody
    public String changeSort(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");// 图文id
        int sort = jsonObject.getIntValue("sort");// 顺序

        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setData("图文id出错");
            return JSON.toJSONString(result);
        }
        try
        {
            ImgTextModel imgTextModel = imgTextService.findById(id);
            if(imgTextModel==null)
            {
                result.setCode(-1);
                result.setData("该图文不存在");
                return JSON.toJSONString(result);
            }
            imgTextModel.setSort(sort);
            imgTextService.save(imgTextModel);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setData("修改图文顺序出错");
            logger.error(e, "修改图文顺序出错");
        }
        return JSON.toJSONString(result);
    }

    /**
     * 修改图文虚拟数
     * 
     * @return
     */
    @RequestMapping("/changeVir.do")
    @ResponseBody
    public String changeVir(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");// 图文id
        int type = jsonObject.getIntValue("type");// 0-修改虚拟访问数，1-修改虚拟点赞数
        int virVisit = jsonObject.getIntValue("virVisit");// 虚拟访问数
        int virClick = jsonObject.getIntValue("virClick");// 虚拟点赞数

        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setData("图文id出错");
            return JSON.toJSONString(result);
        }
        try
        {
            ImgTextModel imgTextModel = imgTextService.findById(id);
            if(imgTextModel==null)
            {
                result.setCode(-1);
                result.setData("该图文不存在");
                return JSON.toJSONString(result);
            }
            if(type==0)
            {
                imgTextModel.setVirVisit(virVisit);
            }
            else if(type==1)
            {

                imgTextModel.setVirClick(virClick);
            }
            imgTextService.save(imgTextModel);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setData("修改图文虚拟数出错");
            logger.error(e, "修改图文虚拟数出错");
        }
        return JSON.toJSONString(result);
    }

}
