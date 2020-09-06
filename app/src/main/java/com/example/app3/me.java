package com.example.app3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class me extends BaseActivity {

    //请求码
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int TAKE_PHOTO = 1;
    private static final int PHOTO_ALBUM = 2;
    private static final int PICTURE_CUT = 3;

    //需要请求的权限
    private static final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private RelativeLayout homepage;
    private RelativeLayout find;
    private RelativeLayout plus;
    private RelativeLayout link;
    private RelativeLayout me;

    private CircleImageView ms_img_show;//Circle
    private  ImageView personal_pic_2;
    private  ImageView personal_setting;

    private Button btn_camera;
    private Button btn_picture;

    private String path;
    private String picPath;



    @SuppressLint("WrongViewCast")
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
        ms_img_show =  findViewById(R.id.personal_pic);

        btn_camera  =  findViewById(R.id.personal_take_photo);
        btn_picture =  findViewById(R.id.personal_photo_album);

        ms_img_show.setImageURI(Uri.parse(picPath));

        personal_pic_2 =findViewById(R.id.personal_pic_2);


        homepage.setBackgroundColor(-1);
        find.setBackgroundColor(-1);
        link.setBackgroundColor(-1);
        me.setBackgroundColor(-3355444);
        personal_setting = findViewById(R.id.personal_setting);
        personal_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(me.this, HealthCheckUp.class));
            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.app3.me.this, HomePage_find.class));

            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.app3.me.this, HomePage.class));


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
                startActivity(new Intent(me.this, ContactBaiduMap.class));


            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        findViewById(R.id.cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.full).setVisibility(View.VISIBLE);
                findViewById(R.id.plus_ui).setVisibility(View.GONE);
            }
        });


        findViewById(R.id.photo_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.head_portrait).setVisibility(View.GONE);
                findViewById(R.id.full).setVisibility(View.VISIBLE);
            }
        });

        ms_img_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.head_portrait).setVisibility(View.VISIBLE);
                personal_pic_2.setImageURI(Uri.parse(picPath));
                findViewById(R.id.full).setVisibility(View.GONE);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, PHOTO_ALBUM);
            }
        });


        Button exitLogin = findViewById(R.id.exit_login);
        exitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.finishAll();
                SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                editor.putBoolean("check_auto", false);
                editor.putBoolean("auto", false);
                editor.apply();
                startActivity(new Intent(com.example.app3.me.this, MainActivity.class));
            }
        });

        LinearLayout myFriend = findViewById(R.id.doctor3);
        myFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(me.this, FriendView.class));
            }
        });

    }

    //如果想在Activity中得到新打开Activity 关闭后返回的数据，需要使用系统提供的
    //startActivityForResult(Intent intent, int requestCode)方法打开新的Activity，0
    //新的Activity 关闭后会向前面的Activity传回数据，为了得到传回的数据，
    //必须在前面的Activity中重写onActivityResult(int requestCode, int resultCode, Intent data)方法。

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
            case TAKE_PHOTO:
                Bitmap bitmap = data.getParcelableExtra("data");
                startPhotoZoom(Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null)), 150);
                break;

            case PHOTO_ALBUM:
                if (data != null)
                    startPhotoZoom(data.getData(), 150);
                break;

            case PICTURE_CUT:
                if (data != null)
                    setPicToView(data);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    //将图片显示到布局中
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            saveBitmapFile((Bitmap) picdata.getExtras().get("data"));
            ms_img_show.setImageBitmap((Bitmap) picdata.getExtras().get("data"));

            startActivity(new Intent(com.example.app3.me.this, com.example.app3.me.class));
        }
    }


    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", size);// 输出图片大小
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        Log.e("zoom", "begin1");
        //如果是调用android自带的intent，就不需要finish。不然要是要finish的
        startActivityForResult(intent, PICTURE_CUT);
    }










    /*******************************************************************************************/















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
     * 获取本活动需要的权限
     */
    private void getPermissions() {

        ArrayList<String> permissionList = new ArrayList<>();

        for (String permission : PERMISSIONS){
            if (ContextCompat.checkSelfPermission(com.example.app3.me.this,
                    permission) != PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(com.example.app3.me.this, permissions, PERMISSION_REQUEST_CODE);
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

    /**
     * 退出程序
     */
/*
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
*/

    long boo = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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










    /***********************测********************************试****************************/










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