package com.example.app3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    public static String mPhone = "1";;
    public static int mScreenWidth = 0;
    public static int mScreenHeight = 0;

    private boolean isPaused = false;

    private ImageView SignIn;
    private ImageView LogIn;

    private ImageView eyeview_on;
    private ImageView eyeview_off;
    private EditText password;
    private EditText user;
    private CheckBox rememberPassword;
    private CheckBox autoLogin;

    static int count=1;

    private Context mContext;
    private ViewFlipper vflp_help;
    private final int[] resId = {R.drawable.guide_1,R.drawable.guide_2,
            R.drawable.guide_3,R.drawable.guide_4};
    private final int[] bgIds = {R.drawable.main_activity_bg1, R.drawable.main_activity_bg2,
            R.drawable.main_activity_bg3, R.drawable.main_activity_bg4,
            R.drawable.main_activity_bg5};

    private final static int MIN_MOVE = 200;   //最小距离
    private MsGestureListener mgListener;
    private GestureDetector mDetector;
    private Button go_SignIn;
    private Button go_LogIn;
    private ImageView bgImage;
    private Timer bgTimer;
    private BGChangeTimerTask timerTask;
    public static String mPicPath;
    private int currentId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        importDatabases();
        Connector.getDatabase();
        mPicPath = getApplicationContext().getFilesDir().getAbsolutePath() + "/avatar" + "/avatar_" + MainActivity.mPhone + ".jpg";
        bgImage = findViewById(R.id.main_activity_bg);
        currentId = new Random().nextInt(4);
        bgImage.setImageResource(bgIds[currentId]);
        user = findViewById(R.id.user);
        password =  findViewById(R.id.password);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;

//        Log.d(TAG, "onCreate: " + "MainActivity".equals(this.getLocalClassName()));

        SharedPreferences preferences = getSharedPreferences("user_info",   MODE_PRIVATE);
        user.setText(preferences.getString("user",""));
        password.setText(preferences.getString("password", ""));

        SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
        if (preferences.getBoolean("isFirst", true)){
            editor.putBoolean("isFirst", true);
        }
        editor.apply();
        if (!preferences.getBoolean("isFirst", false)){
            findViewById(R.id.guide).setVisibility(View.GONE);
            findViewById(R.id.full).setVisibility(View.VISIBLE);
            editor.putBoolean("isFirst", false);
        } else{
            findViewById(R.id.main_activity_skip).setVisibility(View.VISIBLE);
            findViewById(R.id.guide).setVisibility(View.VISIBLE);
            findViewById(R.id.full).setVisibility(View.GONE);
            editor.putBoolean("isFirst", false);
        }
        editor.apply();
        eyeview_on = findViewById(R.id.eyeview_on);
        eyeview_off = findViewById(R.id.eyeview_off);
        eyeview_on.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                findViewById(R.id.eyeview_on).setVisibility(View.GONE);
                findViewById(R.id.eyeview_off).setVisibility(View.VISIBLE);
                password.setInputType(0x81);//密码不可见
            }
        });
        eyeview_off.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                findViewById(R.id.eyeview_off).setVisibility(View.GONE);
                findViewById(R.id.eyeview_on).setVisibility(View.VISIBLE);
                password.setInputType(0x90);//密码可见
            }
        });

        //登录按钮
        LogIn = findViewById(R.id.LogIn);
        LogIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startLogin();
            }
        });

        //注册
        SignIn = findViewById(R.id.SignIn);
        SignIn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this,Sign_In.class));
            }
        });

        findViewById(R.id.main_activity_find).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FindPassword.class));
            }
        });

        //checkbox
        rememberPassword = findViewById(R.id.remember_password);
        autoLogin = findViewById(R.id.auto_login);

        if (preferences.getBoolean("check_remember", false)){
            rememberPassword.setChecked(true);
        } else {
            rememberPassword.setChecked(false);
        }

        if (preferences.getBoolean("check_auto", false)){
            autoLogin.setChecked(true);
        } else {
            autoLogin.setChecked(false);
        }

        if (preferences.getBoolean("auto", false)){
            startLogin();
        }

//        /*********************************测试用button**************************************/
//        Button button1 = (Button) findViewById(R.id.jumpToBaiduMap);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, ContactBaiduMap.class));
//            }
//        });
//        Button button2 = (Button) findViewById(R.id.jumpToCalorieView);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, CalorieView.class));
//            }
//        });
//        /*********************************测试用button**************************************/


        mContext = MainActivity.this;
        //实例化 SimpleOnGestureListener 与 GestureDetector 对象
        mgListener = new MsGestureListener();
        mDetector = new GestureDetector(this, mgListener);
        vflp_help = (ViewFlipper) findViewById(R.id.guide);

