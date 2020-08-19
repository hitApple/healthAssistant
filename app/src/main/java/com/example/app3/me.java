package com.example.app3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class me extends AppCompatActivity {
    private ImageView homepage;
    private ImageView find;
    private ImageView plus;
    private ImageView link;
    private ImageView me;

    private ImageView ms_img_show;
    private ImageView btn_camera;
    private ImageView personal;

    //定义一个保存图片的 File 变量
    private File currentImageFile = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalcenter);

        homepage = findViewById(R.id.homepage);
        find = findViewById(R.id.find);
        plus = findViewById(R.id.plus);
        link = findViewById(R.id.link);
        me = findViewById(R.id.me);
        ms_img_show = (ImageView) findViewById(R.id.personal_pic);
        btn_camera  = (ImageView) findViewById(R.id.camera);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        findViewById(R.id.homepage).getLayoutParams().width= width/7;
        findViewById(R.id.homepage).getLayoutParams().height= width/7;
        findViewById(R.id.find).getLayoutParams().width= width/7;
        findViewById(R.id.find).getLayoutParams().height= width/7;
        findViewById(R.id.plus).getLayoutParams().width= width/7;
        findViewById(R.id.plus).getLayoutParams().height= width/7;
        findViewById(R.id.link).getLayoutParams().width= width/7;
        findViewById(R.id.link).getLayoutParams().height= width/7;
        findViewById(R.id.me).getLayoutParams().width= width/7;
        findViewById(R.id.me).getLayoutParams().height= width/7;

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



        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(it,Activity.DEFAULT_KEYS_DIALER);
            }

            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if(requestCode == Activity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    ms_img_show.setImageBitmap(bitmap);
                }
            }
        });
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