package com.family.demotest.web.adminController.lottery;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.demotest.service.lottery.LotteryOrderService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

@Controller("adminLotteryOrderController")
@RequestMapping("/admin/lotteryOrder")
public class LotteryOrderController
    extends BaseController
{
    @Autowired
    private LotteryOrderService lotteryOrderService;

    /**
     * 用户优惠券列表
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/userList.do")
    @ResponseBody
    public String userList(HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        String data = request.getParameter("data");
        JSONObject jsonObject = JSONObject.parseObject(data);
        String channelNo = jsonObject.getString("channelNo");
        String userId = jsonObject.getString("userId");
        int page = jsonObject.getIntValue("page");
        int pageSize = jsonObject.getIntValue("pageSize");
        if(validator.isBlank(channelNo)||validator.isBlank(userId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = lotteryOrderService.adminUserList(channelNo, userId, page, pageSize);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 活动领取/核销列表
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
        String lotteryId = jsonObject.getString("lotteryId");
        String search = jsonObject.getString("search");
        int status = jsonObject.getIntValue("status");// 0-领取 1-核销
        int type = jsonObject.getIntValue("type");// -1-全部 0-特等奖 1-1等奖 2 3 4
        int page = jsonObject.getIntValue("page");
        int pageSize = jsonObject.getIntValue("pageSize");
        if(validator.isBlank(channelNo)||validator.isBlank(lotteryId))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return JSON.toJSONString(result);
        }

        result = lotteryOrderService.list(channelNo, lotteryId, search, status, type, page, pageSize);

        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * 导出
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/export.do")
    @ResponseBody
    public String export(HttpServletRequest request, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            String channelNo = request.getParameter("channelNo");
            String lotteryId = request.getParameter("lotteryId");
            String search = request.getParameter("search");
            String status = request.getParameter("status");// 0-领取
                                                           // 1-核销
            String type = request.getParameter("type");// -1-全部
                                                       // 0-特等奖
                                                       // 1-1等奖 2
                                                       // 3 4
            if(validator.isBlank(channelNo)||validator.isBlank(lotteryId))
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return JSON.toJSONString(result);
            }
            Workbook wb = lotteryOrderService.newExport(channelNo, lotteryId, search, status, type);

            String fileName = Calendar.getInstance().getTimeInMillis()+".xlsx";

            OutputStream output = response.getOutputStream();
            response.reset();
            response.setContentType("application/msexcel; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename="+URLEncoder.encode(fileName, "utf-8"));
            wb.write(output);

            output.flush();
            output.close();
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("导出领取/核销列表出错");
            logger.error(e, "导出领取/核销列表出错");
        }
        return json(result);
    }
}
