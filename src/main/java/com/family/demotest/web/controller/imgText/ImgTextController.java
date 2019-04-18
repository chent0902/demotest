package com.family.demotest.web.controller.imgText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.base01.util.PageList;
import com.family.demotest.common.biz.ImgTextSummaryRedisBiz;
import com.family.demotest.entity.ImgTextModel;
import com.family.demotest.service.imgText.ImgTextService;
import com.family.demotest.vo.total.ImgTextSummaryVo;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 图文
 * 
 * @author Chen
 *
 */
@Controller("imgTextController")
@RequestMapping("/img/text")
public class ImgTextController
    extends BaseController
{
    @Autowired
    private ImgTextService imgTextService;
    @Autowired
    private ImgTextSummaryRedisBiz summaryRedisBiz;

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
        String channelNo = request.getParameter("channelNo");
        int page = this.getIntValue(request, "page", 1);
        int pageSize = this.getIntValue(request, "length", 10);

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        PageList<ImgTextModel> pageList = imgTextService.pageList(channelNo, pageSize, page);
        List<ImgTextModel> list = new ArrayList<>();
        if(pageList!=null&&pageList.getList().size()>0)
        {
            for(ImgTextModel textModel : pageList.getList())
            {
                ImgTextModel vo = new ImgTextModel();
                ImgTextSummaryVo summaryVo = summaryRedisBiz.get(textModel.getId());
                if(summaryVo!=null)
                {
                    vo.setMessage(vo.getMessage());
                    vo.setLike(vo.getLike());
                    vo.setVisitNum(summaryVo.getVisitNum());
                }
                vo.setChannelNo(textModel.getChannelNo());
                vo.setCreateTime(textModel.getCreateTime());
                vo.setId(textModel.getId());
                vo.setTitle(textModel.getTitle());
                vo.setImg(textModel.getImg());
                vo.setVirClick(textModel.getVirClick());
                vo.setVirVisit(textModel.getVirVisit());
                list.add(vo);
            }
        }
        result.setData(list);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 获取图文详情
     * 
     * @return
     */
    @RequestMapping("/getInfo.do")
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
            if(imgTextModel!=null)
            {
                ImgTextSummaryVo summary = summaryRedisBiz.get(imgTextModel.getId());
                if(summary==null)
                {
                    summary = new ImgTextSummaryVo();
                    summary.setVisitNum(1);
                }
                else
                {
                    summary.setVisitNum(summary.getVisitNum()+1);

                }
                imgTextModel.setLike(summary.getLike());
                imgTextModel.setVisitNum(summary.getVisitNum());
                summaryRedisBiz.put(imgTextModel.getId(), summary, 90, TimeUnit.DAYS);
            }

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
     * 图文点赞
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/like.do")
    @ResponseBody
    public String like(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String imgTextId = request.getParameter("imgTextId");
        if(validator.isBlank(imgTextId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        try
        {
            ImgTextSummaryVo vo = summaryRedisBiz.get(imgTextId);
            if(vo==null)
            {
                vo = new ImgTextSummaryVo();
            }
            vo.setLike(vo.getLike()+1);
            summaryRedisBiz.put(imgTextId, vo, 30, TimeUnit.DAYS);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("图文点赞出错");
            logger.error(e, "图文点赞出错");
        }

        return JSON.toJSONString(result);
    }

}
