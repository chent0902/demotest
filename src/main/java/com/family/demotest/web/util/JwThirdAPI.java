package com.family.demotest.web.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.jeewx.api.core.common.WxstoreUtils;
import org.jeewx.api.core.exception.WexinReqException;
import org.jeewx.api.third.AuthorizerOption;
import org.jeewx.api.third.AuthorizerOptionRet;
import org.jeewx.api.third.AuthorizerSetOption;
import org.jeewx.api.third.AuthorizerSetOptionRet;
import org.jeewx.api.third.model.ApiAuthorizerToken;
import org.jeewx.api.third.model.ApiAuthorizerTokenRet;
import org.jeewx.api.third.model.ApiComponentToken;
import org.jeewx.api.third.model.ApiGetAuthorizer;
import org.jeewx.api.third.model.ApiGetAuthorizerRet;
import org.jeewx.api.third.model.GetPreAuthCodeParam;
import org.jeewx.api.third.model.ReOpenAccessToken;
import org.jeewx.api.wxstore.order.model.OrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JwThirdAPI {
	private static Logger logger = LoggerFactory.getLogger(JwThirdAPI.class);

	private static String api_create_preauthcode_url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
	private static String api_component_token_url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
	private static String get_access_token_bycode_url = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=APPID&code=CODE&grant_type=authorization_code&component_appid=COMPONENT_APPID&component_access_token=COMPONENT_ACCESS_TOKEN";
	private static String api_query_auth_url = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx";

	public static String send_message_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

	private static String api_authorizer_token_url = "https:// api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_ACCESS_TOKEN";

	private static String api_get_authorizer_info_url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";

	private static String api_get_authorizer_option_url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_option?component_access_token=COMPONENT_ACCESS_TOKEN";

	private static String api_set_authorizer_option_url = "https://api.weixin.qq.com/cgi-bin/component/api_set_authorizer_option?component_access_token=COMPONENT_ACCESS_TOKEN";

	public static String getAccessToken(ApiComponentToken apiComponentToken)
			throws WexinReqException {
		String component_access_token = "";
		String requestUrl = api_component_token_url;
		JSONObject obj = JSONObject.fromObject(apiComponentToken);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST",
				obj.toString());
		if (result.has("errcode")) {
			logger.error("获取第三方平台access_token！errcode="
					+ result.getString("errcode") + ",errmsg = "
					+ result.getString("errmsg"));
			throw new WexinReqException("获取第三方平台access_token！errcode="
					+ result.getString("errcode") + ",errmsg = "
					+ result.getString("errmsg"));
		}
		component_access_token = result.getString("component_access_token");

		return component_access_token;
	}

	public static String getPreAuthCode(String component_appid,
			String component_access_token) throws WexinReqException {
		String pre_auth_code = "";
		String requestUrl = api_create_preauthcode_url.replace(
				"COMPONENT_ACCESS_TOKEN", component_access_token);
		GetPreAuthCodeParam getPreAuthCodeParam = new GetPreAuthCodeParam();
		getPreAuthCodeParam.setComponent_appid(component_appid);
		JSONObject obj = JSONObject.fromObject(getPreAuthCodeParam);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST",
				obj.toString());
		if (result.has("errcode")) {
			logger.error("获取权限令牌信息！errcode=" + result.getString("errcode")
					+ ",errmsg = " + result.getString("errmsg"));
			throw new WexinReqException("获取权限令牌信息！errcode="
					+ result.getString("errcode") + ",errmsg = "
					+ result.getString("errmsg"));
		}
		pre_auth_code = result.getString("pre_auth_code");

		return pre_auth_code;
	}

	public static JSONObject getApiQueryAuthInfo(String component_appid,
			String authorization_code, String component_access_token)
			throws WexinReqException {
		String requestUrl = api_query_auth_url.replace("xxxx",
				component_access_token);
		Map mp = new HashMap();
		mp.put("component_appid", component_appid);
		mp.put("authorization_code", authorization_code);
		JSONObject obj = JSONObject.fromObject(mp);
		System.out
				.println("-------------------3、使用授权码换取公众号的授权信息---requestUrl------------------------"
						+ requestUrl);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST",
				obj.toString());
		if (result.has("errcode")) {
			logger.error("获取第三方平台access_token！errcode="
					+ result.getString("errcode") + ",errmsg = "
					+ result.getString("errmsg"));
			throw new WexinReqException("获取第三方平台access_token！errcode="
					+ result.getString("errcode") + ",errmsg = "
					+ result.getString("errmsg"));
		}
		return result;
	}

	public static ApiAuthorizerTokenRet apiAuthorizerToken(
			ApiAuthorizerToken apiAuthorizerToken, String component_access_token)
			throws WexinReqException {
		String requestUrl = api_authorizer_token_url.replace(
				"COMPONENT_ACCESS_TOKEN", component_access_token);
		JSONObject param = JSONObject.fromObject(apiAuthorizerToken);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST",
				param.toString());
		ApiAuthorizerTokenRet apiAuthorizerTokenRet = (ApiAuthorizerTokenRet) JSONObject
				.toBean(result, ApiAuthorizerTokenRet.class);
		return apiAuthorizerTokenRet;
	}

	public static ApiGetAuthorizerRet apiGetAuthorizerInfo(
			ApiGetAuthorizer apiGetAuthorizer, String component_access_token)
			throws WexinReqException {
		String requestUrl = api_get_authorizer_info_url.replace(
				"COMPONENT_ACCESS_TOKEN", component_access_token);
		JSONObject param = JSONObject.fromObject(apiGetAuthorizer);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST",
				param.toString());
		ApiGetAuthorizerRet apiGetAuthorizerRet = (ApiGetAuthorizerRet) JSONObject
				.toBean(result, ApiGetAuthorizerRet.class);
		return apiGetAuthorizerRet;
	}

	public static AuthorizerOptionRet apiGetAuthorizerOption(
			AuthorizerOption authorizerOption, String component_access_token)
			throws WexinReqException {
		String requestUrl = api_get_authorizer_option_url.replace(
				"COMPONENT_ACCESS_TOKEN", component_access_token);
		JSONObject param = JSONObject.fromObject(authorizerOption);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST",
				param.toString());
		AuthorizerOptionRet authorizerOptionRet = (AuthorizerOptionRet) JSONObject
				.toBean(result, AuthorizerOptionRet.class);
		return authorizerOptionRet;
	}

	public static AuthorizerSetOptionRet apiSetAuthorizerOption(
			AuthorizerSetOption authorizerSetOption,
			String component_access_token) throws WexinReqException {
		String requestUrl = api_set_authorizer_option_url.replace(
				"COMPONENT_ACCESS_TOKEN", component_access_token);
		JSONObject param = JSONObject.fromObject(authorizerSetOption);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl, "POST",
				param.toString());
		AuthorizerSetOptionRet authorizerSetOptionRet = (AuthorizerSetOptionRet) JSONObject
				.toBean(result, AuthorizerSetOptionRet.class);
		return authorizerSetOptionRet;
	}

	public static ReOpenAccessToken getAccessTokenByCode(String appid,
			String code, String grant_type, String component_appid,
			String component_access_token) throws WexinReqException {
		String requestUrl = get_access_token_bycode_url
				.replace("COMPONENT_APPID", component_appid)
				.replace("COMPONENT_ACCESS_TOKEN", component_access_token)
				.replace("authorization_code", grant_type)
				.replace("CODE", code).replace("APPID", appid);
		JSONObject result = WxstoreUtils.httpRequest(requestUrl, "GET", null);
		ReOpenAccessToken reOpenAccessToken = (ReOpenAccessToken) JSONObject
				.toBean(result, OrderInfo.class);
		if (result.has("errcode")) {
			logger.error("获取第三方平台access_token！errcode="
					+ result.getString("errcode") + ",errmsg = "
					+ result.getString("errmsg"));
			throw new WexinReqException("获取第三方平台access_token！errcode="
					+ result.getString("errcode") + ",errmsg = "
					+ result.getString("errmsg"));
		}
		return reOpenAccessToken;
	}

	public static String sendMessage(Map<String, Object> obj,
			String ACCESS_TOKEN) {
		JSONObject json = JSONObject.fromObject(obj);
		System.out
				.println("--------发送客服消息---------json-----" + json.toString());

		String url = send_message_url.replace("ACCESS_TOKEN", ACCESS_TOKEN);
		JSONObject jsonObject = WxstoreUtils.httpRequest(url, "POST",
				json.toString());
		return jsonObject.toString();
	}

	public static void main(String[] args) {
		try {
			ApiComponentToken apiComponentToken = new ApiComponentToken();

			apiComponentToken.setComponent_appid("wx056ae5bc88c95c55");
			apiComponentToken
					.setComponent_appsecret("0c79e1fa963cd80cc0be99b20a18faeb");
			apiComponentToken.setComponent_verify_ticket(null);
			String s = getAccessToken(apiComponentToken);
			System.out.println(s);
		} catch (WexinReqException e) {
			e.printStackTrace();
		}
	}
}