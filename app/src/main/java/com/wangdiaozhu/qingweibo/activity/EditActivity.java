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
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
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
 * Created by Administrator on 2016/6/24.
 */

public class EditActivity extends BaseActivity {

    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatusesAPI;
    private UsersAPI mUsersAPI;
    private Button btn_cancel;
    private TextView tv_post_name;
    private TextView tv_user_name;
    private Button btn_send;
    private EditText et_text;
    private User user;
    private CharSequence temp;
BitmapUtils utils;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:

                    tv_user_name.setText(user.screen_name);
                    break;
                case 2:if(images == null){

                    ngi_image_add.setVisibility(View.GONE);
                }else {
                    ngi_image_add.setVisibility(View.VISIBLE);
                    NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
                        @Override
                        protected void onDisplayImage(Context context, ImageView imageView, String s) {
                            utils = new BitmapUtils(context);
                            utils.configDefaultLoadingImage(R.drawable.selector_image_add_item);
                            utils.display(imageView,s);
                        }

                        @Override
                        protected ImageView generateImageView(Context context) {
                            return super.generateImageView(context);
                        }

                        @Override
                        protected void onItemImageClick(Context context, int index, List<String> list) {
                            Toast.makeText(context, "image position is " + index, Toast.LENGTH_SHORT).show();

                        }
                    };
                    ngi_image_add.setAdapter(mAdapter);
                    ngi_image_add.setImagesData(images);
                }
                    break;
            }

        }
    };
    private ImageButton ib_picture;

    private ArrayList<String> images;
    private NineGridImageView ngi_image_add;


    @Override
    public void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit);
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
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        mUsersAPI = new UsersAPI(this, Constants.APP_KEY, mAccessToken);
        long uid = Long.parseLong(mAccessToken.getUid());
        mUsersAPI.show(uid, mListener1);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       et_text.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(final CharSequence s, int start, int before, int count) {
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
                           if (images == null) {
                               mStatusesAPI.update(text, null, null, mListener);
                               Toast.makeText(getApplicationContext(),"微博发送成功",Toast.LENGTH_SHORT).show();
                               finish();
                           }else {
                               String image = images.get(0);
                               Bitmap bitmap = BitmapFactory.decodeFile(image);
                               mStatusesAPI.upload(text,bitmap,null,null,mListener);
                               Toast.makeText(getApplicationContext(),"微博发送成功",Toast.LENGTH_SHORT).show();
                               finish();
                           }
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
           public void afterTextChanged(final Editable s) {
               if (temp != null) {
                   Drawable drawable = getDrawable(R.drawable.buttonstyle2);
                   btn_send.setClickable(true);
                   btn_send.setBackground(drawable);
                   btn_send.setTextColor(Color.WHITE);
                   btn_send.setOnClickListener(new View.OnClickListener() {
                       String text = temp.toString();

                       @Override
                       public void onClick(View v) {
                           if (images == null) {
                               mStatusesAPI.update(text, null, null, mListener);
                               Toast.makeText(getApplicationContext(),"微博发送成功",Toast.LENGTH_SHORT).show();
                               finish();
                           }else {
                               String image = images.get(0);
                               Bitmap bitmap = BitmapFactory.decodeFile(image);
                               mStatusesAPI.upload(text,bitmap,null,null,mListener);
                               Toast.makeText(getApplicationContext(),"微博发送成功",Toast.LENGTH_SHORT).show();
                               finish();

                           }
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
          ib_picture.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                startActivityForResult(new Intent(getApplicationContext(),SelectPhotoActivity.class),1);

              }
          });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                     switch (resultCode){
                         case 1:
                          Bundle bundle =  data.getExtras();
                             images = bundle.getStringArrayList("images");

                             Message msg = new Message();
                             msg.what = 2;
                             handler.sendEmptyMessage(msg.what);


                     }
    }

    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {

                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses = StatusList.parse(response);
                    if (statuses != null && statuses.total_number > 0) {

                    }
                } else if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
                    Status status = Status.parse(response);

                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

            ErrorInfo info = ErrorInfo.parse(e.getMessage());

        }
    };
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

}
