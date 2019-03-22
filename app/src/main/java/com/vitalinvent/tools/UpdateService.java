package com.vitalinvent.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import com.vitalinvent.dictionaries.Weather;
import com.vitalinvent.weather.MainActivity;

public class UpdateService {

    public UpdateService() {
        CheckandUpdateCities();
    }

    public void CheckandUpdateCities(){
            new GetCities().execute();
    }

    public void CheckandUpdateWeaterForAllCities(){
        for (Weather weather: MainActivity.weathers) {
            if (Functions.getDifferenceHours(weather.getDateTimeUpdate(),Calendar.getInstance().getTime())>1){
                new GetCities().execute();
            }
        }
    }

    public void CheckandUpdateWeaterForCityByName(String cityName){
        for (Weather weather:MainActivity.weathers) {
            if (weather.getCity().equals(cityName))
            if (Functions.getDifferenceHours(weather.getDateTimeUpdate(),Calendar.getInstance().getTime())>1){
                new GetWeatherCity().execute();
            }
        }
    }

    private class GetCities extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                //https://yandex.ru/pogoda/static/cities.xml 404
                //https://pogoda.yandex.ru/static/cities.xml т.к. API Яндекса были изменены ими самими было выбрано решение парсить страничку с погодой без привязки к апи
                // это решение не является полностью рабочим т.к. могу измениться параметры страниц , при этом изменение сраниц таже как и апи при изменении надо переделывать
                //возможно апи кроме URL не изменились
                MainActivity.doc = Jsoup.connect("https://yandex.ru/pogoda/static/cities.xml").userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .referrer("https://yandex.ru").ignoreHttpErrors(true).get();
                //MainActivity.doc = Jsoup.connect("https://www.vitalinvent.com") .timeout(60000).validateTLSCertificates(false).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (MainActivity.doc != null) {
                Elements elements = MainActivity.doc.getElementsByClass("place-list__item");
                for (Element element : elements) {
                    System.out.print(element.attr("title"));
                    Weather weather = new Weather(element.child(1).childNode(0).outerHtml(), element.child(1).attributes().get("href"));
                    if (!MainActivity.weathers.contains(weather)) {
                        MainActivity.weathers.add(weather);
                    }
                }
            }
        }
    }


    private class GetWeatherCity extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                MainActivity.doc = Jsoup.connect("https://yandex.ru"+MainActivity.selectedWeatherCity.getCityUrl()).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .referrer("https://yandex.ru").ignoreHttpErrors(true).get();
                //MainActivity.doc = Jsoup.connect("https://www.vitalinvent.com") .timeout(60000).validateTLSCertificates(false).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Elements elements = MainActivity.doc.getElementsByClass("forecast-briefly__day forecast-briefly__day_weekstart_0 day-anchor i-bem");
            for (Element element : elements) {
                //здесь можно сделать более гибкое решение непривязанное к индексам
                String textDay1= element.childNode(0).childNode(3).childNode(0).childNode(0).outerHtml();
                String textDay2= element.childNode(0).childNode(3).childNode(1).childNode(0).outerHtml();
                String textNight1= element.childNode(0).childNode(4).childNode(0).childNode(0).outerHtml();
                String textNight2= element.childNode(0).childNode(4).childNode(1).childNode(0).outerHtml();
                String textweatherText= element.childNode(0).childNodes().get(5).childNode(0).outerHtml();//.childNode(5).childNode(0).childNode(0).outerHtml();
                String imgSrc= element.childNode(0).childNodes().get(2).attr("src");
                MainActivity.selectedWeatherCity.setWeatherDay(textDay1+" "+textDay2);
                MainActivity.selectedWeatherCity.setWeatherNight(textNight1+" "+textNight2);
                MainActivity.selectedWeatherCity.setWeatherTextMessage(textweatherText);
                MainActivity.selectedWeatherCity.setImageUrl(imgSrc);
                Variables.IN_PROGRESS_TRIGGER =false;

                if (!Variables.IN_PROGRESS_TRIGGER){
                    MainActivity.progressBar.setVisibility(ProgressBar.INVISIBLE);
//                    MainActivity.refreshTimer.cancel();
//                    MainActivity.refreshTimer.purge();
                }
//                TextView textDay = (TextView) findViewById(R.id.textDay);
//                TextView textNight = (TextView) findViewById(R.id.textNight);
//                TextView textWeatherTextTW = (TextView) findViewById(R.id.textWeatherText);
//                textDay.setText(textDay1+" "+textDay2);
//                textNight.setText(textNight1+" "+textNight2);
//                textWeatherTextTW.setText(textweatherText);
//                new MainActivity.GetWeatherImage().execute();
            }
//            Spinner spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
//            adapterCities = new ArrayAdapter<String>(MainActivity.this,
//                    android.R.layout.simple_list_item_1,weathers.getArrayListCities());
//            spinnerCity.setAdapter(adapterCities);
//            adapterCities.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
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
        //можно добавить в объект погоды картинку и ее кешировать для каждого города
        }
    }
}
