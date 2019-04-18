package com.family.demotest.dao.admin;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.demotest.entity.AdminTokenModel;

public interface AdminTokenDao
    extends QueryDao<AdminTokenModel>, SaveDao<AdminTokenModel>, DeleteDao<AdminTokenModel>
{

    public AdminTokenModel findOne(StringBuilder where, List<Object> args);

}
