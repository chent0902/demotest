package com.family.demotest.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * http请求工具类
 * 
 * @author lim
 *
 */
public class HttpCommonUtil
{
    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";

    static
    {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
    }

    /**
     * HTTP Get 获取内容
     * 
     * @param url请求的url地址
     *            ?之前的地址
     * @param params请求的参数
     * @param charset编码格式
     * @return 页面内容
     */
    public static String sendGet(String url, Map<String, Object> params)
    {
        if(!StringUtils.hasText(url))
        {
            return "";
        }
        try
        {
            if(params!=null&&!params.isEmpty())
            {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for(String key : params.keySet())
                {
                    pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
                url += "?"+EntityUtils.toString(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode!=200)
            {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :"+statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if(entity!=null)
            {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HTTP Post 获取内容
     * 
     * @param url请求的url地址
     *            ?之前的地址
     * @param params请求的参数
     * @param charset编码格式
     * @return 页面内容
     */
    public static String sendPost(String url, Map<String, Object> params)
    {
        if(!StringUtils.hasText(url))
        {
            return "";
        }
        try
        {
            List<NameValuePair> pairs = null;
            if(params!=null&&!params.isEmpty())
            {
                pairs = new ArrayList<NameValuePair>(params.size());
                for(String key : params.keySet())
                {
                    pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if(pairs!=null&&pairs.size()>0)
            {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode!=200)
            {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :"+statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if(entity!=null)
            {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post请求（用于请求json格式的参数）
     * 
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, String params)
        throws Exception
    {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;

        try
        {

            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if(state==HttpStatus.SC_OK)
            {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                return jsonString;
            }
        }
        finally
        {
            if(response!=null)
            {
                try
                {
                    response.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            try
            {
                httpclient.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

}
