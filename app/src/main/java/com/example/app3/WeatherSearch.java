package com.example.app3;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class WeatherSearch{

    public LocationClient mLocationClient;
    private HomePage_find homePageFind;

    public WeatherSearch(HomePage_find homePageFind){
        //百度地图api需要的初始化操作
        this.homePageFind = homePageFind;
        mLocationClient = new LocationClient(this.homePageFind);
        LocationClientOption locationClientOption = new LocationClientOption();
        locationClientOption.setIsNeedAddress(true);
        mLocationClient.setLocOption(locationClientOption);
        mLocationClient.registerLocationListener(new MyLocationListener());
    }

    public void getWeatherData(final String city) throws JSONException {
        String url = "http://wthrcdn.etouch.cn/weather_mini?city=" + city;
        String characterSet = new String("utf-8");
        String resData = null;
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bReader = null;
        try {
            URL urlWeb = new URL(url);
            URLConnection connection = urlWeb.openConnection();
            bReader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), characterSet));
            while (null != (resData = bReader.readLine())) {
                stringBuffer.append(resData);
            }
            bReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = stringBuffer.toString();
        JSONObject jsonObject = new JSONObject(result);
        final String[][] resultArray = new String[4][];
        JSONObject weatherData = jsonObject.getJSONObject("data");
        JSONArray forecastArray = weatherData.getJSONArray("forecast");
        JSONObject firstDay = (JSONObject)forecastArray.get(0);
        JSONObject secondDay = (JSONObject)forecastArray.get(1);
        JSONObject thirdDay = (JSONObject)forecastArray.get(2);
        resultArray[0] = new String[]{firstDay.getString("date"),
                firstDay.getString("low") + "/" + firstDay.getString("high"),
                firstDay.getString("fengli").substring(9, 11),
                firstDay.getString("fengxiang"),
                firstDay.getString("type")};
        resultArray[1] = new String[]{secondDay.getString("date"),
                secondDay.getString("low") + "/" + secondDay.getString("high"),
                secondDay.getString("fengli").substring(9, 11),
                secondDay.getString("fengxiang"),
                secondDay.getString("type")};
        resultArray[2] = new String[]{thirdDay.getString("date"),
                thirdDay.getString("low") + "/" + thirdDay.getString("high"),
                secondDay.getString("fengli").substring(9, 11),
                secondDay.getString("fengxiang"),
                secondDay.getString("type")};
        resultArray[3] = new String[]{weatherData.getString("ganmao")};
//        final StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < resultArray.length; i++){
//            for (int j = 0; j < resultArray[i].length; j++){
//                builder.append(resultArray[i][j]);
//            }
//            builder.append("\n");
//        }
//        final String weatherText = builder.toString();
        homePageFind.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                homePageFind.weathersTextViews[0].setText(resultArray[0][1]);
                homePageFind.weathersTextViews[1].setText("地点：" + city);
                homePageFind.weathersTextViews[2].setText("天气：" + resultArray[0][4]);
                homePageFind.weathersTextViews[3].setText("风向/风力：" + resultArray[0][3] + "/" + resultArray[0][2]);
                homePageFind.weathersTextViews[4].setText("建议" + resultArray[3][0]);
            }
        });

    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation bdLocation) {

            if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation || bdLocation.getLocType()
                    == BDLocation.TypeGpsLocation){
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            getWeatherData(bdLocation.getCity());
                            mLocationClient.stop();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        }
    }




}