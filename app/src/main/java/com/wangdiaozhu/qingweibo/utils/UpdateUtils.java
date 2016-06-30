package com.wangdiaozhu.qingweibo.utils;

import android.content.Context;

import com.wangdiaozhu.qingweibo.bean.Emotions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/22.
 */
public class UpdateUtils {

    public static void getFriendList(ArrayList<Emotions> emotionsArrayList,Context context) throws Exception {

        List<Emotions> list =emotionsArrayList;
        StringBuilder builder = new StringBuilder();

        for (Emotions emotions : list) {
            String path = emotions.getUrl();
            String pharse = emotions.getPhrase();
            String filename = path.substring(path.lastIndexOf("/") + 1, path.length());
            String str = pharse+"\n"+filename+"\n";
            builder.append(str);
            URL url =  new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            System.out.println("请求失败"+conn.getResponseCode());
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000000);
            System.out.println("请求失败"+conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                InputStream is = conn.getInputStream();
                byte[] data = readStream(is);
                File file = new File(context.getFilesDir().getPath().toString()+ filename);
                FileOutputStream fs = new FileOutputStream(file);
                fs.write(data);
                fs.close();
            }else{
                System.out.println("请求失败");
            }
        }
             String str = builder.toString();
                FileUtils.saveFile(str);





    }

    public static byte[] readStream(InputStream is) throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while((len = is.read(buffer)) != -1){
            os.write(buffer,0,len);
        }
        is.close();
        return os.toByteArray();
    }

}
