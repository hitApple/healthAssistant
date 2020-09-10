package com.example.app3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;


import java.util.ArrayList;
import java.util.List;

public class ContactBaiduMap extends BaseActivity {

    private static final String TAG = "ContactBaiduMap";

    public LocationClient mLocationClient;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private PoiSearch poiSearch;
    private List<String> permissionList = new ArrayList<>();
    private OnGetPoiSearchResultListener listener;
    private BaiduMap.OnMarkerClickListener markerClickListener;
    private List<PoiInfo> hospitalPoiList;
    private List<Hospital> hospitalList;
    private EditText searchEditText;
    private ListView listView;
    private int screen_y;


    private RelativeLayout homepage;
    private RelativeLayout find;
    private RelativeLayout plus;
    private RelativeLayout link;
    private RelativeLayout me;

    /**************************************以下为各种方法和内部类************************************/

    /***********************************活动的生命周期管理************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //百度地图api需要的初始化操作
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        poiSearch = PoiSearch.newInstance();

        setContentView(R.layout.contact_map);

        getPermissions();

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screen_y = metric.heightPixels;   // 屏幕高度（像素）

        listView = (ListView) findViewById(R.id.list_view);
        listView.setVisibility(View.INVISIBLE);
        listView.setOnItemClickListener(new myOnItemClickListener());

        mapView = (MapView) findViewById(R.id.baidu_map_view2);
        baiduMap = mapView.getMap();
        searchEditText = (EditText) findViewById(R.id.search_text_view);

        hospitalList = new ArrayList<>();

        mapView.getChildAt(2).setPadding(0, 0, 0, 100);
        baiduMap.setCompassPosition(new android.graphics.Point(70, 380));

        Button searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new myOnClickSearchListener());

        Button searchThisButton = (Button) findViewById(R.id.search_this);
        searchThisButton.setOnClickListener(new myOnClickSearchThisListener());

        baiduMap.setOnMapTouchListener(new myOnTouchBaiduMapListener());

        setOnPoiListener();

        markerClickListener = new myOnClickMarkerListener();
        baiduMap.setOnMarkerClickListener(markerClickListener);

        baiduMap.setMyLocationEnabled(true);




        homepage = findViewById(R.id.homepage);
        find = findViewById(R.id.find);
        plus = findViewById(R.id.plus);
        link = findViewById(R.id.link);
        me = findViewById(R.id.me);


        homepage.setBackgroundColor(-1);
        find.setBackgroundColor(-1);
        link.setBackgroundColor(-3355444);
        me.setBackgroundColor(-1);
        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactBaiduMap.this,HomePage_find.class));


            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ContactBaiduMap.this,HomePage.class));

                startActivity(new Intent(ContactBaiduMap.this,HomePage_find.class));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full).setVisibility(View.GONE);
                findViewById(R.id.plus_ui).setVisibility(View.VISIBLE);
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactBaiduMap.this,me.class));


            }
        });
        findViewById(R.id.cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full).setVisibility(View.VISIBLE);
                findViewById(R.id.plus_ui).setVisibility(View.GONE);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        requestLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        baiduMap.setOnMarkerClickListener(markerClickListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        baiduMap.removeMarkerClickListener(markerClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    /***********************************活动的生命周期管理************************************/

