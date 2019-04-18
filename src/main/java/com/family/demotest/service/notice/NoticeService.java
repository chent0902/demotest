package com.family.demotest.service.notice;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.NoticeModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
public interface NoticeService
    extends SaveService<NoticeModel>, DeleteService<NoticeModel>, QueryService<NoticeModel>
{
    /**
     * 公告列表
     * 
     * @param page
     * @param pageSize
     * @return
     */
    public ResultCode list(int page, int pageSize);

    /**
     * 保存公告信息
     * 
     * @param title
     * @param content
     * @param content2
     * @return
     */
    public ResultCode saveOrUpdate(String noticeId, String title, String content);

}
