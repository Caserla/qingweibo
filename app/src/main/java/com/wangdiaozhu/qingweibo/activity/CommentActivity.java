package com.wangdiaozhu.qingweibo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.wangdiaozhu.qingweibo.Constants.AccessTokenKeeper;
import com.wangdiaozhu.qingweibo.Constants.Constants;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.base.BaseActivity;
import com.wangdiaozhu.qingweibo.view.nineimageview.NineGridImageView;
import com.wangdiaozhu.qingweibo.view.nineimageview.NineGridImageViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class CommentActivity extends BaseActivity {

    private Oauth2AccessToken mAccessToken;
    private UsersAPI mUsersAPI;
    private ImageButton ib_picture;
    private Button btn_cancel;
    private TextView tv_post_name;
    private TextView tv_user_name;
    private Button btn_send;
    private EditText et_text;
    private NineGridImageView ngi_image_add;
    private User user;
    private CharSequence temp;

    private CommentsAPI mCommentsAPI;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    tv_user_name.setText(user.screen_name);
                    break;


            }
        }
    };
    private Long status_id;


    @Override
    public void initView() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        tv_post_name = (TextView) findViewById(R.id.tv_post_name);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_text = (EditText) findViewById(R.id.et_text);
        ib_picture = (ImageButton) findViewById(R.id.ib_picture);
        ngi_image_add = (NineGridImageView) findViewById(R.id.ngi_image_add);


    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        status_id = intent.getLongExtra("status_id",0);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, mListener1);
        mCommentsAPI = new CommentsAPI(this, Constants.APP_KEY, mAccessToken);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_text.setHint("发评论...");
             et_text.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                 }

                 @Override
                 public void onTextChanged(CharSequence s, int start, int before, int count) {
                     temp = s;
                     if (temp != null) {
                         Drawable drawable = getDrawable(R.drawable.buttonstyle2);
                         btn_send.setClickable(true);
                         btn_send.setBackground(drawable);

                         btn_send.setTextColor(Color.WHITE);
                         btn_send.setOnClickListener(new View.OnClickListener() {
                             String text = temp.toString();

                             @Override
                             public void onClick(View v) {
                                 mCommentsAPI.create(text,status_id,true,mListener);
                                 Toast.makeText(getApplication(),"评论成功",Toast.LENGTH_SHORT).show();
                                 finish();
                             }
                         });
                     }
                     if (temp.length() == 0) {
                         Drawable drawable = getDrawable(R.drawable.buttonstyle);
                         btn_send.setClickable(false);
                         btn_send.setBackground(drawable);
                         Resources resources = getApplicationContext().getResources();
                         ColorStateList colorStateList = resources.getColorStateList(R.color.color_dark_line);
                         if (colorStateList != null) {
                             btn_send.setTextColor(colorStateList);
                         }
                     }
                 }

                 @Override
                 public void afterTextChanged(Editable s) {
                     if (temp != null) {
                         Drawable drawable = getDrawable(R.drawable.buttonstyle2);
                         btn_send.setClickable(true);
                         btn_send.setBackground(drawable);

                         btn_send.setTextColor(Color.WHITE);
                         btn_send.setOnClickListener(new View.OnClickListener() {
                             String text = temp.toString();

                             @Override
                             public void onClick(View v) {
                                  mCommentsAPI.create(text,status_id,true,mListener);
                                    Toast.makeText(getApplication(),"评论成功",Toast.LENGTH_SHORT).show();
                                      finish();

                             }
                         });
                     }
                     if (temp.length() == 0) {
                         Drawable drawable = getDrawable(R.drawable.buttonstyle);
                         btn_send.setClickable(false);
                         btn_send.setBackground(drawable);
                         Resources resources = getApplicationContext().getResources();
                         ColorStateList colorStateList = resources.getColorStateList(R.color.color_dark_line);
                         if (colorStateList != null) {
                             btn_send.setTextColor(colorStateList);
                         }
                     }
                 }
             });


    }
    private RequestListener mListener1 = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {

                // 调用 User#parse 将JSON串解析成User对象
                user = User.parse(response);
                if (user != null) {
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendEmptyMessage(msg.what);
                } else {
                }
            }
        }
        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());

        }
    };
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
