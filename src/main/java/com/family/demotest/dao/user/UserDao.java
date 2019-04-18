package com.family.demotest.dao.user;

import java.util.List;

import com.family.base01.dao.Dao;
import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.UserModel;

public interface UserDao
    extends
    QueryDao<UserModel>,
    SaveDao<UserModel>,
    DeleteDao<UserModel>,
    Dao<UserModel>
{

    /**
     * 查询用户信息,取消关注的用户也可以查
     * 
     * @param string
     * @return
     */
    public UserModel findByOpenIdAndFollow(String string, int follow);

    /**
     * 通过商家编号和openid获取对象
     * 
     * @param channelNo
     * @param openId
     * @return
     */
    public UserModel findByChannelNoAndOpenId(String channelNo, String openId);

    /**
     * 通过openid和关注状态 获取用户信息
     * 
     * @param openId
     *            用户openid
     * @return
     */
    public UserModel getUserInfoByOpenId(String openId);

    /**
     * 通过手机号查找对象
     * 
     * @param channelNo
     * @param phone
     * @return
     */
    public UserModel findByTel(String channelNo, String phone);

    /**
     * 用户列表
     * 
     * @param channelNo
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<UserModel> getPageList(String channelNo, String search, int page, int pageSize);

    /**
     * 统计
     * 
     * @param string
     * @param objects
     * @return
     */
    public int count(String where, Object[] args);

    /**
     * 获取所有用户
     * 
     * @param channelNo
     * @return
     */
    public List<UserModel> getList(String channelNo);

}
