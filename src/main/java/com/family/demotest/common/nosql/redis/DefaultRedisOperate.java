package com.family.demotest.common.nosql.redis;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Title:
 * @Description:
 * @author rico wu ricowu80@163.com
 * @date 2015年5月18日 下午1:35:03
 * @version V1.0
 */
public class DefaultRedisOperate<K, V>
    implements
    RedisOperate<K, V>
{

    private static final long serialVersionUID = 1747359854857860300L;
    RedisTemplateProvider<K, V> provider;

    @Override
    public void del(K key)
    {
        provider.provider().delete(key);
    }

    @Override
    public boolean exists(K key)
    {
        RedisTemplate<K, V> template = getRedisTemplate();
        return template.hasKey(key);
    }

    @Override
    public void set(K key, V value)
    {
        provider.provider().opsForValue().set(key, value);
    }

    @Override
    public void set(K key, V value, int timeout, TimeUnit timeUnit)
    {
        provider.provider().opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public V get(K key)
    {
        return provider.provider().opsForValue().get(key);
    }

    @Override
    public Long size(K key)
    {
        return provider.provider().opsForList().size(key);
    }

    @Override
    public Long leftPush(K key, V value)
    {
        return provider.provider().opsForList().leftPush(key, value);
    }

    @Override
    public Long rightPush(K key, V value)
    {
        return provider.provider().opsForList().rightPush(key, value);
    }

    @Override
    public V leftPop(K key)
    {
        return provider.provider().opsForList().leftPop(key);
    }

    @Override
    public V leftPop(K key, long timeout, TimeUnit unit)
    {
        return provider.provider().opsForList().leftPop(key, timeout, unit);
    }

    @Override
    public V rightPop(K key)
    {
        return provider.provider().opsForList().rightPop(key);
    }

    @Override
    public V rightPop(K key, long timeout, TimeUnit unit)
    {
        return provider.provider().opsForList().rightPop(key, timeout, unit);
    }

    @Override
    public V rightPopAndLeftPush(K sourceKey, K destinationKey)
    {
        return provider.provider().opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
    }

    @Override
    public V rightPopAndLeftPush(K sourceKey, K destinationKey, long timeout, TimeUnit unit)
    {
        return provider.provider().opsForList().rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit);
    }

    /** for hash */
    @Override
    public boolean opsForMapHasKey(K key, K field)
    {

        return provider.provider().opsForHash().hasKey(key, field);
    }

    @Override
    public void opsForMapPut(K key, K field, V value)
    {
        provider.provider().opsForHash().put(key, field, value);
    }

    @Override
    public V opsForMapGet(K key, K field)
    {
        return (V)provider.provider().opsForHash().get(key, field);
    }

    @Override
    public Map<K, V> opsForMapGetAll(K key)
    {
        Map<K, V> map = (Map<K, V>)provider.provider().opsForHash().entries(key);
        return map;
    }

    @Override
    public void delMapFieldKey(K key, K... field)
    {
        provider.provider().opsForHash().delete(key, field);

    }

    @Override
    public Set<K> getFieldSet(K key)
    {
        return (Set<K>)provider.provider().opsForHash().keys(key);
    }

    /** hash end */

    public void setProvider(RedisTemplateProvider provider)
    {

        this.provider = provider;
    }

    /** redis 自增 start */
    @Override
    public long inc(String key)
    {

        return provider.provider().opsForValue().increment((K)key, 1);
    }

    @Override
    public long incBy(String key, long step)
    {
        return provider.provider().opsForValue().increment((K)key, step);
    }

    /** redis 自增 end */

    protected RedisTemplate<K, V> getRedisTemplate()
    {
        RedisTemplate<K, V> template = provider.provider();
        if(template==null)
            throw new RuntimeException(); // 该异常只会在开发阶段出现

        return template;
    }

    /**
     * 设置过期
     */
    @Override
    public void expire(String key, long timeout, TimeUnit unit)
    {
        provider.provider().expire((K)key, timeout, unit);
    }

}
