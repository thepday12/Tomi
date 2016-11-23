package com.theptk.share;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thep on 10/23/2015.
 */
public class ChooseApplicationShare  {
    private static final int SHARE_REQUEST_CODE=1912;
    public static void ShowDialog(Context context, final Uri imageUri){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_share);
        dialog.setTitle("Share with..");

        // set the custom dialog components - text, image and button
        ListView lvListApplication = (ListView) dialog.findViewById(R.id.lvListApplication);
        List<ApplicationObject> lstApp= new ArrayList<>();
        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.contains("facebook")||packageInfo.packageName.contains("com.whatsapp")||packageInfo.packageName.contains("com.tencent.mm")||packageInfo.packageName.contains("com.viber.voip")||packageInfo.packageName.contains("com.instagram.android"))
                lstApp.add(new ApplicationObject(packageInfo.packageName,packageInfo.loadLabel(pm).toString(),packageInfo.loadIcon(pm)));
        }
        ApplicationAdapter adapter = new ApplicationAdapter(context,lstApp);
        lvListApplication.setAdapter(adapter);
        lvListApplication.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/png");
                startActivityForResult(shareIntent, SHARE_REQUEST_CODE);
            }
        });
        dialog.show();
    }
}
