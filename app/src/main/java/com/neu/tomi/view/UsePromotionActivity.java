package com.neu.tomi.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.object.ItemQTY;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.HttpRequest;
import com.neu.tomi.ultity.SqliteHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class UsePromotionActivity extends AppCompatActivity {
    private String imageUrl, expiry, description, note, beaconId, promotionId, promotionName;
    private TextView tvId, tvExpire;
    private WebView wvNote;
    private ImageView ivImageCode, ivMonkeyDance, ivPromotion;
    private ProgressBar pbImageProgress;
    private Button btManualRedemption;
    private DataItems mDataItems;
    private SqliteHelper mSqliteHelper;
    private ProgressDialog mDialog;
    private int codeType;
    private boolean isLoaded = false;
    private boolean isUsePromotion = false;
    private Dialog dialogCongratulations;
    private String bonusPromotions;
    private String errDescription;
    private PromtionObject mPromtionObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogCongratulations = new Dialog(UsePromotionActivity.this);
        new ExitScreen().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        mDataItems = new DataItems(UsePromotionActivity.this);
        mSqliteHelper = mDataItems.getSQLiteHelper();
        Intent intent = getIntent();
        promotionId = intent.getStringExtra(DataItems.PROMOTION_ID_KEY);
        beaconId = intent.getStringExtra(DataItems.BEACON_ID_KEY);
        codeType = intent.getIntExtra(DataItems.PROMOTION_TYPE_CODE_KEY, 0);
        if (codeType == 3) {
            new DeletePromotion(UsePromotionActivity.this, true, "",false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            promotionName = intent.getStringExtra(DataItems.PROMOTION_NAME_KEY);
            if (promotionName != null) {
                if (!promotionName.isEmpty()) {
                    setTitle(promotionName);
                } else {
                    setTitle("Promotion");
                }
            } else {
                setTitle("Promotion");
            }
            imageUrl = intent.getStringExtra(DataItems.PROMOTION_IMAGE_KEY);
            expiry = intent.getStringExtra(DataItems.PROMOTION_EXPIRY_KEY);
            description = intent.getStringExtra(DataItems.PROMOTION_DESCRIPTION_KEY);
            note = intent.getStringExtra(DataItems.PROMOTION_NOTE_KEY);
            setContentView(R.layout.activity_use_promotion);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            init();
            if(codeType==2)//chi co QR
                new DeletePromotion(UsePromotionActivity.this, true, "",false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            deletePromotion();
        }

    }

    private void deletePromotion() {
        PromtionObject promtionObject = mSqliteHelper.getPromotionWithID(promotionId);
        if (promtionObject.getTotal() > 1) {
            mSqliteHelper.updatePromotion(promotionId, promtionObject.getTotal() - 1);
        } else {
            File file = new File(promtionObject.getImageURL());
            file.delete();
            mSqliteHelper.deletePromotion(promotionId);
        }
    }

    @Override
    public void finish() {
        super.finish();
        sendBroadCastSuccess();

    }

    private void init() {
        tvId = (TextView) findViewById(R.id.tvId);
        wvNote = (WebView) findViewById(R.id.wvNote);
        tvExpire = (TextView) findViewById(R.id.tvExpire);
        ivImageCode = (ImageView) findViewById(R.id.ivImageCode);
        ivMonkeyDance = (ImageView) findViewById(R.id.ivMonkeyDance);
        ivPromotion = (ImageView) findViewById(R.id.ivPromotion);
        pbImageProgress = (ProgressBar) findViewById(R.id.pbImageProgress);
        btManualRedemption = (Button) findViewById(R.id.btManualRedemption);
        mPromtionObject = mSqliteHelper.getPromotionWithID(promotionId);

        if (codeType == 2) {
            btManualRedemption.setVisibility(View.GONE);
        } else if (codeType == 3) {
            finish();
        } else {
            btManualRedemption.setVisibility(View.VISIBLE);
        }
        String id = "NO. ";
        try {
            String s = beaconId.replace("28788_", "");
            id += s + "-" + promotionId;
        } catch (Exception e) {
        }
        tvId.setText(id);
        tvExpire.setText("Expiry: " + expiry);
        String text = "<html><head>"
                + "<style type=\"text/css\">body{color: #7b7b7b; background-color: #ffffffff;}"
                + "</style></head>"
                + "<body><small>";

        if (note.isEmpty())
            text += description;
        else
            text += note;
        text += "</small></body></html>";
        wvNote.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
        wvNote.setBackgroundColor(Color.TRANSPARENT);
        wvNote.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        Picasso.with(UsePromotionActivity.this).load(imageUrl).into(ivPromotion);
        String imageCode = DataItems.LINK_API + "/cms/pages/Tommi_API.php?Object=Promotions&act=QRCode";
        imageCode += "&phone_id=" + DataItems.getPhoneId(UsePromotionActivity.this);
        String tmp = beaconId;
        try {
            tmp = URLEncoder.encode(tmp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        imageCode += "&wid=" + tmp + "&promotion_id=" + promotionId;
        Log.e("RESPONSE_SERVER", imageCode);
        final String imageCodeF = imageCode;
        Picasso.with(UsePromotionActivity.this).load(imageCode).memoryPolicy(MemoryPolicy.NO_CACHE).into(ivImageCode, new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                pbImageProgress.setVisibility(View.GONE);
                isLoaded = true;
                ivImageCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(UsePromotionActivity.this, ShowDetailImageActivity.class);
                        i.putExtra(DataItems.PROMOTION_REDIRECT_KEY, "");
                        i.putExtra("IMAGE_URL", imageCodeF);
                        startActivity(i);
                    }
                });

            }
        });


        final AnimationDrawable myAnimationDrawable
                = (AnimationDrawable) ivMonkeyDance.getDrawable();

        ivMonkeyDance.post(
                new Runnable() {

                    @Override
                    public void run() {
                        myAnimationDrawable.start();
                    }
                });

        btManualRedemption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogCode();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showDialogExit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(dialogCongratulations.isShowing())
            finish();
        else
            showDialogExit();
