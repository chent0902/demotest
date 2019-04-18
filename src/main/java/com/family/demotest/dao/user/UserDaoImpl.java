package com.family.demotest.dao.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.entity.UserModel;

@Repository("wx.user.dao")
public class UserDaoImpl
    extends
    DaoSupport<UserModel>
    implements
    UserDao
{
    public static final String FIND_BY_OPENID = " o.openid = ? AND o.follow = ?  ";
    public static final String GET_USER_BY_OPENID = " o.openid = ? ";
    private static final String FIND_BY_CHANNELNO_OPENID = " o.channelNo=? AND  o.openid = ? and o.property=0 ";
    private static final String FIND_BY_TEL = " o.channelNo=? AND  o.tel = ? and o.property=0 ";

    @Autowired
    private Validator validator;

    @Override
    protected Class<UserModel> getModelClass()
    {
        return UserModel.class;
    }

    @Override
    public UserModel findByOpenIdAndFollow(String openid, int follow)
    {
        return findOne(newQueryBuilder().where(FIND_BY_OPENID), new Object[]{openid, follow});
    }

    @Override
    public UserModel findByChannelNoAndOpenId(String channelNo, String openId)
    {
        return findOne(newQueryBuilder().where(FIND_BY_CHANNELNO_OPENID), new Object[]{channelNo, openId});
    }

    @Override
    public UserModel getUserInfoByOpenId(String openId)
    {

        return findOne(newQueryBuilder().where(GET_USER_BY_OPENID), new Object[]{openId});
    }

    @Override
    public UserModel findByTel(String channelNo, String phone)
    {
        return this.findOne(newQueryBuilder().where(FIND_BY_TEL), new Object[]{channelNo, phone});
    }

    @Override
    public PageList<UserModel> getPageList(String channelNo, String search, int page, int pageSize)
    {

        StringBuffer where = new StringBuffer(" o.channelNo = ?  AND o.property = 0 AND o.nickName IS NOT NULL ");
        List<Object> args = new ArrayList<Object>();
        args.add(channelNo);

        if(!validator.isBlank(search))
        {
            where.append(" and (o.nickName like ? or o.tel=?  )");
            args.add("%"+search+"%");
            args.add(search);
        }

        PageList<UserModel> pageList = list(newQueryBuilder().where(where.toString()), args.toArray(), pageSize, page);

        int totalPage = pageList.getPageInfo().getTotalPage();

        if(page>totalPage)
        {
            return null;
        }
        return pageList;
    }

    @Override
    public int count(String where, Object[] args)
    {
        return (int)this.count(newQueryBuilder().where(where), args);
    }

    @Override
    public List<UserModel> getList(String channelNo)
    {

        String sql = "SELECT c_id,c_openid from t_wx_user where c_channel_no=? and c_follow=1 ";
        List<UserModel> userList = new ArrayList<UserModel>();
        UserModel model = null;
        List<Object> list = null;
        List<List<Object>> result = query(sql, new Object[]{channelNo});
        if(result!=null&&result.size()>0)
        {
            for(int i = 0; i<result.size(); i++)
            {
                list = result.get(i);
                if(list!=null&&list.size()>0)
                {
                    model = new UserModel();
                    model.setId(list.get(0).toString());
                    model.setOpenid(list.get(1).toString());
                    userList.add(model);
                }

            }
        }
        return userList;

    }
}
