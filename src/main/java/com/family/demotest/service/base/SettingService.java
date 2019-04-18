package com.family.demotest.service.base;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.base01.util.PageList;
import com.family.demotest.entity.BaseSettingModel;
import com.family.demotest.vo.base.BaseConfigVo;

/**
 * 
 * @author wujf
 *
 */
public interface SettingService
    extends
    SaveService<BaseSettingModel>,
    DeleteService<BaseSettingModel>,
    QueryService<BaseSettingModel>
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
