package com.family.demotest.service.user;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.WxUserRedisBiz;
import com.family.demotest.dao.user.UserDao;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.vo.user.UserVo;
import com.family.demotest.web.util.DateUtils;
import com.family.demotest.web.util.ResultCode;

@Service("wx.user.service")
public class UserServiceImpl
    extends
    ServiceSupport<UserModel>
    implements
    UserService
{
    @Autowired
    private UserDao userDao;
    @Autowired
    private Logger logger;
    @Autowired
    private Validator validator;

    @Autowired
    private ConfigService configService;
    @Autowired
    protected WxUserRedisBiz wxUserRedisBiz;

    @Override
    public UserModel findByOpenIdAndFollow(String openid, int follow)
    {
        return userDao.findByOpenIdAndFollow(openid, follow);

    }

    @Override
    protected QueryDao<UserModel> getQueryDao()
    {
        return userDao;
    }

    @Override
    protected SaveDao<UserModel> getSaveDao()
    {
        return userDao;
    }

    @Override
    protected DeleteDao<UserModel> getDeleteDao()
    {
        return userDao;
    }

    @Override
    public UserModel findByChannelNoAndOpenId(String channelNo, String openId)
    {
        return userDao.findByChannelNoAndOpenId(channelNo, openId);
    }

    @Override
    public boolean existTel(String channelNo, String phone)
    {
        return userDao.findByTel(channelNo, phone)==null?false:true;
    }

    @Override
    public PageList<UserModel> getPageList(String channelNo, String search, int page, int pageSize)
    {

        return userDao.getPageList(channelNo, search, page, pageSize);

    }

    @Override
    public ResultCode userStatus(String userId)
    {
        ResultCode result = new ResultCode();

        try
        {
            UserModel user = this.findById(userId);
            if(user==null)
            {
                result.setCode(999);// 不存在该用户，token设置为失效
                return result;
            }
            if(user.getFollow()==1)
            {
                if(validator.isBlank(user.getTel()))
                {
                    result.setCode(901);// 901 未登陆(绑定手机号)
                }

            }
            else
            {
                // logger.info("*****900未关注:"+JSON.toJSONString(user));
                result.setCode(900);// 900未关注
                BaseConfigModel config = configService.findByChannelNo(user.getChannelNo());
                if(config==null)
                {
                    result.setCode(-1);
                    result.setInfo("参数错误");
                    return result;
                }
                result.setData(config);
            }
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载用户状态出错");
            logger.error(e, "加载用户状态出错");
        }
        return result;
    }

    private static final String TIME = " AND o.createTime >= ? AND o.createTime <= ? ";

    @Override
    public JSONObject getUserJson(String channelNo)
    {
        JSONObject resultJson = new JSONObject();

        StringBuffer where = new StringBuffer(" o.channelNo = ? AND o.property = 0 AND o.nickName IS NOT NULL AND o.headimgurl IS NOT NULL ");
        Date todayStart = DateUtils.getDayStart(new Date());
        Date todayend = DateUtils.getDayEnd(new Date());
        Date yestStart = DateUtils.getDayStart(DateUtils.addDay(new Date(), -1));
        Date yestEnd = DateUtils.getDayEnd(DateUtils.addDay(new Date(), -1));
        // 用户总数
        int totalNum = userDao.count(where.toString(), new Object[]{channelNo});
        resultJson.put("totalNum", totalNum);
        // 今日新增
        int todayNum = userDao.count(where.append(TIME).toString(), new Object[]{channelNo, todayStart, todayend});
        resultJson.put("todayNum", todayNum);
        // 昨日新增
        int yesterdayNum = userDao.count(where.toString(), new Object[]{channelNo, yestStart, yestEnd});
        resultJson.put("yesterdayNum", yesterdayNum);

        return resultJson;
    }

    @Override
    public void putUserVo(String token, UserModel user)
    {
        // 保存1天
        UserVo vo = new UserVo();
        vo.setId(user.getId());
        vo.setChannelNo(user.getChannelNo());
        vo.setOpenid(user.getOpenid());
        vo.setNickName(user.getNickName());
        wxUserRedisBiz.put(token, vo, 1, TimeUnit.DAYS);
    }

    @Override
    public UserVo getUserVo(String token)
    {
        return wxUserRedisBiz.get(token);
    }

    private static final String COUNT_WHERE = " o.createTime >= ? AND o.createTime <= ? AND o.nickName IS NOT NULL ";

    @Override
    public JSONObject homeData()
    {
        JSONObject jsonObject = new JSONObject();
        // 昨日用户 本月用户 上月用户 总用户数
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        Date yestStart = DateUtils.getDayStart(DateUtils.addDay(new Date(), -1));
        Date yestEnd = DateUtils.getDayEnd(DateUtils.addDay(new Date(), -1));
        Date monthStart = DateUtils.getMonthStart(year, month);
        Date monthEnd = DateUtils.getMonthEnd(year, month);
        Date lastMonthStart = DateUtils.getMonthStart(year, month-1);
        Date lastMonthEnd = DateUtils.getMonthEnd(year, month-1);
        int totalNum = userDao.count(" 1 = 1 ", null);
        jsonObject.put("totalNum", totalNum);
        int yesterdayNum = userDao.count(COUNT_WHERE, new Object[]{yestStart, yestEnd});
        jsonObject.put("yesterdayNum", yesterdayNum);
        int monthNum = userDao.count(COUNT_WHERE, new Object[]{monthStart, monthEnd});
        jsonObject.put("monthNum", monthNum);
        int lastMonthNum = userDao.count(COUNT_WHERE, new Object[]{lastMonthStart, lastMonthEnd});
        jsonObject.put("lastMonthNum", lastMonthNum);
        return jsonObject;
    }

    @Override
    public void close()
    {
        userDao.close();
    }

    @Override
    public List<UserModel> getList(String channelNo)
    {
        return userDao.getList(channelNo);
    }

}
