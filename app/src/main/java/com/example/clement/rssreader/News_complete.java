package com.example.clement.rssreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class News_complete extends AppCompatActivity {
WebView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_complete1);

        view= (WebView)findViewById(R.id.webview);
        view.getSettings().setLoadsImagesAutomatically(true);
        view.getSettings().setJavaScriptEnabled(true);
        view.setScrollBarStyle(view.SCROLLBARS_INSIDE_OVERLAY);
        Bundle bundle=getIntent().getExtras();
        view.loadUrl(bundle.getString("link"));
           //view.loadUrl("http://www.sciencemag.org/rss/news_current.xml");

    }


}