    /**
     * 初始化百度地图客户端并启动
     */
    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    /**
     * 进行百度地图客户端的选项设定
     */
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setOpenGps(true);
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    /**
     * 进行各种权限的申请
     */
    private void getPermissions(){
        if (ContextCompat.checkSelfPermission(ContactBaiduMap.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(ContactBaiduMap.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(ContactBaiduMap.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(ContactBaiduMap.this, permissions, 1);
            //申请权限之后再次创建该活动
//            this.recreate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Fail to request permission", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
        }
    }

    /**
     * 用于进行当前位置的设定，第一次打开地图时会自动定位到当前位置
     * @param location
     */
    private void navigateTo(BDLocation location){
        if (isFirstLocate){
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.accuracy(location.getRadius());
        locationBuilder.direction(location.getDirection());
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    /**
     * 判断某位置与当前位置的距离
     * @param latLng 用于传入经纬度
     * @return 两点距离，单位：米
     */
    private double getDistance(LatLng latLng){
        BDLocation bdLocation = mLocationClient.getLastKnownLocation();
        LatLng myLatLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        return DistanceUtil.getDistance(latLng, myLatLng);
    }

    /**
     * 供本类中的listView调用，调用后展示listView上移动画
     */
    private void startAnimation(){
        Animation translateAnimation = new TranslateAnimation(0,0,
                screen_y / 2,0);
        translateAnimation.setDuration(1000);
        translateAnimation.setFillEnabled(true);//使其可以填充效果从而不回到原地
        translateAnimation.setFillAfter(true);//不回到起始位置
        //如果不添加setFillEnabled和setFillAfter则动画执行结束后会自动回到远点
        listView.setAnimation(translateAnimation);//给imageView添加的动画效果
        translateAnimation.startNow();//动画开始执行 放在最后即可
    }


    /**
     * 调用之后可以关闭键盘
     */
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /****************************************监听器相关*********************************************/

    /**
     * 用于创建并注册poi监听器，调用poiSearch.search相关方法会触发该监听器
     */
    private void setOnPoiListener(){

        //创建监听器
        listener = new OnGetPoiSearchResultListener() {

            /**
             * 调用poi.search相关方法后调用，返回PoiResult类对象，用于提供初步信息
             * @param result 还未用到此返回值
             */
            @Override
            public void onGetPoiResult(PoiResult result){
                hospitalPoiList = result.getAllPoi();
                if (hospitalPoiList == null || hospitalPoiList.get(0).uid.equals("") ){
                    Toast.makeText(ContactBaiduMap.this, "未检索到任何信息，请稍后再次查询",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取POI检索结果
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    baiduMap.clear();

                    //创建PoiOverlay对象
                    PoiOverlay poiOverlay = new PoiOverlay(baiduMap);

                    //设置Poi检索数据
                    poiOverlay.setData(result);

                    //将poiOverlay添加至地图并缩放至合适级别
                    poiOverlay.addToMap();
                    poiOverlay.zoomToSpan();
                } else {
                    Toast.makeText(ContactBaiduMap.this, "查询出错，请稍后再次查询",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                hospitalList = new ArrayList<>();
                for (PoiInfo p: hospitalPoiList) {
                    poiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUids(p.uid));
                }

                HospitalAdapter adapter = new HospitalAdapter(ContactBaiduMap.this,
                        R.layout.hospital_item, hospitalList);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(adapter);
                listView.setEnabled(false);

                startAnimation();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ContactBaiduMap.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setEnabled(true);
                            }
                        });
                    }
                }.start();
//                poiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUids(poiInfo1.uid));
//                // uid的集合，最多可以传入10个uid，多个uid之间用英文逗号分隔。

            }

            /**
             * 该方法已被弃用
             * @param result
             */
            public void onGetPoiDetailResult(PoiDetailResult result){
                //获取Place详情页检索结果
            }

            /**
             * 通过poiSearchResult提供的poi.uid值来进行详细信息的提供
             * @param poiDetailSearchResult
             */
            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
                if (poiDetailSearchResult.getPoiDetailInfoList() == null){
                    Toast.makeText(ContactBaiduMap.this, "该地区暂无详细信息",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                PoiDetailInfo info = poiDetailSearchResult.getPoiDetailInfoList().get(0);
                Hospital hospital = new Hospital(info.getName(), info.getTelephone(),
                        info.getAddress(), (int)getDistance(info.getLocation()), info.getShopHours());
                hospitalList.add(hospital);
            }

            /**
             * 此接口不需要使用，没有实现方法
             * @param poiIndoorResult
             */
            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };

        //注册poi监听器
        poiSearch.setOnGetPoiSearchResultListener(listener);

    }

    /**
     * listView点击监听器
     */
    private class myOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(ContactBaiduMap.this, HospitalWebView.class);
            intent.putExtra("医院名称", hospitalList.get(i).getName());
            intent.putExtra("医院电话", hospitalList.get(i).getTel());
            intent.putExtra("医院地址", hospitalList.get(i).getAddress());
            intent.putExtra("医院距离", String.valueOf(hospitalList.get(i).getDistance()));
            intent.putExtra("医院时间", hospitalList.get(i).getTime());

            startActivity(intent);
        }
    }


    /**
     * 搜索按钮点击监听器
     */
    private class myOnClickSearchListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            String city = searchEditText.getText().toString();
            if (city.equals("")){
                Toast.makeText(ContactBaiduMap.this, "请填写城市信息！",
                        Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(ContactBaiduMap.this,
                        "点击列表中的医院信息可访问对应网站并联系医生", Toast.LENGTH_SHORT).show();
                poiSearch.searchInCity(new PoiCitySearchOption()
                        .city(city)
                        .keyword("医院")
                        .pageCapacity(20)
                        .pageNum(0));
            }
            closeKeyboard();
            searchEditText.setText("");
        }
    }

