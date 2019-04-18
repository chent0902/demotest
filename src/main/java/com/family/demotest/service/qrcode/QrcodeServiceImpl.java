package com.family.demotest.service.qrcode;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.IntegerRedisBiz;
import com.family.demotest.common.enumtype.QrcodeSource;
import com.family.demotest.dao.qrcode.QrcodeDao;
import com.family.demotest.entity.QrcodeModel;
import com.family.demotest.service.config.ConfigService;
import com.family.demotest.vo.imgMsg.Article;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author wujf
 *
 */
@Service("qr.code.service")
public class QrcodeServiceImpl
    extends ServiceSupport<QrcodeModel>
    implements QrcodeService
{

    @Autowired
    private QrcodeDao qrcodeDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private Validator validator;
    @Autowired
    private Logger logger;

    @Autowired
    private IntegerRedisBiz integerRedisBiz;

    @Override
    protected QueryDao<QrcodeModel> getQueryDao()
    {

        return qrcodeDao;
    }

    @Override
    protected SaveDao<QrcodeModel> getSaveDao()
    {

        return qrcodeDao;
    }

    @Override
    protected DeleteDao<QrcodeModel> getDeleteDao()
    {

        return qrcodeDao;
    }

    @Override
    public PageList<QrcodeModel> getPageList(String channelNo, int page, int pageSize)
    {
        PageList<QrcodeModel> pageList = qrcodeDao.getPageList(channelNo, page, pageSize);
        if(pageList!=null&&pageList.getList()!=null)
        {
            for(QrcodeModel model : pageList.getList())
            {
                Integer scanNum = this.getScanNum(model.getId());
                // 设置扫码数
                model.setScanNum(scanNum==null?0:scanNum);
            }
        }
        return pageList;
    }

    @Override
    public ResultCode saveQrcode(QrcodeModel qrcode)
    {
        ResultCode result = new ResultCode();
        try
        {
            QrcodeModel model = validator.isBlank(qrcode.getId())?new QrcodeModel():findById(qrcode.getId());

            if(validator.isBlank(qrcode.getId()))
            {
                model.setChannelNo(qrcode.getChannelNo());
                model.setCreateTime(new Date());
            }

            model.setType(qrcode.getType());
            model.setContent(qrcode.getContent());
            model.setArticlesContent(qrcode.getArticlesContent());
            model.setRemark(qrcode.getRemark());

            if(qrcode.getType()==1)
            {
                List<Article> articleList = JSON.parseArray(qrcode.getArticlesContent(), Article.class);
                if(validator.isEmpty(articleList))
                {
                    result.setCode(-1);
                    result.setInfo("图文消息格式不正确");
                    return result;
                }
            }

            qrcodeDao.save(model);

            // 新增
            if(validator.isBlank(qrcode.getId()))
            {
                result = configService.getQrCode(qrcode.getChannelNo(), QrcodeSource.QRCODE.getCode(), model.getId());
                if(result.getCode()==0)
                {
                    JSONObject jsonObject = JSONObject.parseObject(result.getData().toString());
                    if(jsonObject!=null)
                    {
                        model.setQrcodeUrl(jsonObject.getString("qrCode"));
                    }

                }
                else
                {
                    // 数据回滚
                    qrcodeDao.rollback();
                }
            }

        }
        catch(Exception e)
        {
            qrcodeDao.rollback();
            logger.error(e, "保存二维码出错");
            result.setCode(-1);
            result.setInfo("保存二维码出错");
        }

        return result;
    }

    @Override
    public void putScanNum(String id, int num)
    {
        String key = getScanNumKey(id);
        Integer scanNum = this.getScanNum(key);
        if(scanNum==null)
        {
            scanNum = 0;
        }
        integerRedisBiz.put(key, scanNum+num, 30, TimeUnit.DAYS);
    }

    @Override
    public Integer getScanNum(String id)
    {
        String key = getScanNumKey(id);
        return integerRedisBiz.get(key);
    }

    /**
     * 生成缓存key
     * 
     * @param id
     * @return
     */
    private String getScanNumKey(String id)
    {
        return new StringBuffer("scan.num.key-").append(id).toString();
    }

}
