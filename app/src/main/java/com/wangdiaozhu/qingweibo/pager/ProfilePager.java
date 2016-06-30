package com.wangdiaozhu.qingweibo.pager;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.wangdiaozhu.qingweibo.Constants.AccessTokenKeeper;
import com.wangdiaozhu.qingweibo.Constants.Constants;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.activity.MainActivity;
import com.wangdiaozhu.qingweibo.activity.SplashActivity;
import com.wangdiaozhu.qingweibo.base.BasePager;
import com.wangdiaozhu.qingweibo.fragment.ContentFragment;
import com.wangdiaozhu.qingweibo.utils.PrefUtils;
import com.wangdiaozhu.qingweibo.view.CircleImageView;

/**
 * Created by Administrator on 2016/6/5.
 */
public class ProfilePager extends BasePager {


    CircleImageView clv_profile_avatar;
    TextView tv_user_name;
    TextView tv_introduction;
    TextView tv_status_count;
    TextView tv_focus_count;
    TextView tv_funs_count;
    private Oauth2AccessToken mAccessToken;
    private User user;
    public BitmapUtils mBitmapUtils;
    private Button btn_logout;
    private LogoutAPI mLogout;
    boolean isregisted;
    public ProfilePager(Activity activity) {
        super(activity);
            mBitmapUtils = new BitmapUtils(mActivity);
    }





    @Override
    public void initData() {
        super.setTitleMiddleText("我");
        super.showTitleRightText("设置");
        super.showTitleLeftText("添加好友");
        View mProfilePager = View.inflate(mActivity, R.layout.pager_profile,null);
        fl_content.addView(mProfilePager);
        clv_profile_avatar = (CircleImageView) mProfilePager.findViewById(R.id.clv_profile_avatar);
        tv_user_name = (TextView) mProfilePager.findViewById(R.id.tv_user_name);
        tv_introduction = (TextView) mProfilePager.findViewById(R.id.tv_introduction);
        tv_status_count = (TextView) mProfilePager.findViewById(R.id.tv_status_count);
        tv_focus_count = (TextView) mProfilePager.findViewById(R.id.tv_focus_count);
        tv_funs_count = (TextView) mProfilePager.findViewById(R.id.tv_funs_count);
        btn_logout = (Button) mProfilePager.findViewById(R.id.btn_logout);
        user = getUserInfo();
        tv_user_name.setText(user.screen_name);
         mBitmapUtils.display(clv_profile_avatar, user.avatar_hd);
        tv_introduction.setText("简介: " + user.description);
        tv_status_count.setText(user.statuses_count+"");
        tv_focus_count.setText(user.friends_count+"");
        tv_funs_count.setText(user.followers_count+"");
        mAccessToken = AccessTokenKeeper.readAccessToken(mActivity);
        mLogout = new LogoutAPI(mActivity, Constants.APP_KEY,mAccessToken);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mLogout.logout(mListener);
                Intent intent = new Intent(mActivity, SplashActivity.class);
                mActivity.startActivity(intent);

                isregisted = false;
                PrefUtils.putBoolean("isRegisted",isregisted,mActivity);
                 mActivity.finish();
            }
        });


    }
    private User getUserInfo(){
        MainActivity mainActivity = (MainActivity) mActivity;
        ContentFragment fragment =  mainActivity.getContentFragment();
        User user = fragment.getUser();
        if (user != null) {
            return user;
        }else {
            System.out.println("获取数据失败");
        }
        return null;
    }
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {

        }
        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
        }
    };


}
