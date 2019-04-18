package com.family.demotest.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.jeewx.api.core.common.MyX509TrustManager;
import org.jeewx.api.core.exception.WexinReqException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class WxstoreUtils2
{
    private static Logger logger = LoggerFactory.getLogger(WxstoreUtils2.class);

    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr)
    {
        logger.debug("*********HTTPREQUEST START********");
        logger.debug("*********requestUrl is "+requestUrl+" END AND requestMethod IS"+requestMethod+" END AND  outputStr"+outputStr+" END ********");
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try
        {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());

            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection)url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod(requestMethod);

            if("GET".equalsIgnoreCase(requestMethod))
            {
                httpUrlConn.connect();
            }

            if(outputStr!=null)
            {
                OutputStream outputStream = httpUrlConn.getOutputStream();

                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while((str = bufferedReader.readLine())!=null)
            {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();

            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
            if((jsonObject.containsKey("errcode"))&&(jsonObject.getInt("errcode")!=0))
            {
                logger.debug("********* ERROR********{}", buffer.toString());
                logger.debug("*********HTTPREQUEST END********");
                throw new WexinReqException("httpRequest Method！errcode="+jsonObject.getString("errcode")+",errmsg = "+jsonObject.getString("errmsg"));
            }
            logger.debug("********* SUCCESS END********");
        }
        catch(ConnectException ce)
        {
            logger.error("Weixin server connection timed out.");

        }
        catch(Exception e)
        {
            logger.error("https request error:{}"+e.getMessage());

        }
        return jsonObject;
    }

    public static JSONObject httpRequest2(String requestUrl, String requestMethod, byte[] outputStr)
    {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try
        {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());

            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection)url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod(requestMethod);

            if("GET".equalsIgnoreCase(requestMethod))
            {
                httpUrlConn.connect();
            }

            if(outputStr!=null)
            {
                OutputStream outputStream = httpUrlConn.getOutputStream();

                outputStream.write(outputStr);
                outputStream.close();
            }

            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while((str = bufferedReader.readLine())!=null)
            {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();

            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        }
        catch(ConnectException ce)
        {
            logger.error("Weixin server connection timed out.");

        }
        catch(Exception e)
        {
            logger.error("https request error:{}"+e.getMessage());

        }
        return jsonObject;
    }

    public static void saveHttpImage(String requestUrl, String requestMethod, String outputStr, File target)
    {
        try
        {
            URL url = new URL(requestUrl);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            conn.setRequestMethod(requestMethod);

            conn.setConnectTimeout(5000);

            InputStream inStream = conn.getInputStream();

            byte[] data = readInputStream(inStream);

            FileOutputStream outStream = new FileOutputStream(target);

            outStream.write(data);

            outStream.close();
        }
        catch(Exception localException)
        {
        }
    }

    public static byte[] readInputStream(InputStream inStream)
        throws Exception
    {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = inStream.read(buffer))!=-1)
        {
            outstream.write(buffer, 0, len);
        }
        outstream.close();
        inStream.close();

        return outstream.toByteArray();
    }

    public static JSONObject uploadMediaFile(String requestUrl, File file, String content_type)
    {
        JSONObject jsonObject = null;
        StringBuffer bufferStr = new StringBuffer();
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        URL submit = null;
        DataOutputStream dos = null;

        BufferedInputStream bufin = null;
        BufferedReader bufferedReader = null;
        try
        {
            submit = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection)submit.openConnection();

            conn.setDoOutput(true);
            conn.setUseCaches(false);

            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens+boundary+end);
            dos.writeBytes("Content-Disposition: form-data; name=\""+file+"\";filename=\""+file.getName()+";Content-Type=\""+content_type+end);
            dos.writeBytes(end);

            bufin = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[8192];
            int count = 0;
            while((count = bufin.read(buffer))!=-1)
            {
                dos.write(buffer, 0, count);
            }

            bufin.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens+boundary+twoHyphens+end);
            dos.flush();

            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while((str = bufferedReader.readLine())!=null)
            {
                bufferStr.append(str);
            }

            jsonObject = JSONObject.fromObject(bufferStr.toString());
        }
        catch(Exception e)
        {
            System.err.println("异常错误:"+e.toString());
            System.err.println("连接地址是:"+requestUrl);
        }
        finally
        {
            try
            {
                if(dos!=null)
                {
                    dos.close();
                }
                if(bufferedReader!=null)
                {
                    bufferedReader.close();
                }
            }
            catch(Exception localException2)
            {
            }
        }

        return jsonObject;
    }

    public static String httpRequest3(String requestUrl, String requestMethod, String outputStr)
    {
        try
        {
            String path = "";
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());

            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection)url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod(requestMethod);

            if("GET".equalsIgnoreCase(requestMethod))
            {
                httpUrlConn.connect();
            }

            if(outputStr!=null)
            {
                OutputStream outputStream = httpUrlConn.getOutputStream();

                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            InputStream inputStream = httpUrlConn.getInputStream();

            // String imgPath = " /Users/wujf/Desktop/var/tomcat/haibao/logo";
            String imgPath = "/var/tomcat/haibao/logo";
            String imgName = System.currentTimeMillis()+".png";
            int stateInt = saveToImgByInputStream(inputStream, imgPath, imgName);

            if(stateInt==1)
            {
                path = imgPath+"/"+imgName;
            }
            httpUrlConn.disconnect();

            return path;

        }
        catch(Exception e)
        {
            logger.error("https request error:{}"+e.getMessage());

        }
        return null;
    }

    public static InputStream httpRequest4(String requestUrl, String requestMethod, String outputStr)
    {
        try
        {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());

            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection)url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod(requestMethod);

            if("GET".equalsIgnoreCase(requestMethod))
            {
                httpUrlConn.connect();
            }

            if(outputStr!=null)
            {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            InputStream inputStream = httpUrlConn.getInputStream();

            httpUrlConn.disconnect();

            return inputStream;

        }
        catch(Exception e)
        {
            logger.error("https request error:{}"+e.getMessage());

        }
        return null;
    }

    public static int saveToImgByInputStream(InputStream instreams, String imgPath, String imgName)
    {
        int stateInt = 1;
        if(instreams!=null)
        {
            try
            {
                File file = new File(imgPath, imgName);// 可以是任何图片格式.jpg,.png等
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int nRead = 0;
                while((nRead = instreams.read(b))!=-1)
                {
                    fos.write(b, 0, nRead);
                }
                fos.flush();
                fos.close();
            }
            catch(Exception e)
            {
                stateInt = 0;
                e.printStackTrace();
            }
            finally
            {
            }
        }
        return stateInt;
    }
    // public Map getminiqrQr(String sceneStr, String accessToken)
    // {
    // RestTemplate rest = new RestTemplate();
    // InputStream inputStream = null;
    // OutputStream outputStream = null;
    // try
    // {
    // String url =
    // "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken;
    // Map<String, Object> param = new HashMap<>();
    // param.put("scene", sceneStr);
    // param.put("page", "pages/index/index");
    // param.put("width", 430);
    // param.put("auto_color", false);
    // Map<String, Object> line_color = new HashMap<>();
    // line_color.put("r", 0);
    // line_color.put("g", 0);
    // line_color.put("b", 0);
    // param.put("line_color", line_color);
    // // LOG.info("调用生成微信URL接口传参:"+param);
    // MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    // HttpEntity requestEntity = new HttpEntity(param, headers);
    // ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST,
    // requestEntity, byte[].class, new Object[0]);
    // logger.info("调用小程序生成微信永久小程序码URL接口返回结果:"+entity.getBody());
    // byte[] result = entity.getBody();
    // logger.info(Base64.encodeBase64String(result));
    // inputStream = new ByteArrayInputStream(result);
    // File file = new File("C:/Users/wangqiulin/Desktop/1.png");
    // if(!file.exists())
    // {
    // file.createNewFile();
    // }
    // outputStream = new FileOutputStream(file);
    // int len = 0;
    // byte[] buf = new byte[1024];
    // while((len = inputStream.read(buf, 0, 1024))!=-1)
    // {
    // outputStream.write(buf, 0, len);
    // }
    // outputStream.flush();
    // }
    // catch(Exception e)
    // {
    // logger.error("调用小程序生成微信永久小程序码URL接口异常", e);
    // }
    // finally
    // {
    // if(inputStream!=null)
    // {
    // try
    // {
    // inputStream.close();
    // }
    // catch(IOException e)
    // {
    // e.printStackTrace();
    // }
    // }
    // if(outputStream!=null)
    // {
    // try
    // {
    // outputStream.close();
    // }
    // catch(IOException e)
    // {
    // e.printStackTrace();
    // }
    // }
    // }
    // return null;
    // }

}
