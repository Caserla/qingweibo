package com.wangdiaozhu.qingweibo.bean;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/21.
 */
public class EmotionList {


    public ArrayList<Emotions> emotionsArrayList;
    Emotions emotions;

    public static EmotionList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        EmotionList emotionList = new EmotionList();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            if(jsonArray!=null && jsonArray.length()>0){
                int length = jsonArray.length();
                 emotionList.emotionsArrayList = new ArrayList<Emotions>(length);
                for (int x = 0;x<length;x++){

                    emotionList.emotionsArrayList.add(Emotions.parse(jsonArray.getJSONObject(x)));

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return emotionList;
    }
}
