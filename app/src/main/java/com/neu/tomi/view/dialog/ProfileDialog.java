package com.neu.tomi.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.neu.tomi.MainActivity;
import com.neu.tomi.R;
import com.neu.tomi.adapter.ApplicationAdapter;
import com.neu.tomi.object.ApplicationObject;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProfileDialog extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    private static final int RC_RENAME = 722;
    private ProgressDialog mDialog;
    /* Client used to interact with Google APIs. */
    private CallbackManager callbackManager;
    private Dialog dialogChangePassword;
    private RelativeLayout llLayoutProfile;
    private RelativeLayout rlShareProfile;
    private ImageView ivBagProfile, ivMedal, ivRename;
    private TextView tvIdUser, tvCurrentItem, tvPoint, tvLevel, tvXPProgress, tvChangePassword;
    private EditText etCodeProfile;
    private ImageButton btShareImage;
    private ProgressBar pbXPProgress;
    private Button btCodeProfile, btOkProfile, btSignOut;
    private DataItems mDataItems;
    private ShareDialog shareDialog;
    private EditText etOldPassword, etNewPassword, etConfirmNewPassword;
    private  GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_profile);
        init();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(ProfileDialog.this)
                .enableAutoManage(ProfileDialog.this /* FragmentActivity */, ProfileDialog.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int color = Global.getColor();
            GradientDrawable gd = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{color, color});
            gd.setCornerRadius(Global.convertDpToPixel(16, ProfileDialog.this));
            llLayoutProfile.setBackground(gd);
            GradientDrawable gd2 = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{color, color});
            gd2.setCornerRadius(Global.convertDpToPixel(16, ProfileDialog.this));
            rlShareProfile.setBackgroundColor(color);
        }

        dialogChangePassword = new Dialog(ProfileDialog.this);
        dialogChangePassword.setCanceledOnTouchOutside(true);
        dialogChangePassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    private void init() {
        FacebookSdk.sdkInitialize(ProfileDialog.this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.9);
        int screenHeight = (int) (metrics.heightPixels * 0.85);
        getWindow().setLayout(screenWidth, screenHeight);
        mDataItems = new DataItems(ProfileDialog.this);
        int EXTRA_APPWIDGET_ID = getIntent().getIntExtra("WID", 0);

        llLayoutProfile = (RelativeLayout) findViewById(R.id.llLayoutProfile);
        rlShareProfile = (RelativeLayout) findViewById(R.id.rlShareProfile);


        btSignOut = (Button) findViewById(R.id.btSignOut);
        btOkProfile = (Button) findViewById(R.id.btOkProfile);
        btCodeProfile = (Button) findViewById(R.id.btCodeProfile);
        etCodeProfile = (EditText) findViewById(R.id.etCodeProfile);
        btShareImage = (ImageButton) findViewById(R.id.btShareImage);
        tvCurrentItem = (TextView) findViewById(R.id.tvCurrentItem);
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        tvIdUser = (TextView) findViewById(R.id.tvIdUser);
        tvChangePassword = (TextView) findViewById(R.id.tvChangePassword);
        tvLevel = (TextView) findViewById(R.id.tvLevel);
        tvXPProgress = (TextView) findViewById(R.id.tvXPProgress);
        ivBagProfile = (ImageView) findViewById(R.id.ivBagProfile);
        ivRename = (ImageView) findViewById(R.id.ivRename);
        ivMedal = (ImageView) findViewById(R.id.ivMedal);
        pbXPProgress = (ProgressBar) findViewById(R.id.pbXPProgress);

//        pbXPProgress.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        int itemIdForBag = mDataItems.getBagWidget(EXTRA_APPWIDGET_ID);
        int level = mDataItems.getCurrentLevel();
        String name = mDataItems.getNameOfUser();
        if(!mDataItems.isSocialLogin()) {
            tvChangePassword.setPaintFlags(tvChangePassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tvChangePassword.setVisibility(View.VISIBLE);
        }else{
            tvChangePassword.setVisibility(View.GONE);
        }
        if (name.equals("Anonymous")) {
            Intent i = new Intent(ProfileDialog.this, RenameDialog.class);
            startActivityForResult(i, RC_RENAME);
        } else {
            tvIdUser.setText(name);
        }
        tvPoint.setText("POINTS: " + mDataItems.getBalancePoint());
        tvLevel.setText("(Level " + level + ")");
        if (level > 5 && level < 16) {
            ivMedal.setImageResource(R.drawable.ic_rank_6_16);
        } else if (level >= 16) {
            ivMedal.setImageResource(R.drawable.ic_rank_up_16);
        }
        int xpNextLevel = mDataItems.getXPforNextLevel();
        int currentXP = mDataItems.getTotalXP();
        pbXPProgress.setMax(xpNextLevel);
        pbXPProgress.setProgress(currentXP);
        tvXPProgress.setText(currentXP + "/" + xpNextLevel);
        tvCurrentItem.setText("+" + mDataItems.getFoodItemList().size());
        ItemObject.setImage(getBaseContext(), itemIdForBag, ivBagProfile);


        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirm();
            }
        });
        btOkProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpRequest.isOnline(ProfileDialog.this))
                    new SaveImageToDiskAndShare(ProfileDialog.this).execute();
                else
                    Toast.makeText(ProfileDialog.this, "No connection!!", Toast.LENGTH_SHORT).show();
            }
        });

        btCodeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCodeProfile.getText().length() > 0) {
                    if (HttpRequest.isOnline(ProfileDialog.this))
                        new CheckTomiCode(ProfileDialog.this, etCodeProfile.getText().toString()).execute();
                    else
                        Toast.makeText(ProfileDialog.this, "No connection!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileDialog.this, "Please enter code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileDialog.this, RenameDialog.class);
                startActivityForResult(i, RC_RENAME);
            }
        });

        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangePassword();
            }
        });
    }

    private void showDialogChangePassword() {

        if (!dialogChangePassword.isShowing()) {
            dialogChangePassword.setContentView(R.layout.dialog_change_password);

            Button btCancel = (Button) dialogChangePassword.findViewById(R.id.btCancel);
            Button btSubmit = (Button) dialogChangePassword.findViewById(R.id.btSubmit);
            etOldPassword = (EditText) dialogChangePassword.findViewById(R.id.etOldPassword);
            etNewPassword = (EditText) dialogChangePassword.findViewById(R.id.etNewPassword);
            etConfirmNewPassword = (EditText) dialogChangePassword.findViewById(R.id.etConfirmNewPassword);


            etConfirmNewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.submitNewPassword || id == EditorInfo.IME_NULL) {
                        changePassword();
                        return true;
                    }
                    return false;
                }
            });

            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePassword();
                }
            });
            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogChangePassword.dismiss();
                }
            });
            dialogChangePassword.show();
        }

    }

    private void changePassword() {
        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();


        if (oldPassword.length() > 4) {
            if (newPassword.length() > 4) {
                if (newPassword.equals(etConfirmNewPassword.getText().toString())) {
                    if (oldPassword.equals(newPassword)) {
                        etNewPassword.setError(getString(R.string.edit_text_password_is_used));
                        etNewPassword.requestFocus();
                    } else {
                        new ChangePassword(ProfileDialog.this, oldPassword, newPassword).execute();
                    }
                } else {
                    etConfirmNewPassword.setError(getString(R.string.edit_text_password_not_match));
                    etConfirmNewPassword.requestFocus();
                }
            } else {
                etNewPassword.setError(getString(R.string.edit_text_password_short));
                etNewPassword.requestFocus();
            }
        } else {
            etOldPassword.setError(getString(R.string.edit_text_password_short));
            etOldPassword.requestFocus();
        }

    }

    private void showDialogConfirm() {
        final Dialog dialog = new Dialog(ProfileDialog.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_sign_out);
//        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button

        Button btOk = (Button) dialog.findViewById(R.id.btOk);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        // if button is clicked, close the custom dialog
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearApplicationData();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("SIGN_OUT", true);
                setResult(Activity.RESULT_OK, returnIntent);
                try {
                    LoginManager.getInstance().logOut();
                }catch (Exception ex){

                }
                if(mGoogleApiClient.isConnected()) {
                    try {


                        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                                .setResultCallback(
                                        new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(Status status) {
                                                startMainActivity();
                                            }
                                        });

                    } catch (Exception ex) {

                    }
                }else{
                    startMainActivity();
                }
