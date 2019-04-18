package com.family.demotest.web.controller.ticket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.third.JwThirdAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.family.demotest.common.aes.AesException;
import com.family.demotest.common.aes.WXBizMsgCrypt;
import com.family.demotest.service.wxopen.TicketService;
import com.family.demotest.service.wxopen.WxMsgReplyService;
import com.family.demotest.web.controller.BaseController;
import com.family.demotest.web.util.ResultCode;
import com.family.demotest.web.util.StringUtils;
import com.family.demotest.web.util.WxOpenUtil;

/**
 * 微信公众账号
 */
@Controller
@RequestMapping("/ticket")
public class TicketController
    extends
    BaseController
{

    @Autowired
    private TicketService ticketService;
    @Autowired
    private WxMsgReplyService wxMsgReplyService;

    /**
     * 授权事件接收 微信第三方平台---------微信推送Ticket消息10分钟一次-----------
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws AesException
     * @throws DocumentException
     */
    @RequestMapping(value = "/ticket.do")
    public void acceptAuthorizeEvent(HttpServletRequest request, HttpServletResponse response)
        throws IOException,
        AesException,
        DocumentException
    {
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String signature = request.getParameter("signature");
        String msgSignature = request.getParameter("msg_signature");
        // String encryptType = request.getParameter("encrypt_type");

        StringBuilder sb = new StringBuilder();
        BufferedReader in = request.getReader();
        String line;
        while((line = in.readLine())!=null)
        {
            sb.append(line);
        }
        String xml = sb.toString();

        // if(logger.isInfoEnable())
        // {
        // logger.info("******推送nonce:"+nonce+",timestamp:"+timestamp+",signature:"+signature+",msgSignature:"+msgSignature+",encryptType:"+encryptType);
        // logger.info("******推送component_verify_ticket:"+xml);
        //
        // }

        ticketService.processAuthorizeEvent(nonce, timestamp, signature, msgSignature, xml);

        output(response, "success"); // 输出响应的内容。
    }

    /**
     * 用户授权后回调地址，
     * 
     * @param auth_code
     *            授权码
     * @param channelNo
     *            商家编号
     * @param response
     * @throws IOException
     * @throws AesException
     * @throws DocumentException
     * @throws WexinReqException
     */
    @RequestMapping(value = "/{channelNo}/authorCallback.do")
    public void authorCallback(@PathVariable("channelNo") String channelNo, @RequestParam("auth_code") String auth_code, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();

        // logger.info("channelNo="+channelNo+",auth_code="+auth_code);

        if(validator.isBlank(auth_code))
        {
            result.setCode(-1);
            result.setInfo("授权码不能为空");
            this.Ouput(response, JSON.toJSONString(result));
            return;
        }

        try
        {
            result = ticketService.authorCallback(channelNo, auth_code);
            if(result.getCode()==0)
            {
                try
                {
                    response.sendRedirect("http://wx.demotest.co/demotestweb/start/index.html?auth=1");
                    // response.sendRedirect("http://admin.fangkem.cn/demotestweb/start/index.html?auth=1");
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                this.Ouput(response, JSON.toJSONString(result));
                return;
            }
        }
        catch(WexinReqException e)
        {
            logger.error(e, "用户授权后获取用户信息出错");
        }

    }

    /**
     * 一键授权功能
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws AesException
     * @throws DocumentException
     */
    @RequestMapping(value = "/authored.do")
    public void goAuthor(HttpServletRequest request, HttpServletResponse response)
        throws IOException,
        AesException,
        DocumentException
    {
        ResultCode result = new ResultCode();

        String channelNo = request.getParameter("channelNo");
        if(validator.isBlank(channelNo))
        {
            result.setCode(-1);
            result.setInfo("商户编号不能为空");
            this.Ouput(response, JSON.toJSONString(result));
            return;
        }

        String url = ticketService.getComponentloginpage(channelNo);
        if("-1".equals(url))
        {
            result.setCode(-1);
            result.setInfo("获取token失败，请稍后再试");
            this.Ouput(response, JSON.toJSONString(result));
            return;
        }

        result.setData(url);

        JSON.toJSONString(result);

        this.Ouput(response, JSON.toJSONString(result));

    }

    /**
     * 消息与事件接收URL地址
     * 
     * @param appid
     * @param request
     * @param response
     * @throws IOException
     * @throws AesException
     * @throws DocumentException
     */
    @RequestMapping(value = "{appid}/callback.do")
    public void acceptMessageAndEvent(@PathVariable("appid") String appid, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String xml = WxOpenUtil.request2xml(request);// 提起xml 格式
            // logger.info("*******消息与事件接收URL地址xml="+xml);

            String msgSignature = request.getParameter("msg_signature");
            if(validator.isBlank(msgSignature))
            {// 微信推送给第三方开放平台的消息一定是加过密的，无消息加密无法解密消息
                return;
            }

            String toUserName = WxOpenUtil.getEventtoUserName(xml);//
            // 获取小程序的原始ID
            if(toUserName.equals("gh_3c884a361561"))// 测试公众号appid
            {
                testAllNetworkCheck(request, response, xml);
            }
            else
            {
                checkWeixinAllNetworkCheck(request, response, appid, xml);// 处理消息
            }

        }
        catch(Exception e)
        {
            logger.error(e, "消息与事件接收事件处理出错****");
        }

    }

    /**
     * 全网发布测试
     * 
     * @param request
     * @param response
     * @param authorizer_appid
     * @param xml
     * @throws DocumentException
     * @throws IOException
     * @throws AesException
     */
    private void checkWeixinAllNetworkCheck(HttpServletRequest request, HttpServletResponse response, String authorizerAppid, String xml)
        throws Exception
    {

        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String msgSignature = request.getParameter("msg_signature");
        String componentToken = ticketService.getComponentToken();
        String encodingaeskey = ticketService.getComponentEncodingaeskey();
        String componentAppid = ticketService.getComponentAppid();
        WXBizMsgCrypt pc = new WXBizMsgCrypt(componentToken, encodingaeskey, componentAppid);
        xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml);// 解密XML

        // logger.info("解密后的xml信息："+xml);
        Document doc = DocumentHelper.parseText(xml);
        Element rootElt = doc.getRootElement();
        String msgType = rootElt.elementText("MsgType");
        String toUserName = rootElt.elementText("ToUserName");
        String fromUserName = rootElt.elementText("FromUserName");

        Map<String, String> map = new HashMap<String, String>();
        map.put("msgType", msgType);
        map.put("toUserName", toUserName);
        map.put("fromUserName", fromUserName);

        /**
         * 处理事件消息
         */
        if("event".equals(msgType))
        {
            String event = rootElt.elementText("Event");
            String eventKey = rootElt.elementText("EventKey");

            map.put("event", event);
            map.put("eventKey", eventKey);
            // 回复事件消息
            wxMsgReplyService.replyEventMessage(request, response, authorizerAppid, map);

        }
        else if("text".equals(msgType))
        {
            // 回复普通消息
            String content = rootElt.elementText("Content");
            processTextMessage(request, response, content, toUserName, fromUserName);

        }
        else if("image".equals(msgType))
        {
            // 回复图片消息
            // output(response, "success");
            this.replyMessage(request, response, authorizerAppid, fromUserName);

        }

    }

    // 回复客户消息 [全网发布测试]
    private void processTextMessage(HttpServletRequest request, HttpServletResponse response, String content, String toUserName, String fromUserName)
        throws IOException,
        DocumentException
    {
        if("TESTCOMPONENT_MSG_TYPE_TEXT".equals(content))
        {
            String returnContent = content+"_callback";
            replyTextMessage(request, response, returnContent, toUserName, fromUserName);
        }
        else if(StringUtils.startsWithIgnoreCase(content, "QUERY_AUTH_CODE"))
        {
            output(response, "");
            // 接下来客服API再回复一次消息
            replyApiTextMessage(request, response, content.split(":")[1], fromUserName);
        }
        else
        {
            // String returnContent = "欢迎您关注";
            // logger.info("**************回复普通消息："+returnContent);

            // replyTextMessage(request, response, returnContent, toUserName,
            // fromUserName);
        }
    }

    /**
     * 回复客服消息
     * 
     * @param request
     * @param response
     * @param authorizerAppid
     * @param fromUserName
     * @throws DocumentException
     * @throws IOException
     */
    private void replyMessage(HttpServletRequest request, HttpServletResponse response, String authorizerAppid, String fromUserName)
        throws DocumentException,
        IOException
    {

    }

    // 客户消息回复
    private void replyApiTextMessage(HttpServletRequest request, HttpServletResponse response, String auth_code, String fromUserName)
        throws DocumentException,
        IOException
    {
        String authorization_code = auth_code;
        // 得到微信授权成功的消息后，应该立刻进行处理！！相关信息只会在首次授权的时候推送过来
        try
        {
            // 第三方的token
            String component_access_token = ticketService.getAccessToken();
            logger.info("------component_access_token:"+component_access_token+"-------------------------");
            /*
             * System.out .println(
             * "------step.2----使用客服消息接口回复粉丝------- component_access_token = " +
             * component_access_token + "---------authorization_code = " +
             * authorization_code);
             */
            net.sf.json.JSONObject authorizationInfoJson = JwThirdAPI.getApiQueryAuthInfo(ticketService.getComponentAppid(), authorization_code,
                component_access_token);

            logger.info("------authorizationInfoJson:"+authorizationInfoJson+"-------------------------");
            /*
             * System.out .println(
             * 
             * 
             * "------step.3----使用客服消息接口回复粉丝-------------- 获取authorizationInfoJson = "
             * + authorizationInfoJson);
             */
            net.sf.json.JSONObject infoJson = authorizationInfoJson.getJSONObject("authorization_info");
            String authorizer_access_token = infoJson.getString("authorizer_access_token");

            Map<String, Object> obj = new HashMap<String, Object>();
            Map<String, Object> msgMap = new HashMap<String, Object>();
            String msg = auth_code+"_from_api";
            msgMap.put("content", msg);

            obj.put("touser", fromUserName);
            obj.put("msgtype", "text");
            obj.put("text", msgMap);
            JwThirdAPI.sendMessage(obj, authorizer_access_token);
        }
        catch(WexinReqException e)
        {
            logger.error(e, "全网发布检测回复客户消息出错22");
        }

    }

    /**
     * 回复微信服务器"文本消息"
     * 
     * @param request
     * @param response
     * @param content
     * @param toUserName
     * @param fromUserName
     * @throws DocumentException
     * @throws IOException
     */
    private void replyTextMessage(HttpServletRequest request, HttpServletResponse response, String content, String toUserName, String fromUserName)
        throws DocumentException,
        IOException
    {
        Long createTime = Calendar.getInstance().getTimeInMillis()/1000;
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<ToUserName><![CDATA["+fromUserName+"]]></ToUserName>");
        sb.append("<FromUserName><![CDATA["+toUserName+"]]></FromUserName>");
        sb.append("<CreateTime>"+createTime+"</CreateTime>");
        sb.append("<MsgType><![CDATA[text]]></MsgType>");
        sb.append("<Content><![CDATA["+content+"]]></Content>");
        sb.append("</xml>");
        String replyMsg = sb.toString();

        // logger.info("*******44444444回复内容:"+replyMsg);
        String returnvaleue = "";
        try
        {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(ticketService.getComponentToken(), ticketService.getComponentEncodingaeskey(),
                    ticketService.getComponentAppid());
            returnvaleue = pc.encryptMsg(replyMsg, createTime.toString(), "easemob");
        }
        catch(AesException e)
        {
            logger.error(e, "全网发布检测文本消息出错");
        }

        logger.info("*******5555returnvaleue:"+returnvaleue);
        output(response, returnvaleue);
    }

    /**
     * 工具类：回复微信服务器"文本消息"
     * 
     * @param response
     * @param returnvaleue
     */
    private void output(HttpServletResponse response, String returnvaleue)
    {
        try
        {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.write(returnvaleue);
            pw.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    // ---------- 测试 -- --- start --
    // 全网发布测试，不测试则发布不通过
    private void testAllNetworkCheck(HttpServletRequest request, HttpServletResponse response, String xml)
        throws DocumentException,
        IOException,
        AesException
    {
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String msgSignature = request.getParameter("msg_signature");

        WXBizMsgCrypt pc;
        try
        {
            pc = new WXBizMsgCrypt(ticketService.getComponentToken(), ticketService.getComponentEncodingaeskey(), ticketService.getComponentAppid());
            xml = pc.decryptMsg(msgSignature, timestamp, nonce, xml);
        }
        catch(AesException e)
        {
            logger.error(e, "解密微信加密信息出错");
            e.printStackTrace();
        }

        logger.info("xml33333="+xml);

        Document doc = DocumentHelper.parseText(xml);
        Element rootElt = doc.getRootElement();
        String msgType = rootElt.elementText("MsgType");
        String toUserName = rootElt.elementText("ToUserName");
        String fromUserName = rootElt.elementText("FromUserName");

        if("event".equals(msgType))
        {
            String event = rootElt.elementText("Event");
            logger.info("---全网发布接入检测--step.3-----------事件消息--------event:"+event);
            testReplyEventMessage(request, response, event, toUserName, fromUserName);
        }
        else if("text".equals(msgType))
        {
            // LogUtil.info(---全网发布接入检测--step.3-----------文本消息--------);

            String content = rootElt.elementText("Content");

            logger.info("---全网发布接入检测--step.3-----------文本消息--------content:"+content);

            testProcessTextMessage(request, response, content, toUserName, fromUserName);
        }
    }

    private void testReplyEventMessage(HttpServletRequest request, HttpServletResponse response, String event, String toUserName, String fromUserName)
        throws DocumentException,
        IOException
    {
        String content = event+"from_callback";
        // LogUtil.info(---全网发布接入检测------step.4-------事件回复消息 content=+content +
        // toUserName=+toUserName+ fromUserName=+fromUserName);
        replyTextMessage(request, response, content, toUserName, fromUserName);
    }

    private void testProcessTextMessage(HttpServletRequest request, HttpServletResponse response, String content, String toUserName, String fromUserName)
        throws IOException,
        DocumentException
    {
        if("TESTCOMPONENT_MSG_TYPE_TEXT".equals(content))
        {
            String returnContent = content+"_callback";
            replyTextMessage(request, response, returnContent, toUserName, fromUserName);
        }
        else if(StringUtils.startsWithIgnoreCase(content, "QUERY_AUTH_CODE"))
        {
            output(response, "");
            // 接下来客服API再回复一次消息
            replyApiTextMessage(request, response, content.split(":")[1], fromUserName);
        }
    }

    // ---------- 测试 -- --- end --

}
