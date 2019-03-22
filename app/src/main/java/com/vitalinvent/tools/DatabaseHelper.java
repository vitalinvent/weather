package com.vitalinvent.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.vitalinvent.dictionaries.DatabaseColumn;
import com.vitalinvent.dictionaries.Weather;
import com.vitalinvent.dictionaries.WeatherRepository;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, Variables.DATABASE_NAME, null, Variables.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = getTableCreateSql(WeatherRepository.class.getSimpleName(), Weather.Column.values());
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WeatherRepository.class.getSimpleName());
        onCreate(db);
    }

    private String getTableCreateSql(String tableName, DatabaseColumn[] cols) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder
                .append("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append("(");
        String dot = "";
        for (DatabaseColumn col : cols) {
            sqlBuilder.append(dot);
            sqlBuilder.append(col.name());
            sqlBuilder.append(" ");
            sqlBuilder.append(col.getType());
            dot = ", ";
        }
        sqlBuilder.append(");");
        return sqlBuilder.toString();
    }

}
