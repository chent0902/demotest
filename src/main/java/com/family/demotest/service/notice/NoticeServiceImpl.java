package com.family.demotest.service.notice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.dao.notice.NoticeDao;
import com.family.demotest.entity.NoticeModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
@Service("notice.service")
public class NoticeServiceImpl
    extends ServiceSupport<NoticeModel>
    implements NoticeService
{

    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private Logger logger;
    @Autowired
    private Validator validator;

    @Override
    protected QueryDao<NoticeModel> getQueryDao()
    {
        return noticeDao;
    }

    @Override
    protected SaveDao<NoticeModel> getSaveDao()
    {
        return noticeDao;
    }

    @Override
    protected DeleteDao<NoticeModel> getDeleteDao()
    {
        return noticeDao;
    }

    @Override
    public ResultCode list(int page, int pageSize)
    {
        ResultCode result = new ResultCode();
        try
        {
            PageList<NoticeModel> list = noticeDao.list(page, pageSize);
            result.setData(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载公告列表出错");
            logger.error(e, "加载公告列表出错");
        }

        return result;
    }

    @Override
    public ResultCode saveOrUpdate(String noticeId, String title, String content)
    {
        ResultCode result = new ResultCode();
        try
        {
            NoticeModel notice = null;
            if(validator.isBlank(noticeId))
            {
                notice = new NoticeModel();
                notice.setCreateTime(new Date());
                notice.setOnline(1);
            }
            else
            {
                notice = this.findById(noticeId);
                if(notice==null)
                {
                    result.setCode(-1);
                    result.setInfo("参数错误");
                    return result;
                }
            }
            notice.setTitle(title);
            notice.setContent(content);
            this.save(notice);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setData("保存公告信息出错");
            logger.error(e, "保存公告信息出错");
        }
        return result;
    }
}
