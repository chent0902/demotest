package com.family.demotest.dao.qrcode;

import org.springframework.stereotype.Repository;

import com.family.base01.dao.DaoSupport;
import com.family.base01.util.PageList;
import com.family.demotest.entity.QrcodeModel;

/**
 * 
 * @author wujf
 *
 */
@Repository("qr.code.dao")
public class QrcodeDaoImpl
    extends
    DaoSupport<QrcodeModel>
    implements
    QrcodeDao
{

    private static final String LIST_BY_CHANNEL_NO = " o.channelNo=? and "+UN_DELETE;

    private static final String ORDER_BY = "o.createTime desc ";

    @Override
    protected Class<QrcodeModel> getModelClass()
    {
        return QrcodeModel.class;
    }

    @Override
    public PageList<QrcodeModel> getPageList(String channelNo, int page, int pageSize)
    {

        PageList<QrcodeModel> pageList = list(newQueryBuilder().where(LIST_BY_CHANNEL_NO).orderBy(ORDER_BY), new Object[]{channelNo}, pageSize, page);

        int totalPage = pageList.getPageInfo().getTotalPage();

        if(page>totalPage)
        {
            return null;
        }

        return pageList;

    }

}
