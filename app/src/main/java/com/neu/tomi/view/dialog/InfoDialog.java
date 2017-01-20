package com.neu.tomi.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;

import org.json.JSONException;
import org.json.JSONObject;

public class InfoDialog extends AppCompatActivity {
    private DataItems mDataItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_infor);
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final int bonusType= getIntent().getIntExtra(DataItems.EXTRA_BONUS_TYPE_KEY,1);
        final LinearLayout llContent = (LinearLayout) findViewById(R.id.llContent);
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPhone = (EditText) findViewById(R.id.etPhone);
        Button btOk = (Button) findViewById(R.id.btOk);
        if(bonusType<0){
            llContent.setVisibility(View.GONE);
            btOk.setText("OK");
        }
        mDataItems = new DataItems(InfoDialog.this);


        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bonusType>0) {
                    String name = etName.getText().toString();
                    String email = etEmail.getText().toString();
                    String phone = etPhone.getText().toString();
                    if (!name.isEmpty()) {
                        if (Global.isValidEmail(email)) {
                            if (!phone.isEmpty()) {
                                View view = getCurrentFocus();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                new UpdateInfo(name,email,phone).execute();
                            } else {
                                etPhone.requestFocus();
                                etPhone.setError(getString(R.string.edit_text_null_value));
                            }
                        } else {
                            etEmail.setError(getString(R.string.edit_text_email_invalid));
                            etEmail.requestFocus();
                        }
                    } else {
                        etName.setError(getString(R.string.edit_text_null_value));
                        etName.requestFocus();
                    }
                }else{
                    mDataItems.setUpdateInfo(true);
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

    class UpdateInfo extends AsyncTask<Void, Void, JSONObject> {
        private String mName;
        private String mEmail;
        private String mPhone;
        private ProgressDialog mDialog;

        public UpdateInfo(String name,String email, String phone) {
            mName = name;
            mEmail = email;
            mPhone = phone;
            mDialog = ProgressDialog.show(InfoDialog.this, null,
                    "Loading..", true);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.updateInfoGift(mName,mEmail,mPhone,InfoDialog.this);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mDialog.dismiss();
            if (jsonObject != null) {
                try {
                    boolean state=jsonObject.getBoolean("state");
                    if (state) {
                        mDataItems.setUpdateInfo(true);
                    }else{
                        mDataItems.setUpdateInfo(false);
                    }
                    showDialogState(jsonObject.getString("error_description"),state);
                } catch (JSONException e) {
                    Toast.makeText(InfoDialog.this, R.string.error_null_value, Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(InfoDialog.this, R.string.error_null_value, Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(jsonObject);
        }

        private void showDialogState(String errDescription, final boolean state) {
            if (!errDescription.isEmpty()) {
                final Dialog dialog = new Dialog(InfoDialog.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_use_promotion_success);

                TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
                TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
                Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
                tvContent.setText(errDescription);
                if(!state){
                    tvTitle.setText("Error!");
                    tvTitle.setTextColor(Color.parseColor("#cc0000"));
                }


                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(state){
                            finish();
                        }
                    }
                });
                dialog.show();
            }
        }

    }
}
