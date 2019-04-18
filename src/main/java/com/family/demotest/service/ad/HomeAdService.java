package com.family.demotest.service.ad;

import java.util.List;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.base01.util.PageList;
import com.family.demotest.entity.HomeAdModel;

/**
 * 
 * @author wujf
 *
 */
public interface HomeAdService
    extends
    SaveService<HomeAdModel>,
    DeleteService<HomeAdModel>,
    QueryService<HomeAdModel>
{

    /**
     * 通过渠道获取列表
     * 
     * @param channelNo
     * @param type
     * @return
     */
    public List<HomeAdModel> getList(String channelNo, int type);

    /**
     * 后台列表
     * 
     * @param model
     * @param pageSize
     * @param page
     * @return
     */
    public PageList<HomeAdModel> getPageList(HomeAdModel model, int pageSize, int page);

}