    /**
     * 当前位置按钮点击监听器
     */
    private class myOnClickSearchThisListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            BDLocation bdLocation = mLocationClient.getLastKnownLocation();
            if (bdLocation == null || bdLocation.getCountry() == null){
                Toast.makeText(ContactBaiduMap.this, "暂时无法获取位置信息，请稍后再试",
                        Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(ContactBaiduMap.this,
                        "点击列表中的医院信息可访问对应网站并联系医生", Toast.LENGTH_SHORT).show();
                poiSearch.searchInCity(new PoiCitySearchOption()
                        .city(bdLocation.getCity())
                        .keyword("医院")
                        .pageCapacity(30)
                        .pageNum(0));
            }
            closeKeyboard();
            searchEditText.setText("");
        }
    }

    /**
     * 地图触摸监听器
     */
    private class myOnTouchBaiduMapListener implements BaiduMap.OnMapTouchListener{
        @Override
        public void onTouch(MotionEvent motionEvent) {
            if (listView.getVisibility() == View.VISIBLE){
                listView.clearAnimation();
                listView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 地图标记点击监听器
     */
    private class myOnClickMarkerListener implements BaiduMap.OnMarkerClickListener{
        @Override
        public boolean onMarkerClick(Marker marker) {
            LatLng markerLatLng = marker.getPosition();
            for (PoiInfo info : hospitalPoiList){
                LatLng infoLatLng = info.getLocation();
                if (markerLatLng.latitude == infoLatLng.latitude && markerLatLng.longitude == infoLatLng.longitude){
                    hospitalList = new ArrayList<>();
                    poiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUids(info.uid));
                    HospitalAdapter adapter = new HospitalAdapter(ContactBaiduMap.this,
                            R.layout.hospital_item, hospitalList);
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(adapter);
                    startAnimation();

                }
            }

            return true;
        }
    }

    /**
     * 用于判断目前是否可以定位，如果可以则移动到当前位置
     */
    private class MyLocationListener extends BDAbstractLocationListener {

        /**
         * 如果接收到位置信息，则执行定位
         * @param bdLocation
         */
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation || bdLocation.getLocType()
                    == BDLocation.TypeGpsLocation){
                navigateTo(bdLocation);
            }
        }
    }



    /**
     * 退出程序
     */
/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    long boo = 0;
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - boo) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次返回键退出程序",
                        Toast.LENGTH_SHORT).show();
                boo = System.currentTimeMillis();
            } else {
//                Intent home = new Intent(Intent.ACTION_MAIN);
//                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                home.addCategory(Intent.CATEGORY_HOME);
//                startActivity(home);
                ActivityCollector.finishAll();
                return true;
            }
        }
        return false;
    }




    /****************************************监听器相关*********************************************/

}




/****************************************废弃代码**************************************************/
//                List<PoiDetailInfo> list = poiDetailSearchResult.getPoiDetailInfoList();
//                Log.d(TAG, "getDetail: " + list.get(0).getDetail());
//                Log.d(TAG, "getDetail: " + list.get(0).getDetailUrl());
//                Log.d(TAG, "地址: " + list.get(0).getAddress());
//                Log.d(TAG, "评论数: " + list.get(0).getCommentNum());
//                Log.d(TAG, "详情: " + list.get(0).getDetail());
//                Log.d(TAG, "距离: " + getDistance(list.get(0).getLocation()));
//                Log.d(TAG, "环境评价: " + list.get(0).getEnvironmentRating());
//                Log.d(TAG, "上班时间: " + list.get(0).getShopHours());


//            StringBuilder currentPosition = new StringBuilder();
//            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
//            currentPosition.append("经线：").append(bdLocation.getLongitude()).append("\n");
//            currentPosition.append("国家：").append(bdLocation.getLongitude()).append("\n");
//            currentPosition.append("省：").append(bdLocation.getCountry()).append("\n");
//            currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
//            currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
//            currentPosition.append("定位方式");
//            int a = bdLocation.getLocType();
//            Log.d(TAG, bdLocation.getLocTypeDescription());
//            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
//                currentPosition.append("GPS");
//            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
//                currentPosition.append("网络");
//            }
//            positionText.setText(currentPosition);
