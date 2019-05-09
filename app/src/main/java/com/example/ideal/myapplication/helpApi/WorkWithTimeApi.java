package com.example.ideal.myapplication.helpApi;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class WorkWithTimeApi {

    private static final String TAG = "DBInf";

    public Long getSysdateLong(){
        //3600000*3 для москвы это +3 часа
        Date sysdate = new Date();
        Log.d(TAG, "getSysdateLong: " + sysdate.toString());
        return sysdate.getTime()+3600000*3;
    }

    public Long getMillisecondsStringDate(String date){

        Log.d(TAG, "getSysdateLong: " + date);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date orderDate;
            orderDate = formatForDateNow.parse(date);
            return orderDate.getTime();
        }
        catch (Exception e){
            Log.d(TAG, "getMillisecondsStringDate error: " + e );
        }
        return 0L;
    }

    public Long getMillisecondsStringDateWithSeconds(String date){

        Log.d(TAG, "getSysdateLong: " + date);
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date orderDate;
            orderDate = formatForDateNow.parse(date);
            return orderDate.getTime();
        }
        catch (Exception e){
            Log.d(TAG, "getMillisecondsStringDate error: " + e );
        }
        return 0L;
    }

    //возвращает время в формате yyyy-MM-dd HH:mm:ss
    public String getDateInFormatYMDHMS(Date date) {
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatForDateNow.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return formatForDateNow.format(date);
    }


}
