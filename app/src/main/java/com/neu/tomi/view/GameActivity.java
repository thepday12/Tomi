package com.neu.tomi.view;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

public class GameActivity extends AppCompatActivity {
    private WebView wvContent;
    private ProgressBar pbLoadWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wvContent = (WebView) findViewById(R.id.wvContent);
        pbLoadWeb = (ProgressBar) findViewById(R.id.pbLoadWeb);
        wvContent.setWebViewClient(new myWebClient());
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.clearHistory();

        String url = getIntent().getStringExtra(DataItems.GAME_LINK_KEY);
        String name = getIntent().getStringExtra(DataItems.GAME_NAME_KEY);
        if (url.contains("?"))
            url += "&phone_id=" + DataItems.getPhoneId(GameActivity.this);
        else
            url += "?phone_id=" + DataItems.getPhoneId(GameActivity.this);
        setTitle(name);
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
                new UpdatePtsAndXP().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                return true;
            case R.id.itemReload:
                pbLoadWeb.setVisibility(View.VISIBLE);
                wvContent.reload();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
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
            pbLoadWeb.setVisibility(View.GONE);
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
                new UpdatePtsAndXP().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    class UpdatePtsAndXP extends AsyncTask<Void, Void, JSONObject> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(GameActivity.this, null,
                    "Loading..", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.updatePointAndXp(GameActivity.this);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            mDialog.dismiss();
            if (result != null) {
                DataItems dataItems=new DataItems(GameActivity.this);
                try {
                   dataItems.setPoint(0, result.getInt("XP"), result.getInt("Point") * -1);
                    dataItems.clearActionList();
                    dataItems.clearUseItemList();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            finish();
            super.onPostExecute(result);
        }
    }

}
