package com.example.adamlieu.steam_store_web_api_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Adam Lieu on 3/1/2017.
 */

public class WebViewController extends Activity {
    private WebView webView;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steam_webview);
        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");

        webView = (WebView) findViewById(R.id.steam_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

}
