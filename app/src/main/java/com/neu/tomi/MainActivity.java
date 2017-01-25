package com.neu.tomi;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.HttpRequest;
import com.neu.tomi.view.HomeActivity;
import com.neu.tomi.widget.TomiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 0;
    private DataItems mDataItems;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private CallbackManager callbackManager;
    private Dialog dialog, dialogSignUp, dialogSignIn, dialogForgotPassword;
    private SignInButton btSignInGoogle;
    private LoginButton btSignInFacebook;
    private Button btSignIn, btSignUp;
    private EditText etSignupConfirmPassword, etSignupPassword, etSignupEmail, etSignupName;
    private EditText etSignInEmail, etSignInPassword;
    private EditText etForgotEmail;

    private boolean isAddWidget = false;
    private boolean broadcastCanshow = false;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(
                    "android.net.conn.CONNECTIVITY_CHANGE")) {
                if (broadcastCanshow) {
                    if (HttpRequest.isOnline(MainActivity.this)) {
                        String userId = mDataItems.getUserId();
                        if (!userId.isEmpty()) {

                        } else {
                            if (isAddWidget)
                                showDialogLogin();
                        }

                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBatInfoReceiver);
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataItems = new DataItems(MainActivity.this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();

        isAddWidget = new DataItems(MainActivity.this).isHelpFirst();

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(this.mBatInfoReceiver,
                filter);
        dialog = new Dialog(MainActivity.this);
        dialogSignUp = new Dialog(MainActivity.this);
        dialogSignIn = new Dialog(MainActivity.this);
        dialogForgotPassword = new Dialog(MainActivity.this);

        dialog.setCanceledOnTouchOutside(false);
        dialogSignUp.setCanceledOnTouchOutside(true);
        dialogSignIn.setCanceledOnTouchOutside(true);
        dialogForgotPassword.setCanceledOnTouchOutside(true);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSignUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSignIn.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForgotPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);

        ImageView ivMonkeyDance = (ImageView) findViewById(R.id.ivMonkeyDance);
        final AnimationDrawable myAnimationDrawable
                = (AnimationDrawable) ivMonkeyDance.getDrawable();

        ivMonkeyDance.post(
                new Runnable() {

                    @Override
                    public void run() {
                        myAnimationDrawable.start();
                    }
                });


        FacebookSdk.sdkInitialize(MainActivity.this);
        callbackManager = CallbackManager.Factory.create();

        if (!Global.isBluetooth()) {
            Global.isBeaconDetected = false;
            Global.currentAction = "";
        }

        mDataItems.setLastTimeLogin();


        if (HttpRequest.isOnline(MainActivity.this)) {
            if (!mDataItems.getLastDayCheckVersion().equals(Global.getDate())) {
                new CheckVersion().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                if (mDataItems.getCurrentVersion() > BuildConfig.VERSION_CODE)
                    showDialogUpdate();
                else
                    checkAddWidget();
            }
        } else {
            checkAddWidget();
        }
        signOut();
        rename();
    }

    private void rename() {
        if (mDataItems.isNewName()) {
            if (HttpRequest.isOnline(MainActivity.this))
                new Rename(mDataItems, MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void startService() {
        Intent mIntent = new Intent(MainActivity.this, TomiService.class);
        if (Global.serviceNotRun(MainActivity.this)) {

            MainActivity.this.startService(mIntent);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntentAlarm = Global.createClockTickIntent(MainActivity.this);
        try {
            alarmManager.cancel(pendingIntentAlarm);
        } catch (Exception ex) {
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 120000, pendingIntentAlarm);
    }

    private void checkAddWidget() {

        if (isAddWidget) {
            checkLogin();
        } else {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_check_widget);
            dialog.setCanceledOnTouchOutside(true);

            LinearLayout llCheckWidget = (LinearLayout) dialog.findViewById(R.id.llCheckWidget);
            llCheckWidget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isAddWidget = true;
                    checkLogin();
                }
            });
            dialog.show();
        }
    }

    private void checkLogin() {
        String userId = mDataItems.getUserId();

        if (!userId.isEmpty()) {
            startActivityHome();
        } else {
            if (HttpRequest.isOnline(MainActivity.this)) {
                showDialogLogin();
            } else {
                broadcastCanshow = true;
                Toast.makeText(MainActivity.this, "Please! Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startActivityHome() {
        startService();
        Intent currentIntent = getIntent();
        int EXTRA_APPWIDGET_ID = currentIntent.getIntExtra("EXTRA_APPWIDGET_ID", -1);
        String EXTRA_BEACON_INFO = currentIntent.getStringExtra("EXTRA_BEACON_INFO");
        String EXTRA_CURENT_ACTION = currentIntent.getStringExtra("EXTRA_CURENT_ACTION");
        boolean EXTRA_EXPIRE_ACTION = currentIntent.getBooleanExtra("EXPIRE", false);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtra("EXTRA_APPWIDGET_ID", EXTRA_APPWIDGET_ID);
        intent.putExtra("EXTRA_BEACON_INFO", EXTRA_BEACON_INFO);
        intent.putExtra("EXTRA_CURENT_ACTION", EXTRA_CURENT_ACTION);
        intent.putExtra("EXPIRE", EXTRA_EXPIRE_ACTION);
        intent.setClass(MainActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void startActivityHome(boolean isLogin) {
        startService();
        Intent currentIntent = getIntent();
        int EXTRA_APPWIDGET_ID = currentIntent.getIntExtra("EXTRA_APPWIDGET_ID", -1);
        String EXTRA_BEACON_INFO = currentIntent.getStringExtra("EXTRA_BEACON_INFO");
        String EXTRA_CURENT_ACTION = currentIntent.getStringExtra("EXTRA_CURENT_ACTION");
        boolean EXTRA_EXPIRE_ACTION = currentIntent.getBooleanExtra("EXPIRE", false);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtra("EXTRA_APPWIDGET_ID", EXTRA_APPWIDGET_ID);
        intent.putExtra("EXTRA_BEACON_INFO", EXTRA_BEACON_INFO);
        intent.putExtra("EXTRA_CURENT_ACTION", EXTRA_CURENT_ACTION);
        intent.putExtra("EXPIRE", EXTRA_EXPIRE_ACTION);
        intent.putExtra("IS_LOGIN", isLogin);
        intent.setClass(MainActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void showDialogLogin() {

        if (!dialog.isShowing()) {
            dialog.setContentView(R.layout.dialog_login);
            btSignInGoogle = (SignInButton) dialog.findViewById(R.id.btSignInGoogle);
            btSignInFacebook = (LoginButton) dialog.findViewById(R.id.btSignInFacebook);
            btSignIn = (Button) dialog.findViewById(R.id.btSignIn);
            btSignUp = (Button) dialog.findViewById(R.id.btSignUp);

            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        finish();
                    }
                    return true;
                }
            });

            btSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogSignUp();
                }
            });

            btSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogSignIn();
                }
            });

            callbackManager = CallbackManager.Factory.create();
