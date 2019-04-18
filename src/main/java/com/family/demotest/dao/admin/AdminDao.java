package com.family.demotest.dao.admin;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.demotest.entity.AdminModel;

/**
 * 
 * @author lim
 *
 */
public interface AdminDao
    extends QueryDao<AdminModel>, SaveDao<AdminModel>, DeleteDao<AdminModel>
{

    public AdminModel findByAccount(String account);

    /**
     * 代理商管理员列表
     * 
     * @param channelNo
     * @return
     */
    public List<AdminModel> list(String channelNo);

    /**
     * 获取总帐号信息
     * 
     * @param channelNo
     * @return
     */
    public AdminModel getManage(String channelNo);

}
