package com.family.demotest.web.controller.oss;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.family.demotest.service.oss.OssService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;

/**
 * 阿里云oss处理器
 * 
 * @author wujf
 *
 */
@Controller
@RequestMapping("/oss")
public class OSSController
    extends BaseController
{

    private static final String ENDPOINT = "oss-cn-hangzhou.aliyuncs.com";
    private static final String ACCESSID = "LTAI5bLJC7UwfnxH";
    private static final String ACCESSKEY = "2iUwlRYY2u9Q7SvFaximkSYcZkyqVd";
    private static final String BUCKET = "demotest";
    private static final String DIR = "xcx/";

    @Autowired
    protected OssService ossService;

    /**
     * 上次图片到OSS
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/updateImg.do", method = {RequestMethod.POST})
    @ResponseBody
    public String updateImg(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
    {

        ResultCode result = new ResultCode();
        // 原始名称
        String originalFilename = file.getOriginalFilename();
        InputStream inputStream = null;
        try
        {
            if(file.isEmpty())
            {
                result.setCode(-1);
                result.setInfo("文件不存在！");
                return JSON.toJSONString(result);
            }

            // 上传图片
            if(file!=null&&originalFilename!=null&&originalFilename.length()>0)
            {
                // 新的图片名称
                String newFileName = UUID.randomUUID().toString().replace("-", "")+originalFilename.substring(originalFilename.lastIndexOf("."));
                // 存储图片的物理路径
                // String pic_path = "D:\\imgs\\";
                // // 新图片
                // File newFile = new File(pic_path+newFileName);
                // // 将内存中的数据写入磁盘
                // file.transferTo(newFile);

                inputStream = file.getInputStream();

                String imgSign = ossService.uploadImg(inputStream, newFileName);
                if(!validator.isBlank(imgSign))
                {
                    String imgUrl = DIR+newFileName;

                    result.setData(imgUrl);
                }
                else
                {
                    result.setCode(-1);
                    result.setInfo("上次图片失败");
                }

                // 关闭流
                inputStream.close();

            }
        }
        catch(Exception e)
        {
            logger.error(e, "上次图片到OSS出错");
            result.setCode(-1);
            result.setInfo("上次图片到OSS出错");
            return JSON.toJSONString(result);
        }

        return JSON.toJSONString(result);
    }

    @RequestMapping("/getSign.do")
    @ResponseBody
    public void getSign(HttpServletRequest request, HttpServletResponse response)
    {
        // http://papaq.oss-cn-hangzhou.aliyuncs.com
        // String endpoint = "oss-cn-hangzhou.aliyuncs.com";
        // String accessId = "LTAI5bLJC7UwfnxH";
        // String accessKey = "2iUwlRYY2u9Q7SvFaximkSYcZkyqVd";
        // String bucket = "papaq";
        // String dir = "xcx/";
        String host = "http://"+BUCKET+"."+ENDPOINT;
        OSSClient client = new OSSClient(ENDPOINT, ACCESSID, ACCESSKEY);
        try
        {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis()+expireTime*1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, DIR);

            String postPolicy = client.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = client.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", ACCESSID);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            // respMap.put("expire", formatISO8601Date(expiration));
            respMap.put("dir", DIR);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime/1000));
            JSONObject ja1 = JSONObject.fromObject(respMap);

            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
            response(request, response, ja1.toString());

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void response(HttpServletRequest request, HttpServletResponse response, String results)
        throws IOException
    {
        String callbackFunName = request.getParameter("callback");
        if(callbackFunName==null||callbackFunName.equalsIgnoreCase(""))
            response.getWriter().println(results);
        else
            response.getWriter().println(callbackFunName+"( "+results+" )");
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

}
