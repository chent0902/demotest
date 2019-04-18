package com.family.demotest.service.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Security;
import com.family.demotest.dao.admin.AdminTokenDao;
import com.family.demotest.entity.AdminModel;
import com.family.demotest.entity.AdminTokenModel;

@Service("partner.token.service")
public class AdminTokenServiceImpl
    extends ServiceSupport<AdminTokenModel>
    implements AdminTokenService
{

    @Autowired
    private Security security;
    @Autowired
    private AdminTokenDao partnerTokenDao;
    @Autowired
    private AdminService adminService;

    @Override
    protected QueryDao<AdminTokenModel> getQueryDao()
    {
        return partnerTokenDao;
    }

    @Override
    protected SaveDao<AdminTokenModel> getSaveDao()
    {
        return partnerTokenDao;
    }

    @Override
    protected DeleteDao<AdminTokenModel> getDeleteDao()
    {
        return partnerTokenDao;
    }

    @Override
    public AdminTokenModel refresh(String adminId)
    {
        AdminTokenModel tokenModel = this.findByAdmin(adminId);
        if(tokenModel==null)
        {
            tokenModel = new AdminTokenModel();
            tokenModel.setAdminId(adminId);
            tokenModel.setCreateTime(Calendar.getInstance().getTime());
        }

        AdminModel adminModel = adminService.findById(adminId);
        String token = security.md5(adminModel.getAccount()+System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 1);

        tokenModel.setToken(token);
        tokenModel.setExpiredTime(cal.getTime());
        partnerTokenDao.save(tokenModel);
        return tokenModel;
    }

    @Override
    public AdminTokenModel findByAdmin(String adminId)
    {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<Object>();

        where.append(" o.adminId=?");
        args.add(adminId);

        return partnerTokenDao.findOne(where, args);
    }

    @Override
    public AdminTokenModel findByToken(String token)
    {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<Object>();

        where.append(" o.token=?");
        args.add(token);
        return partnerTokenDao.findOne(where, args);
    }

}
