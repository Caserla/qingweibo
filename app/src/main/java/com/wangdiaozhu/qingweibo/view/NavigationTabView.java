package com.wangdiaozhu.qingweibo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wangdiaozhu.qingweibo.R;


/**
 * @className: NavigationTabView
 * @description: 导航栏组合控件
 * @author: qiudengqiang
 * @date: 2014年10月9日 下午9:04:59
 */
public class NavigationTabView extends LinearLayout implements View.OnClickListener{
    private ImageView iv_tab_icon;
    public TextView tv_tab_desc;
    public TextView tv_circle_count;
    public ImageView iv_red_point;
    private boolean isSelected;
    private Drawable navigation_icon_normal;
    private Drawable navigation_icon_selected;
    private int navigation_name_normal;
    private int navigation_name_selected;

    public NavigationTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.layout_navigation_tab, this);
        iv_tab_icon = (ImageView) findViewById(R.id.iv_main_tab_icon);
        tv_circle_count = (TextView) findViewById(R.id.tv_circle_count);
        iv_red_point = (ImageView) findViewById(R.id.iv_red_point);
        tv_tab_desc = (TextView) findViewById(R.id.tv_main_tab_desc);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.NavigationTabView);
        navigation_name_normal = typeArray.getColor(R.styleable.NavigationTabView_navigation_name_normal, Color.BLACK);
        navigation_name_selected = typeArray.getColor(R.styleable.NavigationTabView_navigation_name_selected, Color.RED);
        navigation_icon_normal = typeArray.getDrawable(R.styleable.NavigationTabView_navigation_icon_normal);
        navigation_icon_selected = typeArray.getDrawable(R.styleable.NavigationTabView_navigation_icon_selected);
        String navigation_name_text = typeArray.getString(R.styleable.NavigationTabView_navigation_text);

        if (!TextUtils.isEmpty(navigation_name_text)) {
            tv_tab_desc.setText(navigation_name_text);
        }
        setSelected(false);
        typeArray.recycle();
        this.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    /**
     * 判断当前计数view是否显示
     */
    public boolean isShowCount() {
        return tv_circle_count.getVisibility() == VISIBLE;
    }

    /**
     * 设置当前view是否显示
     *
     * @param isShowCount 是否显示
     * @param num         显示的数量
     */
    public void setShowCount(boolean isShowCount, int num) {
        if (isShowCount) {
            tv_circle_count.setVisibility(View.VISIBLE);
            String numStr = String.valueOf(num);
            if (num > 99) {
                numStr = "99+";
            }
            tv_circle_count.setText(numStr);
        } else {
            tv_circle_count.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置是否显示小红点
     *
     * @param isShow 是否显示
     */
    public void setShowCountPoint(boolean isShow) {
        iv_red_point.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 判断当前tab的状态
     *
     * @return boolean 是否选中
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * 设置tab的状态
     *
     * @param isSelected 是否选中
     */
    public void setSelected(boolean isSelected) {
        if (isSelected) {
            iv_tab_icon.setImageDrawable(navigation_icon_selected);
            tv_tab_desc.setTextColor(navigation_name_selected);
        } else {
            iv_tab_icon.setImageDrawable(navigation_icon_normal);
            tv_tab_desc.setTextColor(navigation_name_normal);
        }

        this.isSelected = isSelected;
    }

    @Override
    public void onClick(View v) {

    }
    /*

    public void imageAnim() {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(iv_tab_icon, "scaleX",
                0.7f, 1.3f, 0.9f, 1.1f, 1.0f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(iv_tab_icon, "scaleY",
                0.7f, 1.3f, 0.9f, 1.1f, 1.0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(15);
        animSet.setInterpolator(new AnticipateOvershootInterpolator());
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }*/
}
