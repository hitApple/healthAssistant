package com.example.app3;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.litepal.LitePal;

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
    private Boolean isTF = true;

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
    public static String picPath;

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalcenter);

        getPermissions();

        path = getApplicationContext().getFilesDir().getAbsolutePath() + "/avatar";
        createDir();
        picPath = path + "/avatar_" + MainActivity.mPhone + ".jpg";

        setItem1();

        final int[] healthCheckUpTrianglesIds = new int[]{R.id.check_out1_1, R.id.check_out2_1,
                R.id.check_out3_1, R.id.check_out4_1, R.id.check_out5_1, R.id.check_out6_1};
        final int[] healthCheckUpIds = new int[]{R.id.check_out1, R.id.check_out2,
                R.id.check_out3, R.id.check_out4, R.id.check_out5, R.id.check_out6};

        for (int i = 0; i < healthCheckUpIds.length; i++){
            findViewById(healthCheckUpIds[i]).setVisibility(View.GONE);
            findViewById(healthCheckUpTrianglesIds[i]).setVisibility(View.GONE);
        }

        homepage = findViewById(R.id.homepage);
        find = findViewById(R.id.find);
        plus = findViewById(R.id.plus);
        link = findViewById(R.id.link);
        me = findViewById(R.id.me);
        ms_img_show =  findViewById(R.id.personal_pic);

        btn_camera  =  findViewById(R.id.personal_take_photo);
        btn_picture =  findViewById(R.id.personal_photo_album);

        if(new File(picPath).exists()){
            ms_img_show.setImageURI(Uri.parse(picPath));
        }


//        ms_img_show.setImageURI(Uri.parse(picPath));

        personal_pic_2 =findViewById(R.id.personal_pic_2);


        homepage.setBackgroundColor(-1);
        find.setBackgroundColor(-1);
        link.setBackgroundColor(-1);
        me.setBackgroundColor(-3355444);
        personal_setting = findViewById(R.id.personal_setting);
        personal_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.app3.me.this, HealthCheckUp.class));
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
                if(!isTF){
                    isTF = true;
                    plus.animate().rotation(-90);
                    /*                findViewById(R.id.plus_ui2).setVisibility(View.VISIBLE);*/
                    findViewById(R.id.plus_ui2).clearAnimation();
                    ObjectAnimator.ofFloat(findViewById(R.id.plus_ui2),
                            "translationY",
                            (float) (MainActivity.mScreenHeight / 2))
                            .setDuration(500)
                            .start();

                }else{
                    isTF = false;
                    plus.animate().rotation(45);
                    //               findViewById(R.id.plus_ui2).setVisibility(View.VISIBLE);
                    findViewById(R.id.plus_ui2).clearAnimation();
                    ObjectAnimator.ofFloat(findViewById(R.id.plus_ui2),
                            "translationY",
                            (float) -(MainActivity.mScreenHeight / 2))
                            .setDuration(500)
                            .start();
                }
            }
        });
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.app3.me.this, ContactBaiduMap.class));


            }
        });
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        findViewById(R.id.me_my_friend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.app3.me.this, FriendView.class));
            }
        });

        findViewById(R.id.me_my_hospital).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(com.example.app3.me.this, FavouritesHospitalView.class));
            }
        });

        findViewById(R.id.username).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.setUerName).setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.setUerName).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.determine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView)findViewById(R.id.username)).setText(((EditText)findViewById(R.id.editName)).getText());
                String str = ((EditText)findViewById(R.id.editName)).getText().toString();
                findViewById(R.id.setUerName).setVisibility(View.GONE);
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

    public void setUnable(){
        findViewById(R.id.full).setClickable(false);
        findViewById(R.id.personal_1).setClickable(false);
        findViewById(R.id.personal_setting).setClickable(false);
        findViewById(R.id.message).setClickable(false);
//        findViewById(R.id.message_unread).setClickable(false);
        findViewById(R.id.personal_2).setClickable(false);
        findViewById(R.id.personal_pic).setClickable(false);
        findViewById(R.id.username).setClickable(false);
        findViewById(R.id.personal_3).setClickable(false);
//        findViewById(R.id.personal_4).setClickable(false);
        findViewById(R.id.par).setClickable(false);
//        findViewById(R.id.my_hospital).setClickable(false);
//        findViewById(R.id.doctor2).setClickable(false);
//        findViewById(R.id.my_friend).setClickable(false);
//        findViewById(R.id.doctor4).setClickable(false);
        findViewById(R.id.exit_login).setClickable(false);
        findViewById(R.id.homepage_find_bottom).setClickable(false);
        findViewById(R.id.plus_ui2).setClickable(false);
        findViewById(R.id.head_portrait).setClickable(false);
        findViewById(R.id.personal_pic_2).setClickable(false);
        findViewById(R.id.personal_take_photo).setClickable(false);
        findViewById(R.id.personal_photo_album).setClickable(false);
        findViewById(R.id.photo_cancel).setClickable(false);
        findViewById(R.id.setUerName).setClickable(false);
        findViewById(R.id.editName).setClickable(false);
        findViewById(R.id.cross).setClickable(false);
        findViewById(R.id.determine).setClickable(false);
    }


    private void setItem1(){

        HealthCheckUpTable healthCheckUpTable = LitePal.where("phone = ?",
                MainActivity.mPhone).findFirst(HealthCheckUpTable.class);

        if(healthCheckUpTable == null){
            return;
        }

        String weight = healthCheckUpTable.getUser_weight() + "kg";
        String diastolic = healthCheckUpTable.getUser_diastolic_blood_pressure() + "mmHg";
        String systolic = healthCheckUpTable.getUser_systolic_lood_pressure() + "mmHg";
        String heartbeats = healthCheckUpTable.getUser_heartbeats_per_minute() + "/min";
        String urination = healthCheckUpTable.getUser_number_of_urination_per_day() + "/day";
        String sleepTime = healthCheckUpTable.getUser_daily_sleep_time() + "/day";

        TextView text1 = findViewById(R.id.me_list1_2);
        TextView text2 = findViewById(R.id.me_list2_2);
        TextView text3 = findViewById(R.id.me_list3_2);
        TextView text4 = findViewById(R.id.me_list4_2);
        TextView text5 = findViewById(R.id.me_list5_2);
        TextView text6 = findViewById(R.id.me_list6_2);

        text1.setText(weight);
        text2.setText(diastolic);
        text3.setText(systolic);
        text4.setText(heartbeats);
        text5.setText(urination);
        text6.setText(sleepTime);
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