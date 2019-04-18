package com.family.demotest.dao.admin;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.demotest.entity.AdminTokenModel;

@Repository("partner.token.dao")
public class AdminTokenDaoImpl
    extends DaoSupport<AdminTokenModel>
    implements AdminTokenDao
{

    @Override
    protected Class<AdminTokenModel> getModelClass()
    {
        return AdminTokenModel.class;
    }

    @Override
    public AdminTokenModel findOne(StringBuilder where, List<Object> args)
    {
        return this.findOne(newQueryBuilder().where(where.toString()), args.toArray());
    }

}
