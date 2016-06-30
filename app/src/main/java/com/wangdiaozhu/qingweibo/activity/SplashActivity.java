package com.wangdiaozhu.qingweibo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.widget.LoginButton;
import com.wangdiaozhu.qingweibo.Constants.AccessTokenKeeper;
import com.wangdiaozhu.qingweibo.Constants.Constants;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.base.BaseActivity;
import com.wangdiaozhu.qingweibo.utils.PrefUtils;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/6/4.
 */
public class SplashActivity extends BaseActivity {

    private RelativeLayout rl_splash;
    private ImageView iv_splash_text;
    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private LoginButton mLoginButton;

    /** 登陆认证对应的listener */
    private AuthListener mLoginListener = new AuthListener();
    private boolean isRegisted;

    @Override
    public void initView() {

        setContentView(R.layout.activity_splash);
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        iv_splash_text = (ImageView) findViewById(R.id.iv_splash_text);
        mLoginButton = (LoginButton) findViewById(R.id.lb_loginout);

//           旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
//             缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(true);

        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);


        rl_splash.startAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);

                alphaAnimation.setDuration(2000);
                alphaAnimation.setFillAfter(true);
                iv_splash_text.startAnimation(alphaAnimation);
                alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
//  在这里启动登陆页面或者主页面

                   boolean hasRegisted =  PrefUtils.getBoolean("isRegisted",false,SplashActivity.this);
                        System.out.println(hasRegisted);
                        if(hasRegisted == true){

                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                                    finish();
                        }else {

                        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);

                        alphaAnimation.setDuration(1000);
                        alphaAnimation.setFillAfter(true);
                        iv_splash_text.startAnimation(alphaAnimation);
                        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);

                                alphaAnimation.setDuration(1000);
                                alphaAnimation.setFillAfter(true);
                                mLoginButton.startAnimation(alphaAnimation);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                    }}

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    @Override
    public void initData() {
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(SplashActivity.this, mAuthInfo);
        mLoginButton.setWeiboAuthInfo(mAuthInfo, mLoginListener);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
            updateTokenView(true);
        }
    }
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String  phoneNum =  mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(SplashActivity.this, mAccessToken);
                isRegisted = true;
                PrefUtils.putBoolean("isRegisted", isRegisted, SplashActivity.this);
                System.out.println(isRegisted);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

                finish();

            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(SplashActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(SplashActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
//        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
//        mTokenText.setText(message);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }

    }
}
