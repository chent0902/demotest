package com.family.demotest.dao.message;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.MessageModel;

@Repository("message.dao")
public class MessageDaoImpl
    extends DaoSupport<MessageModel>
    implements MessageDao
{

    private static final String MESSAGE_RECORD = " o.channelNo = ? AND o.property = 0 ";

    private static final String ORDER_BY = " o.createTime DESC ";

    @Override
    protected Class<MessageModel> getModelClass()
    {
        return MessageModel.class;
    }

    @Override
    public PageList<MessageModel> messageRecord(String channelNo, int page, int pageSize)
    {
        PageList<MessageModel> pageList = list(newQueryBuilder().where(MESSAGE_RECORD).orderBy(ORDER_BY), new Object[]{channelNo}, pageSize, page);

        int totalPage = pageList.getPageInfo().getTotalPage();
        if(page>totalPage)
        {
            return null;
        }

        return pageList;
    }

}
