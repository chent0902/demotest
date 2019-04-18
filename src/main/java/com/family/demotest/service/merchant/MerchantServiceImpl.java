package com.family.demotest.service.merchant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.dao.merchant.MerchantDao;
import com.family.demotest.entity.MerchantModel;
import com.family.demotest.entity.MerchantUserModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
@Service("merchant.service")
public class MerchantServiceImpl
    extends ServiceSupport<MerchantModel>
    implements MerchantService
{
    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private Validator validator;
    @Autowired
    private Logger logger;
    @Autowired
    private MerchantUserService merchantUserService;

    @Override
    protected QueryDao<MerchantModel> getQueryDao()
    {
        return merchantDao;
    }

    @Override
    protected SaveDao<MerchantModel> getSaveDao()
    {
        return merchantDao;
    }

    @Override
    protected DeleteDao<MerchantModel> getDeleteDao()
    {
        return merchantDao;
    }

    @Override
    public ResultCode createOrUpdate(MerchantModel merchant)
    {

        ResultCode result = new ResultCode();
        MerchantModel model = null;
        if(validator.isBlank(merchant.getId()))
        {
            model = new MerchantModel();
            model.setChannelNo(merchant.getChannelNo());
            model.setCreateTime(new Date());
        }
        else
        {
            model = this.findById(merchant.getId());
        }
        model.setName(merchant.getName());
        model.setLogo(merchant.getLogo());
        model.setAddress(merchant.getAddress());
        model.setTel(merchant.getTel());
        model.setPassword(merchant.getPassword());
        model.setLongitude(validator.isBlank(merchant.getLongitude())?"":merchant.getLongitude());
        model.setLatitude(validator.isBlank(merchant.getLatitude())?"":merchant.getLatitude());
        model.setService(validator.isBlank(merchant.getService())?"":merchant.getService());
        model.setContactsTel(validator.isBlank(merchant.getContactsTel())?"":merchant.getContactsTel());
        model.setContacts(validator.isBlank(merchant.getContacts())?"":merchant.getContacts());
        try
        {
            this.save(model);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("保存商户信息出错");
            logger.error(e, "保存商户信息出错");
        }

        return result;

    }

    @Override
    public ResultCode pageList(String channelNo, int page, int pageSize)
    {
        ResultCode result = new ResultCode();

        try
        {
            PageList<MerchantModel> pageList = merchantDao.pageList(channelNo, page, pageSize);
            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取商户列表出错");
            logger.error(e, "获取商户列表出错");
        }

        return result;
    }

    @Override
    public ResultCode findAllName(String channelNo, String name)
    {
        ResultCode result = new ResultCode();
        StringBuffer sql = new StringBuffer(" SELECT t.c_id,t.c_name FROM t_merchant t WHERE t.c_channel_no = ? AND t.c_property = 0 ");
        List<Object> args = new ArrayList<Object>();
        args.add(channelNo);
        if(!validator.isBlank(name))
        {
            sql.append(" AND t.c_name LIKE ? ");
            args.add("%"+name+"%");
        }
        sql.append(" ORDER BY t.c_create_time DESC ");
        try
        {
            List<MerchantModel> list = merchantDao.findAllName(sql, args);
            result.setData(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取全部商户信息出错");
            logger.error(e, "获取全部商户信息出错");
        }
        return result;
    }

    @Override
    public ResultCode findByUserId(String channelNo, String userId)
    {
        ResultCode result = new ResultCode();

        try
        {
            MerchantUserModel model = merchantUserService.findByUserId(channelNo, userId);
            if(model!=null)
            {
                MerchantModel merchant = merchantDao.findById(model.getMerchantId());
                result.setData(merchant);
            }
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载商户信息出错");
            logger.error(e, "加载商户信息出错");
        }

        return result;
    }
}
