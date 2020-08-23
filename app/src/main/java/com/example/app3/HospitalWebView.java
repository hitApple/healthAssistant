package com.example.app3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

import java.util.List;

import static android.webkit.WebSettings.ZoomDensity;

public class HospitalWebView extends BaseActivity {

    private static final String TAG = "HospitalWebView";
    private TextView nameTextView;
    private WebView webView;
    private ProgressBar progressBar;
    private boolean isFound = false;
    private String url;
    public static String hospitalName;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_detail);

        try {
            url = getIntent().getStringExtra("医院名称");
            if (url == null || url.equals("")){
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        hospitalName = url;

        Button contactDoctor = (Button) findViewById(R.id.contact_doctor);
        contactDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HospitalWebView.this, DoctorsOfHospital.class);
                intent.putExtra("医院名称", hospitalName);
                startActivity(intent);
            }
        });

        nameTextView = (TextView) findViewById(R.id.hospital_title);
        nameTextView.setText(url);

        List<HospitalWeb> list = LitePal.findAll(HospitalWeb.class);

        for (HospitalWeb hospitalWeb : list){
            if ( hospitalWeb.getName() != null && (hospitalWeb.getName().contains(url) ||
                    url.contains(hospitalWeb.getName())) ){
                url = hospitalWeb.getWebsite();
                isFound = true;
                break;
            }
        }

        if (!isFound){
            TextView isFoundTextView = (TextView) findViewById(R.id.tip_text_view);
            isFoundTextView.setVisibility(View.VISIBLE);
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            return;
        }

        webView = (WebView) findViewById(R.id.hospital_web_view);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        if(webView == null)
        {
            return;
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);//support zoom
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d(TAG, "densityDpi = " + mDensity);
        if (mDensity == 240) {
            webSettings.setDefaultZoom(ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            webSettings.setDefaultZoom(ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            webSettings.setDefaultZoom(ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            webSettings.setDefaultZoom(ZoomDensity.FAR);
        }
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view,String url)
            {
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(url);

    }


//    public void loadUrl(String url)
//    {
//        if(webView != null)
//        {
//            webView.loadUrl(url);
//            webView.reload();
//        }
//    }
}