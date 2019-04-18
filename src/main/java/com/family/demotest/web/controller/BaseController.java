package com.family.demotest.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.family.base01.dao.orm.Orm;
import com.family.base01.util.BeanFactory;
import com.family.base01.util.Logger;
import com.family.base01.util.Security;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.WxUserRedisBiz;
import com.family.demotest.vo.user.UserVo;
import com.family.demotest.web.util.ResultCode;

public class BaseController
{
    private static final String QINIUIMAGE = "http://o80svg5jt.bkt.clouddn.com/";// 图片URL=QINIUIMAGE+KEY

    protected static final String COMMA = ",";
    private static final char CHARTOCOMMA = ',';
    protected ModelAndView mv = this.getModelAndView();
    @Autowired
    protected Validator validator;
    @Autowired
    protected Logger logger;
    @Autowired
    protected Security security;
    @Autowired
    protected WxUserRedisBiz wxUserRedisBiz;
    // @Value("${coral.access.secrey.key}")
    protected String secretKey = "jfelevenwujf654123js";

    /**
     * 拼接图片url
     * 
     * @return
     */
    protected String getImageUrl(String key)
    {
        if(validator.isBlank(key))
            return "";
        StringBuilder sb = new StringBuilder();
        if(!key.contains(COMMA))
        {
            return QINIUIMAGE+key;
        }
        String[] keys = key.split(COMMA);
        for(int i = 0; i<keys.length; i++)
        {
            if(!validator.isBlank(keys[i]))
            {
                sb.append(QINIUIMAGE+keys[i]).append(COMMA);
            }
            else
            {
                logger.error(new NullPointerException(), "********图片key 保存异常key:"+keys[i]);
                return "";
            }
        }
        char comma = sb.charAt(sb.length()-1);
        if(CHARTOCOMMA==comma)
        {
            sb.delete(sb.length()-1, sb.length());
        }
        return sb.toString();
    }

    protected void Ouput(HttpServletResponse response, String jsonStr)
    {

        close();// 关闭数据
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Origin", "*");// 设置跨域访问
        try
        {
            response.getWriter().write(jsonStr);
            response.getWriter().close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    protected void OputHtml(HttpServletResponse response, String htmlStr)
    {
        close();// 关闭数据
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Origin", "*");// 设置跨域访问
        try
        {
            response.getWriter().write(htmlStr);
            response.getWriter().close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    // public void setPdmenu(RequestMapData pd){
    // mv.addObject("pdm",pd);
    // }

    // public RequestMapData getPageData(){
    // return new RequestMapData(this.getRequest());
    // }

    public ModelAndView getModelAndView()
    {

        return new ModelAndView();
    }

    public HttpServletRequest getRequest()
    {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    /**
     * 验证签名
     * 
     * @param result
     * @param sign
     * @param str
     * @return
     */
    protected boolean checkSign(String sign, String... str)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<str.length; i++)
        {
            sb.append(str[i]);
        }
        String signPlainText = sb.append(secretKey).toString();

        String mySign = "";
        try
        {
            mySign = security.md5(signPlainText.getBytes("utf-8"));
        }
        catch(UnsupportedEncodingException e)
        {
            logger.error(e, "MD5加密出错");
            e.printStackTrace();
        }

        if(!sign.equals(mySign))
        {
            return false;
        }
        return true;
    }

    /**
     * 获取整型值
     * 
     * @param req
     * @param key
     * @param defaultValue
     * @return
     */
    protected Integer getIntValue(HttpServletRequest req, String key, Integer defaultValue)
    {
        String incomeValue = req.getParameter(key);
        if(StringUtils.isNotBlank(incomeValue))
        {
            try
            {
                return Integer.parseInt(incomeValue);
            }
            catch(NumberFormatException e)
            {
                logger.error(e, "字符转整型出错");
            }
        }
        return defaultValue;
    }

    /**
     * 获取字符串值
     * 
     * @param req
     * @param key
     * @param defaultValue
     * @return
     */
    protected String getStringValue(HttpServletRequest req, String key, String defaultValue)
    {
        String incomeValue = req.getParameter(key);
        return StringUtils.isNotBlank(incomeValue)?incomeValue:defaultValue;
    }

    /**
     * 判断是否为空(包含undefind)
     * 
     * @param str
     * @return
     */
    protected boolean isBlank(String string)
    {

        return string==null||string.trim().length()==0||"undefined".equals(string);
    }

    /**
     * 设置跨域访问
     * 
     * @param response
     */
    protected void setCrosDomain(HttpServletResponse response)
    {

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Origin", "*");// 设置跨域访问

    }

    protected void close()
    {
        BeanFactory.getBean(Orm.BEAN_NAME, Orm.class).close();
    }

    /**
     * json字符串拼接
     * 
     * @param data
     *            拼接数据
     * @return
     */
    protected String toJsonStr(String data)
    {

        // String jsonStr =
        // "{\"code\":0,\"data\":"+data+",\"message\":\"success\"}";
        StringBuffer sb = new StringBuffer("{\"code\":0,\"data\":").append(data).append(",\"message\":\"success\"}");

        return sb.toString();
    }

    protected String json(Object result)
    {
        return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss", SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.WriteMapNullValue);
    }

    protected String objToJson(Object result)
    {
        return JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 通过token获取用户信息
     * 
     * @param token
     * @return
     */
    protected UserVo getUserInfo(String token)
    {
        return wxUserRedisBiz.get(token);
    }

    /**
     * 验证token有效性
     * 
     * @param result
     * @param token
     * @param channelNo
     * @return
     */
    protected ResultCode checkToken(ResultCode result, String token, String channelNo)
    {
        if(isBlank(token)||isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("参数错误");
            return result;
        }
        UserVo vo = this.getUserInfo(token);
        if(vo==null||!channelNo.equals(vo.getChannelNo()))
        {
            result.setCode(999);
            result.setInfo("token失效");
            return result;
        }

        result.setData(vo);// 用户信息
        return result;
    }

}
