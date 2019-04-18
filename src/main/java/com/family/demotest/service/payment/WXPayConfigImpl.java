package com.family.demotest.service.payment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.family.demotest.common.wxpay.IWXPayDomain;
import com.family.demotest.common.wxpay.WXPayConfig;

public class WXPayConfigImpl
    extends WXPayConfig
{

    private String appId;// 小程序appid
    private String mchId;// 商户号
    private String key;// 支付key
    private byte[] certData; // 支付证书

    public WXPayConfigImpl(String appId, String mchId, String key, String certPath)
        throws Exception
    {

        this.appId = appId;
        this.mchId = mchId;
        this.key = key;

        // String certPath = "D://CERT/common/apiclient_cert.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int)file.length()];
        certStream.read(this.certData);
        certStream.close();

    }

    @Override
    public String getAppID()
    {
        return appId;
    }

    @Override
    public String getMchID()
    {
        return mchId;
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public InputStream getCertStream()
    {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs()
    {
        return 2000;
    }

    @Override
    public int getHttpReadTimeoutMs()
    {
        return 10000;
    }

    @Override
    public IWXPayDomain getWXPayDomain()
    {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain()
    {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain()
    {
        return "api2.mch.weixin.qq.com";
    }

    @Override
    public int getReportWorkerNum()
    {
        return 1;
    }

    @Override
    public int getReportBatchSize()
    {
        return 2;
    }
}
