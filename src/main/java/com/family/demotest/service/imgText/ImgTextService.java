package com.family.demotest.service.imgText;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.base01.util.PageList;
import com.family.demotest.entity.ImgTextModel;

/**
 * 
 * @author Chen
 *
 */
public interface ImgTextService
    extends SaveService<ImgTextModel>, DeleteService<ImgTextModel>, QueryService<ImgTextModel>
{

    public PageList<ImgTextModel> pageList(String channelNo, int pageSize, int page);

}
