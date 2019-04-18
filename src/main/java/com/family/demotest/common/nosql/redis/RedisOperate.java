package com.family.demotest.common.nosql.redis;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.family.demotest.common.nosql.NoSqlOperate;



/**
 * 
 * @author wujf
 *
 * @param <K>
 * @param <V>
 */
public interface RedisOperate<K, V>
    extends
    NoSqlOperate<K, V>
{

    /** hash map 操作 start */
    public boolean opsForMapHasKey(K key, K field);

    public void opsForMapPut(K key, K field, V value);

    public V opsForMapGet(K key, K field);

    public Map<K, V> opsForMapGetAll(K key);

    /** 删除map中的属性 */
    public void delMapFieldKey(K key, K... field);

    /** 获取map中的所有属性 */
    public Set<K> getFieldSet(K key);

    /** hash map 操作 end */

    /** int long 类型自增 ++ */
    long inc(String key);

    /** int 类型自增，，以step为步长 */
    long incBy(String key, long step);

    void expire(String key, final long timeout, final TimeUnit unit);
}
