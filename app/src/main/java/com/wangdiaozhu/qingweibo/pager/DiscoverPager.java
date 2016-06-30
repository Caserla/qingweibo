package com.wangdiaozhu.qingweibo.pager;

import android.app.Activity;
import android.view.View;

import com.wangdiaozhu.qingweibo.activity.MainActivity;
import com.wangdiaozhu.qingweibo.base.BasePager;

/**
 * Created by Administrator on 2016/6/5.
 */
public class DiscoverPager extends BasePager {


    public DiscoverPager(Activity activity) {

        super(activity);
    }

    @Override
    public void initData() {

        tv_public_title_middle.setText("发现");
        fl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("dsadasdadasdsdasdas");
            }
        });
    }
}
