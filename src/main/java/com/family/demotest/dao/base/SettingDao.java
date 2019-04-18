package com.family.demotest.dao.base;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.BaseSettingModel;
import com.family.demotest.vo.base.BaseConfigVo;

/**
 * 
 * @author wujf
 *
 */
public interface SettingDao
    extends
    QueryDao<BaseSettingModel>,
    SaveDao<BaseSettingModel>,
    DeleteDao<BaseSettingModel>
{

    /**
     * 通过渠道号获取信息
     * 
     * @param channelNo
     * @return
     */
    public BaseSettingModel findByChannelNo(String channelNo);

    /**
     * 总后台获取站点列表
     * 
     * @param model
     * @param pageSize
     * @param page
     * @return
     */
    public PageList<BaseSettingModel> getPageList(BaseConfigVo model, int pageSize, int page);

}
