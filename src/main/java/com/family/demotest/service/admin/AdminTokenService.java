package com.family.demotest.service.admin;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.AdminTokenModel;

public interface AdminTokenService
    extends SaveService<AdminTokenModel>, DeleteService<AdminTokenModel>, QueryService<AdminTokenModel>
{

    public AdminTokenModel refresh(String adminId);

    public AdminTokenModel findByAdmin(String adminId);

    public AdminTokenModel findByToken(String token);

}
