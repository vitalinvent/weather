package com.vitalinvent.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.vitalinvent.dictionaries.Weather;
import com.vitalinvent.dictionaries.WeatherRepository;
import com.vitalinvent.tools.UpdateService;
import com.vitalinvent.tools.Variables;

public class MainActivity extends AppCompatActivity {
    public static Document doc=null;
    public static Weather selectedWeatherCity=null;
    public static WeatherRepository weathers = new WeatherRepository();
    public static UpdateService updateService = new UpdateService();
    public static ArrayAdapter<String> adapterCities=null;
    private Spinner spinnerCity = null;
    public static Timer refreshTimer = new Timer();
    private RefreshTimerTask refreshTimerTask = new RefreshTimerTask();
    public static ProgressBar progressBar = null;
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button button = (Button) findViewById(R.id.buttonRefresh);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adapterCities = new ArrayAdapter<String>(MainActivity.this,
//                        android.R.layout.simple_list_item_1,weathers.getArrayListCities());
//                spinnerCity.setAdapter(adapterCities);
//                adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                TextView textDay = (TextView) findViewById(R.id.textDay);
                TextView textNight = (TextView) findViewById(R.id.textNight);
                TextView textWeatherTextTW = (TextView) findViewById(R.id.textWeatherText);

                if (selectedWeatherCity != null) {
                    textDay.setText(selectedWeatherCity.getWeatherDay());
                    textNight.setText(selectedWeatherCity.getWeatherNight());
                    textWeatherTextTW.setText(selectedWeatherCity.getWeatherTextMessage());
                }

            }
        });
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textDay = (TextView) findViewById(R.id.textDay);
                TextView textNight = (TextView) findViewById(R.id.textNight);
                TextView textWeatherTextTW = (TextView) findViewById(R.id.textWeatherText);
                if (selectedWeatherCity == null) {
                    selectedWeatherCity=weathers.getByIndex(position);
                    progressBar.setVisibility(ProgressBar.VISIBLE);

                    Variables.IN_PROGRESS_TRIGGER=true;
                    updateService.CheckandUpdateWeaterForCityByName(selectedWeatherCity.getCity());

                    if (refreshTimer == null) {
                        refreshTimer = new Timer();
                        refreshTimer.schedule(refreshTimerTask, 1000, 3000);
                    }

                } else if (!selectedWeatherCity.getCity().equals(weathers.getByIndex(position).getCity())){
                    selectedWeatherCity=weathers.getByIndex(position);
                    progressBar.setVisibility(ProgressBar.VISIBLE);

                    Variables.IN_PROGRESS_TRIGGER=true;
                    updateService.CheckandUpdateWeaterForCityByName(selectedWeatherCity.getCity());

                    if (refreshTimer == null) {
                        refreshTimer = new Timer();
                        refreshTimer.schedule(refreshTimerTask, 1000, 3000);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        weathers.load(this);
        if (weathers.size()==0) {
            Variables.IN_PROGRESS_TRIGGER = true;
            refreshTimer.schedule(refreshTimerTask, 1000, 3000);
        }

    }
    class RefreshTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    adapterCities = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_list_item_1,weathers.getArrayListCities());
                    spinnerCity.setAdapter(adapterCities);
                    adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    TextView textDay = (TextView) findViewById(R.id.textDay);
                    TextView textNight = (TextView) findViewById(R.id.textNight);
                    TextView textWeatherTextTW = (TextView) findViewById(R.id.textWeatherText);

                    if (selectedWeatherCity != null) {
                        textDay.setText(selectedWeatherCity.getWeatherDay());
                        textNight.setText(selectedWeatherCity.getWeatherNight());
                        textWeatherTextTW.setText(selectedWeatherCity.getWeatherTextMessage());
                    }
                    if (!Variables.IN_PROGRESS_TRIGGER){
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                        refreshTimer.cancel();
                        refreshTimer.purge();
                        refreshTimer=null;
                    }

                }
            });
            if (MainActivity.selectedWeatherCity != null)
                new MainActivity.GetWeatherImage().execute();
        }
    }

//    public void setAdapter(){
//        adapterCities = new ArrayAdapter<String>(MainActivity.this,
//                android.R.layout.simple_list_item_1,weathers.getArrayListCities());
//        spinnerCity.setAdapter(adapterCities);
//        adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//    }
    @Override
    protected void onStop() {
        super.onStop();
        weathers.save(this);
    }

    private class GetWeatherImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            URL url=null;
            try {
                url = new URL("http:"+MainActivity.selectedWeatherCity.getImageUrl());
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            MainActivity.selectedWeatherCity.setImageBitmap(bmp);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ImageView imageWeather = (ImageView) findViewById(R.id.imageWeather);
            imageWeather.setImageBitmap(MainActivity.selectedWeatherCity.getImageBitmap());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void SaveCities(){
//        MainActivity.weathers.save(this);
//    }
//
//    public void SaveCities(){
//        MainActivity.weathers.save(this);
//    }

}
