package com.wangdiaozhu.qingweibo.pager;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.sina.weibo.sdk.openapi.models.User;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.activity.MainActivity;
import com.wangdiaozhu.qingweibo.base.BasePager;
import com.wangdiaozhu.qingweibo.fragment.ContentFragment;
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
    private User user;
    public BitmapUtils mBitmapUtils;
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

        user = getUserInfo();
        tv_user_name.setText(user.screen_name);
         mBitmapUtils.display(clv_profile_avatar,user.avatar_hd);
        tv_introduction.setText("简介: "+user.description);
        tv_status_count.setText(user.statuses_count+"");
        tv_focus_count.setText(user.friends_count+"");
        tv_funs_count.setText(user.followers_count+"");
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


}
