package com.family.demotest.service.lottery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.family.base01.dao.DeleteDao;
import com.family.base01.dao.QueryDao;
import com.family.base01.dao.SaveDao;
import com.family.base01.service.ServiceSupport;
import com.family.base01.util.Converter;
import com.family.base01.util.Logger;
import com.family.base01.util.PageList;
import com.family.base01.util.Validator;
import com.family.demotest.common.biz.IntegerRedisBiz;
import com.family.demotest.common.biz.LotterySummaryRedisBiz;
import com.family.demotest.dao.lottery.LotteryOrderDao;
import com.family.demotest.entity.LotteryAwardsModel;
import com.family.demotest.entity.LotteryModel;
import com.family.demotest.entity.LotteryOrderModel;
import com.family.demotest.entity.UserModel;
import com.family.demotest.service.queue.templateMsg.TemplateMsgQueueService;
import com.family.demotest.service.user.UserService;
import com.family.demotest.vo.total.LotterySummaryVo;
import com.family.demotest.web.util.DateUtils;
import com.family.demotest.web.util.ResultCode;

/**
 * 
 * @author lim
 *
 */
@Service("lottery.order.service")
public class LotteryOrderServiceImpl
    extends
    ServiceSupport<LotteryOrderModel>
    implements
    LotteryOrderService
{

    @Autowired
    private LotteryOrderDao lotteryOrderDao;
    @Autowired
    private Logger logger;
    @Autowired
    private LotterySummaryRedisBiz summaryRedisBiz;
    @Autowired
    private Validator validator;

    @Autowired
    private LotteryService lotteryService;

    @Autowired
    private LotteryAwardsService lotteryAwardsService;

    @Autowired
    private UserService userService;
    @Autowired
    private Converter converter;

    @Autowired
    private TemplateMsgQueueService templateMsgQueueService;

    @Override
    protected QueryDao<LotteryOrderModel> getQueryDao()
    {
        return lotteryOrderDao;
    }

    @Override
    protected SaveDao<LotteryOrderModel> getSaveDao()
    {
        return lotteryOrderDao;
    }

    @Override
    protected DeleteDao<LotteryOrderModel> getDeleteDao()
    {
        return lotteryOrderDao;
    }

    @Override
    public ResultCode userList(String channelNo, String userId, int type, int page, int pageSize)
    {
        ResultCode result = new ResultCode();

        try
        {
            StringBuffer where = new StringBuffer(
                    " SELECT t.c_id,t.c_lottery_id,t.c_merchant_id,t.c_user_id,t.c_awards_id,t.c_grade,t.c_usecode,t.c_qrcode,t.c_is_check,t.c_check_time,t.c_create_time,t1.c_name,t2.c_close_time,t3.c_name FROM t_lottery_order t LEFT JOIN t_merchant t1 ON t.c_merchant_id=t1.c_id LEFT JOIN t_lottery t2 ON t.c_lottery_id=t2.c_id LEFT JOIN t_lottery_awards t3 ON t.c_awards_id = t3.c_id WHERE t.c_channel_no=? AND t.c_user_id=? AND t.c_property=0 ");
            List<Object> args = new ArrayList<Object>();
            args.add(channelNo);
            args.add(userId);
            // type 0-未使用 1-已使用 2-已过期
            switch(type)
            {
            case 0:
                where.append(" AND t2.c_close_time >= ? AND t.c_is_check = 0 ");
                args.add(new Date());
                break;
            case 1:
                where.append(" AND t.c_is_check = 1 ");
                break;
            case 2:
                where.append(" AND t2.c_close_time < ? AND t.c_is_check = 0 ");
                args.add(new Date());
                break;
            default:
                break;
            }
            where.append(" ORDER BY t.c_create_time DESC LIMIT ?,? ");
            int limitNum = (page-1)*pageSize;// sql limit参数
            args.add(limitNum);
            args.add(pageSize);

            List<LotteryOrderModel> list = lotteryOrderDao.userList(where.toString(), args.toArray());

            result.setData(list);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载用户优惠券列表出错");
            logger.error(e, "加载用户优惠券列表出错");
        }

        return result;
    }

    @Override
    public ResultCode pwdCheck(String orderId, String pwd, String userId, String nickName)
    {
        ResultCode result = new ResultCode();
        try
        {
            LotteryOrderModel order = this.findById(orderId);
            if(order==null)
            {
                result.setCode(-1);
                result.setInfo("订单不存在");
                return result;
            }
            if(order.getMerchant()==null)
            {
                result.setCode(-1);
                result.setInfo("订单信息错误");
                return result;
            }
            if(order.getIsCheck()==1)
            {
                result.setCode(-1);
                result.setInfo("已使用");
                return result;
            }
            LotteryModel lottery = lotteryService.findById(order.getLotteryId());
            if(lottery==null)
            {
                result.setCode(-1);
                result.setInfo("活动信息错误");
                return result;
            }
            if(lottery.getCloseTime().before(new Date()))
            {
                result.setCode(-1);
                result.setInfo("已过期");
                return result;
            }
            if(!order.getMerchant().getPassword().equals(pwd))
            {
                result.setCode(-1);
                result.setInfo("核销密码错误");
                return result;
            }
            order.setIsCheck(1);
            order.setCheckTime(new Date());
            order.setAdminId(userId);
            order.setAdminName(validator.isBlank(nickName)?"":nickName);
            this.save(order);

            LotterySummaryVo summary = summaryRedisBiz.get(order.getLotteryId());
            if(summary==null)
            {
                summary = new LotterySummaryVo();
                summary.setUseNum(1);
            }
            else
            {
                summary.setUseNum(summary.getUseNum()+1);
            }
            summaryRedisBiz.put(order.getLotteryId(), summary, 90, TimeUnit.DAYS);

            // 发送模版消息
            this.sendMsg(order);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("密码核销出错");
            logger.error(e, "密码核销出错");
        }
        return result;
    }

    @Override
    public ResultCode findByUsecode(String merchantId, String usecode)
    {
        ResultCode result = new ResultCode();

        try
        {
            LotteryOrderModel order = lotteryOrderDao.findByUsecode(merchantId, usecode);
            if(order==null)
            {
                result.setCode(-1);
                result.setInfo("优惠码无效");
                return result;
            }
            result.setData(order);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取订单信息出错");
            logger.error(e, "获取订单信息出错");
        }

        return result;
    }

    @Override
    public ResultCode check(String orderId, String userId, String userName)
    {
        ResultCode result = new ResultCode();
        try
        {
            LotteryOrderModel order = this.findById(orderId);
            if(order==null||order.getProperty()==1)
            {
                result.setCode(-1);
                result.setInfo("参数错误");
                return result;
            }
            if(order.getIsCheck()==1)
            {
                result.setCode(-1);
                result.setInfo("已使用");
                return result;
            }
            LotteryModel lottery = lotteryService.findById(order.getLotteryId());
            if(lottery==null)
            {
                result.setCode(-1);
                result.setInfo("活动信息错误");
                return result;
            }
            if(lottery.getCloseTime().before(new Date()))
            {
                result.setCode(-1);
                result.setInfo("已过期");
                return result;
            }
            order.setCheckTime(new Date());
            order.setIsCheck(1);
            order.setAdminId(userId);
            order.setAdminName(validator.isBlank(userName)?"":userName);

            this.save(order);
            LotterySummaryVo summary = summaryRedisBiz.get(order.getLotteryId());
            if(summary==null)
            {
                summary = new LotterySummaryVo();
                summary.setUseNum(1);
            }
            else
            {
                summary.setUseNum(summary.getUseNum()+1);
            }
            summaryRedisBiz.put(order.getLotteryId(), summary, 90, TimeUnit.DAYS);

            // 发送模版消息
            this.sendMsg(order);

        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("优惠券核销出错");
            logger.error(e, "优惠券核销出错");
        }

        return result;
    }

    /**
     * 核销成功发送模版消息
     * 
     * @param channelNo
     * @param openId
     * @param name
     * @param merchantName
     */
    private void sendMsg(LotteryOrderModel order)
    {
        UserModel user = userService.findById(order.getUserId());
        if(user!=null)
        {
            templateMsgQueueService.sendMsg3(order.getChannelNo(), user.getOpenid(), order.getAwards().getName(), order.getMerchant().getName());
        }

    }

    @Override
    public ResultCode checkList(String merchantId, String lotteryId, String startTime, String endTime, int page, int pageSize)
    {
        ResultCode result = new ResultCode();

        StringBuffer where = new StringBuffer(" o.merchantId = ? AND o.isCheck = 1 ");
        List<Object> args = new ArrayList<Object>();
        args.add(merchantId);
        if(!validator.isBlank(lotteryId)&&!"0".equals(lotteryId))
        {
            where.append(" AND o.lotteryId = ? ");
            args.add(lotteryId);
        }
        if(!validator.isBlank(startTime)&&!validator.isBlank(endTime))
        {
            Date startDate = converter.beginDate(converter.toDate(startTime), true, true, true);
            Date endDate = converter.endDate(converter.toDate(endTime), true, true, true);
            where.append(" AND o.checkTime >= ? AND o.checkTime <= ? ");
            args.add(startDate);
            args.add(endDate);
        }

        try
        {
            PageList<LotteryOrderModel> pageList = lotteryOrderDao.checkList(where.toString(), args.toArray(), page, pageSize);
            if(pageList!=null)
            {
                result.setData(pageList.getList());
                result.setOther(pageList.getPageInfo().getTotalRecord()+"");
            }
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("加载核销列表出错");
            logger.error(e, "加载核销列表出错");
        }

        return result;
    }

    @Override
    public int waitUse(String channelNo, String userId)
    {
        return lotteryOrderDao.waitUse(channelNo, userId);
    }

    @Override
    public ResultCode adminUserList(String channelNo, String userId, int page, int pageSize)
    {
        ResultCode result = new ResultCode();

        try
        {
            PageList<LotteryOrderModel> pageList = lotteryOrderDao.adminUserList(channelNo, userId, page, pageSize);
            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取后台用户优惠券列表出错");
            logger.error(e, "获取后台用户优惠券列表出错");
        }
        return result;
    }

    @Override
    public ResultCode list(String channelNo, String lotteryId, String search, int status, int type, int page, int pageSize)
    {
        ResultCode result = new ResultCode();

        // SELECT
        // t.c_id,t.c_grade,t.c_create_time,t.c_check_time,t1.c_nick_name,t1.c_tel
        // FROM t_lottery_order t LEFT JOIN t_wx_user t1 ON t.c_user_id=t1.c_id

        StringBuffer sql = new StringBuffer(
                " SELECT t.c_id,t.c_grade,t.c_create_time,t.c_check_time,t1.c_nick_name,t1.c_tel,t.c_is_check FROM t_lottery_order t LEFT JOIN t_wx_user t1 ON t.c_user_id=t1.c_id WHERE t.c_channel_no = ? AND t.c_lottery_id = ? ");
        StringBuffer count = new StringBuffer(
                "SELECT COUNT(*) FROM t_lottery_order t LEFT JOIN t_wx_user t1 ON t.c_user_id=t1.c_id WHERE t.c_channel_no = ? AND t.c_lottery_id = ? ");
        List<Object> args = new ArrayList<Object>();
        args.add(channelNo);
        args.add(lotteryId);
        // status 0-领取 1-核销
        if(status==1)
        {
            sql.append(" AND t.c_is_check = 1 ");
            count.append(" AND t.c_is_check = 1 ");
        }
        // type -1-全部 0-特等奖 1-1等奖 2 3 4
        if(type!=-1)
        {
            sql.append(" AND t.c_grade = ? ");
            count.append(" AND t.c_grade = ? ");
            args.add(type);
        }
        if(!validator.isBlank(search))
        {
            sql.append(" AND ( t1.c_nick_name LIKE ? OR t1.c_tel LIKE ? ) ");
            count.append(" AND ( t1.c_nick_name LIKE ? OR t1.c_tel LIKE ? ) ");
            args.add("%"+search+"%");
            args.add("%"+search+"%");
        }
        if(status==1)
        {
            sql.append(" ORDER BY t.c_check_time DESC ");
        }
        else
        {
            sql.append(" ORDER BY t.c_create_time DESC ");
        }
        sql.append(" LIMIT ?,? ");
        try
        {
            PageList<LotteryOrderModel> pageList = lotteryOrderDao.list(sql, count, args, page, pageSize);
            result.setData(pageList);
        }
        catch(Exception e)
        {
            result.setCode(-1);
            result.setInfo("获取后台用户优惠券列表出错");
            logger.error(e, "获取后台用户优惠券列表出错");
        }

        return result;
    }

    private static final String RECEIVE_WHERE = " o.channelNo = ? AND o.createTime between ? AND ? ";
    private static final String USE_WHERE = " o.channelNo = ? AND o.isCheck = 1 AND o.checkTime between ? AND ? ";

    @Override
    public JSONObject getReceiveJson(String channelNo)
    {
        JSONObject receiveJson = new JSONObject();
        Date yesDate = DateUtils.addDay(new Date(), -1);
        Date todayStart = converter.beginDate(new Date(), true, true, true);
        Date todayEnd = converter.endDate(new Date(), true, true, true);
        Date yestStart = converter.beginDate(yesDate, true, true, true);
        Date yestEnd = converter.endDate(yesDate, true, true, true);
        // 今日领取
        int todayReceive = lotteryOrderDao.count(RECEIVE_WHERE, new Object[]{channelNo, todayStart, todayEnd});
        receiveJson.put("todayReceive", todayReceive);
        // 昨日领取
        int yesterdayReceive = lotteryOrderDao.count(RECEIVE_WHERE, new Object[]{channelNo, yestStart, yestEnd});
        receiveJson.put("yesterdayReceive", yesterdayReceive);
        // 今日使用
        int todayUse = lotteryOrderDao.count(USE_WHERE, new Object[]{channelNo, todayStart, todayEnd});
        receiveJson.put("todayUse", todayUse);
        // 昨日使用
        int yesterdayUse = lotteryOrderDao.count(USE_WHERE, new Object[]{channelNo, yestStart, yestEnd});
        receiveJson.put("yesterdayUse", yesterdayUse);

        return receiveJson;
    }

    @Override
    public void startExpireMsgTask()
    {
        try
        {
            List<LotteryModel> list = lotteryService.getExpireList();
            if(list!=null&&list.size()>0)
            {
                for(LotteryModel lottery : list)
                {
                    List<LotteryOrderModel> orderList = lotteryOrderDao.getListByLotteryId(lottery.getChannelNo(), lottery.getId());
                    if(orderList.size()>0)
                    {
                        for(LotteryOrderModel order : orderList)
                        {
                            // 发送到期提醒模板消息
                            templateMsgQueueService.sendMsg4(lottery.getChannelNo(), order.getOpenId(), lottery.getTitle(), lottery.getMerchantId(),
                                lottery.getCloseTime());
                        }
                    }
                }

            }
        }
        catch(Exception e)
        {
            logger.error(e, "每天处理活动快过期提醒出错");
        }
        finally
        {
            lotteryOrderDao.close();
        }

    }

    @Override
    public List<LotteryOrderModel> getNewList(String channelNo, String lotteryId)
    {

        List<LotteryOrderModel> list = lotteryOrderDao.getNewList(channelNo, lotteryId);
        // if(list.size()>0)
        // {
        // for(int i = 0; i<2; i++)
        // {
        // LotteryOrderModel model = new LotteryOrderModel();
        // model.setId("id");
        // model.setGrade(1);// 一等奖
        // model.setNickName(NicknameUtils.getChineseName());
        // list.add(i, model);
        // }
        // }
        return list;
    }

    @Autowired
    private IntegerRedisBiz integerRedisBiz;

    @Override
    public ResultCode delete(String id)
    {

        ResultCode result = new ResultCode();
        LotteryOrderModel order = lotteryOrderDao.findById(id);
        if(order==null)
        {
            result.setCode(-1);
            result.setInfo("该优惠券不存在");
            return result;
        }
        order.setProperty(1);

        if(order.getIsCheck()==0)
        {

            LotteryAwardsModel awards = lotteryAwardsService.findById(order.getAwardsId());
            if(awards!=null)
            {
                // num为-1时，库存不限量
                if(awards.getNum()>0)
                {
                    // 还未核销，还原库存数量
                    String key = lotteryService.getLotteryAwardsKey(order.getAwardsId());
                    Integer stockNum = integerRedisBiz.get(key);
                    if(stockNum!=null)
                    {
                        stockNum = stockNum+1;
                        integerRedisBiz.put(key, stockNum, 90, TimeUnit.DAYS);
                    }
                }

            }

        }

        lotteryOrderDao.save(order);
        return result;
    }

    @Override
    public Workbook newExport(String channelNo, String lotteryId, String search, String status, String type)
    {
        try
        {
            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("new sheet");
            Row rowHead = sheet.createRow((short)0);
            rowHead.createCell(0).setCellValue("序号");
            rowHead.createCell(1).setCellValue("领取时间");
            rowHead.createCell(2).setCellValue("用户昵称");
            rowHead.createCell(3).setCellValue("手机号");
            rowHead.createCell(4).setCellValue("奖项");
            rowHead.createCell(5).setCellValue("核销时间");

            StringBuffer sql = new StringBuffer(
                    " SELECT t.c_id,t.c_grade,t.c_create_time,t.c_check_time,t1.c_nick_name,t1.c_tel,t.c_is_check FROM t_lottery_order t LEFT JOIN t_wx_user t1 ON t.c_user_id=t1.c_id WHERE t.c_channel_no = ? AND t.c_lottery_id = ? ");
            StringBuffer count = new StringBuffer(
                    "SELECT COUNT(*) FROM t_lottery_order t LEFT JOIN t_wx_user t1 ON t.c_user_id=t1.c_id WHERE t.c_channel_no = ? AND t.c_lottery_id = ? ");
            List<Object> args = new ArrayList<Object>();
            args.add(channelNo);
            args.add(lotteryId);
            // status 0-领取 1-核销
            if(status.equals("1"))
            {
                sql.append(" AND t.c_is_check = 1 ");
                count.append(" AND t.c_is_check = 1 ");
            }
            // type -1-全部 0-特等奖 1-1等奖 2 3 4
            if(!type.equals("-1"))
            {
                sql.append(" AND t.c_grade = ? ");
                count.append(" AND t.c_grade = ? ");
                args.add(type);
            }
            if(!validator.isBlank(search))
            {
                sql.append(" AND ( t1.c_nick_name LIKE ? OR t1.c_tel LIKE ? ) ");
                count.append(" AND ( t1.c_nick_name LIKE ? OR t1.c_tel LIKE ? ) ");
                args.add("%"+search+"%");
                args.add("%"+search+"%");
            }
            if(status.equals("1"))
            {
                sql.append(" ORDER BY t.c_check_time DESC ");
            }
            else
            {
                sql.append(" ORDER BY t.c_create_time DESC ");
            }
            sql.append(" LIMIT ?,? ");

            PageList<LotteryOrderModel> pageList = lotteryOrderDao.list(sql, count, args, 1, Integer.MAX_VALUE);
            int rowIndex = 1;
            if(pageList!=null&&pageList.getList().size()>0)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                for(LotteryOrderModel item : pageList.getList())
                {
                    Row row = sheet.createRow(rowIndex);
                    row.createCell(0).setCellValue(rowIndex);
                    row.createCell(1).setCellValue(sdf.format(item.getCreateTime()));
                    row.createCell(2).setCellValue(validator.isBlank(item.getNickName())?"-":item.getNickName());
                    row.createCell(3).setCellValue(validator.isBlank(item.getTel())?"-":item.getTel());
                    switch(item.getGrade())
                    {
                    case 0:
                        row.createCell(4).setCellValue("特等奖");
                        break;
                    case 1:
                        row.createCell(4).setCellValue("一等奖");
                        break;
                    case 2:
                        row.createCell(4).setCellValue("二等奖");
                        break;
                    case 3:
                        row.createCell(4).setCellValue("三等奖");
                        break;
                    case 4:
                        row.createCell(4).setCellValue("四等奖");
                        break;

                    default:
                        break;
                    }
                    row.createCell(5).setCellValue(item.getCheckTime()==null?"-":sdf.format(item.getCheckTime()));
                    rowIndex++;
                }
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.setColumnWidth(3, 3000);
                sheet.setColumnWidth(4, 2000);
                sheet.setColumnWidth(5, 3000);
                sheet.setColumnWidth(6, 2000);

            }
            return wb;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
