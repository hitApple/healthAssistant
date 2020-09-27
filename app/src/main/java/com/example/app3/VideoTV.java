package com.example.app3;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class VideoTV extends BaseActivity {

    public static String PATH = HomePage_find.PATH;
    private VideoView videoView;
    private TextView btn_pop;
    private ImageView jiazaidonghua;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videotv);

        jiazaidonghua = (ImageView) findViewById(R.id.jiaZaiDongHua);
        Glide.with(this).load(R.drawable.donghua2).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(jiazaidonghua);


        btn_pop  = findViewById(R.id.chooseTV2);

        btn_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(VideoTV.this,btn_pop);
                popup.getMenuInflater().inflate(R.menu.menu_pop, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.TY1:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8";
                                break;
                            case R.id.TY2:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv2hd.m3u8";
                                break;
                            case R.id.TY3:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8";
                                break;
                            case R.id.TY4:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv4hd.m3u8";
                                break;
                            case R.id.TY5:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8";
                                break;
                            case R.id.TY6:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8";
                                break;
                            case R.id.TY7:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv7hd.m3u8";
                                break;
                            case R.id.TY8:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv8hd.m3u8";
                                break;
                            case R.id.TY9:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv9hd.m3u8";
                                break;
                            case R.id.TY10:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv10hd.m3u8";
                                break;
                            case R.id.TY11:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv12hd.m3u8";
                                break;
                            case R.id.TY12:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv14hd.m3u8";
                                break;
                            case R.id.TY13:
                                PATH = "http://ivi.bupt.edu.cn/hls/cctv17hd.m3u8";
                                break;
                            case R.id.TY14:
                                PATH = "http://ivi.bupt.edu.cn/hls/cgtnhd.m3u8";
                                break;
                            case R.id.TY15:
                                PATH = "http://ivi.bupt.edu.cn/hls/cgtndochd.m3u8";
                                break;
                            case R.id.TY16:
                                PATH = "http://ivi.bupt.edu.cn/hls/chchd.m3u8";
                                break;

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
        videoView = (VideoView) findViewById(R.id.videoView2);
        findViewById(R.id.startTV2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PATH != ""){
                    findViewById(R.id.startTV2).setVisibility(View.GONE);
                    findViewById(R.id.stopTV2).setVisibility(View.VISIBLE);
                    videoView.setVideoPath(PATH);
                    videoView.start();
                }
            }
        });
        findViewById(R.id.stopTV2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PATH != ""){
                    findViewById(R.id.startTV2).setVisibility(View.VISIBLE);
                    findViewById(R.id.stopTV2).setVisibility(View.GONE);
                    videoView.pause();
                    videoView.start();
                }
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置videoView全屏播放
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置videoView横屏播放

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoView
                .getLayoutParams(); // 取控当前的布局参数
        layoutParams.height = MainActivity.mScreenHeight;//设置 当控件的高强
        layoutParams.width = MainActivity.mScreenWidth;
        videoView.setLayoutParams(layoutParams); // 使设置好的布局参数应用到控件
        if(!PATH.equals("")){
            findViewById(R.id.startTV2).setVisibility(View.GONE);
            findViewById(R.id.stopTV2).setVisibility(View.VISIBLE);
            videoView.setVideoPath(PATH);
            videoView.start();
        }

//        findViewById(R.id.quanping2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置videoView全屏播放
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置videoView横屏播放
//
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) videoView
//                        .getLayoutParams(); // 取控当前的布局参数
//                layoutParams.height = MainActivity.mScreenHeight;//设置 当控件的高强
//                layoutParams.width = MainActivity.mScreenWidth;
//                videoView.setLayoutParams(layoutParams); // 使设置好的布局参数应用到控件
//                findViewById(R.id.chooseTV2).setVisibility(View.GONE);
//                findViewById(R.id.startTV2).setVisibility(View.GONE);
//                findViewById(R.id.stopTV2).setVisibility(View.VISIBLE);
//                videoView.setVideoPath(PATH);
//                videoView.start();
//            }
//        });

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener(){
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //开始卡顿-----需要做一些处理(比如：显示加载动画，或者当前下载速度)
                        jiazaidonghua.setVisibility(View.VISIBLE);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //卡顿结束   (隐藏加载动画，或者加载速度)
                        jiazaidonghua.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        }) ;

    }




}
