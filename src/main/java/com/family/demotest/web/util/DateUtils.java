package com.family.demotest.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author ding.w 2012-8-30下午09:36:06
 * @link prestigeding@126.com
 * @version 1.0
 * 
 */

public class DateUtils
{
    /**
     * 转换到分
     */
    public static final String T_MM_FORMATE = "yyyy-MM-dd hh:mm";
    /**
     * 转换到秒
     */
    public static final String T_SS_FORMATE = "yyyy-MM-dd hh:mm:ss";
    /**
     * 转换到小时
     */
    public static final String T_HH_FORMATE = "yyyy-MM-dd hh";
    /**
     * 转换到天
     */
    public static final String T_DD_FORMATE = "yyyy-MM-dd";

    /**
     * 一天的分钟分
     */
    public static final int DAY_MINUTES = 24*60;

    /**
     * 时间戳转格式化的日期
     * 
     * @param tt
     * @return
     */
    public static String parseTimetamp(Integer tt)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sd = sdf.format(new Date(tt*1000));
        return sd;
    }

    /**
     * 获取当前时间戳.1970-1-1 至今的秒数。单位：秒。
     * 
     * @return
     */
    public static int getCurrentTimestamp()
    {
        return (int)(System.currentTimeMillis()/1000);
    }

    /**
     * 将1970-1-1 0:0:0 算起的秒数转化为时间
     * 
     * @param second
     * @return
     */
    public static Date fromSecond(long second)
    {
        return new Date(second*1000);
    }

    /**
     * 将时间转化为时间戳。1970-1-1 至指定时间的秒数。
     * 
     * @param date
     * @return
     */
    public static int dateToTimestamp(Date date)
    {
        return (int)(date.getTime()/1000);
    }

    public static final Date getSystemDate()
    {
        return Calendar.getInstance().getTime();
    }

    /**
     * 格式化日期输出。如果传入data为null，则返回空字符串
     * 
     * @param date
     * @param format
     * @return
     */
    public static final String formate(Date date, String format)
    {
        if(date==null)
            return "";
        format = StringUtils.isBlank(format)?"yyyy-MM-dd":format;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 解析日期
     * 
     * @param dateStr
     * @param format
     * @return
     */
    public static final Date parseDate(String dateStr, String format)
    {
        if(StringUtils.isBlank(dateStr))
        {
            return null;
        }

        if(StringUtils.isBlank(format))
        {
            format = "yyyy-MM-dd";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try
        {
            return sdf.parse(dateStr);
        }
        catch(ParseException e)
        {
            // e.printStackTrace();
            return null;
        }

    }

    /**
     * 将java.util.Date 转换成 java.sql.Date
     * 
     * @param date
     * @return
     */
    public static java.sql.Date toSqlDate(java.util.Date date)
    {
        if(date==null)
            return null;
        return new java.sql.Date(date.getTime());
    }

    /**
     * 将字符串转换成数据集对象
     * 
     * @param dateStr
     *            目前只支持格式 yyyyMMddHHmmss
     * @return
     */
    public static java.sql.Date toSqlDate(String dateStr, String formate)
    {
        if(StringUtils.isBlank(dateStr))
            return null;
        if(StringUtils.isBlank(formate))
        {
            formate = "yyyyMMddHHmmss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try
        {
            java.util.Date jd = sdf.parse(dateStr);
            return new java.sql.Date(jd.getTime());
        }
        catch(ParseException e)
        {
            return null;
        }

    }

    /**
     * 获取当天时间 天 开始时间
     * 
     * @param d
     * @return
     */
    public static final Date getDayStart(Date d)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 获取当天时间 天 开始时间
     * 
     * @param d
     * @return
     */
    public static final Date getDayEnd(Date d)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    public static final Date addDay(Date d, int day)
    {
        return addHour(d, day*24);
    }

    public static final Date addHour(Date d, int hour)
    {
        if(d==null||hour==0)
            return d;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.HOUR, hour);
        return calendar.getTime();
    }

    /**
     * 增加或减少分钟
     * 
     * @param d
     * @param m
     * @return
     */
    public static final Date addMinute(Date d, int m)
    {
        if(d==null||m==0)
            return d;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.MINUTE, m);
        return calendar.getTime();
    }

    /**
     * 增加或减少秒
     * 
     * @param date
     * @param seconds
     * @return
     */
    public static final Date addSeconds(Date date, int seconds)
    {
        if(date==null||seconds==0)
            return date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * 判断日期字符串是否为合法的日期格式
     * 
     * @param str
     * @param format
     * @return
     */
    public static boolean isValidate(String str, String format)
    {
        if(StringUtils.isBlank(str)||StringUtils.isBlank(format))
            return false;

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.parse(str);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }

    }

    public static final Date getYearStart(Date d)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static final Date getYearEnd(Date d)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.MONTH, 12);
        c.set(Calendar.DATE, 31);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    /**
     * 获取月 的开始时间，结束时间
     * 
     * @param d
     * @return
     */
    public static final Date[] getMonthStartAndEndDates(Date d)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        Integer year = c.get(Calendar.YEAR);
        Integer month = c.get(Calendar.MONTH)+1;
        return new Date[]{getMonthStart(year, month), getMonthEnd(year, month)};
    }

    /**
     * 获取月 的开始时间，结束时间
     * 
     * @param d
     * @return
     */
    public static final Date[] getDayStartAndEndDates(Date d)
    {
        return new Date[]{getDayStart(d), getDayEnd(d)};
    }

    public static final Date getMonthStart(Integer year, Integer month)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 
     * @param year
     * @param month
     *            月，，10代表10月
     * @return
     */
    public static final Date getMonthEnd(Integer year, Integer month)
    {
        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);

        return c.getTime();
    }

    /**
     * 获取指定时间的年月
     * 
     * @param date
     * @return
     */
    public static final Integer[] getYearAndMonth(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return new Integer[]{c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1};
    }

    /**
     * 根据传入的格式，获取时间
     * 
     * @param format
     * @return
     */
    public static String getTimeByDateFormat(String format)
    {
        if(StringUtils.isNotEmpty(format))
        {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date()).toString();
        }
        return null;
    }

    /**
     * 时间比较的方法 如果paramsNew>paramsOld return true,否则都return false
     * 
     * @param paramsNew
     * @param paramsOld
     * @return
     */
    public static boolean compareDate(Date paramsNew, Date paramsOld)
    {
        try
        {

            if(paramsNew.getTime()>paramsOld.getTime())
            {
                return true;
            }
            else if(paramsNew.getTime()<paramsOld.getTime())
            {
                return false;
            }
            else
            {
                return false;
            }
        }
        catch(Exception exception)
        {
            return false;
        }
    }

    /**
     * 计算两个日期之间相差的天数
     * 
     * @param smdate
     *            较小的时间
     * @param bdate
     *            较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate)
        throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * data days 天以前的日期
     * 
     * @param date
     * @param days
     * @return
     */
    public static Date daysAgo(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long time = cal.getTimeInMillis();
        long dayAgo = time-(1000l*3600*24*days);
        cal.setTimeInMillis(dayAgo);
        return cal.getTime();
    }

    /**
     * 获取一段时间日期
     */
    public static List<Date> subDate(Date dBegin, Date dEnd)
    {

        List<Date> lDate = new ArrayList<>();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while(dEnd.after(calBegin.getTime()))
        {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }

        return lDate;

    }

    /**
     * 判断当前时间是否在指定的范围(小时,格式：HH:mm)
     * 
     * @param start
     * @param end
     * @param data
     *            比较日期
     * @param type
     *            0-当天 1-次日
     * @param delay
     *            延时(分)
     * @return
     */
    public static boolean within(String start, String end, Date data, int type, int delay)
    {
        if(start==null)
        {
            throw new RuntimeException("开始时间不能为空");
        }
        if(end==null)
        {
            throw new RuntimeException("结束时间不能为空");
        }
        String[] startArray = start.split(":");
        int startHours = Integer.parseInt(startArray[0])*60;
        int startMinutes = 0;
        if(startArray.length>1)
        {
            startMinutes = Integer.parseInt(startArray[1]);
        }
        int startCountMinutes = startHours+startMinutes;

        String[] endArray = end.split(":");
        int endHours = Integer.parseInt(endArray[0])*60;
        int endMinutes = 0;
        if(endArray.length>1)
        {
            endMinutes = Integer.parseInt(startArray[1]);
        }
        int endCountMinutes = endHours+endMinutes+delay;
        if(data==null)
        {
            data = new Date();
        }
        String[] dataArray = formate(data, "HH:mm").split(":");
        int hours = Integer.parseInt(dataArray[0])*60;
        int minutes = Integer.parseInt(dataArray[1]);
        int countMinutes = hours+minutes;
        if(type==0)
        {
            if(countMinutes>=startCountMinutes&&countMinutes<=endCountMinutes)
            {
                return true;
            }
        }
        if(type==1)
        {
            // 1.先判断当前时间是
            if(countMinutes<endCountMinutes)
            {
                return true;
            }
            if(countMinutes>startCountMinutes&&countMinutes>endCountMinutes)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 计算两个时间相差多少分钟
     * 
     * @param start
     *            开始时间 格式HH:mm
     * @param end
     *            结束时间HH:mm
     * @param type
     *            当日次日 0-当日，1-次日
     * @return
     */
    public static int countMinutes(String start, String end, int type)
    {
        if(start==null)
        {
            throw new RuntimeException("开始时间不能为空");
        }
        if(end==null)
        {
            throw new RuntimeException("结束时间不能为空");
        }
        String[] startArray = start.split(":");
        int startHours = Integer.parseInt(startArray[0])*60;
        int startMinutes = 0;
        if(startArray.length>1)
        {
            startMinutes = Integer.parseInt(startArray[1]);
        }
        int startCountMinutes = startHours+startMinutes;

        String[] endArray = end.split(":");
        int endHours = Integer.parseInt(endArray[0])*60;
        int endMinutes = 0;
        if(endArray.length>1)
        {
            endMinutes = Integer.parseInt(endArray[1]);
        }
        int endCountMinutes = endHours+endMinutes;
        if(type==0)
        {
            return endCountMinutes-startCountMinutes;
        }
        if(type==1)
        {
            return DAY_MINUTES-startCountMinutes+endCountMinutes;
        }
        return DAY_MINUTES;
    }

    /**
     * 获取小时
     * 
     * @param time
     * @return
     */
    public static int getHour(Date time)
    {

        Calendar cal = Calendar.getInstance();// 可以对每个时间域单独修改
        cal.setTime(time);

        // int year = cal.get(Calendar.YEAR);
        // int month = cal.get(Calendar.MONTH);
        // int date = cal.get(Calendar.DATE);
        // int hour = cal.get(Calendar.HOUR_OF_DAY);
        // int minute = cal.get(Calendar.MINUTE);
        // int second = cal.get(Calendar.SECOND);
        return cal.get(Calendar.HOUR_OF_DAY);

    }
}