//                finish();

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

    private void startMainActivity(){
        Intent intent = new Intent(ProfileDialog.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void clearApplicationData() {
        mDataItems.clearDataShareReferences();
        mDataItems.getSQLiteHelper().clearData();

    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    @Override
    protected void onResume() {
        tvIdUser.setText(mDataItems.getNameOfUser());
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);


        if (requestCode == RC_RENAME) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra("RENAME");
                tvIdUser.setText(name);
            }
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    class SaveImageToDiskAndShare extends AsyncTask<Void, Void, Uri> {
        private Context mContext;
        private ProgressDialog mDialog;

        public SaveImageToDiskAndShare(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading..", true);
        }

        @Override
        protected Uri doInBackground(Void... params) {
            DataItems dataItems = new DataItems(mContext);
            Uri bmpUri;
            bmpUri = dataItems.getLocalShareBitmapUri(rlShareProfile, "share_image");
            return bmpUri;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            mDialog.dismiss();
            if (uri != null) {
                showDialogShare(uri);
            } else
                Toast.makeText(mContext, "Error loading image", Toast.LENGTH_SHORT).show();
            super.onPostExecute(uri);
        }

    }


    public void showDialogCongratulations(boolean isIncludePromotion, String message) {
        final Dialog dialog = new Dialog(ProfileDialog.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_use_profile_code);

        TextView tvTitleModeSaving = (TextView) dialog.findViewById(R.id.tvTitleModeSaving);
        tvTitleModeSaving.setText(message);

        Button btOk = (Button) dialog.findViewById(R.id.btOk);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        if (isIncludePromotion) {
            btOk.setVisibility(View.VISIBLE);
            btOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileDialog.this, PromotionDialog.class);
                    intent.putExtra("TREAT", true);
                    startActivity(intent);
                    dialog.dismiss();

                }
            });
        } else {
            btOk.setVisibility(View.GONE);
        }
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showDialogShare(final Uri imageUri) {
        final Context context = ProfileDialog.this;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_share);
        dialog.setTitle("Share with..");
        // set the custom dialog components - text, image and button
        final ListView lvListApplication = (ListView) dialog.findViewById(R.id.lvListApplication);
        final List<ApplicationObject> lstApp = new ArrayList<>();
        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.contains("com.facebook.katana") || packageInfo.packageName.contains("com.whatsapp") || packageInfo.packageName.contains("com.tencent.mm") || packageInfo.packageName.contains("com.viber.voip") || packageInfo.packageName.contains("com.instagram.android"))
                lstApp.add(new ApplicationObject(packageInfo.packageName, packageInfo.loadLabel(pm).toString(), packageInfo.loadIcon(pm)));
        }
        if (lstApp.size() > 0) {
            ApplicationAdapter adapter = new ApplicationAdapter(context, lstApp);
            lvListApplication.setAdapter(adapter);
            lvListApplication.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ApplicationObject applicationObject = (ApplicationObject) parent.getAdapter().getItem(position);

                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("image/png");
                    shareIntent.putExtra(Intent.EXTRA_TITLE, "my awesome caption in the EXTRA_TITLE field");
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download tomi here: https://play.google.com/store/apps/details?id=" + getPackageName());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    PackageManager pm = context.getPackageManager();
                    List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);

                    for (final ResolveInfo app : activityList) {


                        if ((app.activityInfo.packageName).equals(applicationObject.getPacketName())) {

                            final ActivityInfo activity = app.activityInfo;
                            final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
//                            if (app.activityInfo.packageName.contains("facebook")) {
//
//                                if (ShareDialog.canShow(ShareLinkContent.class)) {
//                                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                                            .setContentTitle("Hello Facebook")
//                                            .setContentDescription(
//                                                    "The 'Hello Facebook' sample  showcases simple Facebook integration")
//                                            .setContentUrl(Uri.parse("https://play.google.com/store"))
//                                                    .setImageUrl(Uri.parse("http://joombig.com/demo-extensions1/images/gallery_slider/Swan_large.jpg"))
//                                            .build();
//                                    shareDialog.show(linkContent);
//                                }
//
//                            } else {

                            shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            shareIntent.setComponent(name);
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(shareIntent, Global.SHARE_REQUEST_CODE);
//                            }


                            dialog.dismiss();
                            break;

                        }
                    }

                }
            });
            dialog.show();

        } else

        {
            Toast.makeText(context, "Please install Facebook or Viber or Wechat or Whatapp or Instangram", Toast.LENGTH_SHORT).show();
        }
    }


    class CheckTomiCode extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private String mCode;

        public CheckTomiCode(Context context, String code) {
            mContext = context;
            mCode = code;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading..", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.UseCode(mContext, mCode);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            try {
                if (jsonObject.getBoolean("state")) {
                    int point = jsonObject.getInt("point");
                    int pointType = jsonObject.getInt("point_type");
//                    String message = "You Earned " + point + " point + " + pointType + " XP.";
                    String message = jsonObject.getString("error_description");
                    mDataItems.addPoint(point, pointType);
                    JSONArray jsonArray = jsonObject.getJSONArray("promotions");
                    etCodeProfile.setText("");
                    if (jsonArray.length() > 0) {
                        List<PromtionObject> promtionObjects = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                promtionObjects.add(new PromtionObject(object));
                            } catch (JSONException e) {
                            }
                        }
                        new SavePromotionFirstTime(mDialog, promtionObjects, message).execute();
                    } else {
                        mDialog.dismiss();
                        showDialogCongratulations(false, message);
                    }

                } else {
                    mDialog.dismiss();
                    Toast.makeText(mContext, jsonObject.getString("error_description"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(mContext, "Can't connect to server", Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }
            super.onPostExecute(jsonObject);
        }
    }

    class ChangePassword extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private String mOldPassword;
        private String mNewPassword;

        public ChangePassword(Context context, String oldPassword, String newPassword) {
            mContext = context;
            mOldPassword = oldPassword;
            mNewPassword = newPassword;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading..", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.getPassword(mContext, mOldPassword, mNewPassword);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mDialog.dismiss();
            if (jsonObject != null) {
                try {
                    if (jsonObject.getBoolean("state")) {
                        dialogChangePassword.dismiss();
                        Toast.makeText(mContext, "Password is changed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, jsonObject.getString("error_description"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(mContext, "Connect server fail", Toast.LENGTH_LONG).show();
                }
            }


            super.onPostExecute(jsonObject);
        }
    }

    class SavePromotionFirstTime extends AsyncTask<Void, Void, Void> {
        private ProgressDialog mDialog;
        private List<PromtionObject> mPromtionObjects;
        private String mMessage;

        public SavePromotionFirstTime(ProgressDialog progressDialog, List<PromtionObject> promtionObjects, String message) {
            mDialog = progressDialog;
            mPromtionObjects = promtionObjects;
            mMessage = message;
        }

        @Override
        protected Void doInBackground(Void... params) {

            for (PromtionObject promtionObject : mPromtionObjects) {
                try {
                    Global.buyPromotion(promtionObject, mDataItems.getSQLiteHelper());
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mDialog.dismiss();
            showDialogCongratulations(true, mMessage);
            super.onPostExecute(aVoid);
        }
    }
}