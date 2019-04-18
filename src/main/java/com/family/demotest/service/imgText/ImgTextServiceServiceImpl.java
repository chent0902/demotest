package com.family.demotest.service.imgText;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.demotest.common.biz.ImgTextSummaryRedisBiz;
import com.family.demotest.dao.imgText.ImgTextDao;
import com.family.demotest.entity.ImgTextModel;
import com.family.demotest.vo.total.ImgTextSummaryVo;

/**
 * 
 * @author Chen
 *
 */
@Service("img.text.service")
public class ImgTextServiceServiceImpl
    extends ServiceSupport<ImgTextModel>
    implements ImgTextService
{

    @Autowired
    private ImgTextDao imgTextDao;
    @Autowired
    private Logger logger;
    @Autowired
    private ImgTextSummaryRedisBiz summaryRedisBiz;

    @Override
    protected QueryDao<ImgTextModel> getQueryDao()
    {

        return imgTextDao;
    }

    @Override
    protected SaveDao<ImgTextModel> getSaveDao()
    {

        return imgTextDao;
    }

    @Override
    protected DeleteDao<ImgTextModel> getDeleteDao()
    {

        return imgTextDao;
    }

    @Override
    public PageList<ImgTextModel> pageList(String channelNo, int pageSize, int page)
    {

        StringBuffer where = new StringBuffer(" o.channelNo = ? AND  o.property = 0 ");
        List<Object> args = new ArrayList<Object>();
        args.add(channelNo);
        PageList<ImgTextModel> pageList = imgTextDao.pageList(where.toString(), args.toArray(), page, pageSize);
        if(pageList.getList()!=null&&pageList.getList().size()>0)
        {
            for(ImgTextModel model : pageList.getList())
            {
                ImgTextSummaryVo imgTextSummaryVo = summaryRedisBiz.get(model.getId());
                if(imgTextSummaryVo!=null)
                {
                    model.setVisitNum(imgTextSummaryVo.getVisitNum());
                    model.setLike(imgTextSummaryVo.getLike());
                    model.setMessage(imgTextSummaryVo.getMessage());
                }
            }

        }
        return pageList;

    }

}
