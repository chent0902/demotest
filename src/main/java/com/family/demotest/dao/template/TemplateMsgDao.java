package com.family.demotest.dao.template;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.demotest.entity.TemplateMsgModel;

/**
 * 
 * @author wujf
 *
 */
public interface TemplateMsgDao
    extends
    QueryDao<TemplateMsgModel>,
    SaveDao<TemplateMsgModel>,
    DeleteDao<TemplateMsgModel>
{

    /**
     * 获取模板消息列表
     * 
     * @param channelNo
     * @return
     */
    public List<TemplateMsgModel> getList(String channelNo);

    /**
     * 通过类型查找
     * 
     * @param channelNo
     * @param type
     * @return
     */
    public TemplateMsgModel findByType(String channelNo, int type);

}
