package com.family.demotest.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 微信第三方平台开发工具类
 * 
 * @author wuzhisheng
 *
 */
public class WxOpenUtil
{

    /**
     * 提起xml
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public static String request2xml(HttpServletRequest request)
        throws IOException
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = request.getReader();
        String line;
        while((line = in.readLine())!=null)
        {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

    /**
     * 事件触发是获取开发者公众号
     * 
     * @return
     * @throws DocumentException
     */
    public static String getEventtoUserName(String xml)
        throws DocumentException
    {
        Document doc = DocumentHelper.parseText(xml);
        Element rootElt = doc.getRootElement();
        return rootElt.elementText("ToUserName");
    }

    /**
     * 生成noncestr
     * 
     * @return
     */
    public static String generateNonceStr()
    {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for(int i = 0; i<16; i++)
        {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length()-1));
        }
        return res;
    }

    // 获得js signature

    /**
     * 微信jsapi票据签名
     * 
     * @param jsapi_ticket
     * @param timestamp
     * @param nonce
     * @param jsurl
     * @return
     * @throws IOException
     */
    public static String getSignature(String jsapi_ticket, String timestamp, String nonce, String jsurl)
        throws IOException
    {
        /****
         * 对 jsapi_ticket、 timestamp 和 nonce 按字典排序 对所有待签名参数按照字段名的 ASCII
         * 码从小到大排序（字典序）后，使用 URL 键值对的格式（即key1=value1&key2=value2…）拼接成字符串
         * string1。这里需要注意的是所有参数名均为小写字符。 接下来对 string1 作 sha1 加密，字段名和字段值都采用原始值，不进行
         * URL 转义。即 signature=sha1(string1)。
         * **如果没有按照生成的key1=value&key2=value拼接的话会报错
         */
        String[] paramArr = new String[]{"jsapi_ticket="+jsapi_ticket, "timestamp="+timestamp, "noncestr="+nonce, "url="+jsurl};
        Arrays.sort(paramArr);
        // 将排序后的结果拼接成一个字符串
        String content = paramArr[0].concat("&"+paramArr[1]).concat("&"+paramArr[2]).concat("&"+paramArr[3]);
        // System.out.println("拼接之后的content为:"+content);
        String gensignature = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            // 对拼接后的字符串进行 sha1 加密
            byte[] digest = md.digest(content.toString().getBytes());
            gensignature = byteToStr(digest);
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        // 将 sha1 加密后的字符串与 signature 进行对比
        if(gensignature!=null)
        {
            return gensignature;// 返回signature
        }
        else
        {
            return "false";
        }
        // return (String) (ciphertext != null ? ciphertext: false);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray)
    {
        String strDigest = "";
        for(int i = 0; i<byteArray.length; i++)
        {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte)
    {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte>>>4)&0X0F];
        tempArr[1] = Digit[mByte&0X0F];
        String s = new String(tempArr);
        return s;
    }
}
