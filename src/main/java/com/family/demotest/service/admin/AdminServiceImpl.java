package com.family.demotest.service.admin;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.base01.util.Security;
import com.family.demotest.dao.admin.AdminDao;
import com.family.demotest.entity.AdminModel;
import com.family.demotest.entity.AdminTokenModel;
import com.family.demotest.vo.login.ResetPswVo;
import com.family.demotest.web.util.CookieUtils;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
@Service("ppq.admin.service")
public class AdminServiceImpl
    extends
    ServiceSupport<AdminModel>
    implements
    AdminService
{

    @Autowired
    private AdminDao adminDao;
    @Autowired
    private Security security;
    @Autowired
    private AdminTokenService adminTokenService;
    @Autowired
    private Logger logger;

    @Override
    protected QueryDao<AdminModel> getQueryDao()
    {

        return adminDao;
    }

    @Override
    protected SaveDao<AdminModel> getSaveDao()
    {

        return adminDao;
    }

    @Override
    protected DeleteDao<AdminModel> getDeleteDao()
    {

        return adminDao;
    }

    @Override
    public ResultCode login(String account, String password, HttpServletResponse response)
    {
        ResultCode result = new ResultCode();
        try
        {
            AdminModel adminModel = getByAccount(account);
            if(adminModel!=null&&security.md5(password).equals(adminModel.getPasswd()))
            {
                adminModel.setUpdateTime(new Date());
                adminDao.save(adminModel);
                /* 生成token,存放到cookie */
                AdminTokenModel tokenModel = adminTokenService.refresh(adminModel.getId());
                String token = tokenModel.getToken();
                CookieUtils.addCookie(response, "token", token, 2592000);

                // 返回结果
                JSONObject data = new JSONObject();
                data.put("token", token);
                data.put("channelNo", adminModel.getChannelNo());
                data.put("account", adminModel.getAccount());

                result.setData(data);
            }
            else
            {
                result.setCode(-1);
                result.setInfo("帐号密码错误");
            }
        }
        catch(Exception e)
        {
            logger.error(e, "登录出错");
            result.setCode(-1);
            result.setInfo("登录出错");
        }

        return result;
    }

    @Override
    public AdminModel getByAccount(String account)
    {
        return adminDao.findByAccount(account);
    }

    @Override
    public AdminModel ctLogin(String account, String password)
    {

        ResultCode result = new ResultCode();
        AdminModel adminModel = null;
        try
        {

            adminModel = getByAccount(account);
            if(adminModel!=null&&adminModel.getChannelNo().equals("1000")&&security.md5(password).equals(adminModel.getPasswd()))
            {
                adminModel.setUpdateTime(new Date());
                adminDao.save(adminModel);

                AdminModel model = new AdminModel();
                model.setId(adminModel.getId());
                model.setChannelNo(adminModel.getChannelNo());
                model.setAccount(adminModel.getAccount());
                // model.setRoleId(adminModel.getRoleId());
                return model;
            }
            else
            {
                adminModel = null;
            }

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("登录出错");
            logger.error(e, "总后台登录出错");
        }

        return adminModel;

    }

    @Override
    public ResultCode list(String channelNo)
    {
        ResultCode result = new ResultCode();

        try
        {
            List<AdminModel> pageList = adminDao.list(channelNo);
            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取管理员列表出错");
            logger.error(e, "获取管理员列表出错");
        }
        return result;
    }

    @Override
    public ResultCode save(String channelNo, String account, String password)
    {
        ResultCode result = new ResultCode();

        AdminModel manage = this.getManage(channelNo);
        if(manage==null)
        {
            result.setCode(-1);
            result.setInfo("总帐号异常");
            return result;
        }

        AdminModel admin = new AdminModel();
        admin.setChannelNo(channelNo);
        admin.setStatus(0);
        admin.setCreateTime(new Date());
        admin.setAccount(account+"@"+manage.getAccount());
        admin.setPasswd(security.md5(password));

        try
        {
            this.save(admin);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存管理员出错");
            logger.error(e, "保存管理员出错");
        }

        return result;
    }

    @Override
    public AdminModel getManage(String channelNo)
    {
        return adminDao.getManage(channelNo);
    }

    @Override
    public ResultCode resetPws(ResetPswVo vo)
    {
        ResultCode result = new ResultCode();

        AdminModel admin = this.getByAccount(vo.getAccount());
        if(admin==null)
        {
            result.setCode(-1);
            result.setInfo("该账号不存在");
            return result;
        }

        if(!admin.getPasswd().equals(security.md5(vo.getOldPassword())))
        {
            result.setCode(-1);
            result.setInfo("原密码错误");
            return result;

        }
        if(!vo.getNewPassword().equals(vo.getConfirmPassword()))
        {
            result.setCode(-1);
            result.setInfo("新密码不一致");
            return result;
        }

        String newPass = security.md5(vo.getNewPassword());
        admin.setPasswd(newPass);
        this.save(admin);

        return result;

    }

}
