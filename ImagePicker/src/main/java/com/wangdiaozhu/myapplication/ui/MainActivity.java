package com.wangdiaozhu.myapplication.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


import com.wangdiaozhu.myapplication.R;
import com.wangdiaozhu.myapplication.adapter.MainGVAdapter;
import com.wangdiaozhu.myapplication.utils.ScreenUtils;
import com.wangdiaozhu.myapplication.utils.Utility;

import java.util.ArrayList;

/**
 * ��ҳ�棬����ת�����ѡ����Ƭ�����ڴ�ҳ����ʾ��ѡ�����Ƭ��
 * Created by hanj on 14-10-13.
 */
public class MainActivity extends Activity {
    private MainGVAdapter adapter;
    private ArrayList<String> imagePathList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //��ȡ��Ļ����
        ScreenUtils.initScreen(this);

        TextView titleTV = (TextView) findViewById(R.id.topbar_title_tv);
        Button selectImgBtn = (Button) findViewById(R.id.main_select_image);
        GridView mainGV = (GridView) findViewById(R.id.main_gridView);

        titleTV.setText(R.string.app_name);
        imagePathList = new ArrayList<String>();
        adapter = new MainGVAdapter(this, imagePathList);
        mainGV.setAdapter(adapter);

        selectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //��ת�����յ�ѡ��ͼƬҳ��
                Intent intent = new Intent(MainActivity.this, PhotoWallActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int code = intent.getIntExtra("code", -1);
        if (code != 100) {
            return;
        }

        ArrayList<String> paths = intent.getStringArrayListExtra("paths");
        //��ӣ�ȥ��
        boolean hasUpdate = false;
        for (String path : paths) {
            if (!imagePathList.contains(path)) {
                //���9��
                if (imagePathList.size() == 9) {
                    Utility.showToast(this, "�������9��ͼƬ��");
                    break;
                }

                imagePathList.add(path);
                hasUpdate = true;
            }
        }

        if (hasUpdate) {
            adapter.notifyDataSetChanged();
        }
    }
}
