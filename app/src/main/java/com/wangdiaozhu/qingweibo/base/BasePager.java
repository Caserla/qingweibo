package com.wangdiaozhu.qingweibo.base;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.User;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.activity.MainActivity;

import java.util.zip.Inflater;


/**
 * Created by Administrator on 2016/5/20.
 */
public abstract class BasePager implements View.OnClickListener {

public  Activity mActivity;

    public  View  mRootView;
    public FrameLayout fl_content;
    /**
     * 顶部 title 布局
     */
    public RelativeLayout rl_public_title;

    /**
     * title 左侧
     */
    public RelativeLayout rl_public_title_left;
    public ImageView iv_public_title_left;

    /**
     * 中间文字
     */
    public TextView tv_public_title_middle;

    /**
     * title 右侧
     */
    public RelativeLayout rl_public_title_right;
    public ImageView iv_public_title_right;
    public TextView tv_public_title_right;

    /**
     * 灰线
     */
    public View v_public_line;
    private TextView tv_public_title_left;


    public  BasePager(Activity activity){

        mActivity = activity;

        initView();
    }

    public  void  initView(){

        if(mRootView == null) {
            mRootView = View.inflate(mActivity,R.layout.base_pager, null);
            tv_public_title_middle = (TextView) mRootView.findViewById(R.id.public_title_middle);
          tv_public_title_middle.setText("加载中...");
        }

        fl_content = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        rl_public_title = (RelativeLayout)mRootView.findViewById(R.id.public_title);

        rl_public_title_left = (RelativeLayout) mRootView.findViewById(R.id.public_title_left);
        iv_public_title_left = (ImageView) mRootView.findViewById(R.id.iv_public_title_left);
        tv_public_title_left = (TextView) mRootView.findViewById(R.id.tv_public_title_left);




        rl_public_title_right = (RelativeLayout) mRootView.findViewById(R.id.public_title_right);
        iv_public_title_right = (ImageView) mRootView.findViewById(R.id.iv_public_title_right);
        tv_public_title_right = (TextView) mRootView.findViewById(R.id.tv_public_title_right);

        v_public_line = mRootView.findViewById(R.id.v_public_line);


    }



    public  abstract  void initData();

    /**
     * 显示title右边按钮
     *
     * @param icon 要显示的图标，如果为0不显示图标
     */
    protected void showTitleRightBtn(int icon) {
        rl_public_title.setVisibility(View.VISIBLE);

        if (icon != 0) {
            iv_public_title_right.setBackgroundResource(icon);
        }

        iv_public_title_right.setVisibility(icon == 0 ? View.GONE : View.VISIBLE);
        rl_public_title_right.setVisibility(icon == 0 ? View.GONE : View.VISIBLE);


        rl_public_title_right.setOnClickListener(this);
    }

    /**
     * 显示title右边文字
     */
    protected void showTitleRightText(String text) {
        rl_public_title.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(text)) {
            tv_public_title_right.setText(text);
        }

        tv_public_title_right.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        rl_public_title_right.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);


        rl_public_title_right.setOnClickListener(this);
    }

    /**
     * 显示左边按钮
     */
    protected void showTitleLeftBtn(int icon) {
        rl_public_title.setVisibility(View.VISIBLE);

        if (icon != 0) {
//            iv_public_title_left.setBackgroundResource(icon);
            iv_public_title_left.setImageResource(icon);
        }

        iv_public_title_left.setVisibility(icon == 0 ? View.GONE : View.VISIBLE);
        rl_public_title_left.setVisibility(icon == 0 ? View.GONE : View.VISIBLE);


        rl_public_title_left.setOnClickListener(this);
    }

    /**
     * 显示title左边文字
     */
    protected void showTitleLeftText(String text) {
        rl_public_title.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(text)) {
            tv_public_title_left.setText(text);
            iv_public_title_left.setVisibility(View.GONE);
        }

        tv_public_title_left.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        rl_public_title_left.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);


        rl_public_title_left.setOnClickListener(this);
    }

    protected void showTitleLeftBtn() {
        rl_public_title.setVisibility(View.VISIBLE);

        rl_public_title_left.setVisibility(View.VISIBLE);
        rl_public_title_left.setOnClickListener(this);
    }

    /**
     * 设置中间title页面中间描述
     */
    protected void setTitleMiddleText(String text) {
        rl_public_title.setVisibility(View.VISIBLE);

        tv_public_title_middle.setText(text);
    }

    /**
     * 是否显示灰线，默认显示
     */
    protected void showGrayLine(boolean showLine) {
        v_public_line.setVisibility(showLine ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View v) {

    }
}
