package com.wangdiaozhu.qingweibo.base;

import android.app.Activity;
import android.view.View;

import com.wangdiaozhu.qingweibo.activity.StatusActivity;

/**
 * Created by Administrator on 2016/6/15.
 */
public abstract class BaseDetailPager  {

    public StatusActivity mActivity;

    public View mRootView;

  public   BaseDetailPager(Activity activity){

       mActivity = (StatusActivity) activity;

         mRootView = initView();
  }

    public  abstract View initView();

    public  abstract  void initData();

}
