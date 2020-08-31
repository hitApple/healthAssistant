package com.example.app3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class CalorieView extends BaseActivity {

    private static final String TAG = "CalorieView";

    //请求码
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;

    private ProgressBar progressBar;
    private EditText text1;
    private EditText text2;
    private EditText text3;
    private TextView text4;
    private EditText text5;
    private ImageView rightArrow;
    private ImageView displayPic;

    private String[] foodInfo;
    private boolean[] format = {false, true};

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

        text1 = findViewById(R.id.calorie_display1);
        text2 = findViewById(R.id.calorie_display2);
        text3 = findViewById(R.id.calorie_display3);
        text4 = findViewById(R.id.calorie_display4);
        text5 = findViewById(R.id.calorie_display5);

        text2.setOnFocusChangeListener(new TextView.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                format[0] = checkFormat(text2, b);
            }
        });
        text5.setOnFocusChangeListener(new TextView.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                format[1] = checkFormat(text5, b);
            }
        });

        text4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        text4.getPaint().setAntiAlias(true);//抗锯齿
        text5.setText("1");

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

        displayPic = findViewById(R.id.display_image);
        rightArrow = findViewById(R.id.right_arrow);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                format[0] = checkFormat(text2, false);
                format[1] = checkFormat(text5, false);

                if (!format[0]){
                    Toast.makeText(CalorieView.this, "您的卡路里信息未填写或填写错误",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!format[1]){
                    Toast.makeText(CalorieView.this, "您的数量信息未填写或填写错误",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Calorie calorie = new Calorie();
                // calorie.setFoodName(foodInfo[0]);
                // calorie.setCalorieAmount(Integer.parseInt(text2.getText().toString())
                //         * Integer.parseInt(text5.getText().toString()));
                // Calendar calendar = Calendar.getInstance();
                // calorie.setYear(calendar.get(Calendar.YEAR));
                // calorie.setMonth(calendar.get(Calendar.MONTH) + 1);
                // calorie.setDay(calendar.get(Calendar.DAY_OF_MONTH));
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
                        displayPic.setImageBitmap(bitmap);
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
                        displayPic.setImageBitmap(bitmap);
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
            Toast.makeText(CalorieView.this, "请求超时，请稍后重试", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        SearchImage.SearchResult[] searchResults = searchImage.getSearchResults();
        foodInfo = searchResults[0].toString().split("\n");
        text1.setText(foodInfo[0].substring(3));
        text2.setText(foodInfo[1]);
        text3.setText(foodInfo[2].substring(6));
        text4.setText(foodInfo[3]);
        foodInfo[0] = text1.getText().toString();
        format[0] = true;
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


    public static boolean isNumeric(String string){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    private boolean checkFormat(EditText editText, boolean focused){
        if (!focused){
            if (!isNumeric(editText.getText().toString())){
                Toast.makeText(CalorieView.this, "请输入正确的数字！", Toast.LENGTH_SHORT).show();
                editText.setText("");
                return false;
            } else {
                editText.setText(String.valueOf(Integer.parseInt(editText.getText().toString())));
                return true;
            }
        } else {
            return true;
        }
    }

}