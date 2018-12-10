package com.xzh.personalproject.commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

    private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");

    private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");

    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 获取YYYY格式
     *
     * @return
     */
    public static String getYear() {
        return sdfYear.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD格式
     *
     * @return
     */
    public static String getDay() {
        return sdfDay.format(new Date());
    }

    /**
     * 获取YYYYMMDD格式
     *
     * @return
     */
    public static String getDays() {
        return sdfDays.format(new Date());
    }

    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return
     */
    public static String getTime() {
        return sdfTime.format(new Date());
    }

    public static String getTimeAllNumber() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    /**
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @Title: compareDate
     * @Description: 日期比较，如果s>=e 返回true 否则返回false
     * @author luguosui
     */
    public static boolean compareDate(String s, String e) {
        if (fomatDate(s) == null || fomatDate(e) == null) {
            return false;
        }
        return fomatDate(s).getTime() >= fomatDate(e).getTime();
    }

    /**
     * @param s
     * @param e
     * @return boolean
     * @throws
     * @Title: compareDate
     * @Description: 日期比较，如果s>e 返回true 否则返回false
     * @author luguosui
     */
    public static boolean compareDates(String s, String e) {
        if (fomatDate(s) == null || fomatDate(e) == null) {
            return false;
        }
        return fomatDate(s).getTime() > fomatDate(e).getTime();
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date fomatDate(String date) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long stringDateToUnixDate(String date) {
        return fomatDate(date).getTime();
    }

    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fmt.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    public static int getDiffYear(String startTime, String endTime) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long aa = 0;
            int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24)) / 365);
            return years;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return 0;
        }
    }

    /**
     * <li>功能描述：时间相减得到天数
     *
     * @param beginDateStr
     * @param endDateStr
     * @return long
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr, String endDateStr) {
        long day;
        Date beginDate = null;
        Date endDate = null;

        try {
            beginDate = sdfDay.parse(beginDateStr);
            endDate = sdfDay.parse(endDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        //System.out.println("相隔的天数="+day);

        return day;
    }

    public static long getDaySub(Long beginDateLong, Long endDateLong) {
        long day = (endDateLong - beginDateLong) / (24 * 60 * 60);
        return day;
    }


    /**
     * 得到n天之后的日期
     *
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);

        return dateStr;
    }

    /**
     * 得到n天之后是周几
     *
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);

        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }

    /**
     * 得到days天前的日期
     */
    public static String getBeforeDayDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        return sdfDay.format(calendar.getTime());
    }

    public static void main(String[] args) {
        System.out.println(getBeforeDayDate(1));
        System.out.println(getAfterDayWeek("3"));
    }

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    public static String formatDate(Date date, String pattern) {
        if (date == null) throw new IllegalArgumentException("date is null");
        if (pattern == null) throw new IllegalArgumentException("pattern is null");

        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
        formatter.setTimeZone(GMT);
        return formatter.format(date);
    }

    static public long datetimeToUnixTime(String time) {
        LocalDateTime localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    static public long dateToUnixTime(String date) {
        LocalDateTime localDateTime = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    static public String unixTimeToDate(long unixStamp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixStamp), ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(dtf);
    }

    static public String unixTimeToDateWithPattern(long unixStamp, String pattern) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixStamp), ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dtf);
    }

    static public String unixTimeToDateTime(long unixStamp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixStamp), ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dtf);
    }

    public static String timeShow(LocalDateTime localDateTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-n");
        return localDateTime.format(dtf);
    }

    public static String yearMonthShow(LocalDateTime localDateTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        return localDateTime.format(dtf);
    }

    public static String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return sdfDay.format(calendar.getTime());
    }

    public static String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        return sdfTime.format(calendar.getTime());
    }

    public static long getCurrentDateTimeUnix() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getCurrentDayStr() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
    }
}