package com.example.app3;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class me extends AppCompatActivity {

    //请求码
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;

    //需要请求的权限
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageView homepage;
    private ImageView find;
    private ImageView plus;
    private ImageView link;
    private ImageView me;

    private ImageView ms_img_show;
    private ImageView btn_camera;
    private ImageView personal;
    private ImageView btn_picture;

    private Bitmap bitmap = null;

    private String path;
    private String picPath;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalcenter);

        getPermissions();

        path = getApplicationContext().getFilesDir().getAbsolutePath() + "/avatar";
        createDir();
        picPath = path + "/avatar_" + MainActivity.mPhone + ".jpg";

        homepage = findViewById(R.id.homepage);
        find = findViewById(R.id.find);
        plus = findViewById(R.id.plus);
        link = findViewById(R.id.link);
        me = findViewById(R.id.me);
        ms_img_show = (ImageView) findViewById(R.id.personal_pic);
        btn_camera  = (ImageView) findViewById(R.id.camera);
        btn_picture = (ImageView) findViewById(R.id.picture);
        ms_img_show.setImageURI(Uri.parse(picPath));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        homepage.getLayoutParams().width= width/7;
        homepage.getLayoutParams().height= width/7;
        find.getLayoutParams().width= width/7;
        find.getLayoutParams().height= width/7;
        plus.getLayoutParams().width= width/7;
        plus.getLayoutParams().height= width/7;
        link.getLayoutParams().width= width/7;
        link.getLayoutParams().height= width/7;
        me.getLayoutParams().width= width/7;
        me.getLayoutParams().height= width/7;

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(me.this,HomePage.class));
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(me.this,HomePage_find.class));
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(me.this,plus.class));
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(me.this,link.class));
            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(me.this,me.class));
            }
        });


        //点击照相按钮
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 启动相机
                startActivityForResult(photoPicker, CAMERA_REQUEST_CODE);
            }
        });

        //点击图片按钮
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryPicker = new Intent(Intent.ACTION_PICK);    // 选择数据
                galleryPicker.setType("image/*");                               // 获取所有本地图片
                startActivityForResult(galleryPicker, GALLERY_REQUEST_CODE);
            }
        });
    }

    private void createDir(){
        File file = new File(path);
        if (!file.exists()){
            file.mkdir();
        }
    }

    private void saveBitmapFile(Bitmap bitmap){
        File currentImageFile = new File(picPath);
        if (currentImageFile.exists()){
            currentImageFile.delete();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(currentImageFile));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用相册或相机之后会自动调用本方法，用于对图像分别的进行处理
     * @param requestCode 请求码，请参考本类的固定值全局变量
     * @param resultCode 返回码，返回OK说明正常返回
     * @param data 返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        if (data == null){
                            return;
                        }
                        final Uri imageUri = data.getData();
                        if (imageUri == null){
                            return;
                        }
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        bitmap = BitmapFactory.decodeStream(imageStream);
                        saveBitmapFile(bitmap);
                        ms_img_show.setImageBitmap(bitmap);
                        bitmap = null;

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    Bundle bundle = null;
                    if (data != null) {
                        bundle = data.getExtras();
                    } else {
                        return;
                    }
                    if (bundle != null) {
                        bitmap = (Bitmap) bundle.get("data");
                        saveBitmapFile(bitmap);
                        ms_img_show.setImageBitmap(bitmap);
                        bitmap = null;
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取本活动需要的权限
     */
    private void getPermissions() {

        ArrayList<String> permissionList = new ArrayList<>();

        for (String permission : PERMISSIONS){
            if (ContextCompat.checkSelfPermission(me.this,
                    permission) != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(me.this, permissions, PERMISSION_REQUEST_CODE);
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




/*        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ms_img_show.setBackground();
            }
        });*/



/*        btn_camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                File dir = getExternalFilesDir("pictures");
                if(dir.exists()){
                    dir.mkdirs();
                }
                currentImageFile = new File(dir,System.currentTimeMillis() + ".jpg");
                if(!currentImageFile.exists()){
                    try {
                        currentImageFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Uri imageUri = null;
                if (Build.VERSION.SDK_INT>=24)
                {
                    imageUri = FileProvider.getUriForFile(me.this,getPackageName() + ".fileprovider",currentImageFile);
                }
                else
                {
                    imageUri = Uri.fromFile(currentImageFile);
                }

                // 调用系统相机
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                it.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);
            }

            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == Activity.DEFAULT_KEYS_DIALER) {
                    ms_img_show.setImageURI(Uri.fromFile(currentImageFile));
                }
            }
        });*/








/*    public void onClick(View v) {
        File dir = getExternalFilesDir("pictures");
        if(dir.exists()){
            dir.mkdirs();
        }
        currentImageFile = new File(dir,System.currentTimeMillis() + ".jpg");
        if(!currentImageFile.exists()){
            try {
                currentImageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri imageUri = null;
        if (Build.VERSION.SDK_INT>=24)
        {
            imageUri = FileProvider.getUriForFile(me.this,getPackageName() + ".fileprovider",currentImageFile);
        }
        else
        {
            imageUri = Uri.fromFile(currentImageFile);
        }

        // 调用系统相机
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.DEFAULT_KEYS_DIALER) {
            ms_img_show.setImageURI(Uri.fromFile(currentImageFile));
        }
    }*/