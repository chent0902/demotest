package com.family.demotest.dao.imgText;

import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.util.PageList;
import com.family.demotest.entity.ImgTextModel;

/**
 * 
 * @author Chen
 *
 */
public interface ImgTextDao
    extends QueryDao<ImgTextModel>, SaveDao<ImgTextModel>, DeleteDao<ImgTextModel>
{
    /**
     * 
     * @param string
     * @param array
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<ImgTextModel> pageList(String where, Object[] args, int page, int pageSize);

}