/*        //动态导入添加子View
        for(int i = 0;i < resId.length;i++){
            vflp_help.addView(getImageView(resId[i]));
        }*/

        findViewById(R.id.go_LogIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vflp_help.setVisibility(View.GONE);
                findViewById(R.id.full).setVisibility(View.VISIBLE);
                findViewById(R.id.main_activity_skip).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.main_activity_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vflp_help.setVisibility(View.GONE);
                findViewById(R.id.full).setVisibility(View.VISIBLE);
                findViewById(R.id.main_activity_skip).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.main_activity_contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("联系我们");
                dialog.setMessage("如果您在使用软件的过程中遇到问题，请练习我们");
                dialog.setCancelable(true);
                dialog.setPositiveButton("联系我们", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + "15689712036");
                        intent.setData(data);
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("我点错了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });
        bgTimer = new Timer();
//        timerTask = new BGChangeTimerTask();
//        bgTimer.schedule(timerTask, 1000L);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        timerTask = new BGChangeTimerTask();
        bgTimer.schedule(timerTask, 1000L);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;

    }

    /**
     * 将存储在assets文件夹中的数据库文件移动到相应位置
    **/
    private void importDatabases(){
        File file = new File("/data"
                + Environment.getDataDirectory().getAbsolutePath() + "/"
                + "com.example.app3" + "/databases" + "/HospitalWebsite.db");
        if (!file.isFile()){
            new DBManager(this).openDatabase();
        }

    }


    private static final String[] memberPhones = {
            "15689712036", "19861807360", "13184116753","15689711359","13954159704"
    };

    private void memberWelcome(String phone) throws InterruptedException {
        for (String string : memberPhones){
            if (phone.equals(string)){
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("欢迎！");
                dialog.setMessage("检测到您为健康助手的测试人员\n本程序在这里诚挚的欢迎您!\n感谢您为健康助手所做的突出贡献!");
                dialog.setCancelable(true);
                dialog.setPositiveButton("谢谢", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                break;
            }
        }
    }

    private void saveUserInfo(){
        SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
        editor.putString("user", user.getText().toString());
        if (rememberPassword.isChecked()){
            editor.putString("password", password.getText().toString());
            editor.putBoolean("check_remember", true);
        } else {
            editor.putString("password", "");
            editor.putBoolean("check_remember", false);
        }
        if (autoLogin.isChecked()){
            editor.putBoolean("auto", true);
            editor.putBoolean("check_auto", true);
        } else {
            editor.putBoolean("auto", false);
            editor.putBoolean("check_auto", false);
        }
        editor.apply();
    }

    private void startLogin(){
        String[] result = null;
        try {
            LoginClient loginClient = new LoginClient();
            loginClient.sendLogin(user.getText().toString(), password.getText().toString());
            long beginTime = System.currentTimeMillis();
            while (!loginClient.isFinish()){
                if (System.currentTimeMillis() - beginTime > 5000){
                    Toast.makeText(MainActivity.this, "连接超时，请重试",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            result = loginClient.getResultString().split("\n");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (Integer.parseInt(result[1]) != 0){
            Toast.makeText(MainActivity.this, result[0], Toast.LENGTH_LONG).show();
        }
        else{
            saveUserInfo();
            mPhone = user.getText().toString();
            startService(new Intent(MainActivity.this, LoginStatusService.class));
            Toast.makeText(MainActivity.this, result[0], Toast.LENGTH_SHORT).show();
            SharedPreferences preferences = getSharedPreferences("user_info",   MODE_PRIVATE);
            if (preferences.getBoolean("firstLogin", true)){
                SharedPreferences.Editor editor = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                editor.putBoolean("firstLogin", false);
                editor.apply();
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("首次登录");
                dialog.setMessage("检测到您为首次登录,是否立即填写您的个人健康信息,以方便填写?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, HealthCheckUp.class);
                        intent.putExtra("firstLogin", true);
                        finish();
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, HomePage_find.class);
                        intent.putExtra("phone", user.getText().toString());
                        finish();
                        startActivity(intent);
                    }
                });
                dialog.show();

            } else {
                Intent intent = new Intent(MainActivity.this, HomePage_find.class);
                finish();
                startActivity(intent);
            }


        }
    }

    //重写onTouchEvent触发MyGestureListener里的方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    private ImageView getImageView(int resId){
        ImageView img = new ImageView(this);
        img.setBackgroundResource(resId);
        return img;
    }

    //自定义一个GestureListener,这个是View类下的，别写错哦！！！
    private class MsGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
            if(e1.getX() - e2.getX() > MIN_MOVE){
                if(count <4){
                    count ++;
                    vflp_help.setInAnimation(mContext,R.anim.right_in);
                    vflp_help.setOutAnimation(mContext, R.anim.right_out);
                    vflp_help.showNext();
                }
            }else if(e2.getX() - e1.getX() > MIN_MOVE){
                if(count > 1){
                    vflp_help.setInAnimation(mContext,R.anim.left_in);
                    vflp_help.setOutAnimation(mContext, R.anim.left_out);
                    vflp_help.showPrevious();
                    count --;
                }

            }
            return true;
        }
    }

    class BGChangeTimerTask extends TimerTask {

        @Override
        public void run() {
            while (true){
                if (isPaused){
                    return;
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator.ofFloat(bgImage,
                                "scaleX", 1, 1.3f)
                                .setDuration(8000)
                                .start();
                        ObjectAnimator.ofFloat(bgImage,
                                "scaleY", 1, 1.3f)
                                .setDuration(8000)
                                .start();
                    }
                });
                try {
                    Thread.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator.ofFloat(bgImage,
                                "alpha", 1, 0)
                                .setDuration(1000)
                                .start();
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator.ofFloat(bgImage,
                                "alpha", 0, 1)
                                .setDuration(1000)
                                .start();
                        Random random = new Random();
                        int getId = 0;
                        while (true){
                            getId = random.nextInt(4);
                            if (getId != currentId){
                                currentId = getId;
                                break;
                            }
                        }
                        bgImage.setImageResource(bgIds[getId]);

                    }
                });

            }
        }
    }

}