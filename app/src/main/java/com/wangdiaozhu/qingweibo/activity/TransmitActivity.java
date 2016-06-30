package com.wangdiaozhu.qingweibo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
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
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;
import com.wangdiaozhu.qingweibo.Constants.AccessTokenKeeper;
import com.wangdiaozhu.qingweibo.Constants.Constants;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.base.BaseActivity;
import com.wangdiaozhu.qingweibo.utils.NetWorkUtils;
import com.wangdiaozhu.qingweibo.utils.WeiboTextUtils;


/**
 * Created by Administrator on 2016/6/29.
 */
public class TransmitActivity extends BaseActivity {
    private Oauth2AccessToken mAccessToken;
    private UsersAPI mUsersAPI;
    private ImageButton ib_picture;
    private Button btn_cancel;
    private TextView tv_post_name;
    private TextView tv_user_name;
    private Button btn_send;
    private EditText et_text;
    private User user;
    private StatusesAPI mStatusesAPI;

    Status status;
    BitmapUtils utils;
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
    private ImageView iv_friends_icon;
    private TextView tv_friends_name;
    private TextView tv_friends_status;

    @Override
    public void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transmit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        tv_post_name = (TextView) findViewById(R.id.tv_post_name);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_text = (EditText) findViewById(R.id.et_text);
        ib_picture = (ImageButton) findViewById(R.id.ib_picture);
        iv_friends_icon = (ImageView) findViewById(R.id.iv_friends_icon);
        tv_friends_name = (TextView) findViewById(R.id.tv_friends_name);
        tv_friends_status = (TextView) findViewById(R.id.tv_friends_status);
    }

    @Override
    public void initData() {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        mStatusesAPI = new StatusesAPI(getApplicationContext(), Constants.APP_KEY, mAccessToken);
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, mListener1);
        Intent intent = getIntent();
        Bundle bundle =  intent.getExtras();
        status = (Status) bundle.getSerializable("transmit_status");
        System.out.println(status);
        utils = new BitmapUtils(this);
        utils.display(iv_friends_icon, status.user.avatar_large);
        tv_friends_name.setText("@ " + status.user.screen_name);

        String str = status.text;
        SpannableString text = WeiboTextUtils.setTextHighLight(str, getApplicationContext());
        tv_friends_status.setText(text);
        et_text.setHint("说说分享心得...");
        Drawable drawable = getDrawable(R.drawable.buttonstyle2);
        btn_send.setClickable(true);
        btn_send.setBackground(drawable);
        btn_send.setTextColor(Color.WHITE);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = Long.parseLong(status.id);
                   Editable str = et_text.getText();
                     String text = str.toString();
              String rip = NetWorkUtils.getLocalIpAddress(getApplicationContext());
                mStatusesAPI.repost(id,text,0,rip,mListener);
                Toast.makeText(getApplicationContext(),"转发成功",Toast.LENGTH_SHORT).show();
                finish();
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
