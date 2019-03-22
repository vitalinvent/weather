package com.vitalinvent.dictionaries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vitalinvent.tools.DatabaseHelper;
import com.vitalinvent.tools.Functions;
import com.vitalinvent.tools.Variables;
import com.vitalinvent.weather.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeatherRepository extends ArrayList<Weather> implements Serializable {

    public WeatherRepository() {
        DateTimeUpdate = Calendar.getInstance().getTime();
    }

    private Date DateTimeUpdate;

    public Date getDateTimeUpdate() {
        return DateTimeUpdate;
    }

    public void setDateTimeUpdate(Date dateTimeUpdate) {
        DateTimeUpdate = dateTimeUpdate;
    }

    //МОЖНО ДОБАВИТЬ КЕШ ДЛЯ КАЖДОГО ГОРОДА
    public void setImageForCity(){
        //МОЖНО ДОБАВИТЬ КЕШ ДЛЯ КАЖДОГО ГОРОДА}
    }

    public ArrayList<String> getArrayListCities(){
        ArrayList<String> retVal=new ArrayList<>();
        for (int idx=0;idx<size();idx++){
            retVal.add(get(idx).getCity());
        }
        return retVal;
    }
    public Weather getByCity(String city){
        for (int idx=0;idx<size();idx++){
            if (get(idx).getCity().equals(city)){
                return get(idx);
            }
        }
        return null;
    }
    public Weather getByIndex(Integer idx){
        return get(idx);
    }

    public void save(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (int idx=0;idx<size();idx++){
            ContentValues values = new ContentValues();
            values.put(Weather.Column._id.name(), get(idx).getCityUrl());
            values.put("City", get(idx).getCity());
            values.put("WeatherDay", get(idx).getWeatherDay());
            values.put("WeatherNight", get(idx).getWeatherNight());
            values.put("WeatherTextMessage", get(idx).getWeatherTextMessage());
            values.put("cityUrl", get(idx).getCityUrl());
            values.put("imageUrl", get(idx).getImageUrl());
            values.put("imageBitmap", Functions.getByteArrayFromBitmap(get(idx).getImageBitmap()));
            values.put("DateTimeUpdate", get(idx).getDateTimeUpdate().getTime());
            db.insert(WeatherRepository.class.getName(), null, values);
        }
    }
    public void load(Context context) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String query = "SELECT * FROM "+ Variables.DB_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        clear();
//        _id("TEXT PRIMARY KEY"), _City("TEXT"), _WeatherDay("TEXT"), _WeatherNight("TEXT"), _WeatherTextMessage("TEXT")
//                , _cityUrl("TEXT"), _imageUrl("TEXT"), _imageBitmap("BLOB"), _DateTimeUpdate("LONG");
        while (cursor.moveToNext()) {
            add(new Weather(cursor.getString(0),cursor.getString(1)
                    ,cursor.getString(2),cursor.getString(3)
                    ,cursor.getString(4),cursor.getString(5),cursor.getBlob(6),cursor.getString(7)));
        }
    }
}
