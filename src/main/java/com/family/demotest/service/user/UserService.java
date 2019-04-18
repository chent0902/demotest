package com.family.demotest.service.user;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.base01.util.PageList;
import com.family.demotest.entity.UserModel;
import com.family.demotest.vo.user.UserVo;
import com.family.demotest.web.util.ResultCode;

public interface UserService
    extends
    QueryService<UserModel>,
    SaveService<UserModel>,
    DeleteService<UserModel>
{

    /**
     * 通过openid和是否已关注标识获取用户信息
     * 
     * @param openid
     * @param follow
     *            1-已关注，-1-取消关注了
     * @return
     */
    public UserModel findByOpenIdAndFollow(String openid, int follow);

    /**
     * 通过商户编号和openid获取对象
     * 
     * @param channelNo
     * @param openId
     * @return
     */
    public UserModel findByChannelNoAndOpenId(String channelNo, String openId);

    /**
     * 通过openid 获取用户信息
     * 
     * @param openId
     *            用户openid
     * @return
     */
    // public UserModel getUserInfoByOpenId(String openId);

    /**
     * 判断该手机号是否已绑定用户
     * 
     * @param channelNo
     * @param phone
     * @return
     */
    public boolean existTel(String channelNo, String phone);

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
     * 用户关注+登陆状态
     * 
     * @param id
     * @return
     */
    public ResultCode userStatus(String id);

    /**
     * 后台首页用户Json
     * 
     * @param channelNo
     * @return
     */
    public JSONObject getUserJson(String channelNo);

    /**
     * 保存用户信息到缓存
     * 
     * @param token
     * 
     * @param user
     *            用户对象
     */
    public void putUserVo(String token, UserModel user);

    /**
     * 通过token获取用户信息
     * 
     * @param token
     * @return
     */
    public UserVo getUserVo(String token);

    /**
     * 
     * 总后台数据统计
     * 
     * @return
     */
    public JSONObject homeData();

    /**
     * 关闭数据库
     */
    public void close();

    /**
     * 获取所有用户
     * 
     * @param channelNo
     * @return
     */
    public List<UserModel> getList(String channelNo);

}
