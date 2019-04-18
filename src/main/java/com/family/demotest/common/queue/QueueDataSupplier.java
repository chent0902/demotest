package com.family.demotest.common.queue;



import java.util.List;

/**
 * 队列数据管理器。
 * 
 * @author wujf
 */
public interface QueueDataSupplier<T>
{
    /**
     * 获取当前数据。
     * 
     * @return 当前数据集。
     */
    public List<T> getCurrentDatas();
    
    

}
