package com.family.demotest.service.config;

import java.util.List;

import com.family.base01.service.DeleteService;
import com.family.base01.service.QueryService;
import com.family.base01.service.SaveService;
import com.family.base01.util.PageList;
import com.family.demotest.entity.BaseConfigModel;
import com.family.demotest.vo.base.BaseConfigVo;
import com.family.demotest.vo.wx.QrsceneVo;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author wujf
 *
 */
public interface ConfigService
    extends
    SaveService<BaseConfigModel>,
    DeleteService<BaseConfigModel>,
    QueryService<BaseConfigModel>
{

    /**
     * 保存和更新站点信息
     * 
     * @param model
     */
    public ResultCode createOrUpdate(BaseConfigVo configVo);

    /**
     * 通过渠道号获取信息
     * 
     * @param channelNo
     * @return
     */
    public BaseConfigModel findByChannelNo(String channelNo);

    /**
     * 通过appid获取信息
     * 
     * @param authorizer_appid
     * @return
     */
    public BaseConfigModel findByAppid(String authorizer_appid);

    /**
     * 获取所以配置列表
     * 
     * @return
     */
    public List<BaseConfigModel> getListAll();

    /**
     * 生成带参二维码
     * 
     * @param channelNo
     * @param userId
     * @param lotteryId
     * @return
     */
    public ResultCode getQrCode(String channelNo, String userId, String lotteryId);

    /**
     * 设置带参二维码scene的内容
     * 
     * @param key
     * @param userId
     * @param lotteryId
     */
    public void setQrCodeScene(String key, String userId, String lotteryId);

    /**
     * 获取带参二维码scene的内容
     * 
     * @param key
     */
    public String getQrCodeScene(String key);

    /**
     * 获取带参二维码scene的内容
     * 
     * @param key
     */
    public QrsceneVo getQrSceneVo(String key);

    /**
     * 总后台获取站点列表
     * 
     * @param model
     * @param pageSize
     * @param page
     * @return
     */
    public PageList<BaseConfigModel> getPageList(BaseConfigVo model, int pageSize, int page);

}
