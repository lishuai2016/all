package com.ls.lishuai.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/1 17:12
 */


/**
 获取当前天的前n天: getTodayPreNDay（int n）
 取得月第一天：getFirstDateOfMonth(Date date)
 取得月最后一天：getLastDateOfMonth(Date date)
 获取当前的季度：getSeason(Date date)
 当前季度都包含哪些月份：Date[] getSeasonDate(Date date)  月初1号
 当前月有多少天：getDayOfMonth(Date date)
 当前月已经过了多少天：getPassDayOfMonth(Date date)
 当前月还剩多少天：getRemainDayOfMonth(Date date)
 季度的第一天：getFirstDateOfSeason(Date date)
 季度的最后一天：etLastDateOfSeason(Date date)
 季度一共多少天：getDayOfSeason(Date date)
 季度已经过了多少天：getPassDayOfSeason(Date date)
 季度还剩下多少天：getRemainDayOfMonth(Date date)

 */

public class TestCalendar {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM = "yyyy_MM";
    public static final String YYYYMM = "yyyyMM";


    public static void main(String[] args) throws Exception{
        System.out.println(formatDateToStr(getTodayPreNDay(0)));
        System.out.println(formatDateToStr(getTodayPreNDay(1)));
        System.out.println(formatDateToStr(getTodayPreNDay(31)));
        System.out.println(getMonthInt(new Date()));
        System.out.println(getYearInt(new Date()));
        System.out.println(formatDateToStr(getFirstDateOfMonth(new Date())));//月初
        System.out.println(formatDateToStr(getLastDateOfMonth(new Date())));//月末

    }

    /**
     * 取得季度第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfSeason(Date date) {
        return getFirstDateOfMonth(getSeasonDate(date)[0]);
    }

    /**
     * 取得季度最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfSeason(Date date) {
        return getLastDateOfMonth(getSeasonDate(date)[2]);
    }

    /**
     * 取得季度天数
     *
     * @param date
     * @return
     */
    public static int getDayOfSeason(Date date) {
        int day = 0;
        Date[] seasonDates = getSeasonDate(date);
        for (Date date2 : seasonDates) {
            day += getDayOfMonth(date2);
        }
        return day;
    }

    /**
     * 取得季度剩余天数
     *
     * @param date
     * @return
     */
    public static int getRemainDayOfSeason(Date date) {
        return getDayOfSeason(date) - getPassDayOfSeason(date);
    }

    /**
     * 取得季度已过天数
     *
     * @param date
     * @return
     */
    public static int getPassDayOfSeason(Date date) {
        int day = 0;

        Date[] seasonDates = getSeasonDate(date);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);

