package com.family.demotest.service.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Https;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.base01.util.Security;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.PartnerSummaryRedisBiz;
import com.family.demotest.common.biz.StringRedisBiz;
import com.family.demotest.common.constant.Constants;
import com.family.demotest.dao.base.ConfigDao;
import com.family.demotest.entity.AdminModel;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.entity.BaseSettingModel;
import com.family.demotest.service.admin.AdminService;
import com.family.demotest.service.base.SettingService;
import com.family.demotest.service.lottery.LotteryService;
import com.family.demotest.service.oss.OssService;
import com.family.demotest.service.wxopen.TicketService;
import com.family.demotest.vo.base.BaseConfigVo;
import com.family.demotest.vo.total.PartnerDataTotalVo;
import com.family.demotest.vo.wx.QrsceneVo;
import com.family.demotest.web.util.ResultCode;
import com.family.demotest.web.util.WxstoreUtils2;

/**
 * 
 * @author wujf
 *
 */
@Service("base.config.service")
public class ConfigServiceImpl
    extends
    ServiceSupport<BaseConfigModel>
    implements
    ConfigService
{

    private static final String DIR = "xcx/";
    @Autowired
    private ConfigDao configDao;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private Security security;
    @Autowired
    private Https https;
    @Autowired
    private Logger logger;
    @Autowired
    private Validator validator;
    @Autowired
    private StringRedisBiz stringRedisBiz;
    @Autowired
    private OssService ossService;
    @Autowired
    private PartnerSummaryRedisBiz partnerSummaryRedisBiz;
    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private SettingService settingService;

    @Override
    public PageList<BaseConfigModel> getPageList(BaseConfigVo model, int pageSize, int page)
    {
        PageList<BaseConfigModel> pageList = configDao.getPageList(model, pageSize, page);
        if(pageList!=null&&pageList.getList().size()>0)
        {
            for(BaseConfigModel config : pageList.getList())
            {
                PartnerDataTotalVo total = partnerSummaryRedisBiz.get(config.getId());
                if(total==null)
                {
                    total = new PartnerDataTotalVo();
                    int activityNum = lotteryService.countLotteryNum(config.getChannelNo());
                    total.setActivityNum(activityNum);
                    partnerSummaryRedisBiz.put(config.getId(), total, 360, TimeUnit.DAYS);
                }
                config.setActivePointMoney(total.getActivePointMoney());
                config.setSmsMoney(total.getSmsMoney());
                config.setActivityNum(total.getActivityNum());
            }
        }
        return pageList;
    }

    @Override
    public ResultCode createOrUpdate(BaseConfigVo configVo)
    {

        ResultCode result = new ResultCode();
        try
        {
            BaseConfigModel instance = null;
            // 新增
            if(validator.isBlank(configVo.getId()))
            {
                instance = new BaseConfigModel();
                // 生成商家编号
                instance.setChannelNo(this.generatorCode());
                // 10000表示永久
                if(configVo.getActivePoint()==10000)
                {
                    instance.setActivePoint(10000);
                }
                else
                {
                    instance.setActivePoint(10);
                }

                instance.setCreateTime(new Date());
                instance.setAccount(configVo.getAccount());
                instance.setPasswd(security.md5(configVo.getPassword()));
                // 创建登录帐号
                String adminCode = this.createAdmin(instance.getChannelNo(), configVo.getAccount(), configVo.getPassword());

                if("-1".equals(adminCode))
                {
                    result.setCode(-1);
                    result.setInfo("帐号已经存在");
                    return result;
                }

                // 保存设置
                saveSetting(instance.getChannelNo());
            }
            else
            {
                instance = this.findById(configVo.getId());
            }

            // 10000表示永久
            if(configVo.getActivePoint()==10000)
            {
                instance.setActivePoint(10000);
            }

            instance.setSiteName(configVo.getSiteName()); // 站点名称
            instance.setCompanyName(configVo.getCompanyName());// 公司名称
            instance.setAddress(configVo.getAddress());
            instance.setContacts(configVo.getContacts());
            instance.setContactsTel(configVo.getContactsTel());
            instance.setStartTime(configVo.getStartTime());
            instance.setEndTime(configVo.getEndTime());
            instance.setCityName(configVo.getCityName());// 城市名称
            instance.setSalesman(configVo.getSalesman());// 业务员
            configDao.save(instance);
        }
        catch(Exception e)
        {
            logger.error(e, "保存站点信息出错");
            result.setCode(-1);
            result.setInfo("保存站点信息出错");
        }

        return result;

    }

    /**
     * 生成商户编号
     * 
     * @param model
     */
    private String generatorCode()
    {
        String code = configDao.maxCode();
        int cp = Integer.parseInt(code)+1;
        int length = (cp+"").length();
        StringBuffer sb = new StringBuffer();

        for(; code.length()-length>0; length++)
            sb.append(0);
        sb.append(cp);

        return sb.toString();
    }

    /**
     * 创建超级管理员账号
     * 
     * @param account
     * @param password
     * @param channelNo
     * @return
     * @throws Exception
     */
    public String createAdmin(String channelNo, String account, String password)

    {
        AdminModel exist = adminService.getByAccount(account);
        if(exist!=null)
        {
            // throw new Exception("帐号已经存在");
            return "-1";
        }

        AdminModel adminModel = new AdminModel();
        adminModel.setChannelNo(channelNo);
        adminModel.setAccount(account);
        adminModel.setPasswd(security.md5(password));
        // adminModel.setRoleId("37d6782e-b985-43a9-9986-a1508043699b");
        adminModel.setStatus(0);
        adminModel.setManage(1);
        adminModel.setCreateTime(new Date());
        adminService.save(adminModel);
        return "0";
    }

    /**
     * 保存设置
     * 
     * @param channelNo
     * @return
     */
    public String saveSetting(String channelNo)
    {

        BaseSettingModel setting = settingService.findByChannelNo(channelNo);
        if(setting==null)
        {
            setting = new BaseSettingModel();
            setting.setChannelNo(channelNo);
            setting.setCreateTime(new Date());
            settingService.save(setting);
        }

        return "0";
    }

    @Override
    public BaseConfigModel findByChannelNo(String channelNo)
    {

        return configDao.findByChannelNo(channelNo);
    }

    @Override
    protected QueryDao<BaseConfigModel> getQueryDao()
    {

        return configDao;
    }

    @Override
    protected SaveDao<BaseConfigModel> getSaveDao()
    {

        return configDao;
    }

    @Override
    protected DeleteDao<BaseConfigModel> getDeleteDao()
    {

        return configDao;
    }

    @Override
    public BaseConfigModel findByAppid(String appid)
    {
        return configDao.findByAppid(appid);
    }

    @Override
    public List<BaseConfigModel> getListAll()
    {

        return configDao.getListAll();
    }

    @Override
    public ResultCode getQrCode(String channelNo, String userId, String lotteryId)
    {

        ResultCode result = new ResultCode();
        try
        {
            // 用户抢购小程序二维码key
            String qrCoceKey = getQrcodeKey(lotteryId, userId);
            String qrCodeUrl = stringRedisBiz.get(qrCoceKey);
            if(validator.isBlank(qrCodeUrl))
            {
                String sceneKey = UUID.randomUUID().toString().replace("-", "");
                String accessToken = ticketService.getAuthorizerAccessToken(channelNo);
                if(validator.isBlank(accessToken))
                {
                    result.setCode(-1);
                    result.setInfo("token 失效");
                    return result;
                }
                String qrcodeUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;

                // TreeMap<String, String> params = new TreeMap<String,
                // String>();

                Map<String, String> intMap = new HashMap<String, String>();
                intMap.put("scene_str", sceneKey);
                Map<String, Map<String, String>> mapMap = new HashMap<String, Map<String, String>>();
                mapMap.put("scene", intMap);
                Map<String, Object> paramsMap = new HashMap<String, Object>();
                paramsMap.put("expire_seconds", 2592000);// 30天
                paramsMap.put("action_name", "QR_STR_SCENE");
                paramsMap.put("action_info", mapMap);
                String data = JSON.toJSONString(paramsMap);

                String jsonStr = https.post(qrcodeUrl, data);

                if(!validator.isBlank(jsonStr))
                {
                    JSONObject o = JSON.parseObject(jsonStr);
                    String ticket = o.getString("ticket");
                    // int expireSeconds = o.getIntValue("expire_seconds");
                    // String url = o.getString("url");
                    if(!validator.isBlank(ticket))
                    {
                        String showqrcodeUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+URLEncoder.encode(ticket, "utf-8");
                        String qrCodePath = WxstoreUtils2.httpRequest3(showqrcodeUrl, "GET", null);
                        if(validator.isBlank(qrCodePath))
                        {
                            result.setCode(-1);
                            result.setInfo("生成二维码失败");
                            return result;
                        }
                        // 新的图片名称
                        String newFileName = UUID.randomUUID().toString().replace("-", "")+".png";
                        // File file = new File(qrCodePath);
                        InputStream is = new FileInputStream(new File(qrCodePath));
                        String imgSign = ossService.uploadImg(is, newFileName);
                        if(!validator.isBlank(imgSign))
                        {
                            qrCodeUrl = DIR+newFileName;
                            stringRedisBiz.put(qrCoceKey, qrCodeUrl, 2592000, TimeUnit.SECONDS);
                        }
                        else
                        {
                            result.setCode(-1);
                            result.setInfo("生成二维码失败，请重新生成");
                            return result;
                        }
                        // 保存小程序二维码参数信息,用户id和抢购id(有效期一年)
                        this.setQrCodeScene(sceneKey, userId, lotteryId);

                    }
                }
                else
                {
                    result.setCode(-1);
                    result.setInfo("获取ticket失败");
                }

            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("qrCode", qrCodeUrl);// 公众号二维码
            BaseConfigModel config = configDao.findByChannelNo(channelNo);
            if(config!=null&&validator.isBlank(config.getHeadImg()))
            {
                jsonObject.put("logo", config.getHeadImg());// 公众号logo
            }

            result.setData(jsonObject);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("生成带参二维码出错，请重新生成");
            logger.error(e, "生成带参二维码出错");
        }

        return result;
    }

    /**
     * 用户带参二维码key
     * 
     * @param userId
     * @return
     */
    private String getQrcodeKey(String lotteryId, String userId)
    {
        StringBuffer sb = new StringBuffer();
        return sb.append(Constants.QRCODE_USER_KEY).append(lotteryId).append("-").append(userId).toString();
    }

    @Override
    public void setQrCodeScene(String key, String userId, String lotteryId)
    {
        // String content = userId+"#"+lotteryId;
        StringBuilder sb = new StringBuilder();
        sb.append(userId).append("#").append(lotteryId);
        stringRedisBiz.put(key, sb.toString(), 30, TimeUnit.DAYS);

    }

    @Override
    public String getQrCodeScene(String key)
    {
        return stringRedisBiz.get(key);
    }

    @Override
    public QrsceneVo getQrSceneVo(String key)
    {
        try
        {
            String sceneStr = stringRedisBiz.get(key);
            if(!validator.isBlank(sceneStr))
            {
                String[] arr = sceneStr.split("#");
                if(arr!=null&&arr.length>0)
                {
                    QrsceneVo qrsceneVo = new QrsceneVo();
                    qrsceneVo.setUserId(arr[0]);
                    qrsceneVo.setLotteryId(arr[1]);

                    return qrsceneVo;
                }
            }
        }
        catch(Exception e)
        {
            logger.error(e, "解析scene出错");
        }

        return null;
    }

}
