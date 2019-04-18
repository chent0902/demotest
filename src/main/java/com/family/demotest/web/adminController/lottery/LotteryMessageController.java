package com.family.demotest.web.adminController.lottery;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.entity.LotteryMessageModel;
import com.family.demotest.service.lottery.LotteryMessageService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller("adminLotteryMessageController")
@RequestMapping("/admin/lotteryMessage")
public class LotteryMessageController
    extends BaseController
{

    @Autowired
    private LotteryMessageService lotteryMessageService;

    /**
     * 留言列表
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
        String lotteryId = dataJson.getString("lotteryId");
        int page = dataJson.getIntValue("page");
        int pageSize = dataJson.getIntValue("length");

        if(validator.isBlank(channelNo)||validator.isBlank(lotteryId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = lotteryMessageService.pageList(channelNo, lotteryId, pageSize, page);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 保存留言信息
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
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String channelNo = jsonObject.getString("channelNo");
        String lotteryId = jsonObject.getString("lotteryId");
        String content = jsonObject.getString("content");
        if(validator.isBlank(channelNo)||validator.isBlank(lotteryId)||validator.isBlank(content))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = lotteryMessageService.saveOrUpdate(channelNo, lotteryId, content);
        return JSON.toJSONString(result);
    }

    /**
     * 删除留言
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
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");
        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        LotteryMessageModel instance = lotteryMessageService.findById(id);
        if(instance==null)
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        instance.setProperty(1);
        try
        {
            lotteryMessageService.save(instance);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("删除留言出错");
            logger.error(e, "删除留言出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 留言精选
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/choiceness.do")
    @ResponseBody
    public String choiceness(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");
        int choiceness = jsonObject.getIntValue("choiceness");// 0-不精选 1-精选
        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        LotteryMessageModel instance = lotteryMessageService.findById(id);
        if(instance==null)
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        instance.setChoiceness(choiceness);
        try
        {
            lotteryMessageService.save(instance);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("留言精选出错");
            logger.error(e, "留言精选出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 留言回复
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/reply.do")
    @ResponseBody
    public String reply(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String messageId = jsonObject.getString("messageId");
        String reply = jsonObject.getString("reply");
        if(validator.isBlank(messageId)||validator.isBlank(reply))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = lotteryMessageService.reply(messageId, reply);
        return JSON.toJSONString(result);
    }

    /**
     * 留言精选
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/top.do")
    @ResponseBody
    public String top(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");
        int top = jsonObject.getIntValue("top");// 0-取消顶置 1-顶置
        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        LotteryMessageModel instance = lotteryMessageService.findById(id);
        if(instance==null)
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        instance.setTop(top);
        try
        {
            lotteryMessageService.save(instance);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("顶置留言出错");
            logger.error(e, "顶置留言出错");
        }

        return JSON.toJSONString(result);
    }

}
