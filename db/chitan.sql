DROP TABLE IF EXISTS t_base_config;
CREATE TABLE t_base_config (
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_channel_no varchar(20)  NOT NULL COMMENT '商家编号',
  c_appid varchar(50) DEFAULT NULL COMMENT '授权方appid',
  c_nick_name varchar(50) DEFAULT NULL COMMENT '授权方昵称',
  c_head_img varchar(255) DEFAULT NULL COMMENT '授权方头像',
  c_service_type_info int(11) DEFAULT 0 COMMENT '授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号',
  c_verify_type_info int(11) DEFAULT -1 COMMENT '授权方认证类型，-1代表未认证，0代表微信认证',
  c_user_name varchar(50) DEFAULT NULL COMMENT '授权方公众号的原始ID',
  c_qrcode_url varchar(255) DEFAULT NULL COMMENT '二维码图片的URL',
  c_qrcode varchar(255) DEFAULT NULL COMMENT '公众号二维码',
  c_access_token varchar(255) DEFAULT NULL COMMENT '授权token',
  c_refresh_token varchar(255) DEFAULT NULL COMMENT '刷新token',
  c_over_time varchar(20) DEFAULT NULL COMMENT ' token过期时间',
  c_active_point int(11) DEFAULT 20 COMMENT '活动点',
  c_forced_attention int(11) DEFAULT 0 COMMENT '是否需要强制关注后参与活动  0-不需要 1-需要',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性',
  PRIMARY KEY (c_id),
  KEY(c_channel_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '基本配置表' ;



DROP TABLE IF EXISTS  t_wx_user;
CREATE TABLE t_wx_user (
  c_id char(36) NOT NULL COMMENT '主键',
  c_channel_no varchar(20) NOT NULL COMMENT '商家编号',
  c_openid varchar(50) NOT NULL COMMENT '用户openid',
  c_nick_name varchar(50) DEFAULT NULL COMMENT '用户昵称',
  c_sex int(11) DEFAULT 1 COMMENT '性别0-未知，1-男,2-女',
  c_city varchar(50) DEFAULT NULL COMMENT '用户所在城市',
  c_province varchar(50) DEFAULT NULL COMMENT '用户所在省份',
  c_country varchar(50) DEFAULT NULL COMMENT '用户所在国家',
  c_headimgurl varchar(200) DEFAULT NULL COMMENT '用户头像',
  c_subscribe_time varchar(20) DEFAULT NULL COMMENT '用户关注时间',
  c_unionid varchar(50) DEFAULT NULL COMMENT 'unionid',
  c_remark varchar(50) DEFAULT NULL COMMENT '公众号运营者对粉丝的备注',
  c_groupid varchar(50) DEFAULT NULL COMMENT '用户所在的分组ID',
  c_mp_name varchar(50) DEFAULT NULL COMMENT '对应的微信公众号',
  c_mp_appid varchar(50) DEFAULT NULL COMMENT '对应的appid',
  c_cm_appid varchar(50) DEFAULT NULL COMMENT '用户所在的分组ID',
  c_subscribe int(11) DEFAULT 0 COMMENT '用户是否订阅该公众号标识0-否，1-是',
  c_tel varchar(15) DEFAULT NULL COMMENT '手机号码',
  c_user_name varchar(50) DEFAULT NULL COMMENT '姓名',
  c_address varchar(200) DEFAULT NULL COMMENT '地址',
  c_follow int(11) DEFAULT 0 COMMENT '是否关注 0-关注，-1-取消关注',
  c_cancel_time datetime DEFAULT NULL COMMENT '取消关注时间',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性',
  PRIMARY KEY (c_id),
  KEY(c_channel_no),
  KEY(c_openid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户表';



DROP TABLE IF EXISTS t_merchant;
CREATE TABLE t_merchant (
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_channel_no CHAR(36)  NOT NULL COMMENT '所属渠道商',
  c_name varchar(50) DEFAULT NULL COMMENT '商家名称',
  c_logo varchar(255) DEFAULT NULL COMMENT '商家LOGO',
  c_address varchar(255) DEFAULT NULL COMMENT '商家地址',
  c_tel varchar(15) DEFAULT NULL COMMENT '商家电话',
  c_longitude varchar(30) DEFAULT NULL COMMENT '经度',
  c_latitude varchar(30) DEFAULT NULL COMMENT '纬度',
  c_service varchar(255) DEFAULT NULL COMMENT '服务',
  c_password varchar(50) DEFAULT '123456' COMMENT '商家核销密码',
  c_contacts varchar(10) DEFAULT NULL COMMENT '联系人',
  c_contacts_tel varchar(15) DEFAULT NULL COMMENT '联系人电话',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT '0' COMMENT '属性',
  PRIMARY KEY (c_id),
  KEY(c_channel_no),
  KEY(c_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS t_merchant_user;
CREATE TABLE t_merchant_user (
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_channel_no CHAR(36)  NOT NULL COMMENT '所属渠道商',
  c_merchant_id varchar(36) DEFAULT NULL COMMENT '商户ID',
  c_user_id varchar(36) DEFAULT NULL COMMENT '用户ID',
  c_role_code varchar(30) DEFAULT NULL COMMENT '角色类型 0-店员 1-店长',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT '0' COMMENT '属性',
  PRIMARY KEY (c_id),
  KEY(c_channel_no),
  KEY(c_merchant_id),
  KEY(c_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS t_lottery;
CREATE TABLE t_lottery (
    c_id CHAR(36) NOT NULL COMMENT '主键',
    c_channel_no CHAR(36)  NOT NULL COMMENT '所属渠道商',
    c_merchant_id varchar(36) DEFAULT NULL COMMENT '商户id',
    c_title varchar(50) DEFAULT NULL COMMENT '活动标题',
    c_img varchar(200) DEFAULT NULL COMMENT '商品图片',
    c_poster_img varchar(200) DEFAULT NULL COMMENT '海报图片',
    c_start_time datetime DEFAULT NULL COMMENT '开始时间',
    c_end_time datetime DEFAULT NULL COMMENT '结束时间',
    c_close_time datetime DEFAULT NULL COMMENT '消费截止时间',
    c_price varchar(9) DEFAULT '0' COMMENT '市场价',
    c_surplus_open int(11) DEFAULT 1 COMMENT '剩余数开关 0-关闭 1-开启',
    c_virtual_surplus int(11) DEFAULT 0 COMMENT '虚拟剩余数',
    c_virtual_attention int(11) DEFAULT 0 COMMENT '虚拟关注量',
    c_virtual_receive int(11) DEFAULT 0 COMMENT '虚拟领取数',
    c_virtual_like int(11) DEFAULT 0 COMMENT '虚拟点赞数',
    c_message_open int(11) DEFAULT 0 COMMENT '留言开关 0-关闭 1-开启',
    c_online int(11) DEFAULT 1 COMMENT '上下架 0-下架 1-上架',
    c_sort int(11) DEFAULT 0 COMMENT '排序',
    c_create_time datetime DEFAULT NULL COMMENT '创建时间',
    c_property int(11) DEFAULT 0 COMMENT '属性',
    PRIMARY KEY (c_id),
    KEY(c_channel_no),
    KEY(c_merchant_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '抽奖活动表';

DROP TABLE IF EXISTS t_lottery_details;
CREATE TABLE t_lottery_details (
    c_id CHAR(36) NOT NULL COMMENT '主键',
    c_channel_no CHAR(36)  NOT NULL COMMENT '所属渠道商',
    c_lottery_id varchar(36) DEFAULT NULL COMMENT '抽奖活动id',
    c_details longtext DEFAULT NULL COMMENT '抽奖详情',
    c_create_time datetime DEFAULT NULL COMMENT '创建时间',
    c_property int(11) DEFAULT 0 COMMENT '属性',
    PRIMARY KEY (c_id),
    KEY(c_channel_no),
    KEY(c_lottery_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '抽奖详情表';

DROP TABLE IF EXISTS t_lottery_message;
CREATE TABLE t_lottery_message (
    c_id CHAR(36) NOT NULL COMMENT '主键',
    c_channel_no CHAR(36)  NOT NULL COMMENT '所属渠道商',
    c_lottery_id varchar(36) DEFAULT NULL COMMENT '抽奖活动id',
    c_user_id varchar(36) DEFAULT NULL COMMENT '用户ID',
    c_content varchar(600) DEFAULT NULL COMMENT '留言内容',
    c_reply varchar(600) DEFAULT NULL COMMENT '回复',
    c_choiceness int(6) DEFAULT 0 COMMENT '精选 0-否 1-是',
    c_create_time datetime DEFAULT NULL COMMENT '创建时间',
    c_property int(11) DEFAULT 0 COMMENT '属性',
    PRIMARY KEY (c_id),
    KEY(c_channel_no),
    KEY(c_lottery_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '抽奖评论表';

DROP TABLE IF EXISTS t_lottery_awards;
CREATE TABLE t_lottery_awards (
    c_id CHAR(36) NOT NULL COMMENT '主键',
    c_channel_no CHAR(36)  NOT NULL COMMENT '所属渠道商',
    c_lottery_id varchar(36) DEFAULT NULL COMMENT '抽奖活动id',
    c_grade int(6) DEFAULT 0 COMMENT '奖项等级  0-特等奖  1-1等奖  2 3 4 ',
    c_name varchar(36) DEFAULT NULL COMMENT '优惠名称',
    c_price varchar(9) DEFAULT '0' COMMENT '价格',
    c_limit int(6) DEFAULT 0 COMMENT '是否限量 0-不限量 1-限量',
    c_num int(11) DEFAULT 0 COMMENT '限量多少份',
    c_lucky_num int(9) DEFAULT 1 COMMENT '每人中奖次数',
    c_create_time datetime DEFAULT NULL COMMENT '创建时间',
    c_property int(11) DEFAULT 0 COMMENT '属性',
    PRIMARY KEY (c_id),
    KEY(c_channel_no),
    KEY(c_lottery_id),
    KEY(c_grade)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '抽奖奖项表';


DROP TABLE IF EXISTS t_lottery_order;
CREATE TABLE t_lottery_order (
    c_id CHAR(36) NOT NULL COMMENT '主键',
    c_channel_no CHAR(36)  NOT NULL COMMENT '所属渠道商',
    c_lottery_id varchar(36) DEFAULT NULL COMMENT '抽奖活动id',
    c_merchant_id varchar(36) DEFAULT NULL COMMENT '商户ID',
    c_user_id varchar(36) DEFAULT NULL COMMENT '用户ID',
    c_awards_id varchar(36) DEFAULT NULL COMMENT '奖项ID',
    c_grade int(6) DEFAULT 0 COMMENT '奖项等级  0-特等奖  1-1等奖  2 3 4 ',
    c_tel varchar(16) DEFAULT NULL COMMENT '手机号',
    c_username varchar(50) DEFAULT NULL COMMENT '用户名',
    c_usecode varchar(36) DEFAULT NULL COMMENT '核销码',
    c_qrcode varchar(200) DEFAULT NULL COMMENT '核销二维码',
    c_is_check int(11) DEFAULT 0 COMMENT '是否已核销  0-否  1-是',
    c_check_time datetime DEFAULT NULL COMMENT '核销时间',
    c_admin_id varchar(36) DEFAULT NULL COMMENT '核销人员ID',
    c_admin_name varchar(36) DEFAULT NULL COMMENT '核销人员昵称',
    c_close_time datetime DEFAULT NULL COMMENT '过期时间',
    c_create_time datetime DEFAULT NULL COMMENT '创建时间',
    c_property int(11) DEFAULT 0 COMMENT '属性',
    PRIMARY KEY (c_id),
    KEY(c_channel_no),
    KEY(c_merchant_id),
    KEY(c_lottery_id),
    KEY(c_awards_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '抽奖订单表';



DROP TABLE IF EXISTS t_admin;
CREATE TABLE t_admin (
  c_id char(36) NOT NULL COMMENT '主键',
  c_channel_no char(36) NOT NULL COMMENT '渠道商',
  c_account varchar(20) NOT NULL COMMENT '管理员账号',
  c_passwd varchar(50) DEFAULT NULL COMMENT '管理员密码',
  c_status int(6) DEFAULT '1' COMMENT '是否禁止登陆0-否，1-是',
  c_update_time datetime DEFAULT NULL COMMENT '更新时间',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性', 
  PRIMARY KEY (c_id),
  KEY(c_account)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '管理员表';



ALTER TABLE t_base_config ADD COLUMN c_qrcode varchar(100) DEFAULT NULL COMMENT '公众号二维码' after c_qrcode_url;


DROP TABLE IF EXISTS t_admin_token;
CREATE TABLE t_admin_token (
  c_id char(36) NOT NULL COMMENT '主键',
  c_admin_id char(36) NOT NULL COMMENT '代理商ID',
  c_token varchar(36) NOT NULL COMMENT 'token',
  c_expired_time datetime DEFAULT NULL COMMENT '过期时间',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性', 
  PRIMARY KEY (c_id),
  KEY(c_admin_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '代理商登录token';



DROP TABLE IF EXISTS t_lottery_scan_help;
CREATE TABLE t_lottery_scan_help (
  c_id char(36) NOT NULL COMMENT '主键',
  c_channel_no varchar(20) NOT NULL COMMENT '渠道商',
  c_lottery_id char(36) NOT NULL COMMENT '活动id',
  c_user_id char(36) NOT NULL COMMENT '用户id',
  c_help_user_id char(36) NOT NULL COMMENT '帮扫用户id',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性', 
  PRIMARY KEY (c_id),
  KEY(c_channel_no),
  KEY(c_lottery_id),
  KEY(c_user_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '用户帮扫码记录表';


DROP TABLE IF EXISTS t_activity_package;
CREATE TABLE t_activity_package (
  c_id char(36) NOT NULL COMMENT '主键',
  c_name varchar(100) NOT NULL COMMENT '套餐名',
  c_num int(11) NOT NULL COMMENT '套餐点数',d
  c_price int(11) NOT NULL COMMENT '价格 单位分',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性', 
  PRIMARY KEY (c_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '活动点套餐表';


DROP TABLE IF EXISTS t_activity_package_order;
CREATE TABLE t_activity_package_order (
  c_id char(36) NOT NULL COMMENT '主键',
  c_channel_no varchar(36) NOT NULL COMMENT '站点配置表ID',
  c_activity_package_id varchar(36) DEFAULT NULL COMMENT '套餐id',
  c_nick_name varchar(36) DEFAULT NULL COMMENT '授权方名称',
  c_num int(11) DEFAULT 0 COMMENT '套餐点数',
  c_price int(11) DEFAULT 0 COMMENT '价格 单位分',
  c_status int(11) DEFAULT 0 COMMENT '支付状态  0-待支付 1-已支付',
  c_trans_id varchar(32) DEFAULT NULL COMMENT '支付回调单号',
  c_trade_no varchar(32) DEFAULT NULL COMMENT '商户订单号',
  c_prepay_id varchar(64) DEFAULT NULL COMMENT '预支付ID',
  c_pay_time varchar(20) DEFAULT NULL COMMENT '支付时间',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性', 
  PRIMARY KEY (c_id),
  KEY(c_channel_no),
  KEY(c_activity_package_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '活动点订单表';


ALTER TABLE t_activity_package add column c_start_time datetime DEFAULT NULL COMMENT '开始时间' after c_price;
ALTER TABLE t_activity_package add column c_end_time datetime DEFAULT NULL COMMENT '结束时间' after c_start_time;
ALTER TABLE t_activity_package add column c_online int(11) DEFAULT 1 COMMENT '上下架 0-下架 1-上架' after c_end_time;
ALTER TABLE t_admin ADD COLUMN c_manage int(6) DEFAULT 0 COMMENT '0-子账号 1-总帐号' after c_status;


DROP TABLE IF EXISTS t_wx_pay_order;
CREATE TABLE t_wx_pay_order (
  c_id char(36) NOT NULL COMMENT '主键',
  c_pay_no varchar(32) NOT NULL COMMENT '我方支付订单流水号',
  c_order_id char(36) DEFAULT NULL COMMENT '订单ID',
  c_subject varchar(256) DEFAULT NULL COMMENT '订单标题',
  c_transaction_id varchar(64) DEFAULT NULL COMMENT '微信支付订单号',
  c_openid varchar(64) DEFAULT NULL COMMENT '用户openid',
  c_trade_type varchar(16) DEFAULT NULL COMMENT '交易类型',
  c_bank_type varchar(16) DEFAULT NULL COMMENT '付款银行',
  c_settle_date varchar(20) DEFAULT NULL COMMENT '对账日期',
  c_total_fee int(11) DEFAULT '0' COMMENT '订单金额(分)',
  c_cash_fee int(11) DEFAULT '0' COMMENT '现金支付金额(分)',
  c_refund_fee int(11) DEFAULT '0' COMMENT '退款金额',
  c_end_time varchar(14) DEFAULT NULL COMMENT '交易支付完成时间',
  c_pay_status int(11) DEFAULT '0' COMMENT '支付状态0-未支付,1-支付成功,-2-已退款',
  c_pay_source int(11) DEFAULT '0' COMMENT '支付来源1-抢购',
  c_prepay_id varchar(64) DEFAULT NULL COMMENT '预支付交易会话标识',
  c_refund_no varchar(32) DEFAULT NULL COMMENT '退款流水号',
  c_createtime datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性',
  PRIMARY KEY (c_id),
  KEY(c_pay_no),
  KEY(c_pay_source)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '微信支付订单表';

 --2018-09-06
ALTER TABLE t_base_config ADD COLUMN c_account varchar(30) DEFAULT NULL COMMENT '帐号' after c_forced_attention;
ALTER TABLE t_base_config ADD COLUMN c_passwd varchar(36) DEFAULT NULL COMMENT '密码' after c_account;
ALTER TABLE t_base_config ADD COLUMN c_city_name varchar(30) DEFAULT NULL COMMENT '城市名称' after c_password;

 --2018-09-07
ALTER TABLE t_lottery_message ADD COLUMN c_nick_Name varchar(50) DEFAULT NULL COMMENT '用户昵称' after c_choiceness;
ALTER TABLE t_lottery_message ADD COLUMN c_headurl varchar(255) DEFAULT NULL COMMENT '用户头像' after c_nick_Name;



DROP TABLE IF EXISTS t_sms_order;
CREATE TABLE t_sms_order (
  c_id char(36) NOT NULL COMMENT '主键',
  c_channel_no varchar(36) NOT NULL COMMENT '商家编号',
  c_money int(11) DEFAULT 0 COMMENT '金额(分)',
  c_name varchar(64) DEFAULT NULL COMMENT '短信套餐',
  c_num int(11) DEFAULT 0 COMMENT '短信数',
  c_status int(11) DEFAULT 0 COMMENT '支付状态  0-待支付 1-已支付',
  c_trans_id varchar(32) DEFAULT NULL COMMENT '支付回调单号',
  c_trade_no varchar(32) DEFAULT NULL COMMENT '商户订单号',
  c_prepay_id varchar(64) DEFAULT NULL COMMENT '预支付ID',
  c_pay_time varchar(20) DEFAULT NULL COMMENT '支付时间',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT '0' COMMENT '属性',
  PRIMARY KEY (c_id),
  KEY c_channel_no (c_channel_no),
  KEY c_status (c_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信充值订单表';




 --2018-09-09
 
 ALTER TABLE t_base_config ADD COLUMN c_sms_num int(11) DEFAULT 0 COMMENT '短信数量' after c_city_name;
 
 ALTER TABLE t_lottery_awards ADD COLUMN c_rate int(11) DEFAULT 0 COMMENT '中奖概率0-100' after c_lucky_num;
 ALTER TABLE t_lottery_awards ADD COLUMN c_min_times int(11) DEFAULT 0 COMMENT '最少抽奖次数' after c_lucky_num;
 
--2018-09-11
ALTER TABLE t_base_config ADD COLUMN c_salesman varchar(20) DEFAULT NULL COMMENT '业务员' after c_sms_num;
ALTER TABLE t_activity_package_order MODIFY COLUMN c_pay_time datetime DEFAULT NULL COMMENT '支付时间';
ALTER TABLE t_sms_order MODIFY COLUMN c_pay_time datetime DEFAULT NULL COMMENT '支付时间';


--2018-09-13

ALTER TABLE t_lottery ADD COLUMN c_help_num int(11) DEFAULT -1 COMMENT '每人帮次数' after c_sort;
ALTER TABLE t_lottery ADD COLUMN c_template int(11) DEFAULT 0 COMMENT '模版 0-大转盘，1-九宫格，2-翻牌' after c_help_num;

 

--2018-09-13
DROP TABLE IF EXISTS t_message;
CREATE TABLE t_message (
  c_id char(36) NOT NULL COMMENT '主键',
  c_channel_no varchar(36) NOT NULL COMMENT '渠道商编号',
  c_news varchar(8000) DEFAULT NULL COMMENT '消息',
  c_send_num int(11) DEFAULT 0 COMMENT '发送人数',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性', 
  PRIMARY KEY (c_id),
  KEY(c_channel_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '群发消息表';

ALTER TABLE t_lottery_message ADD COLUMN c_top int(6) DEFAULT 0 COMMENT '是否顶置 1-顶置' after c_choiceness;

ALTER TABLE t_message ADD COLUMN c_type int(11) DEFAULT 0 COMMENT '群发类型 0-图文消息 1-模板消息' after c_channel_no;
 

DROP TABLE IF EXISTS t_home_ad;
CREATE TABLE t_home_ad
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_channel_no CHAR(10)  NOT NULL COMMENT '所属渠道商',
  c_type int(11) DEFAULT 1 COMMENT '图片类型 1-轮播',
  c_name varchar(20) DEFAULT NULL COMMENT '名称',
  c_img varchar(200) DEFAULT NULL COMMENT '图片',
  c_sort int(11) DEFAULT 0 COMMENT '显示顺序',
  c_link_url varchar(255) DEFAULT NULL COMMENT '跳转网址',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property INT DEFAULT 0 COMMENT '属性', 
  PRIMARY KEY(c_id),
  KEY(c_channel_no),
  KEY(c_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '首页轮播表';



ALTER TABLE t_base_config ADD COLUMN c_open_tel int(11) DEFAULT 0  COMMENT '开启手机验证0-否，1-是' after c_sms_num;


ALTER TABLE t_lottery ADD COLUMN c_open_tel int(11) DEFAULT 0  COMMENT '开启手机验证0-否，1-是' after c_help_num;



-- 2018-11-19
DROP TABLE IF EXISTS t_base_setting;
CREATE TABLE t_base_setting (
  c_id char(36) NOT NULL COMMENT '主键',
  c_channel_no varchar(20)  NOT NULL COMMENT '商家编号',
  c_share_title varchar(255) DEFAULT NULL COMMENT '分享标题',
  c_share_logo varchar(255) DEFAULT NULL COMMENT '分享logo',
  c_follow_reply varchar(5000) DEFAULT NULL COMMENT '公众号关注回复',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性',
  PRIMARY KEY (c_id),
  KEY(c_channel_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '基本设置表' ;






DROP TABLE IF EXISTS t_qrcode;
CREATE TABLE t_qrcode (
  c_id char(36) NOT NULL COMMENT '主键',
  c_channel_no varchar(20)  NOT NULL COMMENT '商家编号',
  c_type int(11) DEFAULT 0 COMMENT '类型:0-文字，1-图文，2-图片',
  c_content varchar(255) DEFAULT NULL COMMENT '文字内容',
  c_articles_content varchar(10000) DEFAULT NULL COMMENT '图文内容',
  c_remark varchar(500) DEFAULT NULL COMMENT '备注',
  c_qrcode_url varchar(100) DEFAULT NULL COMMENT '二维码url',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性',
  PRIMARY KEY (c_id),
  KEY(c_channel_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '带参数二维码表' ;



DROP TABLE IF EXISTS t_img_text;
CREATE TABLE t_img_text (
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_channel_no varchar(20)  NOT NULL COMMENT '商家编号',
  c_title varchar(255) DEFAULT NULL COMMENT '图文标题',
  c_img varchar(255) DEFAULT NULL COMMENT '图片',
  c_intro varchar(255) DEFAULT NULL COMMENT '图文简介',
  c_content varchar(5000) DEFAULT NULL COMMENT '图文内容',
  c_sort int(11) DEFAULT 0 COMMENT '顺序',
  c_vir_visit int(11) DEFAULT 0 COMMENT '虚拟访问数',
  c_vir_click int(11) DEFAULT 0 COMMENT '虚拟点赞数',
  c_message_switch int(11) DEFAULT 0 COMMENT '留言开关 0-关闭，1-开启',
  c_create_time datetime DEFAULT NULL COMMENT '创建时间',
  c_property int(11) DEFAULT 0 COMMENT '属性',
  PRIMARY KEY (c_id),
  KEY(c_channel_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '图文设置表';


DROP TABLE IF EXISTS t_comment;
CREATE TABLE t_comment (
    c_id CHAR(36) NOT NULL COMMENT '主键',
    c_channel_no CHAR(36)  NOT NULL COMMENT '所属渠道商',
    c_img_text_id varchar(36) DEFAULT NULL COMMENT '图文id',
    c_user_id varchar(36) DEFAULT NULL COMMENT '用户ID',
    c_content varchar(600) DEFAULT NULL COMMENT '留言内容',
    c_reply varchar(600) DEFAULT NULL COMMENT '回复',
    c_headurl varchar(255) DEFAULT NULL COMMENT '头像',
    c_nick_Name varchar(60) DEFAULT NULL COMMENT '昵称',
    c_choiceness int(6) DEFAULT 0 COMMENT '精选 0-否 1-是',
    c_create_time datetime DEFAULT NULL COMMENT '创建时间',
    c_property int(11) DEFAULT 0 COMMENT '属性',
    PRIMARY KEY (c_id),
    KEY(c_channel_no),
    KEY(c_img_text_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '图文评论表';


ALTER TABLE t_base_config ADD COLUMN c_open_imgtext int(11) DEFAULT 0  COMMENT '开启图文列表0-否，1-是' after c_open_tel;
ALTER TABLE t_base_config ADD COLUMN c_introduce varchar(1000) DEFAULT NULL  COMMENT '站点介绍' after c_open_imgtext;

 