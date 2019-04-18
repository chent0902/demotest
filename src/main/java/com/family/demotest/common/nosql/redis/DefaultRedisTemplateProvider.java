package com.family.demotest.common.nosql.redis;





import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Title: 
 * @Description: 
 * @author rico wu ricowu80@163.com
 * @date 2015年5月18日 下午2:10:20
 * @version V1.0
 */
public class DefaultRedisTemplateProvider<K,V> implements RedisTemplateProvider<K,V> {
	
	private RedisTemplate<K, V> redisTemplate;

	public RedisTemplate<K, V> provider() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	
}
