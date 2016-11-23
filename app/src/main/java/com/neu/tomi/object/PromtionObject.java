package com.neu.tomi.object;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Thep on 10/22/2015.
 */
public class PromtionObject {

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActionObject getGrapBonus() {
        return grapBonus;
    }

    public void setGrapBonus(ActionObject grapBonus) {
        this.grapBonus = grapBonus;
    }

    public ActionObject getUseBonus() {
        return useBonus;
    }

    public void setUseBonus(ActionObject useBonus) {
        this.useBonus = useBonus;
    }

    public ActionObject getShareBonus() {
        return shareBonus;
    }

    public void setShareBonus(ActionObject shareBonus) {
        this.shareBonus = shareBonus;
    }

    public String getBeaconID() {
        return beaconID;
    }

    public void setBeaconID(String beaconID) {
        this.beaconID = beaconID;
    }


    public String getPromotionLink() {
        return promotionLink;
    }

    public void setPromotionLink(String promotionLink) {
        this.promotionLink = promotionLink;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getEndTimeShow() {
        String[] items = endTime.split(" ");
        return items[0];
    }

    public int getCodeType() {
        return codeType;
    }

    public void setCodeType(int codeType) {
        this.codeType = codeType;
    }

    public int getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(int showUrl) {
        this.showUrl = showUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    private String promotionId;
    private String promotionLink;
    private String imageURL;
    private String endTime;
    private String description;
    private String beaconID;
    private ActionObject grapBonus;
    private ActionObject useBonus;
    private ActionObject shareBonus;
    private int codeType;
    private String name;
    private String note;
    private int total;
    private int showUrl;

    public PromtionObject(JSONObject jsonObject) {
        try {
            setPromotionId(jsonObject.getString("promo_id"));
            setPromotionLink(jsonObject.getString("promo_link"));
            setImageURL(jsonObject.getString("img_url"));
            setEndTime(jsonObject.getString("end_time"));
            setDescription(jsonObject.getString("description"));
            setNote(jsonObject.getString("note"));
            setBeaconID(jsonObject.getString("beacon_id"));
            setGrapBonus(new ActionObject(jsonObject.getJSONObject("grab")));
            setUseBonus(new ActionObject(jsonObject.getJSONObject("use")));
            setShareBonus(new ActionObject(jsonObject.getJSONObject("share")));


            setName(jsonObject.getString("promo_name"));
            try {
                setShowUrl(jsonObject.getInt("showURL"));
            } catch (Exception e) {
                setShowUrl(1);
            }

            try {
                setTotal(jsonObject.getInt("total"));
            } catch (Exception e) {
                setTotal(1);
            }
            try {
                setCodeType(jsonObject.getInt("code_type"));
            } catch (Exception e) {
                setCodeType(1);
            }
        } catch (JSONException e) {
        }
    }

    public PromtionObject(String adsId, String imageURL, String description,String note, ActionObject useBonus, ActionObject shareBonus, String endTime, String beaconID, String promoLink, int total, int showUrl, int codeType, String name) {
        setPromotionId(adsId);
        setImageURL(imageURL);
        setDescription(description);
        setNote(note);
        setUseBonus(useBonus);
        setShareBonus(shareBonus);
        setEndTime(endTime);
        setBeaconID(beaconID);
        setPromotionLink(promoLink);
        setTotal(total);
        setCodeType(codeType);
        setShowUrl(showUrl);
        setName(name);

    }

}
