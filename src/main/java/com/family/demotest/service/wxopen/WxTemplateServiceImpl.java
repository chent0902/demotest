package com.family.demotest.service.wxopen;

import org.jeewx.api.core.common.WxstoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.util.Logger;
import com.family.base01.util.Validator;
import com.family.demotest.common.notify.NotifyManager;
import com.family.demotest.vo.wx.MessageVo;
import com.family.demotest.vo.wx.TemplateMsgSendVo;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author wujf
 *
 */
@Service("wx.template.service")
public class WxTemplateServiceImpl
    implements
    WxTemplateService
{

    @Autowired
    private TicketService ticketService;

    @Autowired
    private Validator validator;
    @Autowired
    private Logger logger;
    //
    // @Value("${applet.domain.url}")
    // private String domainUrl;

    @Override
    public ResultCode setIndustry(String channelNo)
    {

        ResultCode result = new ResultCode();
        String accessToken = ticketService.getAuthorizerAccessToken(channelNo);
        if(validator.isBlank(accessToken))
        {
            result.setCode(-1);
            result.setInfo("token失效");
            return result;
        }
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token="+accessToken;
        net.sf.json.JSONObject param = new net.sf.json.JSONObject();
        param.put("industry_id1", 1);
        param.put("industry_id2", 2);

        net.sf.json.JSONObject res = WxstoreUtils.httpRequest(requestUrl, "POST", param.toString());

        result.setData(res);
        if(logger.isInfoEnable())
        {
            logger.info("设置所属行业返回结果："+res.toString());
        }

        return result;
    }

    @Override
    public ResultCode getIndustry(String channelNo)
    {
        ResultCode result = new ResultCode();
        try
        {
            String accessToken = ticketService.getAuthorizerAccessToken(channelNo);

            if(validator.isBlank(accessToken))
            {
                result.setCode(-1);
                result.setInfo("token失效");
                return result;
            }

            String requestUrl = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token="+accessToken;

            net.sf.json.JSONObject res = WxstoreUtils.httpRequest(requestUrl, "GET", null);
            result.setData(res);

        }
        catch(Exception e)
        {
            logger.error(e, "获取设置的行业信息出错");
            result.setCode(-1);
            result.setInfo("获取设置的行业信息出错");
            return result;
        }

        return result;

    }

    @Override
    public ResultCode getTemplateId(String channelNo, String templateIdShort)
    {
        ResultCode result = new ResultCode();
        try
        {
            String accessToken = ticketService.getAuthorizerAccessToken(channelNo);

            if(validator.isBlank(accessToken))
            {
                result.setCode(-1);
                result.setInfo("token失效");
                return result;
            }

            String requestUrl = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token="+accessToken;

            net.sf.json.JSONObject param = new net.sf.json.JSONObject();
            param.put("template_id_short", templateIdShort); // 到账通知
            // param.put("template_id_short", "OPENTM405464954"); // 到账通知

            net.sf.json.JSONObject res = WxstoreUtils.httpRequest(requestUrl, "POST", param.toString());

            logger.info("获得模板ID结果"+res.toString());
            if(res.getInt("errcode")==0)
            {
                result.setCode(0);
                result.setData(res);
                result.setOther(res.getString("template_id"));

            }
            else
            {
                result.setCode(-1);
                result.setInfo(res.getString("errmsg"));
            }

        }
        catch(Exception e)
        {
            logger.error(e, "获得模板ID出错");
            result.setCode(-1);
            result.setInfo("获得模板ID出错");
            return result;
        }

        return result;

    }

    @Override
    public ResultCode getTemplateList(String channelNo)
    {
        ResultCode result = new ResultCode();
        try
        {
            String accessToken = ticketService.getAuthorizerAccessToken(channelNo);

            if(validator.isBlank(accessToken))
            {
                result.setCode(-1);
                result.setInfo("token失效");
                return result;
            }

            String requestUrl = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token="+accessToken;

            net.sf.json.JSONObject res = WxstoreUtils.httpRequest(requestUrl, "GET", null);
            result.setData(res);

        }
        catch(Exception e)
        {
            logger.error(e, "获取设置的行业信息出错");
            result.setCode(-1);
            result.setInfo("获取设置的行业信息出错");
            return result;
        }

        return result;

    }

    @Override
    public ResultCode deleteTemplate(String channelNo, String templateId)
    {
        ResultCode result = new ResultCode();
        try
        {
            String accessToken = ticketService.getAuthorizerAccessToken(channelNo);

            if(validator.isBlank(accessToken))
            {
                result.setCode(-1);
                result.setInfo("token失效");
                return result;
            }

            String requestUrl = "https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token="+accessToken;

            net.sf.json.JSONObject param = new net.sf.json.JSONObject();
            param.put("template_id", templateId);

            net.sf.json.JSONObject res = WxstoreUtils.httpRequest(requestUrl, "POST", param.toString());

            logger.info("删除模版消息返回结果"+res.toString());
            if(res.getInt("errcode")==0)
            {
                result.setCode(0);
                result.setData(res);
            }
            else
            {
                result.setCode(-1);
                result.setInfo(res.getString("errmsg"));
            }

        }
        catch(Exception e)
        {
            logger.error(e, "删除模板出错");
            result.setCode(-1);
            result.setInfo("删除模板出错");
            return result;
        }

        return result;

    }

    @Override
    public ResultCode sendMsg(TemplateMsgSendVo sendVo)
    {
        ResultCode result = new ResultCode();
        try
        {
            String accessToken = ticketService.getAuthorizerAccessToken(sendVo.getChannelNo());

            if(validator.isBlank(accessToken))
            {
                result.setCode(-1);
                result.setInfo("token失效");
                return result;
            }

            JSONObject keyList = new JSONObject();

            JSONObject first = new JSONObject();
            first.put("value", "恭喜您：您的好友帮您增加1次抽奖机会");
            first.put("color", "#173177");
            keyList.put("first", first);

            JSONObject keyword1 = new JSONObject();
            keyword1.put("value", "吴大帅哥");
            keyword1.put("color", "#173177");
            keyList.put("keyword1", keyword1);

            JSONObject keyword2 = new JSONObject();
            keyword2.put("value", "1次抽奖机会");
            keyword2.put("color", "#173177");
            keyList.put("keyword2", keyword2);

            JSONObject keyword3 = new JSONObject();
            keyword3.put("value", "2018-08-01 10:00:00");
            keyword3.put("color", "#173177");
            keyList.put("keyword3", keyword3);

            JSONObject remark = new JSONObject();
            remark.put("value", "点击查看，继续抽奖");
            remark.put("color", "#173177");
            keyList.put("remark", remark);

            MessageVo msgVo = new MessageVo();
            msgVo.setChannelNo("1000001");
            msgVo.setToken(accessToken);
            msgVo.setTouser("oQksr1O0hlE3RqEIIhHAYNPNl4VE");
            msgVo.setTemplateId("XMhBqdtLcedJjeTLVfNnO5TPM6HYnw5TaWrhgh4Fz5I");
            msgVo.setData(keyList);
            NotifyManager.instance.notifyTemplateMsg(msgVo);
        }
        catch(Exception e)
        {
            logger.error(e, "发送模板出错");
            result.setCode(-1);
            result.setInfo("发送模板出错");
            return result;
        }

        return result;

    }

}
