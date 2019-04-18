//package com.family.coral.common.impl;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.context.request.ServletWebRequest;
//
//import com.family.base01.util.Cache;
//import com.family.base01.util.Converter;
//import com.family.base01.util.Logger;
//import com.family.base01.util.Security;
//import com.family.base01.util.Validator;
//import com.family.coral.common.api.SpringMVCHelper;
//
//
///**
// * @author WUJF
// */
//@Service
//public class SpringMVCHelperImpl
//    implements SpringMVCHelper
//{
//
//    private static final String SESSION_PREFIX = "action.action-helper.session:";
//    private static final String PAGE_SIZE = "pageSize";
//    private static final String CURRENT_PAGE = "currentPage";
//
//    @Autowired
//    private Logger logger;
//    @Autowired
//    private Validator validator;
//    @Autowired
//    private Converter converter;
//    @Autowired
//    private Security security;
//    @Autowired
//    private Cache cache;
//    private String rootPath;
//
//    @Override
//    public void setRootPath(String path)
//    {
//        rootPath = path.replace('\\', '/');
//
//        if(logger.isInfoEnable())
//            logger.info("设置项目根路径："+rootPath);
//    }
//
//    @Override
//    public String getRealPath(String path)
//    {
//        return validator.isBlank(path)?rootPath:rootPath+path.trim();
//    }
//
//
//    @Override
//    public  ServletRequestAttributes getServletRequestAttributes() {
//        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//    }
//    
//    @Override
//    public  ServletWebRequest getServletWebRequest() {
//        return ((ServletWebRequest) RequestContextHolder.getRequestAttributes());
//    }
//    
//    @Override
//    public HttpServletRequest getRequest()
//    {
//        return getServletRequestAttributes().getRequest();
//    }
//
//    @Override
//    public String getRequestUri(boolean suffix)
//    {
//        String uri = getRequest().getRequestURI();
//
//        return suffix?uri:uri.substring(0, uri.lastIndexOf('.'));
//    }
//
//    @Override
//    public Map<String, String> getRequestParameters()
//    {
//        Map<String, String> parameters = new HashMap<String, String>();
//        Map<String, String[]> parameterMap = getRequest().getParameterMap();
//        for(String key : parameterMap.keySet())
//            parameters.put(key, converter.toString(parameterMap.get(key), ","));
//
//        return parameters;
//    }
//
//    @Override
//    public String getRequestParameters(String[] includes, String[] excludes)
//    {
//        StringBuilder parameters = new StringBuilder();
//        Map<String, String[]> parameterMap = getRequest().getParameterMap();
//        for(String key : parameterMap.keySet())
//        {
//            if(validator.isBlank(key))
//                continue;
//
//            if(isExclude(excludes, key))
//                continue;
//
//            if(isInclude(includes, key))
//                parameters.append('&').append(key).append('=').append(converter.toString(parameterMap.get(key), ","));
//        }
//
//        return parameters.substring(parameters.length()==0?0:1);
//    }
//
//    private boolean isInclude(String[] includes, String key)
//    {
//        if(validator.isEmpty(includes))
//            return true;
//
//        for(int i = 0; i<includes.length; i++)
//            if(key.equalsIgnoreCase(includes[i]))
//                return true;
//
//        return false;
//    }
//
//    private boolean isExclude(String[] excludes, String key)
//    {
//        if(validator.isEmpty(excludes))
//            return false;
//
//        for(int i = 0; i<excludes.length; i++)
//            if(key.equalsIgnoreCase(excludes[i]))
//                return true;
//
//        return false;
//    }
//
//    @Override
//    public HttpServletResponse getResponse()
//    {
//        return getServletWebRequest().getResponse();
//    }
//
//
//    @Override
//    public HttpSession getSession()
//    {
//        return getRequest().getSession();
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <T> T getSessionAttribute(String name)
//    {
//        return (T)getSession().getAttribute(name);
//    }
//
//    @Override
//    public String getSessionId()
//    {
//        return getRequest().getSession().getId();
//    }
//
//    @Override
//    public void putToSession(Object key, Object value)
//    {
//        Map<Object, Object> session = getSess();
//        session.put(key, value);
//        putSession(session);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <T> T getFromSession(Object key)
//    {
//        return (T)getSess().get(key);
//    }
//
//    @Override
//    public void removeFromSession(Object key)
//    {
//        Map<Object, Object> session = getSess();
//        session.remove(key);
//        putSession(session);
//    }
//
//    private Map<Object, Object> getSess()
//    {
//        Map<Object, Object> session = cache.get(getSessionKey());
//
//        if(session==null)
//        {
//            if(logger.isInfoEnable())
//                logger.info("无法获得session["+getSessionId()+"]信息。");
//
//            session = new HashMap<Object, Object>();
//            putSession(session);
//        }
//
//        return session;
//    }
//
//    private void putSession(Map<Object, Object> session)
//    {
//        cache.put(getSessionKey(), session);
//    }
//
//    private String getSessionKey()
//    {
//        return SESSION_PREFIX+getSessionId();
//    }
//
//
//
//    @Override
//    public int getPageSize()
//    {
//        return getParameterAsInt(PAGE_SIZE);
//    }
//
//    @Override
//    public int getCurrentPage()
//    {
//        return getParameterAsInt(CURRENT_PAGE);
//    }
//
//    private int getParameterAsInt(String name)
//    {
//        String string = getRequest().getParameter(name);
//
//        return validator.isBlank(string)?0:Integer.parseInt(string);
//    }
//
//    @Override
//    public void setRequestAttribute(String key, Object obj)
//    {
//        getRequest().setAttribute(key, obj);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public <T> T getRequestAttribute(String key)
//    {
//        return (T)getRequest().getAttribute(key);
//    }
//
//
//
//
//}
