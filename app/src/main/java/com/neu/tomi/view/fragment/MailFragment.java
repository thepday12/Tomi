package com.neu.tomi.view.fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.tomi.R;
import com.neu.tomi.object.MailObject;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.neu.tomi.view.DailyActivity;
import com.neu.tomi.view.GameSelectActivity;
import com.neu.tomi.view.VisitWebsiteActivity;
import com.neu.tomi.view.dialog.ProfileDialog;
import com.neu.tomi.view.dialog.PromotionDialog;
import com.neu.tomi.view.dialog.ShopDialog;

public class MailFragment extends Fragment {

    private TextView tvMailTitle, tvMailDate, tvMailContent;
    private Button btMailLink;

    public static MailFragment newInstance(MailObject mailObject) {
        MailFragment mailFragment = new MailFragment();
        Bundle args = new Bundle();
        args.putString("MAIL_ID", mailObject.getId());
        args.putString("MAIL_CONTENT", mailObject.getContent());
        args.putString("MAIL_DATE", mailObject.getDate());
        args.putString("MAIL_LINK", mailObject.getLink());
        args.putString("MAIL_LINK_CAPTION", mailObject.getLinkCaption());
        args.putString("MAIL_TITLE", mailObject.getTitle());
        args.putInt("MAIL_TYPE", mailObject.getLinkType());
        mailFragment.setArguments(args);
        return mailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_mail_detail, container, false);
        tvMailTitle = (TextView) rootView.findViewById(R.id.tvMailTitle);
        tvMailDate = (TextView) rootView.findViewById(R.id.tvMailDate);
        tvMailContent = (TextView) rootView.findViewById(R.id.tvMailContent);
        btMailLink = (Button) rootView.findViewById(R.id.btMailLink);
        final String mailId = getArguments().getString("MAIL_ID");
        String mailContent = getArguments().getString("MAIL_CONTENT");
        String mailDate = getArguments().getString("MAIL_DATE");
        final String mailLink = getArguments().getString("MAIL_LINK");
        final String mailLinkCaption = getArguments().getString("MAIL_LINK_CAPTION");
        String mailTitle = getArguments().getString("MAIL_TITLE");
        final int mailType = getArguments().getInt("MAIL_TYPE", 9);

        btMailLink.setText(mailLinkCaption);
        tvMailTitle.setText(mailTitle);
        tvMailDate.setText("Date: " + mailDate);
        tvMailContent.setText(Html.fromHtml(mailContent));
        clearNotification(rootView.getContext(), mailId);
        final Context context = getContext();
        switch (mailType) {
            case 1: {
                btMailLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ProfileDialog.class);
                        startActivity(intent);
                    }
                });
                break;
            }
            case 2: {
                btMailLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ShopDialog.class);
                        startActivity(intent);
                    }
                });
                break;
            }
            case 3: {

                break;
            }
            case 4: {
                btMailLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataItems dataItems = new DataItems(context);
                        String beaconInfo = dataItems.getListBeaconDetect();
                        if (beaconInfo.isEmpty()) {
                            Toast.makeText(context, "You did not miss anything", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(context, PromotionDialog.class);
                            intent.putExtra("TREAT", false);
                            intent.putExtra("MISS", true);
                            intent.putExtra("WID", 0);
                            intent.putExtra("INFO", beaconInfo);
                            startActivity(intent);
                        }
                    }
                });
                break;
            }
            case 5: {
                btMailLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, PromotionDialog.class);
                        intent.putExtra("TREAT", true);
                        startActivity(intent);
                    }
                });
                break;
            }
            case 6: {
                btMailLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, GameSelectActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            }
            case 7: {
                btMailLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DailyActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            }case 10: {
                btMailLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String beaconInfo=mailLink;
                        Intent intent = new Intent(context, PromotionDialog.class);
                        intent.putExtra("TREAT", false);
                        intent.putExtra("MISS", true);
                        intent.putExtra("WID", 0);
                        intent.putExtra("INFO", beaconInfo);
                        startActivity(intent);
                    }
                });
                break;
            }
            default: {
                if (mailLink != null) {
                    if (!mailLink.isEmpty()) {
                        btMailLink.setVisibility(View.VISIBLE);
                        btMailLink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String link = mailLink.replace(".user_id.", DataItems.getPhoneId(context));
                                if (mailType == 8) {
                                    Intent intent = new Intent(getContext(), VisitWebsiteActivity.class);
                                    intent.putExtra(DataItems.PROMOTION_REDIRECT_KEY, link);
                                    intent.putExtra(DataItems.STATUS_REDIRECT_KEY, true);
                                    startActivity(intent);
                                } else {
                                    Uri uri=Uri.parse(link);
                                    if(uri!=null) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                }

                            }
                        });
                    } else {
                        btMailLink.setVisibility(View.GONE);
                    }
                } else {
                    btMailLink.setVisibility(View.GONE);
                }
                break;
            }
        }

        return rootView;
    }

    private void clearNotification(Context context, String mailId) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Global.NOTIFICATION_ID);
            notificationManager.cancel(Integer.valueOf(mailId));
        } catch (Exception ex) {

        }
    }


}

