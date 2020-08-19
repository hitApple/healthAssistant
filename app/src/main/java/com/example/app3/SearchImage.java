package com.example.app3;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.baidu.aip.imageclassify.AipImageClassify;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class SearchImage extends Thread{
    public static final String APP_ID = "21959927";
    public static final String API_KEY = "GZdwpjZmMr6foqN7AV1b66CX";
    public static final String SECRET_KEY = "OZj7gooXwXlnC7hOBAocqXjyWpDVtiSS";
    private static final String TAG = "ImageSearch";
    private JSONObject res;
    private final AipImageClassify client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);
    private final HashMap<String, String> options = new HashMap<>();
    private final String path;
    private SearchResult[] searchResults;
    private final byte[] bytes;

    public SearchImage(String path) {
        this.path = path;
        this.bytes = null;
        init();
    }

    public SearchImage(byte[] bytes) {
        this.path = null;
        this.bytes = bytes;
        init();
    }

    private void init(){
        options.put("top_num", "3");
        options.put("filter_threshold", "0.7");
        options.put("baike_num", "5");
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(6000);
    }

    @Override
    public void run() {
        super.run();
        if (path != null){
            res = client.dishDetect(path, options);
        } else{
            res = client.dishDetect(bytes, options);
        }

//        try {
//            result[0] = res.toString(2);
//            Log.d(TAG, "jsonPrint: ");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }


    public SearchResult[] getSearchResults() throws JSONException {
        SearchResult[] searchResults;
        if (!isAlive()){
            HashMap<String, String> hashMap = jsonObjectToHashMap(res);
            if (hashMap.get("result_num") == null){
                return null;
            }
            int resultNum = Integer.parseInt(hashMap.get("result_num"));

            String result = hashMap.get("result");
            if(result == null){
                return null;
            }
            String[] splitStrings = result.split("[\\[\\]{},]");

            searchResults = getUsefulStrings(splitStrings, resultNum);

        } else {
            return null;
        }
        return searchResults;
    }

    public JSONObject getRes() {
        return res;
    }


    private static HashMap<String, String> jsonObjectToHashMap(JSONObject jsonObj) throws JSONException {
        HashMap<String, String> data = new HashMap<>();
        Iterator<String> it = jsonObj.keys();
        while(it.hasNext()){
            String key = it.next();
            String value = jsonObj.get(key).toString();
            data.put(key, value);
        }
        System.out.println(data);
        return data;
    }

    private SearchResult[] getUsefulStrings(String[] strings, int result_num){
        int stringsIndex = 0;
        SearchResult[] searchResults = new SearchResult[result_num];
        for (int i = 0; i < result_num ; i++){
            searchResults[i] = new SearchResult();
            while (true){
                String tempString = strings[stringsIndex];
                if (tempString.contains("has_calorie")) {
                    searchResults[i].has_calorie = tempString.contains("true");
                } else if (tempString.contains("calorie")){
                    searchResults[i].calorie = tempString.substring(
                            tempString.lastIndexOf(':') + 2, tempString.length() - 1);
                } else if (tempString.contains("description")){
                    searchResults[i].description = tempString.substring(14);
                } else if (tempString.contains("probability")){
                    searchResults[i].probability = tempString.substring(15, tempString.length() - 2);
                } else if (tempString.contains("name")){
                    searchResults[i].name = tempString.substring(8, tempString.length() - 1);
                    if (searchResults[i].name.contains("非菜")){
                        searchResults[i].description = "该物品有" + Double.parseDouble(searchResults[i].probability) * 100 + "%的可能不是一道菜，或本程序无法识别";
                    }
                    stringsIndex++;
                    break;
                }
                stringsIndex++;
            }
        }
        return searchResults;
    }

    class SearchResult{
        boolean has_calorie = false;
        String calorie = "暂无信息";
        String name = "暂无信息";;
        String description = "暂无信息";;
        String probability = "暂无信息";;

        void print(){
            Log.d(TAG, "SearchResult print: \n"
                    + "name: " + name + "\n"
                    + "has_calorie: " + has_calorie + "\n"
                    + "calorie:" + calorie + "\n"
                    + "description: " + description + "\n"
                    + "probability: " + probability + "\n");
        }

        @Override
        public String toString() {
            return "has_calorie=" + has_calorie + '\n' +
                    "calorie='" + calorie + '\n' +
                    "name='" + name + '\n' +
                    "description='" + description + '\n' +
                    "probability='" + probability;
        }
    }

}





//    private String getJSONString() throws InterruptedException {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                res = client.objectDetect(path, new HashMap<String, String>());
//                res = client.dishDetect(path, options);
//                try {
////                    System.out.println(res.toString(2));
//                    result[0] = res.toString(2);
//                    Log.d(TAG, "jsonPrint: ");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        Thread.sleep(3000);
//        return result[0];
//    }



// 调用接口
//        String path = "android.resource://" + getPackageName() + "/" + R.raw.timg;

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                res = client.objectDetect(path, new HashMap<String, String>());
//                res = client.dishDetect(path, options);
//                try {
//                    System.out.println(res.toString(2));
////                    string = res.toString();
//                    Log.d(TAG, "jsonPrint: ");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//
//                }
//            }
//        }).start();
