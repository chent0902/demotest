package com.family.demotest.service.commentService;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.demotest.entity.CommentModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author Chen
 *
 */
public interface CommentService
    extends SaveService<CommentModel>, DeleteService<CommentModel>, QueryService<CommentModel>
{
    /**
     * 获取评价列表
     * 
     * @param channelNo
     * @param imgTextId
     * @param pageSize
     * @param page
     * @return
     */
    public ResultCode pageList(String channelNo, String imgTextId, int pageSize, int page);

    /**
     * 保存图文评价
     * 
     * @param channelNo
     * @param imgTextId
     * @param content
     * @return
     */
    public ResultCode saveOrUpdate(String channelNo, String imgTextId, String content);

    /**
     * 回复
     * 
     * @param messageId
     * @param reply
     * @return
     */
    public ResultCode reply(String messageId, String reply);

    /**
     * 图文留言信息
     * 
     * @param imgTextId
     * @param page
     * @param pageSize
     * @return
     */
    public ResultCode imgTextMessage(String imgTextId, int page, int pageSize);

    /**
     * 我的留言
     * 
     * @param channelNo
     * @param imgTextId
     * @param id
     * @return
     */
    public ResultCode myMessage(String channelNo, String imgTextId, String id);

    /**
     * 留言
     * 
     * @param channelNo
     * @param lotteryId
     * @param id
     * @param content
     * @return
     */
    public ResultCode save(String channelNo, String imgTextId, String id, String content);

}
