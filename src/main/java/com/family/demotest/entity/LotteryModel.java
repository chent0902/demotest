package com.family.demotest.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.family.base01.model.ModelSupport;

@Component("lottery.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "lottery")
@Table(name = "t_lottery")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LotteryModel
    extends
    ModelSupport
{

    private static final long serialVersionUID = 5889821006792238695L;

    private String channelNo;// 渠道编号
    private String merchantId;// 商家ID
    private String title;// 活动标题
    private String img;// 商品图片
    private String posterImg;// 海报图片
    private Date startTime;// 开始时间
    private Date endTime;// 结束时间
    private Date closeTime;// 消费截止时间
    private String price;// 市场价
    private int surplusOpen;// 虚拟剩余数开关
    private int virtualSurplus;// 虚拟剩余数

    private int virtualAttention;// 虚拟关注量
    private int virtualReceive;// 虚拟领取数
    private int virtualLike;// 虚拟点赞数
    private int messageOpen;// 留言开关 0-关闭 1-开启
    private int online;// 上下架 0-下架 1-上架
    private int sort;// 排序
    private int helpNum;// 每人帮次数，-1-不限次数
    private int template;// 抽奖样式 0-转盘 1-九宫格 2-翻牌子
    private Date createTime;// 创建时间

    private int openTel;// 开启手机验证0-否，1-是

    private String details;// 详情

    private MerchantModel merchant;//

    // 缓存
    private int attention;// 关注量
    private int join;// 参与数
    private int receive;// 领取数
    private int like;// 点赞数
    private int message;// 留言数
    private int useNum;// 使用数
    private int timesNum;// 抽奖次数

    private String lowestPrice;// 最低价

    private List<LotteryMessageModel> messageList;// 留言List

    private String serviceName;// 公众号名称
    private String qrCode;// 公众号二维码

    private String userId;
    private int participated;// 是否参与过了 0-未参与过 1-已参与过

    @Column(name = "c_channel_no")
    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    @Column(name = "c_merchant_id")
    public String getMerchantId()
    {
        return merchantId;
    }

    public void setMerchantId(String merchantId)
    {
        this.merchantId = merchantId;
    }

    @Column(name = "c_title")
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @Column(name = "c_img")
    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    @Column(name = "c_poster_img")
    public String getPosterImg()
    {
        return posterImg;
    }

    public void setPosterImg(String posterImg)
    {
        this.posterImg = posterImg;
    }

    @Column(name = "c_start_time")
    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    @Column(name = "c_end_time")
    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    @Column(name = "c_close_time")
    public Date getCloseTime()
    {
        return closeTime;
    }

    public void setCloseTime(Date closeTime)
    {
        this.closeTime = closeTime;
    }

    @Column(name = "c_price")
    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    @Column(name = "c_surplus_open")
    public int getSurplusOpen()
    {
        return surplusOpen;
    }

    public void setSurplusOpen(int surplusOpen)
    {
        this.surplusOpen = surplusOpen;
    }

    @Column(name = "c_virtual_surplus")
    public int getVirtualSurplus()
    {
        return virtualSurplus;
    }

    public void setVirtualSurplus(int virtualSurplus)
    {
        this.virtualSurplus = virtualSurplus;
    }

    @Column(name = "c_virtual_attention")
    public int getVirtualAttention()
    {
        return virtualAttention;
    }

    public void setVirtualAttention(int virtualAttention)
    {
        this.virtualAttention = virtualAttention;
    }

    @Column(name = "c_virtual_receive")
    public int getVirtualReceive()
    {
        return virtualReceive;
    }

    public void setVirtualReceive(int virtualReceive)
    {
        this.virtualReceive = virtualReceive;
    }

    @Column(name = "c_virtual_like")
    public int getVirtualLike()
    {
        return virtualLike;
    }

    public void setVirtualLike(int virtualLike)
    {
        this.virtualLike = virtualLike;
    }

    @Column(name = "c_message_open")
    public int getMessageOpen()
    {
        return messageOpen;
    }

    public void setMessageOpen(int messageOpen)
    {
        this.messageOpen = messageOpen;
    }

    @Column(name = "c_online")
    public int getOnline()
    {
        return online;
    }

    public void setOnline(int online)
    {
        this.online = online;
    }

    @Column(name = "c_sort")
    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    @Column(name = "c_help_num")
    public int getHelpNum()
    {
        return helpNum;
    }

    public void setHelpNum(int helpNum)
    {
        this.helpNum = helpNum;
    }

    @Column(name = "c_template")
    public int getTemplate()
    {
        return template;
    }

    public void setTemplate(int template)
    {
        this.template = template;
    }

    @Column(name = "c_create_time")
    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    @Column(name = "c_open_tel")
    public int getOpenTel()
    {
        return openTel;
    }

    public void setOpenTel(int openTel)
    {
        this.openTel = openTel;
    }

    @Transient
    public String getDetails()
    {
        return details;
    }

    public void setDetails(String details)
    {
        this.details = details;
    }

    @ManyToOne
    @JoinColumn(name = "c_merchant_id", updatable = false, insertable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public MerchantModel getMerchant()
    {
        return merchant;
    }

    public void setMerchant(MerchantModel merchant)
    {
        this.merchant = merchant;
    }

    @Transient
    public int getAttention()
    {
        return attention;
    }

    public void setAttention(int attention)
    {
        this.attention = attention;
    }

    @Transient
    public int getJoin()
    {
        return join;
    }

    public void setJoin(int join)
    {
        this.join = join;
    }

    @Transient
    public int getReceive()
    {
        return receive;
    }

    public void setReceive(int receive)
    {
        this.receive = receive;
    }

    @Transient
    public int getLike()
    {
        return like;
    }

    public void setLike(int like)
    {
        this.like = like;
    }

    @Transient
    public int getMessage()
    {
        return message;
    }

    public void setMessage(int message)
    {
        this.message = message;
    }

    @Transient
    public int getUseNum()
    {
        return useNum;
    }

    public void setUseNum(int useNum)
    {
        this.useNum = useNum;
    }

    @Transient
    public int getTimesNum()
    {
        return timesNum;
    }

    public void setTimesNum(int timesNum)
    {
        this.timesNum = timesNum;
    }

    @Transient
    public String getLowestPrice()
    {
        return lowestPrice;
    }

    public void setLowestPrice(String lowestPrice)
    {
        this.lowestPrice = lowestPrice;
    }

    @Transient
    public List<LotteryMessageModel> getMessageList()
    {
        return messageList;
    }

    public void setMessageList(List<LotteryMessageModel> messageList)
    {
        this.messageList = messageList;
    }

    @Transient
    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    @Transient
    public String getQrCode()
    {
        return qrCode;
    }

    public void setQrCode(String qrCode)
    {
        this.qrCode = qrCode;
    }

    @Transient
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    @Transient
    public int getParticipated()
    {
        return participated;
    }

    public void setParticipated(int participated)
    {
        this.participated = participated;
    }

}
