package com.cr.pn.Utils.TimeUtil.DateUtil;

import android.annotation.TargetApi;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zy on 2017/7/21.
 */

public class DateUtil {

    /**
     * 字符串转时间.
     * @param dateString
     * @param model
     * @return
     * @throws ParseException
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static Date StringDate2Date(String dateString, String model) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat(model);
        Date date=sdf.parse(dateString);
        return date;
    }

    /**
     * 字符串转时间.
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date StringDate2Date(String dateString) throws ParseException {
        return StringDate2Date(dateString,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Date转时间戳.
     * @param date
     * @return
     */
    public static long Date2timeStamp(Date date){
        return date.getTime();
    }

    /**
     * 时间戳转Date.
     * @param timeStamp
     * @return
     */
    public static Date timeStamp2Date(long timeStamp){
        return new Date(timeStamp);
    }

    /**
     * 当前时间戳转Date.
     * @return
     */
    public static Date timeStamp2Date(){
        return timeStamp2Date(timeStamp());
    }

    /**
     * Date转时间字符串.
     * @param model
     * @param date
     * @return
     */
    public static String Date2stringDate(String model,Date date){
        SimpleDateFormat sdr = new SimpleDateFormat(model);
        String times = sdr.format(date);
        return times;
    }

    /**
     * Date转时间字符串.
     * @param date
     * @return
     */
    public static String Date2stringDate(Date date){
        return Date2stringDate("yyyy-MM-dd HH:mm:ss",date);
    }

    /**
     * 获取当前时间戳.
     * @return
     */
    public static long timeStamp(){
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间字符串，自定义模式.
     * @param model
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static String getTimeString(String model){
        SimpleDateFormat sdr = new SimpleDateFormat(model);
        long lcc = timeStamp();
        String times = sdr.format(new Date(lcc));
        return times;
    }

    /**
     * 获取当前时间字符串.
     * @return
     */
    public static String getTimeString(){
        return getTimeString("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据时间戳获取星期.
     * @param timeStamp
     * @return
     * 1-周日
     * 2-周一
     * ...
     * 7-周六.
     */
    public static int getWeek(long timeStamp) {
        int mydate = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(Calendar.DAY_OF_WEEK);
        return mydate;
    }

    /**
     * 根据时间戳获取几号.
     */
    public static int getDayOfMouth(long timeStamp) {
        int mydate = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(Calendar.DAY_OF_MONTH);
        return mydate;
    }

    /**
     * 根据时间戳获取月份.
     */
    public static int getMonth(long timeStamp) {
        int mydate = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(Calendar.MONTH);
        return mydate;
    }

    /**
     * 根据时间戳获取年份.
     */
    public static int getYear(long timeStamp) {
        int mydate = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(Calendar.YEAR);
        return mydate;
    }

    /**
     * 根据时间戳获取想要的时间点(年月日十分秒等.).
     * 参考Calendar
     */
    public static int getTime(long timeStamp,int i) {
        int mydate = 0;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp));
        mydate = cd.get(i);
        return mydate;
    }

}
