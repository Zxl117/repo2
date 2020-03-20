package com.Z.project.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by macbook on 2017/5/21.
 */

public class TimeUtils {

    /**
     * 时间戳转为时间(年月日，时分秒)
     *
     * @param cc_time 时间戳
     * @return
     */
    public static String getStrTime(long cc_time) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(cc_time);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 时间转换为时间戳
     *
     * @param timeStr 时间 例如: 2016-03-09
     * @return
     */
    public static long getTimeStamp(String timeStr) {
       try {
           SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           Date date = simpleDateFormat.parse(timeStr);
           long ts = date.getTime();

           return ts;
       }catch (Exception e){

       }
        return  0;
    }

    public static String getDistanceTime(long  start, long end ) {
        long day = 0;
        long hour = 0;
        long hour1 = 0;
        long min = 0;
        long sec = 0;
        long diff ;
        String flag;

            diff = end - start;
        if(diff<0){
            return "0";
        }else {
            day = diff / (24 * 60 * 60 * 1000);
            hour1 = (diff / (60 * 60 * 1000) - day * 24);
//            hour = (diff / (60 * 60 * 1000) );
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour1 * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour1 * 60 * 60 - min * 60);
            String retStr = "";
            if (day != 0) retStr += day + "天";
            if (hour1 != 0) {retStr += getAndO(hour1) + ":";}else {retStr += "00" + ":";}
            if (min != 0) {retStr += getAndO(min) + ":";}else {retStr += "00" + ":";}
            if (sec != 0) {retStr += getAndO(sec) ;}else {retStr += "00" ;}
            return retStr;
        }
    }

    public static String getAndO(long i){
        if(i<10){
            return "0"+i;
        }
        return i+"";

    }

}
