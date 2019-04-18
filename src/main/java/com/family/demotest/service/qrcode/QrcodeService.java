package com.family.demotest.service.qrcode;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.base01.util.PageList;
import com.family.demotest.entity.QrcodeModel;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author wujf
 *
 */
public interface QrcodeService
    extends
    SaveService<QrcodeModel>,
    DeleteService<QrcodeModel>,
    QueryService<QrcodeModel>
{

    /**
     * 获取列表
     * 
     * @param channelNo
     * @param page
     * @param pageSize
     * @return
     */
    public PageList<QrcodeModel> getPageList(String channelNo, int page, int pageSize);

    /**
     * 
     * @param model
     * @return
     */
    public ResultCode saveQrcode(QrcodeModel model);

    /**
     * 设置扫码数量
     * 
     * @param id
     * @param num
     */
    public void putScanNum(String id, int num);

    /**
     * 获取扫码数量
     * 
     * @param id
     * @return
     */
    public Integer getScanNum(String id);

}
