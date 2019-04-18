package com.family.demotest.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.family.base01.model.ModelSupport;

/**
 * 
 * @author wujf
 *
 */
@Component("wx.user.model")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity(name = "wx.user")
@Table(name = "t_wx_user")
public class UserModel
    extends ModelSupport
{

    private static final long serialVersionUID = -5967778050040846924L;
    private String channelNo;// 所属商家编号
    private String openid; // 用户openid
    private String nickName; // 用户的昵称
    private int sex; // 用户的性别0-未知,1-男性,2-女性
    private String city; // 用户所在城市
    private String country; // 用户所在国家
    private String province; // 用户所在省份
    private String headimgurl; // 用户头像
    private String subscribeTime; // 用户关注时间(时间戳)
    private String unionid; // unionid
    private String remark; // 公众号运营者对粉丝的备注
    private String groupid; // 用户所在的分组ID
    private int subscribe; // 用户是否订阅该公众号标识0-否，1-是
    private String tel; // 手机号
    private String userName; // 姓名
    private String address; // 地址
    private int follow; // 是否关注 0-用户已授权，-1-取消关注,1-已关注
    private Date cancelTime; // 取消关注时间
    private Date createTime; // 创建时间

    private int waitUse;// 待使用优惠券数量
    private int isAdmin;// 是否是店铺管理员 0-不是 1-是

    @Column(name = " c_channel_no")
    public String getChannelNo()
    {
        return channelNo;
    }

    public void setChannelNo(String channelNo)
    {
        this.channelNo = channelNo;
    }

    @Column(name = " c_openid")
    public String getOpenid()
    {
        return openid;
    }

    public void setOpenid(String openid)
    {
        this.openid = openid;
    }

    @Column(name = "c_nick_name")
    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    @Column(name = "c_sex")
    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    @Column(name = "c_city")
    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    @Column(name = "c_country")
    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    @Column(name = "c_province")
    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    @Column(name = "c_headimgurl")
    public String getHeadimgurl()
    {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl)
    {
        this.headimgurl = headimgurl;
    }

    @Column(name = "c_subscribe_time")
    public String getSubscribeTime()
    {
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime)
    {
        this.subscribeTime = subscribeTime;
    }

    @Column(name = "c_unionid")
    public String getUnionid()
    {
        return unionid;
    }

    public void setUnionid(String unionid)
    {
        this.unionid = unionid;
    }

    @Column(name = "c_remark")
    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Column(name = "c_groupid")
    public String getGroupid()
    {
        return groupid;
    }

    public void setGroupid(String groupid)
    {
        this.groupid = groupid;
    }

    @Column(name = "c_subscribe")
    public int getSubscribe()
    {
        return subscribe;
    }

    public void setSubscribe(int subscribe)
    {
        this.subscribe = subscribe;
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

    @Column(name = "c_user_name")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Column(name = "c_address")
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    @Column(name = "c_follow")
    public int getFollow()
    {
        return follow;
    }

    public void setFollow(int follow)
    {
        this.follow = follow;
    }

    @Column(name = "c_cancel_time")
    public Date getCancelTime()
    {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime)
    {
        this.cancelTime = cancelTime;
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

    @Transient
    public int getWaitUse()
    {
        return waitUse;
    }

    public void setWaitUse(int waitUse)
    {
        this.waitUse = waitUse;
    }

    @Transient
    public int getIsAdmin()
    {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin)
    {
        this.isAdmin = isAdmin;
    }

}
