package com.androiddev.twitter;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;


public class PrivacyPolicyActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        WebView myWebView =  findViewById(R.id.web);
        myWebView.loadUrl("https://sites.google.com/view/entertaininglogixapps/home");
        myWebView.setBackgroundColor(Color.TRANSPARENT);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                if(progress>5)
                    findViewById(R.id.progress_view).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public AssetManager getAssets() {
        return getResources().getAssets();
    }
}