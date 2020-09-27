package com.example.app3;

import android.widget.ImageView;

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

public class WeatherSearch {

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
                homePageFind.weathersTextViews[4].setText("建议：" + resultArray[3][0]);

                weatherchoose(resultArray[0][4], 1);
//                weatherchoose("雷阵雨", 1);

                homePageFind.weathersTextViews2[0].setText(resultArray[1][1]);
                homePageFind.weathersTextViews2[1].setText("地点：" + city);
                homePageFind.weathersTextViews2[2].setText("天气：" + resultArray[1][4]);
                homePageFind.weathersTextViews2[3].setText("风向/风力：" + resultArray[1][3] + "/" + resultArray[1][2]);
                homePageFind.weathersTextViews2[4].setText("建议" + resultArray[3][0]);
                weatherchoose(resultArray[1][4], 2);


                homePageFind.weathersTextViews3[0].setText(resultArray[2][1]);
                homePageFind.weathersTextViews3[1].setText("地点：" + city);
                homePageFind.weathersTextViews3[2].setText("天气：" + resultArray[02][4]);
                homePageFind.weathersTextViews3[3].setText("风向/风力：" + resultArray[2][3] + "/" + resultArray[2][2]);
                homePageFind.weathersTextViews3[4].setText("建议" + resultArray[3][0]);
                weatherchoose(resultArray[2][4], 3);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

    public void weatherchoose(String weather, int n){
        ImageView wea ;
        if(n == 1){
            wea = homePageFind.findViewById(R.id.today);
        }else if (n == 2){
            wea = homePageFind.findViewById(R.id.tomorrow2);
        }else{
            wea = homePageFind.findViewById(R.id.dayAfterTomorrow3);
        }
        switch (weather){
            case "晴":
                wea.setBackgroundResource(R.drawable.qing);
                break;
            case "多云":
                wea.setBackgroundResource(R.drawable.duoyun);
                break;
            case "阴":
                wea.setBackgroundResource(R.drawable.yintian);
                break;
            case "阵雨":
                wea.setBackgroundResource(R.drawable.zhenyu);
                break;
            case "雷阵雨":
                wea.setBackgroundResource(R.drawable.leizhenyu);
                break;
            case "雷阵雨伴有冰雹":
                break;
            case "雨夹雪":
                wea.setBackgroundResource(R.drawable.yujiaxue);
                break;
            case "小雨":
                wea.setBackgroundResource(R.drawable.xiaoyu);
                break;
            case "中雨":
                wea.setBackgroundResource(R.drawable.zhongyu);
                break;
            case "大雨":
                wea.setBackgroundResource(R.drawable.dayu);
                break;
            case "暴雨":
                wea.setBackgroundResource(R.drawable.baoyu);
                break;
            case "大暴雨":
                wea.setBackgroundResource(R.drawable.dabaoyu);
                break;
            case "阵雪":
                wea.setBackgroundResource(R.drawable.zhenxue);
                break;
            case "特大暴雨":
                wea.setBackgroundResource(R.drawable.dabaoyu);
                break;
            case "小雪":
                wea.setBackgroundResource(R.drawable.xiaoxue);
                break;
            case "中雪":
                wea.setBackgroundResource(R.drawable.zhongxue);
                break;
            case "大雪":
                wea.setBackgroundResource(R.drawable.daxue);
                break;
            case "暴雪":
                wea.setBackgroundResource(R.drawable.baoxue);
                break;
            case "雾":
                wea.setBackgroundResource(R.drawable.wu);
                break;
            case "冻雨":
                wea.setBackgroundResource(R.drawable.zhongyu);
                break;
            case "沙尘暴":
                wea.setBackgroundResource(R.drawable.shachenbao);
                break;
            case "小雨-中雨":
                wea.setBackgroundResource(R.drawable.xiaozhuanzhong);
                break;
            case "中雨-大雨":
                wea.setBackgroundResource(R.drawable.zhongzhuanda);
                break;
            case "大雨-暴雨":
                wea.setBackgroundResource(R.drawable.zhongzhuanda);
                break;
            case "暴雨-大暴雨":
                wea.setBackgroundResource(R.drawable.dabaoyu);
                break;
            case "大暴雨-特大暴雨":
                wea.setBackgroundResource(R.drawable.dabaoyu);
                break;
            case "小雪-中雪":
                wea.setBackgroundResource(R.drawable.zhongxue);
                break;
            case "中雪-大雪":
                wea.setBackgroundResource(R.drawable.daxue);
                break;
            case "大雪-暴雪":
                wea.setBackgroundResource(R.drawable.baoxue);
                break;
            case "浮尘":
                wea.setBackgroundResource(R.drawable.fuchen);
                break;
            case "扬沙":
                wea.setBackgroundResource(R.drawable.yangsha);
                break;
            case "强沙尘暴":
                wea.setBackgroundResource(R.drawable.qianghsachenbao);
                break;
            case "霾":
                wea.setBackgroundResource(R.drawable.mai);
                break;

        }
    }


}