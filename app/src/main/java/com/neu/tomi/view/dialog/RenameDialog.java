package com.neu.tomi.view.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RenameDialog extends AppCompatActivity {
    private DataItems mDataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rename);
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final EditText etName = (EditText) findViewById(R.id.etName);
        Button btOk = (Button) findViewById(R.id.btOk);
        mDataItems = new DataItems(RenameDialog.this);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if (name != null) {
                    String tmp = name.replace(" ", "");
                    if (!tmp.isEmpty()) {
                        mDataItems.setIsNewName(true);
                        if (HttpRequest.isOnline(RenameDialog.this)) {
                            new Rename(name).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        }
                            setNewNameInDevice(name);
                    } else {
                        Toast.makeText(RenameDialog.this, "Name not valid", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();
                }
            }
        });
    }

    private void setNewNameInDevice(String name) {
        mDataItems.setNameOfUser(name);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("RENAME",name);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    class Rename extends AsyncTask<Void, Void, JSONObject> {
        private String mName;

        public Rename(String name) {
            mName = name;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.Rename(RenameDialog.this, mName);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getBoolean("state")) {
                        mDataItems.setIsNewName(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(jsonObject);
        }
    }
}
