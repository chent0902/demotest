package com.family.demotest.dao.ad;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.HomeAdModel;

/**
 * 
 * @author wujf
 *
 */
public interface HomeAdDao
    extends
    QueryDao<HomeAdModel>,
    SaveDao<HomeAdModel>,
    DeleteDao<HomeAdModel>
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
     * 
     * @param where
     * @param args
     * @param pageSize
     * @param page
     * @return
     */
    public PageList<HomeAdModel> getPageList(HomeAdModel model, int pageSize, int page);
}
