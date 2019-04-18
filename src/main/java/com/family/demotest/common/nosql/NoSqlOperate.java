package com.family.demotest.common.nosql;



import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @Title:
 * @Description:
 * @author rico wu ricowu80@163.com
 * @date 2015年5月18日 上午11:53:31
 * @version V1.0
 */
public interface NoSqlOperate<K, V> extends Serializable {
	
	
	boolean exists(K key);
	
	void del(K key);

	/**
	 * 设置值
	 * 
	 * @param key
	 * @param value
	 */
	void set(K key, V value);

	void set(K key, V value, int timeout, TimeUnit timeUnit);

	/**
	 * 
	 * @param key
	 * @return
	 */
	V get(K key);

	/** List 相关 API */
	Long size(K key);

	Long leftPush(K key, V value);

	Long rightPush(K key, V value);

	V leftPop(K key);

	V leftPop(K key, long timeout, TimeUnit unit);

	V rightPop(K key);

	V rightPop(K key, long timeout, TimeUnit unit);

	V rightPopAndLeftPush(K sourceKey, K destinationKey);

	V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout,
			TimeUnit unit);
	/** List 相关 API */

}
