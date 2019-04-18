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
import com.family.demotest.entity.LotteryDetailsModel;
import com.family.demotest.entity.LotteryModel;
import com.family.demotest.service.lottery.LotteryDetailsService;
import com.family.demotest.service.lottery.LotteryService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller("adminLotteryController")
@RequestMapping("/admin/lottery")
public class LotteryController
    extends
    BaseController
{

    @Autowired
    private LotteryService lotteryService;
    @Autowired
    private LotteryDetailsService lotteryDetailsService;

    /**
     * 抽奖列表
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
        int type = dataJson.getIntValue("type"); // 0-全部 1-进行中 2-已结束 3-已截止
        String name = dataJson.getString("search"); // 活动/商户名称模糊查询

        int page = dataJson.getIntValue("page");
        int pageSize = dataJson.getIntValue("length");

        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        result = lotteryService.pageList(channelNo, type, name, pageSize, page);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 保存抽奖信息
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
            LotteryModel model = JSON.parseObject(data, LotteryModel.class);
            if(validator.isBlank(model.getChannelNo())||validator.isBlank(model.getMerchantId())||validator.isBlank(model.getTitle())
                    ||validator.isBlank(model.getImg())||validator.isBlank(model.getPrice()))
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            result = lotteryService.saveOrUpdate(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存活动出错");
            logger.error(e, "保存活动出错***");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 获取抽奖详情
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
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");

        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        try
        {
            LotteryModel instance = lotteryService.findById(id);
            if(instance==null)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            LotteryDetailsModel details = lotteryDetailsService.findByLotteryId(instance.getId());
            instance.setDetails(details!=null&&!validator.isBlank(details.getDeatils())?details.getDeatils():"");
            result.setData(instance);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取抽奖详情出错");
            logger.error(e, "获取抽奖详情出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 删除抽奖活动
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
        result = lotteryService.delete(id);
        return JSON.toJSONString(result);
    }

    /**
     * 上下架
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/online.do")
    @ResponseBody
    public String online(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");
        int online = jsonObject.getIntValue("online");// 传0使下架 1-上架
        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        LotteryModel instance = lotteryService.findById(id);
        if(instance==null)
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        instance.setOnline(online);
        try
        {
            lotteryService.save(instance);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("抽奖活动上下架出错");
            logger.error(e, "抽奖活动上下架出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 修改访问数
     * 
     * @param request
     * @param response
     * @return
     * 
     */
    @RequestMapping("/updateVirtualNum.do")
    @ResponseBody
    public String updateVirtualNum(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");
        int type = jsonObject.getIntValue("type");// 修改类型 0-虚拟关注 1-虚拟领取 2-虚拟点赞
        int num = jsonObject.getIntValue("num");// 虚拟数
        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        LotteryModel instance = lotteryService.findById(id);
        if(instance==null)
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        if(type==0)
        {
            instance.setVirtualAttention(num);
        }
        else if(type==1)
        {
            instance.setVirtualReceive(num);
        }
        else
        {
            instance.setVirtualLike(num);
        }

        lotteryService.save(instance);

        return JSON.toJSONString(result);
    }

    /**
     * 排序
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/sort.do")
    @ResponseBody
    public String sort(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String id = jsonObject.getString("id");
        int sort = jsonObject.getIntValue("sort");
        if(validator.isBlank(id))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        LotteryModel instance = lotteryService.findById(id);
        if(instance==null)
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }
        instance.setSort(sort);
        try
        {
            lotteryService.save(instance);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("修改抽奖活动排序出错");
            logger.error(e, "修改抽奖活动排序出错");
        }

        return JSON.toJSONString(result);
    }

    /**
     * 抽奖列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/allList.do")
    @ResponseBody
    public String allList(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        String data = request.getParameter("data");
        JSONObject dataJson = JSONObject.parseObject(data);
        int type = dataJson.getIntValue("type"); // 0-全部 1-进行中 2-已结束 3-已截止
        String name = dataJson.getString("search"); // 活动/商户名称模糊查询
        int page = dataJson.getIntValue("page");
        int pageSize = dataJson.getIntValue("length");

        result = lotteryService.allList(type, name, pageSize, page);
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

}
