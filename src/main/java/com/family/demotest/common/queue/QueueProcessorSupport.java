package com.family.demotest.common.queue;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lpw.wormhole.queue.QueueProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.family.base01.scheduler.DateJob;
import com.family.base01.util.Converter;
import com.family.base01.util.Logger;

/**
 * 队列处理器支持。
 * 
 * @author wujf
 */
public abstract class QueueProcessorSupport<T, E>
    implements
    QueueProcessor,
    DateJob,
    QueueDataSupplier<T>,
    QueueDataReloader
{
    protected static final String DATE_PATTERN = "yyyyMMdd";// 日期key格式。
    protected static final String HOUR_PATTERN = DATE_PATTERN+"HH";// 小时key格式。

    @Autowired
    protected Converter converter;
    @Autowired
    protected Logger logger;
    // @Value("${jade.queue-processor.save-interval}")
    protected int saveInterval = 30;// 保存时间间隔(分)。
    protected Map<String, T> datas;// 处理后的数据集。
    protected Map<String, Integer> repeats;// 数据重复次数。

    public QueueProcessorSupport()
    {
        datas = Collections.synchronizedMap(new HashMap<String, T>());
        repeats = Collections.synchronizedMap(new HashMap<String, Integer>());
    }

    @Override
    public int getProcessSort()
    {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(Map<Object, Object> context, Object value)
    {
        E v = (E)value;
        String dateKey = getKey(v);
        process(dateKey, dateKey.substring(0, dateKey.indexOf('-')), v);

    }

    /**
     * 处理队列消息。
     * 
     * @param dataKey
     *            缓存key。
     * @param time
     *            时间片段。
     * @param value
     *            要处理的对象。
     */
    protected abstract void process(String dataKey, String time, E value);

    /**
     * 获取引用key。
     * 
     * @param value
     *            要处理的数据。
     * @return 引用key。
     */
    protected abstract String getKey(E value);

    /**
     * 处理队列消息。
     * 
     * @param time
     *            时间片段。
     * @param data
     *            缓存的数据。
     * @param context
     *            处理上下文环境，可在多个处理器中传递。
     * @param value
     *            要处理的对象。
     */
    protected abstract T process(String time, T data, Map<Object, Object> context, E value);

    /**
     * 获取数据重复量。
     * 
     * @param time
     *            时间。
     * @param key
     *            数据类型key。
     * @param value
     *            数据值。
     * @return 数据重复次数；不存在则返回0。
     */
    protected int getRepeat(String time, String key, String value)
    {
        Integer repeat = repeats.get(getRepeatKey(time, key, value));
        return repeat==null?0:repeat;
    }

    /**
     * 设置数据重复量。每次自动增加1。
     * 
     * @param time
     *            时间。
     * @param key
     *            数据类型key。
     * @param value
     *            数据值。
     */
    protected void setRepeat(String time, String key, String value)
    {
        String repeatKey = getRepeatKey(time, key, value);
        Integer repeat = repeats.get(repeatKey);
        repeats.put(repeatKey, repeat==null?1:(repeat+1));
    }

    /**
     * 获取重复数据引用key。
     * 
     * @param time
     *            时间。
     * @param key
     *            数据类型key。
     * @param value
     *            数据值。
     * @return 引用key。
     */
    protected String getRepeatKey(String time, String key, String value)
    {
        return new StringBuilder().append(time).append('-').append(key).append('`').append(value).toString();
    }

    @Override
    public void executeDateJob()
    {
        save();// 每天同步前一天的缓存数据到数据库中
        close();// 关闭数据库

    }

    /**
     * 清除过期数据。
     * 
     * @param map
     *            要清除的数据集。
     * @param date
     *            日期(当天)。
     * @param hour
     *            小时。
     */
    protected void clear(Map<String, ?> map, String date, String hour)
    {
        Set<String> overdue = new HashSet<String>();
        for(String key : map.keySet())
        {
            String prefix = key.substring(0, key.indexOf('-')); // 截取key前面的时间(格式:20161115)
            if(prefix.equals(date))
                continue;

            if(logger.isInfoEnable())
                logger.info(date+",***清除缓存数据key："+key);

            overdue.add(key);
        }

        if(overdue.isEmpty())
            return;

        for(String key : overdue)
            map.remove(key);
    }

    /**
     * 同步缓存数据到数据库。
     * 
     */
    protected abstract void save();

    /**
     * 检索指定时间的所有持久化的统计数据。
     * 
     * @param date
     *            日期。
     * @return 统计数据集；为空则返回空集。
     */
    protected abstract Iterator<T> iterate(Date date);

    /**
     * 关闭数据库连接。
     */
    protected abstract void close();

    @Override
    public List<T> getCurrentDatas()
    {
        List<T> list = new ArrayList<T>();
        String date = converter.toString(new Date(), DATE_PATTERN);
        String hour = converter.toString(new Date(), HOUR_PATTERN);
        for(String key : datas.keySet())
        {
            String prefix = key.substring(0, key.indexOf('-'));
            if(prefix.equals(date)||prefix.equals(hour))
                list.add(datas.get(key));
        }

        return list;
    }

    /**
     * 重新加载数据到内存空间。
     */
    @Override
    public void reload()
    {
        // for(Iterator<T> iterator = iterate(new Date()); iterator!=null
        // &&iterator.hasNext();)
        // reload(iterator.next());
        // close();
    }

    /**
     * 重新加载数据。
     * 
     * @param data
     *            持久化的数据。
     */
    protected void reload(T data)
    {
        process(null, data);
    }
}