//        super.onBackPressed();
    }

    private void showDialogCode() {
        final Dialog dialog = new Dialog(UsePromotionActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setContentView(R.layout.dialog_code);
        final EditText etCode = (EditText) dialog.findViewById(R.id.etCode);
        Button btOkCode = (Button) dialog.findViewById(R.id.btOkCode);
        btOkCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpRequest.isOnline(UsePromotionActivity.this)) {

                    String code = etCode.getText().toString();
                    if (code.length() > 0) {
                        new DeletePromotion(UsePromotionActivity.this, true, code,true).execute();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(UsePromotionActivity.this, "Please! Enter code first", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UsePromotionActivity.this, "Please! Turn on internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void showDialogExit() {
//        if(isLoaded) {
//            showDialogCongratulations();
//        }else {
        if(codeType!=2&&!isUsePromotion)
            new DeletePromotion(UsePromotionActivity.this, true, "",true).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else {
            final Dialog dialog = new Dialog(UsePromotionActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_confirm_exit);

            Button btOk = (Button) dialog.findViewById(R.id.btOk);
            Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
            // if button is clicked, close the custom dialog
            btOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogCongratulations();
                    dialog.dismiss();
                }
            });

            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
//        }
    }

    class ExitScreen extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(900000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }
    }

    class DeletePromotion extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private boolean mIsUse;
        private boolean mIsShowLoading;
        private String mCode;

        public DeletePromotion(Context context, boolean isUse, String code, boolean isShowLoading) {
            mContext = context;
            mIsUse = isUse;
            mCode = code;
            mIsShowLoading=isShowLoading;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bonusPromotions="";
            if (mIsShowLoading)
                mDialog = ProgressDialog.show(mContext, null,
                        "Loading..", true);
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            if (mIsUse) {
                return DataItems.UsePromotion(mContext, promotionId, mCode, beaconId, codeType);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if(mDialog.isShowing())
                mDialog.dismiss();
            if (mIsUse) {
                try {
                    if (result != null) {

                        isUsePromotion=true;
                        errDescription = result.getString("message");
                        if (result.getBoolean("state")) {
                            if(!mCode.isEmpty())
                                btManualRedemption.setVisibility(View.GONE);
                            int point = mPromtionObject.getUseBonus().getPoint();
                            int xp = mPromtionObject.getUseBonus().getXp();
                            bonusPromotions=result.getString("promotions");
                            List<ItemQTY> itemQTYList = new ArrayList<>();
                            JSONArray jsonArray = result.getJSONArray("item");
                            int length = 0;
                            if (jsonArray != null)
                                length = jsonArray.length();

                            if (length > 0) {
                                for (int i = 0; i < length; i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    ItemObject itemObject = new ItemObject(object, true);
                                    int qty = object.getInt("qty");
                                    itemQTYList.add(new ItemQTY(itemObject, qty));
                                }
                                try {
                                    new DownloadBonusItemActionUse(UsePromotionActivity.this, point, xp).execute(itemQTYList);
                                } catch (Exception e) {
                                    if(!mCode.isEmpty())
                                        finish();
                                }
                            } else {
                                mDataItems.addPoint(point, xp);
                                showDialogUseCode(errDescription, true);
                            }

                        } else {

                            if(!mCode.isEmpty()) {
                                showDialogUseCode(errDescription, false);
                            }
                        }
                    } else {
                        Toast.makeText(mContext, "Connect to server failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
//                    Toast.makeText(mContext, "Connect to server failed", Toast.LENGTH_SHORT).show();
                }

            }
            super.onPostExecute(result);
        }
    }
    private void showDialogUseCode(String errDescription, final boolean state) {
        if (!errDescription.isEmpty()) {
            final Dialog dialog = new Dialog(UsePromotionActivity.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_use_promotion_success);

            TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
            TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
            Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
            tvContent.setText(Html.fromHtml(errDescription));
//            tvContent.setText(errDescription);
            if (!state) {
                tvTitle.setText("Error!");
                tvTitle.setTextColor(Color.parseColor("#cc0000"));
            }


            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }


    private void sendBroadCastSuccess(){
        Intent intent = new Intent(Global.BROADCAST_USE_CODE+promotionId);
        intent.putExtra(Global.EXTRA_ID, promotionId);
        intent.putExtra(Global.EXTRA_DATA, bonusPromotions);
        intent.putExtra(Global.EXTRA_DESCRIPTION, errDescription);
        sendBroadcast(intent);

    }

    public void showDialogCongratulations() {
        dialogCongratulations.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCongratulations.setCanceledOnTouchOutside(false);
        dialogCongratulations.setContentView(R.layout.dialog_congratulations);
//        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button

        Button btOk = (Button) dialogCongratulations.findViewById(R.id.btOk);
        // if button is clicked, close the custom dialog
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                dialogCongratulations.dismiss();
            }
        });

        dialogCongratulations.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        dialogCongratulations.show();
    }

    class DownloadBonusItemActionUse extends AsyncTask<List<ItemQTY>, Void, Void> {
        private Context mContext;
        private int mPoint;
        private int mXP;

        public DownloadBonusItemActionUse(Context context, int point, int xp) {
            mPoint = point;
            mXP = xp;
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(List<ItemQTY>... params) {
            List<ItemQTY> itemQTYList = params[0];
            for (ItemQTY item : itemQTYList) {
                //add to list item
                mDataItems.addItemForGrab(item);

                if (mSqliteHelper.isItemSave(item.getItemObject().getId())) {
                    continue;
                } else {
                    //insert item to db
                    mSqliteHelper.insertItemFromShop(item.getItemObject());
                    Global.saveIconItem(mSqliteHelper, item.getItemObject().getIconUrl(), item.getItemObject().getId());
                    String itemType = item.getItemObject().getActionId();
                    if (itemType.equals(Global.TYPE_TOMI_THEME) || itemType.equals(Global.TYPE_TOMI_ACTION_EAT) || itemType.equals(Global.TYPE_TOMI_ACTION_DAILY)) {
                        for (String s : item.getItemObject().getImageList()) {
                            Global.saveImageItem(s, item.getItemObject().getId());
                        }
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mDataItems.addPoint(mPoint, mXP);
            showDialogUseCode(errDescription, true);
            super.onPostExecute(aVoid);
        }
    }


}
