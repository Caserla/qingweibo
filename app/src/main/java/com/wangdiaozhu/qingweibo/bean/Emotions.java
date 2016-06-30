package com.wangdiaozhu.qingweibo.bean;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Emotions implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /** 表情使用的替代文字 */
    private String phrase;
    /** 表情图片类型 */
    private String type;
    /** 表情图片存放的位置 */
    private String url;
    /** 是否为热门表情 */
    private boolean isHot;
    /** 是否属于通用 */
    private boolean isCommon;
    /** 表情分类 */
    private String category;
    /** 表情名称 */
    private String imageName;

    private String picid;

    private  String value;

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(boolean isHot) {
        this.isHot = isHot;
    }

    public boolean getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(boolean isCommon) {
        this.isCommon = isCommon;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public static  Emotions parse(JSONObject jsonObject){

          if (jsonObject == null){
              return  null;
          }

        Emotions emotions = new Emotions();



            emotions.category = jsonObject.optString("category");
            emotions.isCommon = jsonObject.optBoolean("common", false);
            emotions.isHot = jsonObject.optBoolean("hot", false);
            emotions.imageName = jsonObject.optString("icon");
            emotions.phrase = jsonObject.optString("phrase");
            emotions.picid = jsonObject.optString("picid");
            emotions.type =jsonObject.optString("type");
            emotions.url = jsonObject.optString("url");
            emotions.value = jsonObject.optString("value");



             return  emotions;
    }

    @Override
    public String toString() {
        return "Emotions{" +
                "phrase='" + phrase + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", isHot=" + isHot +
                ", isCommon=" + isCommon +
                ", category='" + category + '\'' +
                ", imageName='" + imageName + '\'' +
                ", picid='" + picid + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}