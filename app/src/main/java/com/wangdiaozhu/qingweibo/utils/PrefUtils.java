package com.wangdiaozhu.qingweibo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/4/13.
 */
public class PrefUtils {

    public static void putBoolean(String key, boolean value,Context ctx){

       SharedPreferences preferences =  ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
          preferences.edit().putBoolean(key,value).commit();

    }
    public static  boolean getBoolean(String key, boolean devalue,Context ctx){
        SharedPreferences preferences =  ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
            return  preferences.getBoolean(key,devalue);

    }
    public static void putString(String key, String value,Context ctx){

        SharedPreferences preferences =  ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).commit();

    }
    public static  String getString(String key, String devalue,Context ctx){
        SharedPreferences preferences =  ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return  preferences.getString(key, devalue);

    }
    public static void putInt(String key, int value,Context ctx){

        SharedPreferences preferences =  ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).commit();

    }
    public static  int getInt(String key, int devalue,Context ctx){
        SharedPreferences preferences =  ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        return  preferences.getInt(key, devalue);

    }
    public static void  remove(String key,Context ctx){

        SharedPreferences preferences = ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        preferences.edit().remove(key).commit();


    }
}