        if (month == Calendar.JANUARY || month == Calendar.APRIL
                || month == Calendar.JULY || month == Calendar.OCTOBER) {// 季度第一个月
            day = getPassDayOfMonth(seasonDates[0]);
        } else if (month == Calendar.FEBRUARY || month == Calendar.MAY
                || month == Calendar.AUGUST || month == Calendar.NOVEMBER) {// 季度第二个月
            day = getDayOfMonth(seasonDates[0])
                    + getPassDayOfMonth(seasonDates[1]);
        } else if (month == Calendar.MARCH || month == Calendar.JUNE
                || month == Calendar.SEPTEMBER || month == Calendar.DECEMBER) {// 季度第三个月
            day = getDayOfMonth(seasonDates[0]) + getDayOfMonth(seasonDates[1])
                    + getPassDayOfMonth(seasonDates[2]);
        }
        return day;
    }

    /**
     * 取得月的剩余天数
     *
     * @param date
     * @return
     */
    public static int getRemainDayOfMonth(Date date) {
        int dayOfMonth = getDayOfMonth(date);
        int day = getPassDayOfMonth(date);
        return dayOfMonth - day;
    }

    /**
     * 取得月已经过的天数
     *
     * @param date
     * @return
     */
    public static int getPassDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得月天数
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得季度月
     *
     * @param date
     * @return
     */
    public  static Date[] getSeasonDate(Date date) {
        Date[] season = new Date[3];
        Calendar c = Calendar.getInstance();
        int nSeason = getSeason(date);
        if (nSeason == 1) {// 第一季度
            c.set(Calendar.MONTH, Calendar.JANUARY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.FEBRUARY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MARCH);
            season[2] = c.getTime();
        } else if (nSeason == 2) {// 第二季度
            c.set(Calendar.MONTH, Calendar.APRIL);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MAY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.JUNE);
            season[2] = c.getTime();
        } else if (nSeason == 3) {// 第三季度
            c.set(Calendar.MONTH, Calendar.JULY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.AUGUST);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);
            season[2] = c.getTime();
        } else if (nSeason == 4) {// 第四季度
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.NOVEMBER);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.DECEMBER);
            season[2] = c.getTime();
        }
        return season;
    }

    /**
     * 获取当前季度
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date
     * @return
     */
    public static int getSeason(Date date) {

        int season = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     * 取得月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得月最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 获取指定日期所属的月份
     * @param date
     * @return
     */
    public static int getMonthInt(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取指定日期所属的年
     * @param date
     * @return
     */
    public static int getYearInt(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取当前天的前n天
     * @param n
     * @return
     */
    public static Date getTodayPreNDay(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -n);
        return calendar.getTime();
    }


    /**
     * 通过Calendar获取今天的日期
     * @return Date
     */
    public static Date getTodayFromCalendar(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 通过pattern设定日期的格式
     * @return
     */
    public static String getTodayStr() { return getTodayStr(YYYYMMDD); }
    public static String getTodayStr(String pattern) {
        Date NowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(NowDate);
    }

    /**
     * 字符串转date
     * 根据字符串格式的日期和期望要的格式生成date日期
     * @param strDate
     * @return
     */
    public static Date parseStrToDate(String strDate) {
        return parseStrToDate(strDate, null);
    }
    public static Date parseStrToDate(String strDate, String pattern) {
        Date date = null;
        try {
            if (pattern == null) {
                pattern = YYYYMMDD;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            date = format.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     *  date转字符串
     * @param date
     * @return
     */
    public static String formatDateToStr(Date date) {
        return formatDateToStr(date, null);
    }
    public static String formatDateToStr(Date date, String pattern) {
        String strDate = null;
        try {
            if (pattern == null) {
                pattern = YYYYMMDD;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            strDate = format.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }


    public static void calendar() {
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.YEAR, 1);  //一年后的今天为基准（add具有累加效果）
        //calendar.add(Calendar.MONTH, 1);// 同理换成下个月的今天（add具有累加效果）
        calendar.set(Calendar.YEAR, 2000);  //直接覆盖年这个字段，其他的去默认值
        System.out.println("现在是" + calendar.get(Calendar.YEAR) + "年");
        calendar.set(2018, 7, 1);//直接覆盖年月日这个字段，其他的去默认值
        int year = calendar.get(Calendar.YEAR); // 获取年
        int month = calendar.get(Calendar.MONTH) + 1;// 获取月，这里需要需要月份的范围为0~11，因此获取月份的时候需要+1才是当前月份值
        int day = calendar.get(Calendar.DAY_OF_MONTH); // 获取日
        int hour = calendar.get(Calendar.HOUR); // 获取时
        // int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24小时表示
        int minute = calendar.get(Calendar.MINUTE);// 获取分
        int second = calendar.get(Calendar.SECOND);// 获取秒
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);// 星期，英语国家星期从星期日开始计算（从星期天开始算的,比如，今天是星期三，返回4）
        System.out.println("现在是" + year + "年" + month + "月" + day + "日" + hour
                + "时" + minute + "分" + second + "秒" + "星期" + weekday);
    }
}
