package com.neu.tomi.image_slide;

import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.adapter.ApplicationAdapter;
import com.neu.tomi.adapter.ItemBonusAdapter;
import com.neu.tomi.object.ActionObject;
import com.neu.tomi.object.ApplicationObject;
import com.neu.tomi.object.BonusItemObject;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.object.ItemQTY;
import com.neu.tomi.object.PromtionObject;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.ultity.HttpRequest;
import com.neu.tomi.ultity.SqliteHelper;
import com.neu.tomi.view.HomeActivity;
import com.neu.tomi.view.ShowDetailImageActivity;
import com.neu.tomi.view.UsePromotionActivity;
import com.neu.tomi.view.VisitWebsiteActivity;
import com.neu.tomi.view.dialog.InfoDialog;
import com.neu.tomi.view.dialog.PromotionDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Vlonjat Gashi (vlonjatg)
 */
public class MaterialSlide extends Fragment {

    private static boolean isLoadedImage = false;
    private Date endDay;
    private PromtionObject mPromtionObject;
    RelativeLayout slideRelativeLayout, rlLayoutPromotion, rlLayoutDetail;
    LinearLayout llBonusShow;
    ImageView slideImageView, ivPromotionDetail, ivBack, ivDelete, ivTotalPromotion;
    ImageButton btShareImage;
    Button btShowDetail;
    ProgressBar pbImageProgress;
    private TextView tvGrab, tvExpire, tvExpired, tvDayleft, tvVisitWebsite, tvWarningNotFoundImage;
    private WebView wvDescriptionDetail;
    RecyclerView rvItemBonusShare, rvItemBonusGrap;
    private boolean isTreat;
    private SqliteHelper mSqliteHelper;
    private DataItems mDataItems;
    private ProgressDialog mDialog;
    private String redirectLink;
    private AlertDialog.Builder alertDialogBuilder = null;
    private AlertDialog alertDialog = null;
    private Dialog dialog;
    private Context mContext;

    public static MaterialSlide newInstance(PromtionObject promtionObject, boolean isTreat) {
        MaterialSlide materialSlide = new MaterialSlide();
        materialSlide.setDataDefault(promtionObject, isTreat);
        return materialSlide;
    }

    public void setDataDefault(PromtionObject promtionObject, boolean isTreat) {
        this.isTreat = isTreat;
        mPromtionObject = promtionObject;
    }

