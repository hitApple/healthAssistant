package com.example.app3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class CalorieView extends AppCompatActivity {

    private static final String TAG = "CalorieView";

    //请求码
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;

    private ProgressBar progressBar;
    private TextView textView;

    //需要请求的权限
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    /*********************************活动的生命周期管理********************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_view);

        getPermissions();

        progressBar = (ProgressBar) findViewById(R.id.waiting_calorie_server);
        progressBar.setVisibility(View.GONE);

        textView = (TextView) findViewById(R.id.calorie_display);

        Button galleryButton = (Button) findViewById(R.id.open_gallery);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);    // 选择数据
                photoPickerIntent.setType("image/*");                               // 获取所有本地图片
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE);
            }
        });
        Button cameraButton = (Button) findViewById(R.id.open_camera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 启动相机
                startActivityForResult(intent1, CAMERA_REQUEST_CODE);
            }
        });

    }

    /*********************************活动的生命周期管理********************************************/

    /**
     * 调用相册或相机之后会自动调用本方法，用于对图像分别的进行处理
     * @param requestCode 请求码，请参考本类的固定值全局变量
     * @param resultCode 返回码，返回OK说明正常返回
     * @param data 返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
//        SearchImage searchImage = null;
//        boolean isOutOfTime = false;
        switch (requestCode){
            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        assert data != null;
                        final Uri imageUri = data.getData();
                        assert imageUri != null;
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        bitmap = BitmapFactory.decodeStream(imageStream);
//                        Log.d(TAG, "onActivityResult: I have gotten a photo!");

                        startSearchImage(bitmap);
                    } catch (FileNotFoundException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    try {
                        Bundle bundle = null;
                        if (data != null) {
                            bundle = data.getExtras();
                        } else {
                            return;
                        }
                        if (bundle != null) {
                            bitmap = (Bitmap) bundle.get("data");
                        }
                        if (bitmap == null){
                            return;
                        }
                        startSearchImage(bitmap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
            default:
                break;
        }
    }

    /**
     * 用于向服务器发出请求，图像资源被保存在bitmap中
     * @param bitmap 传入的图像资源
     * @throws JSONException
     */
    private void startSearchImage(Bitmap bitmap)
            throws JSONException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        SearchImage searchImage = new SearchImage(outputStream.toByteArray());
        searchImage.start();
        progressBar.setVisibility(View.VISIBLE);
        boolean isOutOfTime = false;
        long startTime2 = System.currentTimeMillis();
        while (searchImage.isAlive()){
            if (System.currentTimeMillis() - startTime2 >= 5000){
                isOutOfTime = true;
                break;
            }
        }
        progressBar.setVisibility(View.GONE);
        if (isOutOfTime){
            return;
        }
        SearchImage.SearchResult[] searchResults = searchImage.getSearchResults();
        textView.setText(searchResults[0].toString());
    }

    /**
     * 获取本活动需要的权限
     */
    private void getPermissions() {

        ArrayList<String> permissionList = new ArrayList<>();

        for (String permission : PERMISSIONS){
            if (ContextCompat.checkSelfPermission(CalorieView.this,
                    permission) != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(CalorieView.this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 权限回应处理，得不到权限时调用Toast显示信息并结束本活动
     * @param requestCode 请求码，请参考本类的固定值全局变量
     * @param permissions
     * @param grantResults 授权结果
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Fail to request permission", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
        }
    }

}