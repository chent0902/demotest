package com.family.demotest.web.util;

import java.util.Properties;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;

/**
 * 
 * @author wujf
 */
public class CloudMQUtil
{

    private static final String ACCESS_KEY = "LTAI45tk6EEMFEFh"; // 你自己的AccessKey
    private static final String SECRET_KEY = "Z7yYOcie7dLb1zkbZLXDVW3GCZufL9"; // 你自己的SecretKey

    /**
     * 获取消息的 Producer
     *
     * @param producerId
     *            producerId
     * @return Producer
     */
    public static Producer getProducer(String producerId)
    {
        Properties properties = new Properties();
        // 您在 MQ 控制台创建的 Producer ID
        properties.put(PropertyKeyConst.ProducerId, producerId);
        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, ACCESS_KEY);
        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, SECRET_KEY);
        // 设置 TCP 接入域名（此处以公共云的公网接入为例）
        properties.put(PropertyKeyConst.ONSAddr, "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet");
        Producer producer = ONSFactory.createProducer(properties);

        // producer.send(msg);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        producer.start();
        return producer;
    }
}
