package com.family.demotest.service.admin;

import javax.servlet.http.HttpServletResponse;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.AdminModel;
import com.family.demotest.vo.login.ResetPswVo;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
public interface AdminService
    extends
    SaveService<AdminModel>,
    DeleteService<AdminModel>,
    QueryService<AdminModel>
{

    /**
     * 代理商登陆
     * 
     * @param account
     * @param password
     * @param response
     * @return
     */
    public ResultCode login(String account, String password, HttpServletResponse response);

    /**
     * 通过账号查找
     * 
     * @param account
     * @return
     */
    public AdminModel getByAccount(String account);

    /**
     * 总后台登录
     * 
     * @param account
     * @param password
     * @return
     */
    public AdminModel ctLogin(String account, String password);

    /**
     * 代理商管理员列表
     * 
     * @param channelNo
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode list(String channelNo);

    /**
     * 保存管理员信息
     * 
     * @param account
     * @param password
     * @param password2
     * @return
     */
    public ResultCode save(String channelNo, String account, String password);

    /**
     * 获取总帐号信息
     * 
     * @param channelNo
     * @return
     */
    public AdminModel getManage(String channelNo);

    /**
     * 重置密码
     * 
     * @param vo
     * @return
     */
    public ResultCode resetPws(ResetPswVo vo);

}
