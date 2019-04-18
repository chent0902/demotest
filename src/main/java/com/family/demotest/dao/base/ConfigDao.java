package com.family.demotest.dao.base;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.vo.base.BaseConfigVo;

/**
 * 
 * @author wujf
 *
 */
public interface ConfigDao
    extends
    QueryDao<BaseConfigModel>,
    SaveDao<BaseConfigModel>,
    DeleteDao<BaseConfigModel>
{

    /**
     * 通过渠道号获取信息
     * 
     * @param channelNo
     * @return
     */
    public BaseConfigModel findByChannelNo(String channelNo);

    /**
     * 获取最大编号
     * 
     * @return
     */
    public String maxCode();

    /**
     * 通过appid获取信息
     * 
     * @param appid
     * @return
     */
    public BaseConfigModel findByAppid(String appid);

    /**
     * 获取所以配置列表
     * 
     * @return
     */
    public List<BaseConfigModel> getListAll();

    /**
     * 总后台获取站点列表
     * 
     * @param model
     * @param pageSize
     * @param page
     * @return
     */
    public PageList<BaseConfigModel> getPageList(BaseConfigVo model, int pageSize, int page);

}
