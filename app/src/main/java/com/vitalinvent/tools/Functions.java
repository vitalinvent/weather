package com.vitalinvent.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Functions {
    public static double getDifferenceHours(Date startDate, Date endDate) {
        double different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        different = different % hoursInMilli;
        return different;
    }
    public static Date stringToDate(String s){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try{
            return df.parse(s);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] getByteArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return byteArrayOutputStream .toByteArray();
    }

    public static Date UnixDateTimeToDate(String unixTime){
        long unixSeconds = Long.parseLong(unixTime);
        return   new java.util.Date(unixSeconds*1000L);
    }
}
