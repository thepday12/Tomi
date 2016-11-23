package com.neu.tomi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.neu.tomi.R;
import com.neu.tomi.object.ItemObject;
import com.neu.tomi.ultity.DataItems;
import com.neu.tomi.ultity.Global;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Thep on 9/15/2015.
 */
public class ShopAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ItemObject> mListItem;
    private ViewHoler mHoler;

    public ShopAdapter(Context context, List<ItemObject> lstMenu) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListItem = lstMenu;
    }

    @Override
    public int getCount() {
        return mListItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        if (view == null) {
            view = mInflater.inflate(R.layout.row_item_shop, null);
            mHoler = new ViewHoler();
            mHoler.tvName = (TextView) view.findViewById(R.id.tvName);
            mHoler.tvItemPoint = (TextView) view.findViewById(R.id.tvItemPoint);
            mHoler.tvLimited = (TextView) view.findViewById(R.id.tvLimited);
            mHoler.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            mHoler.ivItemIcon = (ImageView) view.findViewById(R.id.ivItemIcon);
            view.setTag(mHoler);
        } else {
            mHoler = (ViewHoler) view.getTag();
        }
        final ItemObject itemObject = mListItem.get(position);
        if (itemObject.isResource()) {
            mHoler.ivItemIcon.setImageResource(itemObject.getIconResouce());
        } else {
            Picasso.with(mContext).load(DataItems.uriIconItem(itemObject, mContext)).into(mHoler.ivItemIcon);
        }
        mHoler.tvName.setText(itemObject.getName());
        int limit =itemObject.getLimit();
        if(limit>0) {
            mHoler.tvLimited.setText("Limited: "+limit);

        }else if(limit ==0) {
            if(itemObject.getActionId().equals(Global.TYPE_TOMI_ACTION_VOUCHER)){
                mHoler.tvLimited.setText("Fully Redeem");
            }else {
                mHoler.tvLimited.setText("Limited: " + 0);
            }
        }else{
            mHoler.tvLimited.setText("Unlimited");
        }
        mHoler.tvPrice.setText(Global.formatNumber(itemObject.getPrice()) + "pts");
        int pts = itemObject.getPoint();
        int xp = itemObject.getPointType();
        String bonus = "";

        if (xp > 0) {
            bonus += xp + "XP";
            if (pts > 0) {
                bonus += "-" + pts + "PT";
            }
        } else {
            if (pts > 0) {
                bonus = pts + "PT";
            }
        }
        mHoler.tvItemPoint.setText(bonus);
//        mHoler.tvBuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mDataItems.getIntBalancePoint()>itemObject.getPrice()) {
//                    if(itemObject.getActionId().equals(Global.TYPE_TOMI_ACTION_EAT)){
//                        if(mDataItems.getFoodItemList().size()>9){
//                            Toast.makeText(mContext,"Max item!!",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
//                    new BuyItem(itemObject).execute();
//                }else{
//                    Toast.makeText(mContext,"Not enough!!",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        if (itemObject.getActionId().equals(Global.TYPE_TOMI_THEME)) {
//            String themeList = mDataItems.getTheme();
//            if(!themeList.isEmpty()){
//            String[] themes = themeList.split(";");
//            int itemId = itemObject.getId();
//
//            for (String item : themes) {
//                if (itemId == Integer.valueOf(item)) {
//                    mHoler.tvUse.setEnabled(true);
//                    mHoler.tvBuy.setEnabled(false);
//                    mHoler.tvUse.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Global.isChangeTheme=true;
//                            Global.idThemeItem=itemObject.getId();
//                        }
//                    });
//                    break;
//                }
//            }
//            }
//        }
        return view;
    }

    private class ViewHoler {
        ImageView ivItemIcon;
        TextView tvName, tvItemPoint, tvPrice,tvLimited;
    }

}
