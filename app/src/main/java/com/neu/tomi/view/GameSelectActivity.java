package com.neu.tomi.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.adapter.GameSelectAdapter;
import com.neu.tomi.object.GameSelectObject;
import com.neu.tomi.ultity.DataItems;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thep on 12/1/2015.
 */

public class GameSelectActivity extends FragmentActivity {
    private GridView gvGame;
    private TextView tvError;
    private boolean isResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.95);
        int screenheight = (int) (metrics.heightPixels * 0.65);
        setContentView(R.layout.activity_game_select);
        getWindow().setLayout(screenWidth, screenWidth);
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        isResume = true;
        super.onPause();
    }


    private void init() {
        gvGame = (GridView) findViewById(R.id.gvGame);
        tvError = (TextView) findViewById(R.id.tvError);
        new LoadGameList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    class LoadGameList extends AsyncTask<Void, Void, JSONArray> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(GameSelectActivity.this, null,
                    "Loading..", true);
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            return DataItems.getGameList(GameSelectActivity.this);
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            mDialog.dismiss();
            if (result != null) {
                List<GameSelectObject> gameSelectObjects = new ArrayList<>();
                if (result.length() > 0) {
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            gameSelectObjects.add(new GameSelectObject(result.getJSONObject(i)));
                        } catch (JSONException e) {

                        }
                    }
                }
                if (gameSelectObjects.size() > 0) {
                    gvGame.setVisibility(View.VISIBLE);
                    tvError.setVisibility(View.GONE);
                    GameSelectAdapter adapter = new GameSelectAdapter(GameSelectActivity.this, gameSelectObjects);
                    gvGame.setAdapter(adapter);
                    gvGame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            GameSelectObject object = (GameSelectObject) parent.getAdapter().getItem(position);
                            Intent intent= new Intent(GameSelectActivity.this,GameActivity.class);
                            intent.putExtra(DataItems.GAME_LINK_KEY,object.getLink());
                            intent.putExtra(DataItems.GAME_NAME_KEY,object.getName());
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    noGame();
                }
            } else {
                Toast.makeText(GameSelectActivity.this, "Connect server fail", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }
    }

    private void noGame() {
        gvGame.setVisibility(View.GONE);
        tvError.setVisibility(View.VISIBLE);
    }
}
