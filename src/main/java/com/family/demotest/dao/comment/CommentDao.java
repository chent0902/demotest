package com.family.demotest.dao.comment;

import java.util.List;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.CommentModel;

/**
 * 
 * @author Chen
 *
 */
public interface CommentDao
    extends QueryDao<CommentModel>, SaveDao<CommentModel>, DeleteDao<CommentModel>
{
    /**
     * 获取图文评价列表
     * 
     * @param string
     * @param imgTextId
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<CommentModel> pageList(String string, String imgTextId, int page, int pageSize);

    /**
     * 精选留言
     * 
     * @param imgTextId
     * @param page
     * @param pageSize
     * @return
     */
    public List<CommentModel> choicenessList(String imgTextId, int page, int pageSize);

    /**
     * 我的留言
     * 
     * @param channelNo
     * @param imgTextId
     * @param userId
     * @return
     */
    public List<CommentModel> myMessage(String channelNo, String imgTextId, String userId);

}
