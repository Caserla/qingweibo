package com.wangdiaozhu.qingweibo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;
import com.wangdiaozhu.qingweibo.Constants.AccessTokenKeeper;
import com.wangdiaozhu.qingweibo.Constants.Constants;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.activity.EditActivity;
import com.wangdiaozhu.qingweibo.activity.MainActivity;
import com.wangdiaozhu.qingweibo.base.BaseFragment;
import com.wangdiaozhu.qingweibo.base.BasePager;
import com.wangdiaozhu.qingweibo.pager.DiscoverPager;
import com.wangdiaozhu.qingweibo.pager.HomePager;
import com.wangdiaozhu.qingweibo.pager.MessagePager;
import com.wangdiaozhu.qingweibo.pager.ProfilePager;
import com.wangdiaozhu.qingweibo.utils.PrefUtils;
import com.wangdiaozhu.qingweibo.view.NavigationTabView;



import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/5.
 */
public class ContentFragment extends BaseFragment implements View.OnClickListener{

    @ViewInject(R.id.vp_content)
    private ViewPager mViewPager;

    @ViewInject(R.id.ntv_main_tab_home)
     private NavigationTabView ntv_main_tab_home;
    @ViewInject(R.id.ntv_main_tab_discover)
    private NavigationTabView ntv_main_tab_discover;
    @ViewInject(R.id.ntv_main_tab_message)
    private NavigationTabView ntv_main_tab_message;
    @ViewInject(R.id.ntv_main_tab_profile)
    private NavigationTabView ntv_main_tab_profile;
    @ViewInject(R.id.nav_mian_tab_compose)
    private ImageButton nav_mian_tab_compose;
    private ArrayList<BasePager> mPagers;
    private ArrayList<Status> statuses;
    private User user;


    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.fragment_content,null);
        ViewUtils.inject(this, view);
        return view;
    }

    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        switch (msg.what){

            case 1:
                setUser(user);
                break;
            case 2:
                setStatuses(statuses);
                if (mPagers!= null){
                mPagers.get(0).initData();
                ntv_main_tab_home.setSelected(true);
                ntv_main_tab_message.setSelected(false);
                ntv_main_tab_discover.setSelected(false);
                ntv_main_tab_profile.setSelected(false);
                }
                   break;


        }


        }
    };

    @Override
    public void initData() {

        mPagers = new ArrayList<BasePager>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new MessagePager(mActivity));
        mPagers.add(new DiscoverPager(mActivity));
        mPagers.add(new ProfilePager(mActivity));

            ContentAdapter mAdapter = new ContentAdapter();
        mViewPager.setAdapter(mAdapter);
        ntv_main_tab_home.setOnClickListener(this);
        ntv_main_tab_message.setOnClickListener(this);
        ntv_main_tab_discover.setOnClickListener(this);
        ntv_main_tab_profile.setOnClickListener(this);
        nav_mian_tab_compose.setOnClickListener(this);

           Bundle bundle = getArguments();


        if (bundle.getSerializable("user")!= null) {

            user = (User) bundle.getSerializable("user");

            Message msg = new Message();
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);

        }

        if( bundle.getSerializable("statues") != null){

            statuses = (ArrayList<Status>) bundle.getSerializable("statues");
            Message msg = new Message();
            msg.what = 2;
            handler.sendEmptyMessage(msg.what);
        }
        System.out.println("userInfo" + user);
        System.out.println("statusesInfo" + statuses);

    }
//            获取当前登录用户所关注用户的最新微博

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ntv_main_tab_home:
                mViewPager.setCurrentItem(0, false);
                mPagers.get(0).initData();
                ntv_main_tab_home.setSelected(true);
                ntv_main_tab_message.setSelected(false);
                ntv_main_tab_discover.setSelected(false);
                ntv_main_tab_profile.setSelected(false);
//                ntv_main_tab_home.imageAnim();
                break;
            case R.id.ntv_main_tab_message:
                mViewPager.setCurrentItem(1, false);
                mPagers.get(1).initData();
                ntv_main_tab_message.setSelected(true);
                ntv_main_tab_home.setSelected(false);
                ntv_main_tab_discover.setSelected(false);
                ntv_main_tab_profile.setSelected(false);
//                ntv_main_tab_message.imageAnim();
                break;
            case R.id.ntv_main_tab_discover:
                mViewPager.setCurrentItem(2, false);
                mPagers.get(2).initData();
                ntv_main_tab_home.setSelected(false);
                ntv_main_tab_message.setSelected(false);
                ntv_main_tab_discover.setSelected(true);
                ntv_main_tab_profile.setSelected(false);
//                ntv_main_tab_discover.imageAnim();
                break;
            case R.id.ntv_main_tab_profile:
                mViewPager.setCurrentItem(3, false);
                mPagers.get(3).initData();
                ntv_main_tab_home.setSelected(false);
                ntv_main_tab_message.setSelected(false);
                ntv_main_tab_discover.setSelected(false);
                ntv_main_tab_profile.setSelected(true);
//                ntv_main_tab_profile.imageAnim();
                break;
            case R.id.nav_mian_tab_compose:
                Intent intent = new Intent(mActivity, EditActivity.class);
                     startActivity(intent);


                break;

            default:

                break;
        }
    }
    class ContentAdapter extends PagerAdapter{


        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            BasePager pager = mPagers.get(position);
            container.addView(pager.mRootView);
            return pager.mRootView;
        }



        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
