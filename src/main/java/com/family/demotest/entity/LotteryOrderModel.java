package com.family.demotest.entity;

import java.util.Date;

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

@Component("lottery.order.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "lottery.order")
@Table(name = "t_lottery_order")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LotteryOrderModel
    extends ModelSupport
{

    private static final long serialVersionUID = 5068610099008378635L;

    private String channelNo;// 渠道编号
    private String lotteryId;// 抽奖活动id
    private String merchantId;// 商户ID
    private String userId;// 用户ID
    private String awardsId;// 奖项ID
    private int grade;// 奖项等级 0-特等奖 1-1等奖 2 3 4
    private String tel;// 手机号
    private String username;// 用户名
    private String usecode;// 核销码
    private String qrcode;// 核销二维码
    private int isCheck;// 是否已核销 0-否 1-是
    private Date checkTime;// 核销时间
    private String adminId;// 核销人员ID
    private String adminName;// 核销人员昵称
    private Date closeTime;// 过期时间
    private Date createTime;// 创建时间
    private MerchantModel merchant;
    private LotteryAwardsModel awards;
    private String openId;// 微信openid
    private String nickName;// 用户昵称
    private String merchantName;// 商户名
    private String awardsName;// 奖项名
    private int template;// 抽奖样式 0-转盘 1-九宫格 2-翻牌子

    @Column(name = "c_channel_no")
    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    @Column(name = "c_lottery_id")
    public String getLotteryId()
    {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId)
    {
        this.lotteryId = lotteryId;
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

    @Column(name = "c_user_id")
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    @Column(name = "c_awards_id")
    public String getAwardsId()
    {
        return awardsId;
    }

    public void setAwardsId(String awardsId)
    {
        this.awardsId = awardsId;
    }

    @Column(name = "c_grade")
    public int getGrade()
    {
        return grade;
    }

    public void setGrade(int grade)
    {
        this.grade = grade;
    }

    @Column(name = "c_tel")
    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    @Column(name = "c_username")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @Column(name = "c_usecode")
    public String getUsecode()
    {
        return usecode;
    }

    public void setUsecode(String usecode)
    {
        this.usecode = usecode;
    }

    @Column(name = "c_qrcode")
    public String getQrcode()
    {
        return qrcode;
    }

    public void setQrcode(String qrcode)
    {
        this.qrcode = qrcode;
    }

    @Column(name = "c_is_check")
    public int getIsCheck()
    {
        return isCheck;
    }

    public void setIsCheck(int isCheck)
    {
        this.isCheck = isCheck;
    }

    @Column(name = "c_check_time")
    public Date getCheckTime()
    {
        return checkTime;
    }

    public void setCheckTime(Date checkTime)
    {
        this.checkTime = checkTime;
    }

    @Column(name = "c_admin_id")
    public String getAdminId()
    {
        return adminId;
    }

    public void setAdminId(String adminId)
    {
        this.adminId = adminId;
    }

    @Column(name = "c_admin_name")
    public String getAdminName()
    {
        return adminName;
    }

    public void setAdminName(String adminName)
    {
        this.adminName = adminName;
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

    @Column(name = "c_create_time")
    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
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

    @ManyToOne
    @JoinColumn(name = "c_awards_id", updatable = false, insertable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    public LotteryAwardsModel getAwards()
    {
        return awards;
    }

    public void setAwards(LotteryAwardsModel awards)
    {
        this.awards = awards;
    }

    @Transient
    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    @Transient
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Transient
    public String getMerchantName()
    {
        return merchantName;
    }

    public void setMerchantName(String merchantName)
    {
        this.merchantName = merchantName;
    }

    @Transient
    public String getAwardsName()
    {
        return awardsName;
    }

    public void setAwardsName(String awardsName)
    {
        this.awardsName = awardsName;
    }

    @Transient
    public int getTemplate()
    {
        return template;
    }

    public void setTemplate(int template)
    {
        this.template = template;
    }

}
