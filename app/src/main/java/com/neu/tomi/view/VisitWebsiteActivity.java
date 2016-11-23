package com.neu.tomi.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.neu.tomi.R;
import com.neu.tomi.ultity.DataItems;

public class VisitWebsiteActivity extends AppCompatActivity {
    private WebView wvContent;
    private ProgressBar pbLoadWeb;
    private int countRedirect=0;
    private boolean normal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_website);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wvContent = (WebView) findViewById(R.id.wvContent);
        pbLoadWeb = (ProgressBar) findViewById(R.id.pbLoadWeb);
        wvContent.setWebViewClient(new myWebClient());
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.clearHistory();

        String url = getIntent().getStringExtra(DataItems.PROMOTION_REDIRECT_KEY);
        normal = getIntent().getBooleanExtra(DataItems.STATUS_REDIRECT_KEY,false);
        wvContent.loadUrl(url);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.visit_website_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.itemReload:
                pbLoadWeb.setVisibility(View.VISIBLE);
                countRedirect=1;
                wvContent.reload();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            pbLoadWeb.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(normal||countRedirect>0) {
                pbLoadWeb.setVisibility(View.GONE);
            }
            countRedirect++;
            super.onPageFinished(view, url);

        }
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (wvContent.canGoBack()) {
                wvContent.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
