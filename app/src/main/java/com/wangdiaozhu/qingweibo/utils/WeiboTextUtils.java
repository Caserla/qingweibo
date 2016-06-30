package com.wangdiaozhu.qingweibo.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.wangdiaozhu.qingweibo.Constants.Constants;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.activity.MainActivity;
import com.wangdiaozhu.qingweibo.bean.Emotions;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConstants;

/**
 * Created by Administrator on 2016/6/20.
 */
public class WeiboTextUtils {
    /**
     * 高亮部分文本
     * @param content  文本内容
     * @return
     *
     */
    /**微博短链接正则表达式*/
         static final String regex_http = "http(s)?://([a-zA-Z|\\d]+\\.)+[a-zA-Z|\\d]+(/[a-zA-Z|\\d|\\-|\\+|_./?%=]*)?";
          static final String regex_at = "@[\\u4e00-\\u9fa5\\w\\-]+";
          static final String regex_sharp="#([^\\#|.]+)#";
         static final String regex_emoji="\\[[a-zA-Z0-9\\u4e00-\\u9fa5]+\\]";
    private static String str;

    public static SpannableString setTextHighLight(String content,Context context) {
        SpannableString result = new SpannableString(content);

        if (content.contains("@")) {
            Pattern p = Pattern.compile(regex_at);
            Matcher m = p.matcher(result);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                result.setSpan(
                        (new ForegroundColorSpan(Color.parseColor("#33b5e5"))),
                        start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }

        if (content.contains("#") ){
            Pattern p = Pattern.compile(regex_sharp);
            Matcher m = p.matcher(result);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                result.setSpan(
                        (new ForegroundColorSpan(Color.parseColor("#ff7d00"))),
                        start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }

        if (content.contains("http://") ){
            Pattern p = Pattern.compile(regex_http);
            Matcher m = p.matcher(result);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                result.setSpan(
                        (new ForegroundColorSpan(Color.parseColor("#33b5e5"))),
                        start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
        if(content.contains("[")&&content.contains("]")){
            Pattern p = Pattern.compile(regex_emoji);
            Matcher m = p.matcher(result);
            while(m.find()){
                int start = m.start();
                int end = m.end();
                String phrase = content.substring(start, end);
                String imageName = "";

                    List<Emotions> list = MainActivity.emotionList;

                for (Emotions emotions : list) {
                    if (emotions.getPhrase().equals(phrase)) {
                        imageName = emotions.getImageName();
                        str = imageName.replace(".gif","");
                    }
                }

                try {
                    if (str != null) {
                        Field f = R.drawable.class.getDeclaredField(str);

                        int i = f.getInt(R.drawable.class);
                        Drawable drawable = context.getResources().getDrawable(i);
                        if (drawable != null) {
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                                    drawable.getIntrinsicHeight());
                            ImageSpan span = new ImageSpan(drawable,
                                    ImageSpan.ALIGN_BASELINE);
                            result.setSpan(span, start, end,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }else {result = null;}
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                }

            }

        }


        return result;
    }
}
