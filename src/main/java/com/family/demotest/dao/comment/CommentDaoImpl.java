package com.family.demotest.dao.comment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.CommentModel;

/**
 * 
 * @author Chen
 *
 */
@Repository("comment.dao")
public class CommentDaoImpl
    extends DaoSupport<CommentModel>
    implements CommentDao
{

    private static final String FIND_BY_IMG_TEXT_ID = " o.channelNo = ? AND o.imgTextId = ? AND o.property = 0  ";
    private static final String ORDER_BY = " o.createTime DESC ";
    private static final String CHOICENESS_LIST = " o.imgTextId = ? AND o.choiceness = 1 AND o.property = 0 ";
    private static final String MY_MESSAGE = " o.channelNo = ? AND o.imgTextId = ? AND o.userId = ? AND o.property = 0 ";

    @Override
    protected Class<CommentModel> getModelClass()
    {
        return CommentModel.class;
    }

    @Override
    public PageList<CommentModel> pageList(String channelNo, String imgTextId, int page, int pageSize)
    {
        PageList<CommentModel> pageList = list(newQueryBuilder().where(FIND_BY_IMG_TEXT_ID).orderBy(ORDER_BY), new Object[]{channelNo, imgTextId}, pageSize,
            page);

        int totalPage = pageList.getPageInfo().getTotalPage();

        if(page>totalPage)
        {
            return null;
        }

        return pageList;
    }

    @Override
    public List<CommentModel> choicenessList(String imgTextId, int page, int pageSize)
    {
        PageList<CommentModel> pageList = list(newQueryBuilder().where(CHOICENESS_LIST).orderBy(ORDER_BY), new Object[]{imgTextId}, pageSize, page);
        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return new ArrayList<CommentModel>();
        }
        return pageList.getList();
    }

    @Override
    public List<CommentModel> myMessage(String channelNo, String imgTextId, String userId)
    {
        PageList<CommentModel> pageList = list(newQueryBuilder().where(MY_MESSAGE).orderBy(ORDER_BY), new Object[]{channelNo, imgTextId, userId},
            Integer.MAX_VALUE, 1);

        return pageList.getList();
    }

}
