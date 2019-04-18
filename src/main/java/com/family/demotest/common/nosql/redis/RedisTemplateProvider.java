package com.family.demotest.common.nosql.redis;




import org.springframework.data.redis.core.RedisTemplate;



/**
 * 
 * @author wujf
 *
 * @param <K>
 * @param <V>
 */
public interface RedisTemplateProvider<K,V> {
	 RedisTemplate<K, V> provider();
}