//            btSignInFacebook.setReadPermissions(Arrays.asList("public_profile", "user_birthday","email"));
            btSignInFacebook.setReadPermissions(Arrays.asList(
                    "public_profile", "email"));
            btSignInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
//                    "User ID: "
//                            + loginResult.getAccessToken().getUserId()
//                            + "\n" +
//                            "Auth Token: "
//                            + loginResult.getAccessToken().getToken()
                    handleSignInFacebook(loginResult.getAccessToken(), loginResult.getAccessToken().getUserId());

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException error) {

                }
            });


            btSignInGoogle.setSize(SignInButton.SIZE_STANDARD);
            btSignInGoogle.setScopes(gso.getScopeArray());

            btSignInGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mGoogleApiClient.isConnecting()) {
                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    }
                }
            });


            dialog.show();
        }

    }

    private void showDialogSignUp() {

        if (dialogSignUp != null && !dialogSignUp.isShowing()) {
            dialogSignUp.setContentView(R.layout.dialog_sign_up);

            Button btCancel = (Button) dialogSignUp.findViewById(R.id.btCancel);
            Button btSubmit = (Button) dialogSignUp.findViewById(R.id.btSubmit);

            etSignupConfirmPassword = (EditText) dialogSignUp.findViewById(R.id.etConfirmPassword);
            etSignupEmail = (EditText) dialogSignUp.findViewById(R.id.etEmail);
            etSignupPassword = (EditText) dialogSignUp.findViewById(R.id.etPassword);
            etSignupName = (EditText) dialogSignUp.findViewById(R.id.etName);

            etSignupConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.submit || id == EditorInfo.IME_NULL) {
                        signUpNewUser();
                        return true;
                    }
                    return false;
                }
            });

            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUpNewUser();
                }
            });
            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSignUp.dismiss();
                }
            });
            dialogSignUp.show();
        }

    }

    private void showDialogSignIn() {

        if (!dialogSignIn.isShowing()) {
            dialogSignIn.setContentView(R.layout.dialog_sign_in);
            TextView tvForgotPassword = (TextView) dialogSignIn.findViewById(R.id.tvForgotPassword);
            Button btCancel = (Button) dialogSignIn.findViewById(R.id.btCancel);
            Button btSignIn = (Button) dialogSignIn.findViewById(R.id.btSignIn);
            etSignInEmail = (EditText) dialogSignIn.findViewById(R.id.etEmail);
            etSignInPassword = (EditText) dialogSignIn.findViewById(R.id.etPassword);
            tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            tvForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogForgotPassword();
                }
            });

            etSignInPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.signIn || id == EditorInfo.IME_NULL) {
                        loginWithEmailAndPassword();
                        return true;
                    }
                    return false;
                }
            });

            btSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginWithEmailAndPassword();
                }
            });
            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSignIn.dismiss();
                }
            });
            dialogSignIn.show();
        }

    }

    private void showDialogForgotPassword() {

        if (!dialogForgotPassword.isShowing()) {
            dialogForgotPassword.setContentView(R.layout.dialog_forgot_password);

            Button btCancel = (Button) dialogForgotPassword.findViewById(R.id.btCancel);
            Button btGetPassword = (Button) dialogForgotPassword.findViewById(R.id.btGetPassword);
            etForgotEmail = (EditText) dialogForgotPassword.findViewById(R.id.etEmail);
            btCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogForgotPassword.dismiss();
                }
            });

            etForgotEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.id.getPassword || id == EditorInfo.IME_NULL) {
                        getPassword();
                        return true;
                    }
                    return false;
                }
            });

            btGetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPassword();
                }
            });
            dialogForgotPassword.show();
        }

    }

    private void verifyPermissions(Activity activity) {
        // Here, thisActivity is the current activity
        int permissionWrite = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRead = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCoarseLocation = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionFineLocation = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionReadPhoneState = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_PHONE_STATE);
        if ((permissionCoarseLocation + permissionFineLocation + permissionWrite + permissionRead + permissionReadPhoneState) != PackageManager.PERMISSION_GRANTED) {

//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//
//            } else {


            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    100);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//            }
        } else {
            checkAddWidget();
        }
    }


    private void signUpNewUser() {
        String password = etSignupPassword.getText().toString();
        String email = etSignupEmail.getText().toString();
        String name = etSignupName.getText().toString();
        if (name.isEmpty()) {
            etSignupName.setError(getString(R.string.edit_text_null_value));
            etSignupName.requestFocus();
        } else {
            if (email.contains("@")) {
                if (password.length() > 4) {
                    if (password.equals(etSignupConfirmPassword.getText().toString())) {
                        new SignUp(MainActivity.this, email, password, name).execute();
                    } else {
                        etSignupConfirmPassword.setError(getString(R.string.edit_text_password_not_match));
                        etSignupConfirmPassword.requestFocus();
                    }
                } else {
                    etSignupPassword.setError(getString(R.string.edit_text_password_short));
                    etSignupPassword.requestFocus();
                }
            } else {
                etSignupEmail.setError(getString(R.string.edit_text_email_invalid));
                etSignupEmail.requestFocus();
            }
        }
    }

    private void loginWithEmailAndPassword() {
        String password = etSignInPassword.getText().toString();
        String email = etSignInEmail.getText().toString();

        if (email.isEmpty()) {
            etSignInEmail.setError(getString(R.string.edit_text_null_value));
            etSignInEmail.requestFocus();
        } else {
            if (email.contains("@")) {
                if (password.length() > 4) {

                    new SignIn(MainActivity.this, email, password).execute();

                } else {
                    etSignInPassword.setError(getString(R.string.edit_text_password_short));
                    etSignInPassword.requestFocus();
                }
            } else {
                etSignInEmail.setError(getString(R.string.edit_text_email_invalid));
                etSignInEmail.requestFocus();
            }
        }
    }

    private void getPassword() {
        String email = etForgotEmail.getText().toString();

        if (email.isEmpty()) {
            etForgotEmail.setError(getString(R.string.edit_text_null_value));
            etForgotEmail.requestFocus();
        } else {
            if (email.contains("@")) {
                new GetPassword(MainActivity.this, email).execute();
            } else {
                etForgotEmail.setError(getString(R.string.edit_text_email_invalid));
                etForgotEmail.requestFocus();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    int result = PackageManager.PERMISSION_GRANTED;
                    for (int grant : grantResults) {
                        if (grant != PackageManager.PERMISSION_GRANTED) {
                            result = PackageManager.PERMISSION_DENIED;
                            break;
                        }
                    }
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        checkAddWidget();
                        return;
                    }
                }
                Toast.makeText(MainActivity.this, "Please allow all permissions of Tomi app!", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

        }
    }

    private void showDialogUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update are ready to install")
                .setMessage("It won't take long to upgrade - and you'll get all the lastest improvements and fixes.")
                .setCancelable(false)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=com.neu.tomi"));
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class CheckVersion extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute() {
            mDataItems.setLastDayCheckVersion();
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            return DataItems.CheckVersion(MainActivity.this);

        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    int version = result.getInt("version");
                    mDataItems.setCurrentVersion(version);
                    if (version > BuildConfig.VERSION_CODE) {
                        showDialogUpdate();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                verifyPermissions(MainActivity.this);
            } else {
                checkAddWidget();
            }

        }
    }

    class Rename extends AsyncTask<Void, Void, JSONObject> {
        private String mName;
        private DataItems mDataItems;
        private Context mContext;

        public Rename(DataItems dataItems, Context context) {
            mContext = context;
            mDataItems = dataItems;
            mName = dataItems.getNameOfUser();

        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.Rename(mContext, mName);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInFacebook(AccessToken accessToken, final String socialId) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        String name = mDataItems.getNameOfUser();
                        String link = "";
                        String gender = "";
                        String email = "";
                        try {
                            name = object.getString("name");
                            JSONObject picture = object.getJSONObject("picture");
                            link = picture.getJSONObject("data").getString("url");// "http://graph.facebook.com/" + socialId + "/picture?width=100&height=100";
                        } catch (JSONException e) {
                        }
                        try {
                            gender = object.getString("gender");
                        } catch (JSONException e) {

                        }
                        String birthday = "";

                        try {
                            birthday = Global.formatDateFacebook(object.getString("birthday"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            email = object.getString("email");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        int sex = 0;
                        if (gender.equals("female") || gender.equals("nữ")) {
                            sex = 1;
                        }
                        String socialIdNew = "facebook" + socialId;
                        new GetPointFirstRun(MainActivity.this, socialIdNew, link, name, birthday, sex, email).execute();
                        dialog.dismiss();
                        mDataItems.setNameOfUser(name);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,gender,birthday,age_range,picture.width(150).height(150)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            // Signed in successfully, show authenticated UI.
//            result.getSignInAccount().getIdToken();
            int sex = person.getGender();
            String birthday = "";
            if (person.getBirthday() != null)
                birthday = person.getBirthday();
            GoogleSignInAccount acct = result.getSignInAccount();
            String email = acct.getEmail();
            String name = acct.getDisplayName();
            mDataItems.setNameOfUser(email);
            mDataItems.setLoginGmailState();
            String link = "";
            try {
                Uri uri = acct.getPhotoUrl();
                String is = acct.getIdToken();
                if (uri != null)
                    link = uri.toString();
            } catch (Exception ex) {
            }
//            String socialIdNew ="google"+acct.getId();
            String socialIdNew = "google" + email;
            new GetPointFirstRun(MainActivity.this, socialIdNew, link, name, birthday, sex, email).execute();
            dialog.dismiss();
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(MainActivity.this, result.getStatus().toString(), Toast.LENGTH_SHORT).show();
        }
    }


    class GetPointFirstRun extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private String mSocialId;
        private String mAvatar;
        private String mName;
        private String mBirthday;
        private String mEmail;
        private int mSex;
        private ProgressDialog mDialog;

        public GetPointFirstRun(Context context, String socialId, String avatar, String name, String birthday, int sex, String email) {
            mContext = context;
            mSocialId = socialId;
            mAvatar = avatar;
            mName = name;
            mBirthday = birthday;
            mSex = sex;
            mEmail = email;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Sync loading..", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return mDataItems.getFirstRunService(mSocialId, mAvatar, mName, mBirthday, mSex, MainActivity.this, mEmail);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mDialog.dismiss();
            if (jsonObject != null) {
                mDataItems.setUserId(mSocialId);
                mDataItems.setSocialLogin(true);
                try {

                    mDataItems.setLoadFirstRun();
                    if (jsonObject.getBoolean("state")) {
                        mDataItems.addPoint(jsonObject.getInt("point"), 0);

                    } else {
                        mDataItems.setFirstShow();
                    }
                    startActivityHome(true);
                } catch (JSONException e) {
                    connectFailed();
                }

            } else {
                connectFailed();
            }
            super.onPostExecute(jsonObject);
        }
    }

    private void showDialogUseCodeStatus(String errDescription, final boolean state, boolean showResend) {
        if (!errDescription.isEmpty()) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_status_code);

            TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
            TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
            Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
            Button btResend = (Button) dialog.findViewById(R.id.btResend);
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
                    if (state) {
                        if (dialogSignUp != null && dialogSignUp.isShowing())
                            dialogSignUp.dismiss();
                    }
                }
            });
            if (showResend) {
                btResend.setVisibility(View.VISIBLE);
                btResend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        new ResendEmail(MainActivity.this, etSignInEmail.getText().toString()).execute();
                    }
                });
            }
            dialog.show();
        }
    }


    class ResendEmail extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private String mEmail;

        private ProgressDialog mDialog;

        public ResendEmail(Context context, String email) {
            mContext = context;
            mEmail = email;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Sign up...", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return mDataItems.resendEmail(mEmail, MainActivity.this);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mDialog.dismiss();
            if (jsonObject != null) {
                try {
                    boolean state = jsonObject.getBoolean("state");
                    String message = jsonObject.getString("err_description");
                    showDialogUseCodeStatus(message, state,false);

                } catch (JSONException e) {
                    connectFailed();
                }

            } else {
                connectFailed();
            }
            super.onPostExecute(jsonObject);
        }
    }

    class SignUp extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private String mEmail;
        private String mPassword;
        private String mName;

        private ProgressDialog mDialog;

        public SignUp(Context context, String email, String password, String name) {
            mContext = context;
            mEmail = email;
            mPassword = password;
            mName = name;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Sign up...", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return mDataItems.getFirstRunService(mEmail, mPassword, mName, MainActivity.this);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mDialog.dismiss();
            if (jsonObject != null) {
                try {
                    boolean state = jsonObject.getBoolean("state");
                    String message = jsonObject.getString("err_description");

                    if (state) {
                        mDataItems.addPoint(jsonObject.getInt("point"), 0);
                        boolean exist = jsonObject.getBoolean("exist");
                        mDataItems.setServerDetectLostData(exist);
                        mDataItems.setNameOfUser(mName);
                        mDataItems.setUserId(mEmail);
                        mDataItems.setLoadFirstRun();
//                        startActivityHome();
                    } else {
                        etSignupEmail.setError(getString(R.string.edit_text_email_Exist));
                        etSignupEmail.requestFocus();
                    }
                    showDialogUseCodeStatus(message, state,false);

                } catch (JSONException e) {
                    connectFailed();
                }

            } else {
                connectFailed();
            }
            super.onPostExecute(jsonObject);
        }
    }

    class SignIn extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private String mEmail;
        private String mPassword;


        private ProgressDialog mDialog;

        public SignIn(Context context, String email, String password) {
            mContext = context;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Sign in...", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return mDataItems.signInWithEmailPassword(mEmail, mPassword, MainActivity.this);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mDialog.dismiss();
            if (jsonObject != null) {
                try {
                    if (jsonObject.getBoolean("state")) {
//                        mDataItems.setNameOfUser(jsonObject.getString("name"));
//                        mDataItems.setServerDetectLostData(true);
                        mDataItems.setSocialLogin(false);
                        mDataItems.setUserId(mEmail);
                        startActivityHome(true);
                    } else {
                        String errorCode = jsonObject.getString("error_code");
                        boolean isShowRecent = errorCode.equals("ACC_NOT_ACTIVATED");
                        showDialogUseCodeStatus(jsonObject.getString("err_description"), false,isShowRecent);
//                        Toast.makeText(MainActivity.this, jsonObject.getString("err_description"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    connectFailed();
                }

            } else {
                connectFailed();
            }
            super.onPostExecute(jsonObject);
        }
    }

    class GetPassword extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private String mEmail;


        private ProgressDialog mDialog;

        public GetPassword(Context context, String email) {
            mContext = context;
            mEmail = email;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading...", true);
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return mDataItems.getPassword(mEmail, MainActivity.this);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mDialog.dismiss();
            if (jsonObject != null) {
                try {
                    if (jsonObject.getBoolean("state")) {
                        Toast.makeText(MainActivity.this, "The system will send a temporary password to your email address", Toast.LENGTH_LONG).show();
                        dialogForgotPassword.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, jsonObject.getString("err_description"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    connectFailed();
                }

            } else {
                connectFailed();
            }
            super.onPostExecute(jsonObject);
        }
    }

    private void connectFailed() {
        Toast.makeText(MainActivity.this, "Connect server fail", Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        if (!mDataItems.isUpdateVersion41()) {
            File fileOrDirectory = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/tomi_image");
            if (fileOrDirectory.isDirectory()) {
                if (fileOrDirectory.listFiles() != null && fileOrDirectory.listFiles().length > 0)
                    for (File child : fileOrDirectory.listFiles())
                        child.delete();
            }

            mDataItems.clearDataShareReferences();
            mDataItems.getSQLiteHelper().clearData();
            signOutSocial();
            mDataItems.setUpdateVersion41(true);
        }
        if (!mDataItems.isUpdateVersion48()) {
            mDataItems.setUserId("");
            signOutSocial();
            mDataItems.setUpdateVersion48(true);
        }
    }

    private void signOutSocial() {
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception ex) {

        }
        if (mGoogleApiClient.isConnected()) {
            try {


                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                        .setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                    }
                                });

            } catch (Exception ex) {

            }
        }
    }
}


