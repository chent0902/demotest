package com.family.demotest.dao.imgText;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.ImgTextModel;

/**
 * 
 * @author Chen
 *
 */
@Repository("img.text.dao")
public class ImgTextDaoImpl
    extends DaoSupport<ImgTextModel>
    implements ImgTextDao
{

    private static final String FIND_BY_CHANNEL_NO = " o.channelNo=? and "+UN_DELETE;
    private static final String ORDER_BY = " o.sort DESC, o.createTime DESC ";

    @Override
    protected Class<ImgTextModel> getModelClass()
    {
        return ImgTextModel.class;
    }

    @Override
    public PageList<ImgTextModel> pageList(String where, Object[] args, int page, int pageSize)
    {
        PageList<ImgTextModel> pageList = list(newQueryBuilder().where(where).orderBy(ORDER_BY), args, pageSize, page);
        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return null;
        }

        return pageList;
    }

}