    public String getPromotionId() {
        return mPromtionObject.getPromotionId();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
//                Toast.makeText(mContext, "aaaaaa", Toast.LENGTH_SHORT).show();
                restoreImagePromotions();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_material_slide, container, false);
        mContext=rootView.getContext();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mContext.registerReceiver(this.mBatInfoReceiver,
                filter);
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSqliteHelper = SqliteHelper.getInstanceSQLiteHelper(mContext);
        mDataItems = new DataItems(mContext);
        slideRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.slideRelativeLayout);
        rlLayoutPromotion = (RelativeLayout) rootView.findViewById(R.id.rlLayoutPromotion);
        rlLayoutDetail = (RelativeLayout) rootView.findViewById(R.id.rlLayoutDetail);
        slideImageView = (ImageView) rootView.findViewById(R.id.slideImageView);
        ivPromotionDetail = (ImageView) rootView.findViewById(R.id.ivPromotionDetail);
        tvGrab = (TextView) rootView.findViewById(R.id.tvGrab);
        wvDescriptionDetail = (WebView) rootView.findViewById(R.id.wvDescriptionDetail);
        tvVisitWebsite = (TextView) rootView.findViewById(R.id.tvVisitWebsite);
        tvWarningNotFoundImage = (TextView) rootView.findViewById(R.id.tvWarningNotFoundImage);
        tvExpire = (TextView) rootView.findViewById(R.id.tvExpire);
        tvDayleft = (TextView) rootView.findViewById(R.id.tvDayleft);
        tvExpired = (TextView) rootView.findViewById(R.id.tvExpired);
        ivBack = (ImageView) rootView.findViewById(R.id.ivBack);
        btShowDetail = (Button) rootView.findViewById(R.id.btShowDetail);
        ivDelete = (ImageView) rootView.findViewById(R.id.ivDelete);
        ivTotalPromotion = (ImageView) rootView.findViewById(R.id.ivTotalPromotion);
        btShareImage = (ImageButton) rootView.findViewById(R.id.btShareImage);
        pbImageProgress = (ProgressBar) rootView.findViewById(R.id.pbImageProgress);
        rvItemBonusGrap = (RecyclerView) rootView.findViewById(R.id.rvItemBonusGrap);
        rvItemBonusShare = (RecyclerView) rootView.findViewById(R.id.rvItemBonusShare);
        llBonusShow = (LinearLayout) rootView.findViewById(R.id.llBonusShow);

        rvItemBonusGrap.setHasFixedSize(true);
        rvItemBonusShare.setHasFixedSize(true);
        rvItemBonusGrap.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rvItemBonusShare.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        redirectLink = mPromtionObject.getPromotionLink();
        if (redirectLink.length() > 0) {
            redirectLink = DataItems.LINK_API + "/cms/pages/Tommi_API.php?Object=Promotions&phone_id=" + mDataItems.getPhoneId(mContext) + "&act=Click&promotion_id=" + mPromtionObject.getPromotionId() + "&beacon_id=" + mPromtionObject.getBeaconID();
            tvVisitWebsite.setVisibility(View.VISIBLE);
            tvVisitWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent;
                        if (mPromtionObject.getShowUrl() > 0) {
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(redirectLink));

                        } else {
                            intent = new Intent(mContext, VisitWebsiteActivity.class);
                            intent.putExtra(DataItems.PROMOTION_REDIRECT_KEY, redirectLink);
                        }
                        startActivity(intent);
                    } catch (Exception ex) {

                    }
                }
            });

        } else {
            tvVisitWebsite.setVisibility(View.GONE);
        }
        int total = mPromtionObject.getTotal();
        if (total > 1) {
            ivTotalPromotion.setVisibility(View.VISIBLE);
            int imageId;
            switch (total) {
                case 2:
                    imageId = R.drawable.ic_total_2;
                    break;
                case 3:
                    imageId = R.drawable.ic_total_3;
                    break;
                case 4:
                    imageId = R.drawable.ic_total_4;
                    break;
                case 5:
                    imageId = R.drawable.ic_total_5;
                    break;
                case 6:
                    imageId = R.drawable.ic_total_6;
                    break;
                case 7:
                    imageId = R.drawable.ic_total_7;
                    break;
                case 8:
                    imageId = R.drawable.ic_total_8;
                    break;
                case 9:
                    imageId = R.drawable.ic_total_9;
                    break;
                default:
                    imageId = R.drawable.ic_total_9_plus;
                    break;
            }
            ivTotalPromotion.setImageResource(imageId);
        } else {
            ivTotalPromotion.setVisibility(View.GONE);
        }

        slideImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowDetailImageActivity.class);
                intent.putExtra("IMAGE_URL", mPromtionObject.getImageURL());
                intent.putExtra(DataItems.PROMOTION_REDIRECT_KEY, redirectLink);
                intent.putExtra(DataItems.PROMOTION_SHOW_URL_KEY, mPromtionObject.getShowUrl());
                startActivity(intent);
            }
        });
        Picasso.with(mContext).load(mPromtionObject.getImageURL()).error(R.drawable.ic_image_error).into(slideImageView, new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                isLoadedImage = true;
                if (!isTreat && AppTourDialog.PROMOTION_SELECTED.equals(mPromtionObject.getPromotionId())) {
                    if (!PromotionDialog.promotionExist(mPromtionObject.getPromotionId()))
                        PromotionDialog.addViewPromotion(mPromtionObject);
                }
                pbImageProgress.setVisibility(View.GONE);
                super.onSuccess();
            }

            @Override
            public void onError() {
                pbImageProgress.setVisibility(View.GONE);
                if (!HttpRequest.isOnline(mContext))
                    tvWarningNotFoundImage.setVisibility(View.VISIBLE);
                super.onError();
            }
        });
        loadItemBonus(mContext, false);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleLayoutDetail(mContext, false);
            }
        });

        btShowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleLayoutDetail(mContext, true);
            }
        });
        if (isTreat) {
            tvGrab.setText("Use");
            if (mPromtionObject.getCodeType() < 0) {
                tvGrab.setVisibility(View.GONE);
                rvItemBonusGrap.setVisibility(View.GONE);
            } else {
                rvItemBonusGrap.setVisibility(View.VISIBLE);
                tvGrab.setVisibility(View.VISIBLE);
            }

        }
        String dateExpire = mPromtionObject.getEndTime();

        endDay = new Date();
        try {
            endDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateExpire);

        } catch (ParseException e) {

        }
        int dayLeft = Global.getIntSubDate(endDay);
        if (dayLeft < 0) {
            tvExpired.setVisibility(View.VISIBLE);
        } else {
            if (dayLeft < 4) {
                String currentDate = Global.convertDateTimeToString(new Date());
                if (Global.dateAndDate(currentDate, dateExpire)) {
                    tvDayleft.setText("Only Valid Today!");
                } else {
                    if (dateExpire.split(" ")[1].equals("00:00:00") && Global.dateAndDate(Global.convertDateTimeToString(Global.add1Day()), dateExpire)) {
                        tvDayleft.setText("Only Valid Today!");
                    } else {
                        tvDayleft.setText(Global.getTextSubDate(endDay) + " left");
                    }
                }
                tvDayleft.setVisibility(View.VISIBLE);
                ObjectAnimator objAnim = (ObjectAnimator) AnimatorInflater.loadAnimator(getActivity(), R.animator.waring);
                objAnim.setTarget(tvDayleft);
                objAnim.setEvaluator(new ArgbEvaluator());
                objAnim.start();
            }
            tvExpired.setVisibility(View.GONE);
        }


        promotionExits();
        return rootView;
    }

    private void visibleLayoutDetail(Context context, boolean isDetail) {
        loadItemBonus(context, isDetail);
        if (isDetail) {
            rlLayoutDetail.setVisibility(View.VISIBLE);
//            rlLayoutPromotion.setVisibility(View.GONE);
            tvExpired.setVisibility(View.GONE);
            fadeViewOut(rlLayoutPromotion);
            Picasso.with(context).load(mPromtionObject.getImageURL()).into(ivPromotionDetail, new Callback.EmptyCallback() {
                @Override
                public void onSuccess() {
                    isLoadedImage = true;
                    pbImageProgress.setVisibility(View.GONE);
                    super.onSuccess();
                }
            });
            String text = "<html><head>"
                    + "<style type=\"text/css\">body{color: #fff; background-color: #ffffffff;}"
                    + "</style></head>"
                    + "<body>"
                    + mPromtionObject.getDescription()
                    + "</body></html>";
            wvDescriptionDetail.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
            wvDescriptionDetail.setBackgroundColor(Color.TRANSPARENT);
            wvDescriptionDetail.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            setDateExpire();
        } else {
            if (Global.getIntSubDate(endDay) < 0) {
                tvExpired.setVisibility(View.VISIBLE);
            }
            rlLayoutPromotion.setVisibility(View.VISIBLE);
            fadeViewOut(rlLayoutDetail);
        }
    }

    private void setDateExpire() {
        String dateExpire = mPromtionObject.getEndTimeShow();
        tvExpire.setText("Expiry: " + dateExpire + "(" + Global.getTextSubDate(endDay) + ")");

    }

    private void loadItemBonus(final Context context, boolean isDetail) {
        ActionObject actionObject;
        if (isDetail) {

            actionObject = mPromtionObject.getShareBonus();
            int point = actionObject.getPoint();
            final List<BonusItemObject> bonusItemObjects = actionObject.getBonusItemObjects();
            rvItemBonusShare.setAdapter(new ItemBonusAdapter(bonusItemObjects));
            btShareImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLoadedImage) {
                        new SaveImageToDiskAndShare(context).execute();
                        ivDelete.setVisibility(View.VISIBLE);
                    } else {
                        ivDelete.setVisibility(View.GONE);
                        Toast.makeText(context, "Please wait loading image", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            if (isTreat) {
                actionObject = mPromtionObject.getUseBonus();
                ivDelete.setVisibility(View.VISIBLE);
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogDelete();

                    }
                });
            } else
                actionObject = mPromtionObject.getGrapBonus();
            final int point = actionObject.getPoint();
            final int xp = actionObject.getXp();
            final List<BonusItemObject> bonusItemObjects = actionObject.getBonusItemObjects();
            showBonus();
            tvGrab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grabClick(mDataItems, bonusItemObjects, point, xp);
                }
            });
        }

    }

    private void showBonus() {
        ActionObject actionObject;

        if (isTreat)
            actionObject = mPromtionObject.getUseBonus();
        else
            actionObject = mPromtionObject.getGrapBonus();
        final List<BonusItemObject> bonusItemObjects = actionObject.getBonusItemObjects();
        rvItemBonusGrap.setAdapter(new ItemBonusAdapter(bonusItemObjects));
    }

    private void grabClick(DataItems dataItems, List<BonusItemObject> bonusItemObjects, int point, int xp) {
        if (Global.getIntSubDate(endDay) < 0) {
            Toast.makeText(mContext, "Promotion expired!", Toast.LENGTH_SHORT).show();
        } else {
            if (isTreat) {
                if (HttpRequest.isOnline(mContext)) {
                    if (mPromtionObject.getCodeType() == 1) {
                        showDialogCode();
                    } else {
                        showDialogRedeem();

                    }
                } else {
                    Toast.makeText(mContext, "Please turn on internet first", Toast.LENGTH_SHORT).show();
                }

//                showDialogCode();
            } else {
//                dataItems.addActionToList(mPromtionObject.getPromotionId(), DataItems.ID_ACTION_GRAB, mPromtionObject.getBeaconID());
//                new SaveImageToDisk(mContext, dataItems, bonusItemObjects, point).execute();
                if (mSqliteHelper.getAllPromotion().size() >= 50) {
                    showTreasureBoxWhenLimit();
                } else {
                    new GrabPromotion(mContext, mPromtionObject.getPromotionId(), mPromtionObject.getBeaconID(), bonusItemObjects, point, xp).execute();
                }
            }
        }
    }

    private void showTreasureBoxWhenLimit() {
        if (alertDialogBuilder == null) {
            alertDialogBuilder = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            alertDialogBuilder.setTitle("Tomi");
            // Setting Dialog Message
            alertDialogBuilder.setMessage("The promotions grab exceed the allowed limit.");
            alertDialogBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
            // Setting Negative "NO" Button
            alertDialogBuilder.setNegativeButton("Open treasure box",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().finish();
                            Intent intent = new Intent(mContext, PromotionDialog.class);
                            intent.putExtra("TREAT", true);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
//        alertDialog.show();
            alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
            alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
            alertDialog = alertDialogBuilder.create();
        }
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void promotionExits() {
        if (mSqliteHelper.promotionExist(mPromtionObject.getPromotionId()) && isTreat == false) {
//            llBonusShow.setVisibility(View.GONE);
        }
    }

    private void showDialogCode() {
        if (dialog != null && dialog.isShowing()) return;
        
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setContentView(R.layout.dialog_code);
        final EditText etCode = (EditText) dialog.findViewById(R.id.etCode);
        Button btOkCode = (Button) dialog.findViewById(R.id.btOkCode);
        btOkCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpRequest.isOnline(mContext)) {

                    String code = etCode.getText().toString();
                    if (code.length() > 0) {
                        new DeletePromotion(mContext, true, code).execute();
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(mContext, "Please! Turn on internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Global.SHARE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mDataItems.addActionToList(mPromtionObject.getPromotionId(), DataItems.ID_ACTION_SHARE, mPromtionObject.getBeaconID());
                DataItems dataItems = new DataItems(mContext);
                grabClick(dataItems, mPromtionObject.getShareBonus().getBonusItemObjects(), mPromtionObject.getShareBonus().getPoint(), mPromtionObject.getShareBonus().getXp());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class SaveImageToDiskAndShare extends AsyncTask<Void, Void, Uri> {
        private Context mContext;

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
            if (isTreat)
                bmpUri = Uri.parse(mPromtionObject.getImageURL());
            else
                bmpUri = dataItems.getLocalShareBitmapUri(slideImageView, "share_image");
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

    public boolean isLoadedImage() {
        return isLoadedImage;
    }

    public PromtionObject getPromotionObject() {
        return mPromtionObject;
    }


    private void fadeViewOut(final View view) {
        view.setVisibility(View.GONE);
//        Animation fadeOut = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
//        fadeOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                view.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//
//        view.startAnimation(fadeOut);
    }

    class SaveImageToDisk extends AsyncTask<Void, Void, Uri> {
        private Context mContext;
        private DataItems mDataItems;
        private int mPoint;
        private int mXp;
        private String nameSave;
        private boolean mIsBonus;
        private int mType;
        private PromtionObject promtionObject;

        public SaveImageToDisk(Context context, DataItems dataItems, int point, int xp, boolean isBonus, PromtionObject promtionObject, int type) {
            mContext = context;
            mDataItems = dataItems;
            mPoint = point;
            mXp = xp;
            nameSave = mPromtionObject.getImageURL();
            mIsBonus = isBonus;
            this.promtionObject = promtionObject;
            mType = type;
        }


        @Override
        protected Uri doInBackground(Void... params) {
            DataItems dataItems = new DataItems(mContext);
            Uri bmpUri = dataItems.getLocalBitmapUri(slideImageView, nameSave);
            List<BonusItemObject> bonusUse = mPromtionObject.getUseBonus().getBonusItemObjects();
            ActionObject bonusUseTmpAction = mPromtionObject.getUseBonus();
            List<BonusItemObject> bonusUseTmp = new ArrayList<>();
            for (int i = 0; i < bonusUse.size(); i++) {
                BonusItemObject itemObject = bonusUse.get(i);
                itemObject.setImageURL(Global.saveImageItemBonusUse(itemObject.getImageURL(), itemObject.getItemId()));
                bonusUseTmp.add(itemObject);
            }
            bonusUseTmpAction.setBonusItemObjects(bonusUseTmp);
            mPromtionObject.setUseBonus(bonusUseTmpAction);

            List<BonusItemObject> bonusShare = mPromtionObject.getShareBonus().getBonusItemObjects();
            ActionObject bonusShareTmpAction = mPromtionObject.getShareBonus();
            List<BonusItemObject> bonusShareTmp = new ArrayList<>();
            for (int i = 0; i < bonusShare.size(); i++) {
                BonusItemObject itemObject = bonusShare.get(i);
                itemObject.setImageURL(Global.saveImageItemBonusShare(itemObject.getImageURL(), itemObject.getItemId()));
                bonusShareTmp.add(itemObject);
            }
            bonusShareTmpAction.setBonusItemObjects(bonusShareTmp);
            mPromtionObject.setShareBonus(bonusShareTmpAction);
            return bmpUri;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            mDialog.dismiss();
            if (uri != null) {
                mPromtionObject.setImageURL(uri.toString());
                if (mSqliteHelper.insertPromotion(mPromtionObject)) {
                    fadeViewOut(llBonusShow);
                    mDataItems.addPoint(mPoint, mXp);
                }
                if (mIsBonus) {
                    if (mType == 0) {
                        new DownloadPromotion(mContext, promtionObject).execute();
                    } else if (mType == 1||mType == -1) {
                        showDialogUpdateInfo(mType);
                    }
                }
            } else
                Toast.makeText(mContext, "Image saving failed", Toast.LENGTH_SHORT).show();
            super.onPostExecute(uri);
        }

    }

    private void showDialogUpdateInfo(int type) {
        mDataItems.setUpdateInfo(false);
        Intent intent = new Intent(mContext, InfoDialog.class);
        intent.putExtra(DataItems.EXTRA_BONUS_TYPE_KEY,type);
        startActivity(intent);
    }


    class DeletePromotion extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;
        private boolean mIsUse;
        private String mCode;

        public DeletePromotion(Context context, boolean isUse, String code) {
            mContext = context;
            mIsUse = isUse;
            mCode = code;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading..", true);
        }


        @Override
        protected JSONObject doInBackground(Void... params) {
            if (mIsUse) {
                return DataItems.UsePromotion(mContext, mPromtionObject.getPromotionId(), mCode, mPromtionObject.getBeaconID(), mPromtionObject.getCodeType());
            } else {
                mSqliteHelper.deletePromotion(mPromtionObject.getPromotionId());
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (mIsUse) {
                try {
                    if (result != null) {
                        if (result.getBoolean("state")) {
                            File file = new File(mPromtionObject.getImageURL());
                            file.delete();
                            mSqliteHelper.deletePromotion(mPromtionObject.getPromotionId());
                            int point = mPromtionObject.getUseBonus().getPoint();
                            int xp = mPromtionObject.getUseBonus().getXp();

                            List<ItemQTY> itemQTYList = new ArrayList<>();
                            JSONArray jsonArray = result.getJSONArray("item");
                            int length = jsonArray.length();
                            if (length > 0) {
                                for (int i = 0; i < length; i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    ItemObject itemObject = new ItemObject(object, true);
                                    int qty = object.getInt("qty");
                                    itemQTYList.add(new ItemQTY(itemObject, qty));
                                }
                                try {
                                    new DownloadBonusItemActionUse(mContext, point, xp).execute(itemQTYList);
                                } catch (Exception e) {
                                    mDialog.dismiss();
                                }
                            } else {
                                mDataItems.addPoint(point, xp);
                                mDialog.dismiss();
                                Toast.makeText(mContext, "Promotion used!!!", Toast.LENGTH_LONG).show();
                                getActivity().finish();
                            }

                        } else {
                            mDialog.dismiss();
                            Toast.makeText(mContext, "Code incorrect!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(mContext, "Connect to server failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(mContext, "Connect to server failed", Toast.LENGTH_SHORT).show();
                }

            } else {
                mDialog.dismiss();
                getActivity().finish();
            }

            Intent intent = new Intent(mContext, PromotionDialog.class);
            intent.putExtra("TREAT", true);
            intent.putExtra("RESTART", true);
            startActivity(intent);
            super.onPostExecute(result);
        }
    }


    public void showDialogShare(final Uri imageUri) {
        final Context context = mContext;
        if (dialog != null && dialog.isShowing()) return;
        dialog.setContentView(R.layout.dialog_share);
        dialog.setTitle("Share with..");
        // set the custom dialog components - text, image and button
        ListView lvListApplication = (ListView) dialog.findViewById(R.id.lvListApplication);
        List<ApplicationObject> lstApp = new ArrayList<>();
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
//                    shareIntent.setType("text/plain");
//                    shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Tomi");

                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download tomi here: https://play.google.com/store/apps/details?id=" + context.getPackageName());
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

                    PackageManager pm = context.getPackageManager();
                    List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
                    for (final ResolveInfo app : activityList) {
                        if ((app.activityInfo.packageName).equals(applicationObject.getPacketName())) {
                            final ActivityInfo activity = app.activityInfo;
                            final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);


//                            ComponentName name = new ComponentName(applicationObject.getPacketName(), applicationObject.getPacketName());
//                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                            shareIntent.setType("image/png");
                            shareIntent.setComponent(name);
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(shareIntent, Global.SHARE_REQUEST_CODE);
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

    public void showDialogRedeem() {
        if (dialog != null && dialog.isShowing())
            return;
        
        dialog.setContentView(R.layout.dialog_confirm_use_promotion);
//        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        TextView tvTitleModeSaving = (TextView) dialog.findViewById(R.id.tvTitleModeSaving);

        Button btOk = (Button) dialog.findViewById(R.id.btOk);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        // if button is clicked, close the custom dialog
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, UsePromotionActivity.class);
                intent.putExtra(DataItems.PROMOTION_ID_KEY, mPromtionObject.getPromotionId());
                intent.putExtra(DataItems.PROMOTION_NAME_KEY, mPromtionObject.getName());
                intent.putExtra(DataItems.PROMOTION_IMAGE_KEY, mPromtionObject.getImageURL());
                intent.putExtra(DataItems.PROMOTION_EXPIRY_KEY, mPromtionObject.getEndTimeShow());
                intent.putExtra(DataItems.PROMOTION_DESCRIPTION_KEY, mPromtionObject.getDescription());
                intent.putExtra(DataItems.PROMOTION_NOTE_KEY, mPromtionObject.getNote());
                intent.putExtra(DataItems.PROMOTION_TYPE_CODE_KEY, mPromtionObject.getCodeType());
                intent.putExtra(DataItems.BEACON_ID_KEY, mPromtionObject.getBeaconID());
                startActivity(intent);
                dialog.dismiss();
                getActivity().finish();
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

    public void showDialogDelete() {
        if (dialog != null && dialog.isShowing()) return;
        
        dialog.setContentView(R.layout.dialog_congratulations_use_promotion);
//        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button
        TextView tvTitleModeSaving = (TextView) dialog.findViewById(R.id.tvTitleModeSaving);

        Button btOk = (Button) dialog.findViewById(R.id.btOk);
        Button btCancel = (Button) dialog.findViewById(R.id.btCancel);
        tvTitleModeSaving.setText(Html.fromHtml("Are you sure you want to delete this promotion? <br/><b>Warning: You can't restore deleted</b> "));
        // if button is clicked, close the custom dialog
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataItems.addActionToList(mPromtionObject.getPromotionId(), DataItems.ID_ACTION_DELETE, mPromtionObject.getBeaconID());
                new DeletePromotion(mContext, false, "").execute();
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


    public void showDialogBonusPromotion(PromtionObject promtionObject) {
        if (dialog != null && dialog.isShowing()) return;
        
        dialog.setContentView(R.layout.dialog_bonus_promotion);
//        dialog.setTitle("Title...");

        // set the custom dialog components - text, image and button

        TextView tvId = (TextView) dialog.findViewById(R.id.tvId);
        WebView wvDescriptionDetail = (WebView) dialog.findViewById(R.id.wvDescriptionDetail);
        TextView tvExpire = (TextView) dialog.findViewById(R.id.tvExpire);
        ImageView ivPromotion = (ImageView) dialog.findViewById(R.id.ivPromotion);
        Button btOk = (Button) dialog.findViewById(R.id.btOk);
        final ProgressBar pbImageProgress = (ProgressBar) dialog.findViewById(R.id.pbImageProgress);

        String id = "NO. ";
        try {
            String s = promtionObject.getBeaconID().replace("28788_", "");
            id += s + "-" + promtionObject.getPromotionId();
        } catch (Exception e) {
        }
        tvId.setText(id);
        String text = "<html><head>"
                + "<style type=\"text/css\">body{color: #fff; background-color: #ffffffff;}"
                + "</style></head>"
                + "<body>"
                + mPromtionObject.getDescription()
                + "</body></html>";
        wvDescriptionDetail.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);
        wvDescriptionDetail.setBackgroundColor(Color.TRANSPARENT);
        wvDescriptionDetail.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        tvExpire.setText("Expiry: " + promtionObject.getEndTimeShow());
        Picasso.with(mContext).load(promtionObject.getImageURL()).into(ivPromotion, new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                pbImageProgress.setVisibility(View.GONE);
            }
        });

        // if button is clicked, close the custom dialog
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    class GrabPromotion extends AsyncTask<Void, Void, JSONObject> {
        private Context mContext;

        private String mPromotionID;
        private String mBeaconID;
        private List<BonusItemObject> mBonusItemObjects;
        private int mPoint;
        private int mXp;

        public GrabPromotion(Context context, String promotionID, String beaconID, List<BonusItemObject> bonusItemObjects, int point, int xp) {
            mContext = context;
            mPromotionID = promotionID;
            mBeaconID = beaconID;
            mBonusItemObjects = bonusItemObjects;
            mPoint = point;
            mXp = xp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading..", true);
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return DataItems.GrabPromotionRequest(mContext, mPromotionID, mBeaconID);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    if (jsonObject.getBoolean("state")) {
                        PromtionObject tmp=mSqliteHelper.getPromotionWithID(mPromotionID);
                        if ( tmp!= null) {
                            mSqliteHelper.updatePromotion(mPromotionID, tmp.getTotal() + 1);
                            mDialog.dismiss();
                        }else {
                            List<ItemQTY> itemQTYList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("item");
                            PromtionObject promtionObject = null;
                            boolean isBonus = jsonObject.getBoolean("bonus");
                            int bonusType = jsonObject.getInt("bonus_type");
                            if (isBonus && bonusType == 0) {
                                promtionObject = new PromtionObject(jsonObject.getJSONObject("promotion_bonus"));
                            }
                            int length = jsonArray.length();
                            if (length > 0) {
                                for (int i = 0; i < length; i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    ItemObject itemObject = new ItemObject(object, true);

                                    int qty = object.getInt("qty");
                                    itemQTYList.add(new ItemQTY(itemObject, qty));
                                }
                                try {
                                    new DownloadBonusItem(mPoint, mXp, isBonus, promtionObject, bonusType).execute(itemQTYList).get(120000, TimeUnit.MILLISECONDS);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (TimeoutException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                new SaveImageToDisk(mContext, mDataItems, mPoint, mXp, isBonus, promtionObject, bonusType).execute();
                            }
                        }

                    } else {
                        Toast.makeText(mContext, jsonObject.getString("error_description"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, "Connect server fail", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(jsonObject);
        }
    }

    class DownloadBonusItem extends AsyncTask<List<ItemQTY>, Void, Void> {
        private int mPoint;
        private int mXP;
        private boolean mIsBonus;
        private PromtionObject promtionObject;
        private int mType;

        public DownloadBonusItem(int point, int xp, boolean isBonus, PromtionObject promtionObject, int type) {
            mPoint = point;
            mXP = xp;
            mIsBonus = isBonus;
            this.promtionObject = promtionObject;
            mType = type;
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
            new SaveImageToDisk(mContext, mDataItems, mPoint, mXP, mIsBonus, promtionObject, mType).execute();
            super.onPostExecute(aVoid);
        }
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
            mDialog.dismiss();
            mDataItems.addPoint(mPoint, mXP);
            Toast.makeText(mContext, "Promotion used!!!", Toast.LENGTH_LONG).show();
            getActivity().finish();
            super.onPostExecute(aVoid);
        }
    }


    class DownloadPromotion extends AsyncTask<Void, Void, Boolean> {
        private Context mContext;
        private PromtionObject mPromtionObject;
        private ProgressDialog mDialog;

        public DownloadPromotion(Context context, PromtionObject promtionObject) {
            this.mContext = context;
            this.mPromtionObject = promtionObject;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(mContext, null,
                    "Loading..", true);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return Global.savePromotion(mPromtionObject, mSqliteHelper);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mDialog.dismiss();
            if (aBoolean) {
                showDialogBonusPromotion(mPromtionObject);
            }
            super.onPostExecute(aBoolean);
        }
    }

    private void restoreImagePromotions() {
        if (HttpRequest.isOnline(mContext)) {
            File file = new File(Uri.parse(mPromtionObject.getImageURL()).getPath());
            if (!file.exists()) {
                String urlImage = DataItems.LINK_API + "/cms/gallery/" + mPromtionObject.getImageURL().substring(mPromtionObject.getImageURL().lastIndexOf('/') + 1);
                new DownloadImagePromotions(urlImage).execute();
            }
        }
    }

    class DownloadImagePromotions extends AsyncTask<Void, Void, String> {
        private String mUrlImage = "";

        public DownloadImagePromotions(String urlImage) {
            this.mUrlImage = urlImage;
            tvWarningNotFoundImage.setVisibility(View.GONE);
            pbImageProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            return Global.saveImagePromotion(mUrlImage);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.isEmpty()) {
                Picasso.with(mContext).load(mPromtionObject.getImageURL()).error(R.drawable.ic_image_error).into(slideImageView, new Callback.EmptyCallback() {
                    @Override
                    public void onSuccess() {
                        isLoadedImage = true;
                        if (!isTreat && AppTourDialog.PROMOTION_SELECTED.equals(mPromtionObject.getPromotionId())) {
                            if (!PromotionDialog.promotionExist(mPromtionObject.getPromotionId()))
                                PromotionDialog.addViewPromotion(mPromtionObject);
                        }
                        pbImageProgress.setVisibility(View.GONE);
                        super.onSuccess();
                    }
                });
            }
        }
    }
}
