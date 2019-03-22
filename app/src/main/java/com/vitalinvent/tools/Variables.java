package com.vitalinvent.tools;

import com.vitalinvent.dictionaries.WeatherRepository;

public class Variables {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "weather.db";
    public volatile static boolean IN_PROGRESS_TRIGGER = false;
    public static final String DB_TABLE_NAME = WeatherRepository.class.getSimpleName();
}
