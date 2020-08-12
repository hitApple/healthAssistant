package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import static android.webkit.WebSettings.*;

public class HospitalWebView extends AppCompatActivity {

    private static final String TAG = "HospitalWebView";
    private WebView webView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_detail);
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
        webView.loadUrl("http://www.wfph.cn/");

    }


    public void loadUrl(String url)
    {
        if(webView != null)
        {
            webView.loadUrl(url);
            webView.reload();
        }
    }
}