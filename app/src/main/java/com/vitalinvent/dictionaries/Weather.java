package com.vitalinvent.dictionaries;

import android.graphics.Bitmap;

import com.vitalinvent.tools.Functions;

import java.io.Serializable;
import java.util.Date;

public class Weather {

    public Weather(String city, String weatherDay, String weatherNight, String weatherTextMessage, String cityUrl, String imageUrl, byte[] imageBitmapByteArray, String dateTimeUpdate) {
        City = city;
        WeatherDay = weatherDay;
        WeatherNight = weatherNight;
        WeatherTextMessage = weatherTextMessage;
        this.cityUrl = cityUrl;
        this.imageUrl = imageUrl;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        this.imageBitmap = Bitmap.createBitmap(1, 1, conf);


        DateTimeUpdate = Functions.UnixDateTimeToDate(dateTimeUpdate);
    }

    public static enum Column implements DatabaseColumn {
        _id("TEXT PRIMARY KEY"), _City("TEXT"), _WeatherDay("TEXT"), _WeatherNight("TEXT"), _WeatherTextMessage("TEXT")
        , _cityUrl("TEXT"), _imageUrl("TEXT"), _imageBitmap("BLOB"), _DateTimeUpdate("LONG");

        private String fld;

        private Column(String type) {
            fld = type;
        }

        @Override
        public String getType() {
            return fld;
        }
    }

    private String City;
    private String WeatherDay;
    private String WeatherNight;
    private String WeatherTextMessage;
    private String cityUrl;
    private String imageUrl;
    private Bitmap imageBitmap;//МОЖНО ДОБАВИТЬ КЕШ ДЛЯ КАЖДОГО ГОРОДА
    private Date DateTimeUpdate;

    public Date getDateTimeUpdate() {
        return DateTimeUpdate;
    }

    public void setDateTimeUpdate(Date dateTimeUpdate) {
        DateTimeUpdate = dateTimeUpdate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public Weather(String city, String cityUrl) {
        City = city;
        this.cityUrl = cityUrl;
        //DateTimeUpdate = Calendar.getInstance().getTime();
        DateTimeUpdate = Functions.stringToDate("1978-06-24T04:21:37+00:00");
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCityUrl() {
        return cityUrl;
    }

    public void setCityUrl(String cityUrl) {
        this.cityUrl = cityUrl;
    }

    public String getWeatherDay() {
        return WeatherDay;
    }

    public void setWeatherDay(String weatherDay) {
        WeatherDay = weatherDay;
    }

    public String getWeatherNight() {
        return WeatherNight;
    }

    public void setWeatherNight(String weatherNight) {
        WeatherNight = weatherNight;
    }

    public String getWeatherTextMessage() {
        return WeatherTextMessage;
    }

    public void setWeatherTextMessage(String weatherTextMessage) {
        WeatherTextMessage = weatherTextMessage;
    }

}
